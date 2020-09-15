package com.aptitude.learning.e2buddy.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;

import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentConfirmationActivity;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentDetailsActivity;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

public class School_Code_Activity extends AppCompatActivity {

    EditText schoolCode,etAdmissionNo;
    ImageView submit;
    Animation fromtop;
    TextView  text;
    FirebaseAuth firebaseAuth;
    private LinearLayout admissionlayout;
    private  int userid;
    ImageView icon;
    FirebaseAuth auth;
    String userImageUrl, userName, emailId;
    private boolean status ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__code_);

        schoolCode = findViewById(R.id.editText11);
        submit = findViewById(R.id.btsubmit);
       // skip = findViewById(R.id.buttonSkip);
        admissionlayout = findViewById(R.id.admissionlayout);
        icon = findViewById(R.id.logo);
        etAdmissionNo = findViewById(R.id.etAdmissionNo);
        text = findViewById(R.id.t1);

       // skip.setText(R.string.skip_string);

        userid = getIntent().getIntExtra("id", 0);
        userName = getIntent().getStringExtra("userName");
        emailId = getIntent().getStringExtra("userEmail");
        userImageUrl = getIntent().getStringExtra("imageUrl");


        Log.e("userdata", userid + "  "+userImageUrl);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    text.startAnimation(animation);
                    sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                if (checkInternet.isOnline()==true){

                if (schoolCode.getText().toString().isEmpty()){
                    schoolCode.setError("Enter School Code");
                }
                else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_DETAILS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.e("school", response);

                                    try {
                                        JSONArray array = new JSONArray(response);

                                        for (int i = 0; i < array.length(); i++) {

                                            JSONObject data = array.getJSONObject(i);

                                            final int id = data.getInt("id");
                                            final String schoolName = data.getString("schoolName");
                                            final String schoolCodes = data.getString("schoolCode");
                                            final String city = data.getString("city");
                                            final String state = data.getString("state");
                                            final String pincode = data.getString("pincode");
                                            final String type = data.getString("type");
                                            final int systemTaken = data.getInt("systemTaken");
                                            final String schoolLogo = data.getString("schoolLogo");

                                            Log.e("school", id+ "  "+ schoolName+  "  "+ schoolName);

                                            if (schoolCode.getText().toString().equalsIgnoreCase(schoolCodes)){

                                                if (systemTaken==1) {
                                                    System.gc();
                                                    admissionlayout.setVisibility(View.VISIBLE);

                                                    submit.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (etAdmissionNo.getText().toString().isEmpty()){
                                                                Utils.showToast("Admission No is required.");
                                                            }
                                                            else {
                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_CHECK_ADMISSION_NO, new com.android.volley.Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        Log.e("resp",response+"");


                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            String studentId = jsonObject.getString("userId");
                                                                            String dateOfBirth = jsonObject.getString("dateofBirth");
                                                                            int classId = jsonObject.getInt("classId");
                                                                            int sectionId = jsonObject.getInt("sectionId");
                                                                            String userName = jsonObject.getString("userName");

                                                                            int studentid = Integer.parseInt(studentId);

                                                                            Intent intent = new Intent(getApplicationContext(), StudentDetailsActivity.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            intent.putExtra("id", id);
                                                                            intent.putExtra("schoolName", schoolName);
                                                                            intent.putExtra("schoolCode", schoolCodes);
                                                                            intent.putExtra("city", city);
                                                                            intent.putExtra("state", state);
                                                                            intent.putExtra("pincode", pincode);
                                                                            intent.putExtra("type", type);
                                                                            intent.putExtra("systemTaken", systemTaken);
                                                                            intent.putExtra("schoolLogo",schoolLogo);
                                                                            intent.putExtra("imageUrl", userImageUrl);
                                                                            intent.putExtra("userId", userid);
                                                                            intent.putExtra("userName", userName);
                                                                            intent.putExtra("userEmail", emailId);
                                                                            intent.putExtra("dateOfBirth",dateOfBirth);
                                                                            intent.putExtra("classId", classId);
                                                                            intent.putExtra("sectionId", sectionId);
                                                                            intent.putExtra("admissionNo",etAdmissionNo.getText().toString());

                                                                            startActivity(intent);
                                                                            finish();


                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                    }
                                                                },
                                                                        new com.android.volley.Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {

                                                                                Log.e("DKKKerror", ""+error.getMessage());
                                                                                Toast.makeText(School_Code_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }){

                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {

                                                                        HashMap<String, String> parms = new HashMap<>();
                                                                        parms.put("userId", String.valueOf(userid));
                                                                        parms.put("schoolCodeId", String.valueOf(id));
                                                                        parms.put("admissionNo", etAdmissionNo.getText().toString());
                                                                        return parms;
                                                                    }
                                                                };
                                                                RequestQueue requestQueue = Volley.newRequestQueue(School_Code_Activity.this);
                                                                requestQueue.add(stringRequest);

                                                            }
                                                        }
                                                    });


                                                }
                                                else {

                                                    System.gc();
                                                    Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("id", id);
                                                    intent.putExtra("schoolName", schoolName);
                                                    intent.putExtra("schoolCode", schoolCodes);
                                                    intent.putExtra("city", city);
                                                    intent.putExtra("state", state);
                                                    intent.putExtra("pincode", pincode);
                                                    intent.putExtra("type", type);
                                                    intent.putExtra("imageUrl", userImageUrl);
                                                    intent.putExtra("userId", userid);
                                                    intent.putExtra("userName", userName);
                                                    intent.putExtra("userEmail", emailId);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                status = true;
                                                break;

                                            }

                                        }
                                        if (status==false){
                                            //  schoolCode.setError("Invalid School Code");
                                            Utils.showToast("Invalid School Code");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                    //adding our stringrequest to queue
                    Volley.newRequestQueue(School_Code_Activity.this).add(stringRequest);

                }
                }
                else {
                    Toast.makeText(School_Code_Activity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

/*
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User(
                        userid, userName, emailId
                );

                SessionManager.getInstance(School_Code_Activity.this).userLogin(user);

                Intent intent = new Intent(getApplicationContext(), HomeNavActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
*/

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Enter school code or Skip",
                    Toast.LENGTH_LONG).show();

        return false;
    }
    private void updateUserInfo(final int schoollId, final int userid){


    }


}
