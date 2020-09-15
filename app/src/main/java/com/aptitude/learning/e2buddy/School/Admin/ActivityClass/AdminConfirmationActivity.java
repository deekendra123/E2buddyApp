package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.ActivityClass.HomeNavActivity;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class AdminConfirmationActivity extends AppCompatActivity {

    //logoIJS.jpg
    private Button btNext;
    private AdminData user;
    private SchoolData schoolData;
    private int adminId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage;
    private CircleImageView imgSchool;
    private TextView tvSchoolName, tvAdminName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_confirmation);
        btNext = findViewById(R.id.btNext);
        imgSchool = findViewById(R.id.imgSchool);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        tvAdminName = findViewById(R.id.tvAdminName);

        user = SchoolAdminSessionManager.getInstance(this).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        schoolCode = user.getSchoolCode();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();


        if (!schoolImage.isEmpty()){

            Glide.with(AdminConfirmationActivity.this)
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

        Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
