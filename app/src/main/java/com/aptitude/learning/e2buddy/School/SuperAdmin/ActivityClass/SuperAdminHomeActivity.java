package com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;

import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;

import com.aptitude.learning.e2buddy.School.SuperAdmin.FragmentClass.SuperAdminNoticeFragment;
import com.aptitude.learning.e2buddy.School.SuperAdmin.FragmentClass.SuperAdminTeacherFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuperAdminHomeActivity extends AppCompatActivity {

    private TextView tvStudentName,tvSchoolName;
    private SuperAdminData superAdminData;
    private int adminId;
    String studentName, studentImage, schoolName,schoolLogo,schoolImage;
    private GoogleSignInClient mGoogleSignInClient;
    private CircleImageView imgStudent,imgSchool;

    final SuperAdminTeacherFragment superAdminTeacherFragment = new SuperAdminTeacherFragment();
    final SuperAdminNoticeFragment superAdminNoticeFragment = new SuperAdminNoticeFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = superAdminTeacherFragment;
    private SchoolData schoolData;
    private SuperAdminData user;

    private String adminName, adminImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_home);

        imgStudent = findViewById(R.id.imgStudent);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        imgSchool = findViewById(R.id.imgSchool);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.frame_container, superAdminNoticeFragment, "2").hide(superAdminNoticeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container,superAdminTeacherFragment, "1").commit();

        user = SchoolSuperAdminSessionManager.getInstance(this).getUser();
        adminId = user.getSuperAdminId();
        adminName = user.getSuperAdminName();

        adminImage = user.getSuperAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();


        tvStudentName.setText(""+adminName);

        Glide.with(SuperAdminHomeActivity.this)
                .load(adminImage)
                .into(imgStudent);

        tvSchoolName.setText(""+schoolName);

        Glide.with(SuperAdminHomeActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchool);

        handleOnClick();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_test:
                    fragmentManager.beginTransaction().hide(active).show(superAdminTeacherFragment).commit();
                    active = superAdminTeacherFragment;
                    return true;

                case R.id.navigation_notice:
                    fragmentManager.beginTransaction().hide(active).show(superAdminNoticeFragment).commit();
                    active = superAdminNoticeFragment;
                    return true;

            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(SuperAdminHomeActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SuperAdminHomeActivity.super.onBackPressed();
                    }
                }).create().show();

    }

    private void handleOnClick(){
        imgStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SuperAdminHomeActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // logout
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();

                                mGoogleSignInClient = GoogleSignIn.getClient(SuperAdminHomeActivity.this,gso);

                                mGoogleSignInClient.signOut().addOnCompleteListener(SuperAdminHomeActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                SchoolSuperAdminSessionManager.getInstance(getApplicationContext()).logout();
                                                finish();
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // user doesn't want to logout
                            }
                        })
                        .show();
            }
        });

    }

}