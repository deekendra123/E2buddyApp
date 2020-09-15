package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.AdminHomePagerAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudHomeActivity;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper.DATABASE_NAME;

public class AdminHomeActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_home);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        tvAdminName = findViewById(R.id.tvAdminName);
        imgAdmin = findViewById(R.id.imgAdmin);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        progressBar = findViewById(R.id.progressBar);
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

        runCodeAtFirstTime();
        checkInternet3();

        setTabName();

        Glide.with(AdminHomeActivity.this)
                .load(adminImage)
                .into(imgAdmin);

        Glide.with(AdminHomeActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        imgAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdminHomeActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // logout
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();

                                mGoogleSignInClient = GoogleSignIn.getClient(AdminHomeActivity.this,gso);

                                mGoogleSignInClient.signOut().addOnCompleteListener(AdminHomeActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                SchoolAdminSessionManager.getInstance(getApplicationContext()).logout();
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
    private boolean checkInternet3(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    insertQuestion();
                    getUserData();
                    getAdminData();
                    getAllClassTestData();
                    getSchoolSubject();
                }
            });
            return true;
        }
        else{
            checkNetworkConnection3();
            Log.d("Network","Not Connected");
            return false;
        }
    }
    private void getUserData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_USER_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int userId = dataobj.getInt("userId");
                                        String userName = dataobj.getString("userName");
                                        String dateofBirth = dataobj.getString("dateofBirth");
                                        int userInfoId = dataobj.getInt("userInfoId");

                                        int classId = dataobj.getInt("classId");
                                        int sectionId = dataobj.getInt("sectionId");
                                        int schoolCode = dataobj.getInt("schoolCode");
                                        int admissionNumber = dataobj.getInt("admissionNumber");
                                        String token = dataobj.getString("token");
                                        String rollNo = dataobj.getString("rollNo");


                                        if(dbHelper.insertUserData(userId, userName, dateofBirth, userInfoId, classId, sectionId, schoolCode, admissionNumber, token, rollNo)){
                                            Log.e("msg","done");
                                        } else{
                                            Log.e("msg","not done");

                                        }
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> parms = new HashMap<>();
                parms.put("schoolId", String.valueOf(schoolId));
                return parms;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getAdminData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_ADMIN_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int adminId = dataobj.getInt("adminId");
                                        String adminName = dataobj.getString("adminName");
                                        String adminEmail = dataobj.getString("adminEmail");
                                        String adminPhone = dataobj.getString("adminPhone");

                                        String adminImage = dataobj.getString("adminImage");
                                        String dateTime = dataobj.getString("dateTime");
                                        int schoolCode = dataobj.getInt("schoolCode");

                                        if(dbHelper.insertAdminData(adminId, adminName, adminEmail, adminPhone, adminImage, dateTime, schoolCode)){
                                            Log.e("msg","done");
                                        } else{
                                            Log.e("msg","not done");
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> parms = new HashMap<>();
                parms.put("schoolId", String.valueOf(schoolId));
                return parms;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);

    }


    private void checkNetworkConnection3(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkInternet3();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void insertQuestion() {
        List<QuestionData> questionDataList = dbHelper.getAllAdminQuestion(schoolId);
        for (int j = 0; j<questionDataList.size(); j++){
            QuestionData questionData = questionDataList.get(j);
            insertQuestionToServer(questionData.getQuestionid(), questionData.getClassTestId(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(), questionData.getAnswer(), questionData.getDescription());
        }
    }

    private void insertQuestionToServer(final int questionId, final int classTestId, final String question, final String option1, final String option2, final String option3, final String option4, final String answer, final String description) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_QUESTION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("schoolCodeId", String.valueOf(schoolId));
                parms.put("classTestId", String.valueOf(classTestId));
                parms.put("questionId", String.valueOf(questionId));
                parms.put("question", question);
                parms.put("option1", option1);
                parms.put("option2", option2);
                parms.put("option3", option3);
                parms.put("option4", option4);
                parms.put("answer", answer);
                parms.put("description", description);

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);

    }


    private void runCodeAtFirstTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            checkInternet();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {

            // getAllClassTestData();
            getSchoolSection();
            getSchoolClass();

            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    private void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkInternet();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void getAllClassTestData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_ALL_CLASS_TEST_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int classTestId = dataobj.getInt("classTestId");
                                        String classTestName = dataobj.getString("classTestName");
                                        int classId = dataobj.getInt("classId");
                                        int sectionId = dataobj.getInt("sectionId");
                                        int subjectId = dataobj.getInt("subjectId");
                                        int maxMark = dataobj.getInt("maxMark");
                                        int passMark = dataobj.getInt("passMark");
                                        String classTestDate = dataobj.getString("classTestDate");
                                        int timeAlloted = dataobj.getInt("timeAlloted");
                                        int totalQuestion = dataobj.getInt("totalQuestion");
                                        int correctAnswerMark = dataobj.getInt("correctAnswerMark");
                                        int wrongAnswerMark = dataobj.getInt("wrongAnswerMark");
                                        int notAttemptedMark = dataobj.getInt("notAttemptedMark");
                                        int schoolCode = dataobj.getInt("schoolCode");
                                        int status = dataobj.getInt("status");
                                        int classTestAddedBy = dataobj.getInt("classTestAddedBy");
                                        int classTestVisible = dataobj.getInt("classTestVisible");

                                        if (dbHelper.insertClassTest(
                                                classTestId, classTestName, classId, sectionId, subjectId, maxMark, passMark, classTestDate,
                                                timeAlloted, totalQuestion, correctAnswerMark, wrongAnswerMark, notAttemptedMark, schoolCode, status, classTestAddedBy, classTestVisible
                                        )
                                        ) {
                                            Log.e("msg", "done");
                                        } else {
                                            Log.e("msg", "not done");

                                        }

                                        // getQuestion(classTestId);
                                        getAllStudentAnswer(classTestId);
                                        getAllStudentMarks(classTestId);
                                        // getAllStudentTimeLeft(classTestId);


                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                }){ @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("schoolCodeId", String.valueOf(schoolId));
            parms.put("adminId", String.valueOf(adminId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getQuestion(final int classTestId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_STUDENT_QUESTION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int schoolCode = data.getInt("schoolCode");
                        int classTestId = data.getInt("classTestId");
                        int questionId = data.getInt("questionId");
                        String question = data.getString("question");
                        String option1 = data.getString("option1");
                        String option2 = data.getString("option2");
                        String option3 = data.getString("option3");
                        String option4 = data.getString("option4");
                        String answer = data.getString("correctAnswer");
                        String description = data.getString("description");

                        if(dbHelper.insertQuestion(schoolId, classTestId, questionId, question, option1, option2, option3, option4, answer, description)){
                            Log.e("msg","done");
                        } else {
                            Log.e("msg", "not done");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("classTestId", String.valueOf(classTestId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getAllStudentMarks(final int classTestId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_STUDENT_MARKS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("res",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject data = dataArray.getJSONObject(i);
                                int classTestId = data.getInt("classTestId");
                                int userId = data.getInt("userId");
                                int correctAnswer = data.getInt("correctAnswer");
                                int wrongAnswer = data.getInt("wrongAnswer");
                                int classTestMark = data.getInt("classTestMark");
                                int status = data.getInt("status");
                                String teacherRemark = data.getString("teacherRemark");

                                if (dbHelper.insertStudentMarks(classTestId, userId, correctAnswer, wrongAnswer, classTestMark, status, teacherRemark )) {
                                    Log.e("msg", "done");
                                } else {
                                    Log.e("msg", "not done");
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("classTestId", String.valueOf(classTestId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getAllStudentAnswer(final int classTestId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_STUDENT_ANSWER, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("res",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject data = dataArray.getJSONObject(i);
                                int classTestId = data.getInt("classTestId");
                                int userId = data.getInt("userId");
                                int questionId = data.getInt("questionId");
                                String answer = data.getString("answer");
                                int status = data.getInt("status");
                                String dateTime = data.getString("dateTime");

                                if (dbHelper.insertAnswer(classTestId, userId, questionId, answer, status, dateTime)) {
                                    Log.e("msg", "done");
                                } else {
                                    Log.e("msg", "not done");
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("classTestId", String.valueOf(classTestId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }




    private void getSchoolSubject(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_ALL_SUBJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int subjectId = dataobj.getInt("subjectId");
                                        int classId = dataobj.getInt("classId");
                                        String subjectName = dataobj.getString("subjectName");
                                        int schoolCode = dataobj.getInt("schoolCode");

                                        AdminDBHelper mydb = new AdminDBHelper(AdminHomeActivity.this);
                                        if(mydb.insertSchoolSubject(
                                                subjectId,
                                                classId,
                                                subjectName,
                                                schoolCode
                                        )

                                        ){
                                            Log.e("msg","done");
                                        } else{
                                            Log.e("msg","not done");

                                        }
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);


    }


    private void  getSchoolClass(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_CLASS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int classId = data.getInt("classId");
                        String className = data.getString("className");

                        AdminDBHelper mydb = new AdminDBHelper(AdminHomeActivity.this);
                        if(mydb.insertSchoolClass(
                                classId,
                                className
                        )

                        ){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSchoolSection(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_ALL_SECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int sectionId = dataobj.getInt("sectionId");
                                        int classId = dataobj.getInt("classId");
                                        String sectionName = dataobj.getString("sectionName");

                                        AdminDBHelper mydb = new AdminDBHelper(AdminHomeActivity.this);
                                        if(mydb.insertSchoolSection(
                                                sectionId,
                                                classId,
                                                sectionName
                                        )

                                        ){
                                            Log.e("msg","done");
                                        } else{
                                            Log.e("msg","not done");

                                        }
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        requestQueue.add(stringRequest);


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

                    final AdminHomePagerAdapter adapter = new AdminHomePagerAdapter(AdminHomeActivity.this,getSupportFragmentManager(), tabLayout.getTabCount());
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


                        Toast.makeText(AdminHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void btAddTestOnClick(View view) {

        Intent intent = new Intent(getApplicationContext(), AdminAddTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(AdminHomeActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AdminHomeActivity.super.onBackPressed();
                    }
                }).create().show();

    }
}
