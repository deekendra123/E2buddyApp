package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.ActivityClass.HomeNavActivity;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminConfirmationActivity;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentConfirmationActivity extends AppCompatActivity {

    private StudentData studentData;
    private int id;
    String studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName;
    private int systemTaken;
    String schoolLogo;
    private TextView tvStudentName,tvSchoolName,tvUserName;
    private CircleImageView imgSchool,imgStudent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_confirmation);
        imgSchool = findViewById(R.id.imgSchool);
        imgStudent = findViewById(R.id.imgStudent);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        tvUserName = findViewById(R.id.tvUserName);

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        schoolLogo = studentData.getSchoolLogo();
        studentImage = studentData.getStudentImage();

        tvStudentName.setText(""+studentName);
        tvSchoolName.setText(""+schoolName);
        tvUserName.setText("Congratulations "+studentName);

        if (!schoolLogo.isEmpty()){
            Glide.with(StudentConfirmationActivity.this)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolLogo)
                    .into(imgSchool);
        }
        else {
            imgSchool.setBackgroundResource(R.drawable.science);
        }

        Glide.with(StudentConfirmationActivity.this)
                .load(studentImage)
                .into(imgStudent);
    }

    public void btNextOnclick(View view) {

       Intent intent = new Intent(StudentConfirmationActivity.this, StudHomeActivity.class);
       //Intent intent = new Intent(StudentConfirmationActivity.this, StudentHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
