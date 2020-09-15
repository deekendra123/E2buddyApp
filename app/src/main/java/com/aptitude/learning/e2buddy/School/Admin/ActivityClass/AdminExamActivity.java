package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ExamTestAdapter;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.SchoolTestAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminExamActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int adminId,schoolId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage;
    private AdminData user;
    private SchoolData schoolData;
    private TextView tvAdminName;
    private CircleImageView imgAdmin,imgSchoolLogo;
    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;
    private RelativeLayout relativeLayoutAddExam;
    private RecyclerView recyclerViewTests;
    private TextView tvTestMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exam);
        tvAdminName = findViewById(R.id.tvAdminName);
        imgAdmin = findViewById(R.id.imgAdmin);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        progressBar = findViewById(R.id.progressBar);
        relativeLayoutAddExam = findViewById(R.id.relativeLayoutAddExam);
        recyclerViewTests = findViewById(R.id.recyclerViewTests);
        tvTestMsg = findViewById(R.id.tvTestMsg);
        progressBar.setVisibility(View.VISIBLE);
        dbHelper = new AdminDBHelper(this);

        user = SchoolAdminSessionManager.getInstance(this).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        schoolCode = user.getSchoolCode();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolId = schoolData.getSchoolId();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        tvAdminName.setText(""+schoolName);

        Glide.with(AdminExamActivity.this)
                .load(adminImage)
                .into(imgAdmin);

        Glide.with(AdminExamActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        onClick();
    }
    private void onClick(){
        relativeLayoutAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminExamActivity.this,AdminAddExamActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTest();

    }

    private void checkTest(){

        final List<ExamData> list = new ArrayList<>();

        List<ExamData> examDataList = dbHelper.getAllExam();

        if (examDataList.size()>0) {

            for (int j = 0; j < examDataList.size(); j++) {
                ExamData examData = examDataList.get(j);

                list.add(new ExamData(examData.getExamTypeId(),examData.getExamId(),examData.getExamName(),examData.getClassId(),examData.getSectionId(),examData.getSubjectId(),examData.getMaxMark(),examData.getPassMark(),examData.getExamDate(),examData.getExamStartTime(),examData.getExamStopTime(),examData.getTimeAllotted(),examData.getTotalQuestion(),examData.getCorrectAnswerMark(),examData.getWrongAnswerMark(),examData.getNotAttemptedMark(),examData.getSchoolCode(),examData.getStatus(),examData.getExamAddedBy(),examData.getExamVisible(),examData.getExamResult()));
            }

            tvTestMsg.setVisibility(View.GONE);
            recyclerViewTests.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminExamActivity.this, RecyclerView.VERTICAL, false);
            recyclerViewTests.setLayoutManager(layoutManager);
            ExamTestAdapter schoolTestAdapter = new ExamTestAdapter(AdminExamActivity.this, list, dbHelper);
            recyclerViewTests.setAdapter(schoolTestAdapter);
            recyclerViewTests.setItemViewCacheSize(list.size());
            progressBar.setVisibility(View.GONE);


        }
        else {
            recyclerViewTests.setVisibility(View.GONE);
            tvTestMsg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }

    }

}