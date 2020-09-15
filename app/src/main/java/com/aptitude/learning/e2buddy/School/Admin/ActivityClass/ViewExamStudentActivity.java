package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ViewExamStudentAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ExamStatus;

import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;

import com.aptitude.learning.e2buddy.School.Admin.DataClass.UserData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewExamStudentActivity extends AppCompatActivity {

    private int adminId,schoolId,classTestId,classId,maxMarks,passMarks;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage,subjectName,testName;
    private AdminData user;
    private SchoolData schoolData;
    private TextView tvSchoolName,tvClassName,tvSectionName,tvTestName,tvMaxMarks,tvSubjectName,tvMsg,tvCounter;
    private CircleImageView imgSchoolLogo;
    private RecyclerView recyclerViewStudents;
    private List<ExamStatus> list;
    private ArrayList<String> sectionId;
    RadioGroup mRgAllButtons;
    private ImageView imgUp,imgDown;
    private int studentSectionId, studentClassId;
    private boolean status;
    private ProgressBar progressBar;
    private ViewExamStudentAdapter studentTestAdapter;
    private AdminDBHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam_student);

        imgSchoolLogo = findViewById(R.id.imgSchoolLogo);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        tvClassName = findViewById(R.id.tvClassName);
        tvSectionName = findViewById(R.id.tvSectionName);
        tvTestName = findViewById(R.id.tvTestName);
        tvMaxMarks = findViewById(R.id.tvMaxMarks);
        tvSubjectName = findViewById(R.id.tvSubjectName);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        mRgAllButtons = findViewById(R.id.radiogroup);

        progressBar = findViewById(R.id.progressBar);
        tvMsg = findViewById(R.id.tvMsg);
        tvCounter = findViewById(R.id.tvCounter);

        progressBar.setVisibility(View.VISIBLE);


        classTestId = getIntent().getIntExtra("examId",-1);
        classId = getIntent().getIntExtra("classId",-1);
        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        testName = getIntent().getStringExtra("testName");
        subjectName = getIntent().getStringExtra("subjectName");

        passMarks = getIntent().getIntExtra("passMarks",-1);

        sectionId = getIntent().getStringArrayListExtra("sectionId");

        dbHelper = new AdminDBHelper(this);



        list = new ArrayList<>();

        studentTestAdapter = new ViewExamStudentAdapter(ViewExamStudentActivity.this, list,passMarks,classTestId, dbHelper);

        getSchoolSection();
        tvSubjectName.setText(""+subjectName);
        tvTestName.setText(""+testName);
        tvMaxMarks.setText("Max Marks: "+maxMarks);

        GetExamSection getSectionName = new GetExamSection(ViewExamStudentActivity.this, classTestId,tvSectionName);
        getSectionName.getSchoolSection();

        if (classId==1){
            tvClassName.setText("Class: Pre Nursery");
        }
        else if (classId==2){
            tvClassName.setText("Class: Nursery");
        }
        else if (classId==3){
            tvClassName.setText("Class: LKG");
        }else if (classId==4){
            tvClassName.setText("Class: UKG");
        }else if (classId==5){
            tvClassName.setText("Class: First");
        }else if (classId==6){
            tvClassName.setText("Class: Second");
        }else if (classId==7){
            tvClassName.setText("Class: Third");
        }else if (classId==8){
            tvClassName.setText("Class: Fourth");
        }else if (classId==9){
            tvClassName.setText("Class: Fifth");
        }else if (classId==10){
            tvClassName.setText("Class: Sixth");
        }else if (classId==11){
            tvClassName.setText("Class: Seventh");
        }else if (classId==12){
            tvClassName.setText("Class: Eighth");
        }else if (classId==13){
            tvClassName.setText("Class: Ninth");
        }else if (classId==14){
            tvClassName.setText("Class: Tenth");
        }
        else if (classId==15){
            tvClassName.setText("Class: Eleventh");
        }

        else if (classId==16){
            tvClassName.setText("Class: Twelfth");
        }

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
        tvSchoolName.setText(""+schoolName);

        Glide.with(ViewExamStudentActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        checkInternet1();

        int count = dbHelper.getStudentCount(classTestId);
        int totalStudents = studentTestAdapter.getItemCount()+1;
        tvCounter.setText("Students : "+count+"/"+totalStudents);
    }

    private boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            for (int i=0;i<sectionId.size();i++){

                getStudents(sectionId.get(i));
            }

            return true;
        }
        else{
            checkNetworkConnection1();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    private void checkNetworkConnection1(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkInternet1();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getStudents(final String sectionId){


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewStudents.setLayoutManager(layoutManager);
        recyclerViewStudents.addItemDecoration(new DividerItemDecoration(recyclerViewStudents.getContext(), DividerItemDecoration.VERTICAL));
        list.clear();

        List<UserData> userDataList = dbHelper.getUserInfo(schoolId,classId, Integer.parseInt(sectionId));

        if (userDataList.size()>0) {
            recyclerViewStudents.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.GONE);

            for (int i = 0; i < userDataList.size(); i++) {
                UserData userData = userDataList.get(i);
                getTestData(list, userData.getUserName(), userData.getSectionName(), userData.getUserId(), userData.getAdmissionNo(), userData.getRollNo());
            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            tvMsg.setVisibility(View.VISIBLE);
            recyclerViewStudents.setVisibility(View.GONE);
        }

    }

    private void getTestData(final List<ExamStatus> list, final String userNmae, final String sectionName, final int userId, final String admissionNumber, String rollNo){

        String status = dbHelper.getStudentExamStatus(classTestId,userId);

        list.add(new ExamStatus(userId, userNmae, sectionName, status, admissionNumber, rollNo));

        Collections.sort(list, new Comparator<ExamStatus>() {
            @Override
            public int compare(final ExamStatus object1, final ExamStatus object2) {

                return object2.getUserId() - object1.getUserId() ;

            }
        });

        recyclerViewStudents.setAdapter(studentTestAdapter);
        progressBar.setVisibility(View.GONE);

    }


    public void getSchoolSection(){
        mRgAllButtons.setOrientation(LinearLayout.HORIZONTAL);
        final List<SectionData> section = new ArrayList<>();
        section.clear();

        List<SectionData> sectionDataList = dbHelper.getExamSection(classTestId);
        for (int i =0; i<sectionDataList.size();i++){
            SectionData sectionData = sectionDataList.get(i);
            studentSectionId = sectionData.getSectionId();
            studentClassId = sectionData.getClassId();

            section.add(new SectionData(studentSectionId,studentClassId,sectionData.getSectionName()));

        }


        Collections.sort(section, new Comparator<SectionData>() {
            @Override
            public int compare(final SectionData object1, final SectionData object2) {
                return object1.getSectionName().compareTo(object2.getSectionName());
            }
        });

        section.add(new SectionData(studentSectionId,studentClassId,"All"));

        for (int i=0; i<section.size();i++){

            RadioButton rdbtn = new RadioButton(getApplicationContext());
            rdbtn.setId(View.generateViewId());
            if (section.get(i).getSectionName().equals("All")){
                rdbtn.setText("" + section.get(i).getSectionName());
                rdbtn.setChecked(true);
            }
            else {
                rdbtn.setText("Section " + section.get(i).getSectionName());

            }
            final int finalI = i;
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (section.get(finalI).getSectionName().equals("All")){
                        for (int i=0;i<sectionId.size();i++){
                            progressBar.setVisibility(View.VISIBLE);
                            getStudents(sectionId.get(i));

                        }
                    }else {
                        progressBar.setVisibility(View.VISIBLE);

                        getStudents(String.valueOf(section.get(finalI).getSectionId()));
                    }
                }
            });
            mRgAllButtons.addView(rdbtn);
        }

    }
}