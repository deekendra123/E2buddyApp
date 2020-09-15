package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.AdminHomePagerAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminTestActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int adminId,schoolId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage;
    private AdminData user;
    private SchoolData schoolData;
    private TextView tvAdminName;
    private CircleImageView imgAdmin,imgSchoolLogo;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;
    private RelativeLayout relativeLayoutAddExam;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_test);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        tvAdminName = findViewById(R.id.tvAdminName);
        imgAdmin = findViewById(R.id.imgAdmin);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        progressBar = findViewById(R.id.progressBar);
        relativeLayoutAddExam = findViewById(R.id.relativeLayoutAddExam);
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

        setTabName();

        Glide.with(AdminTestActivity.this)
                .load(adminImage)
                .into(imgAdmin);

        Glide.with(AdminTestActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

    }

    private void setTabName(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_CLASS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int classId = data.getInt("classId");
                        String className = data.getString("className");

                        tabLayout.addTab(tabLayout.newTab().setText(""+className));

                    }
                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                    final AdminHomePagerAdapter adapter = new AdminHomePagerAdapter(AdminTestActivity.this,getSupportFragmentManager(), tabLayout.getTabCount());
                    tabLayout.getTabCount();
                    viewPager.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());
                        }
                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }
                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void btAddTestOnClick(View view) {

        Intent intent = new Intent(getApplicationContext(), AdminAddTestActivity.class);
        startActivity(intent);
    }


}