package com.aptitude.learning.e2buddy.ActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminHomeActivity;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudHomeActivity;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.SuperAdminHomeActivity;
import com.aptitude.learning.e2buddy.Service.OnClearFromRecentService;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Animation zoomout;

    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(getApplicationContext());

        textView = findViewById(R.id.splashtex);
        imageView = findViewById(R.id.imageView);

        zoomout = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoomin);
        imageView.setAnimation(zoomout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        textView.startAnimation(animation);

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if (SessionManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }

                if (SchoolAdminSessionManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                    finish();

                }

                if (SchoolStudentSessionManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, StudHomeActivity.class));

                    finish();

                }
                if (SchoolSuperAdminSessionManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, SuperAdminHomeActivity.class));
                    finish();

                }
                if (!SessionManager.getInstance(SplashActivity.this).isLoggedIn() && !SchoolAdminSessionManager.getInstance(SplashActivity.this).isLoggedIn() && !SchoolStudentSessionManager.getInstance(SplashActivity.this).isLoggedIn() && !SchoolSuperAdminSessionManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }



            }
        }, SPLASH_TIME);
    }

}
