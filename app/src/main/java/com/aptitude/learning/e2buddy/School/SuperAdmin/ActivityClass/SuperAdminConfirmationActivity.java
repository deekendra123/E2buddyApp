package com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuperAdminConfirmationActivity extends AppCompatActivity {

    private Button btNext;
    private SuperAdminData user;
    private SchoolData schoolData;
    private int adminId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage;
    private CircleImageView imgSchool;
    private TextView tvSchoolName, tvAdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_confirmation);

        btNext = findViewById(R.id.btNext);
        imgSchool = findViewById(R.id.imgSchool);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        tvAdminName = findViewById(R.id.tvAdminName);

        user = SchoolSuperAdminSessionManager.getInstance(this).getUser();
        adminId = user.getSuperAdminId();
        adminName = user.getSuperAdminName();
        adminEmail = user.getSuperAdminEmail();
        schoolCode = user.getSchoolCode();
        adminImage = user.getSuperAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();


        if (!schoolImage.isEmpty()){

            Glide.with(SuperAdminConfirmationActivity.this)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                    .into(imgSchool);
        }
        else {

            imgSchool.setBackgroundResource(R.drawable.school);

        }

        tvSchoolName.setText(""+schoolName);
        tvAdminName.setText("Congratulations "+ adminName);


    }

    public void btNextOnclick(View view) {

        Intent intent = new Intent(getApplicationContext(), SuperAdminHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

}