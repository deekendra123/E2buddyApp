package com.aptitude.learning.e2buddy.School.Student.ActivityClass;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
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
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.School.Student.FragmentClass.StudentExamFragment;
import com.aptitude.learning.e2buddy.School.Student.FragmentClass.StudentNoticeFragment;
import com.aptitude.learning.e2buddy.School.Student.FragmentClass.StudentTestFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudHomeActivity extends AppCompatActivity {

    private TextView tvStudentName,tvSchoolName;
    private StudentData studentData;
    private int id,schoolId,classId,sectionId,studentId;
    String studentName, studentImage, schoolName,schoolLogo;
    private GoogleSignInClient mGoogleSignInClient;
    private CircleImageView imgStudent,imgSchool;
    final StudentTestFragment studentTestFragment = new StudentTestFragment();
    final StudentNoticeFragment studentNoticeFragment = new StudentNoticeFragment();
    final StudentExamFragment studentExamFragment = new StudentExamFragment();

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = studentTestFragment;
    private AdminDBHelper dbHelper;
    private ProgressBar progressBar;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_home);
        progressBar = findViewById(R.id.progressBar);

        studentData = SchoolStudentSessionManager.getInstance(StudHomeActivity.this).getUser();
        studentId = studentData.getId();
        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        studentImage = studentData.getStudentImage();
        schoolId = studentData.getSchoolId();
        classId = studentData.getClassId();
        sectionId = studentData.getSectionId();
        schoolLogo = studentData.getSchoolLogo();
        dbHelper = new AdminDBHelper(StudHomeActivity.this);
        progressBar.setVisibility(View.VISIBLE);

        try {
            checkInternet3();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        imgStudent = findViewById(R.id.imgStudent);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        imgSchool = findViewById(R.id.imgSchool);

        Glide.with(StudHomeActivity.this)
                .load(studentImage)
                .into(imgStudent);

        tvSchoolName.setText(""+schoolName);

        Glide.with(StudHomeActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolLogo)
                .into(imgSchool);
        tvStudentName.setText(""+studentName);

        handleOnClick();

    }

    private boolean checkInternet3(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            progressBar.setVisibility(View.VISIBLE);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    //TODO your background code
                    dbHelper.deleteSubjectData();
                    dbHelper.deleteUserData();
                    dbHelper.deleteAdminData();
                    dbHelper.deleteClassTest();
                    dbHelper.deleteClassTestAnswer();
                    dbHelper.deleteClassTestMarks();
                   // dbHelper.deleteClassTestTimeLeft();
                    dbHelper.deleteClassTestQuestion();

                    getSchoolSubject();
                    getUserData();
                    getAdminData();
                    getAllClassTestData();
                    getExam();

                }
            });



          //  progressBar.setVisibility(View.GONE);

            return true;
        }
        else{
            checkNetworkConnection3();
            Log.d("Network","Not Connected");
            return false;
        }
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

                                        if(dbHelper.insertUserData(userId, userName, dateofBirth, userInfoId, classId, sectionId, schoolCode, admissionNumber, token,rollNo)){
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

        RequestQueue requestQueue = Volley.newRequestQueue(StudHomeActivity.this);
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

        RequestQueue requestQueue = Volley.newRequestQueue(StudHomeActivity.this);
        requestQueue.add(stringRequest);

    }


    private void getAllClassTestData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_ALL_CLASS_TEST,
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

                                        if (dbHelper.insertClassTest2(
                                                classTestId, classTestName, classId, sectionId, subjectId, maxMark, passMark, classTestDate,
                                                timeAlloted, totalQuestion, correctAnswerMark, wrongAnswerMark, notAttemptedMark, schoolCode, status, classTestAddedBy, classTestVisible
                                        )

                                        ) {
                                            Log.e("msg", "done");
                                        } else {
                                            Log.e("msg", "not done");

                                        }

                                        getQuestion(classTestId);
                                        getAllStudentAnswer(classTestId);
                                        getAllStudentMarks(classTestId);
                                        getAllStudentTimeLeft(classTestId);

                                    }

                                    status = true;

                                    if (status){
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                BottomNavigationView navigation = findViewById(R.id.navigation);
                                                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                                                fragmentManager.beginTransaction().add(R.id.frame_container, studentExamFragment, "3").hide(studentExamFragment).commit();
                                                fragmentManager.beginTransaction().add(R.id.frame_container, studentNoticeFragment, "2").hide(studentNoticeFragment).commit();
                                                fragmentManager.beginTransaction().add(R.id.frame_container,studentTestFragment, "1").commit();

                                            }
                                        }, 1000);
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
            parms.put("classId", String.valueOf(classId));
            parms.put("sectionId", String.valueOf(sectionId));

            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudHomeActivity.this);
        requestQueue.add(stringRequest);



    }

    private void getSchoolSubject(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_ALL_STUDENT_SUBJECT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dkkkkkkres",response);
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

                                Log.e("dksubject",subjectName);

                                if (!dbHelper.checkSchoolSubject(subjectId)){
                                    if(dbHelper.insertSchoolSubject(
                                            subjectId,
                                            classId,
                                            subjectName,
                                            schoolCode
                                    )
                                    ){
                                        Log.e("msg","done");
                                    }else {
                                        Log.e("msg", "not done");
                                    }
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

                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("classId", String.valueOf(classId));
            parms.put("schoolId", String.valueOf(schoolId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_test:
                    fragmentManager.beginTransaction().hide(active).show(studentTestFragment).commit();
                    active = studentTestFragment;
                    return true;

                case R.id.navigation_notice:
                    fragmentManager.beginTransaction().hide(active).show(studentNoticeFragment).commit();
                    active = studentNoticeFragment;
                    return true;

//                case R.id.navigation_exam:
//                    fragmentManager.beginTransaction().hide(active).show(studentExamFragment).commit();
//                    active = studentExamFragment;
//                    return true;

            }
            return false;
        }
    };


    
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(StudHomeActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        StudHomeActivity.super.onBackPressed();
                    }
                }).create().show();

    }

    private void handleOnClick(){
        imgStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StudHomeActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // logout
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();

                                mGoogleSignInClient = GoogleSignIn.getClient(StudHomeActivity.this,gso);

                                mGoogleSignInClient.signOut().addOnCompleteListener(StudHomeActivity.this,
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

                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void getAllStudentTimeLeft(final int classTestId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_TIME_LEFT, new com.android.volley.Response.Listener<String>() {
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
                                int timeLeft = data.getInt("timeLeft");

                                if (dbHelper.insertTestTimeLeft(classTestId, userId, timeLeft)) {
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
                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void getExam(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_EXAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("dkreskss",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject dataobj = dataArray.getJSONObject(i);

                                        int examTypeId = dataobj.getInt("examTypeId");
                                        int examId = dataobj.getInt("examId");
                                        String examName = dataobj.getString("examName");
                                        int classId = dataobj.getInt("classId");
                                        int sectionId = dataobj.getInt("sectionId");
                                        int subjectId = dataobj.getInt("subjectId");
                                        int maxMark = dataobj.getInt("maxMark");
                                        int passMark = dataobj.getInt("passMark");
                                        String examDate = dataobj.getString("examDate");
                                        String examStartTime = dataobj.getString("examStartTime");
                                        String examStopTime = dataobj.getString("examStopTime");
                                        int timeAlloted = dataobj.getInt("timeAlloted");
                                        int totalQuestion = dataobj.getInt("totalQuestion");
                                        int correctAnswerMark = dataobj.getInt("correctAnswerMark");
                                        int wrongAnswerMark = dataobj.getInt("wrongAnswerMark");
                                        int notAttemptedMark = dataobj.getInt("notAttemptedMark");
                                        int schoolCode = dataobj.getInt("schoolCode");
                                        int status = dataobj.getInt("status");
                                        int examAddedBy = dataobj.getInt("examAddedBy");
                                        int examVisible = dataobj.getInt("examVisible");
                                        int examResult = dataobj.getInt("examResult");

                                        if (dbHelper.insertExam(
                                                examTypeId, examId, examName, classId, sectionId, subjectId, maxMark, passMark, examDate,examStartTime,examStopTime,
                                                timeAlloted, totalQuestion, correctAnswerMark, wrongAnswerMark, notAttemptedMark, schoolCode, status, examAddedBy, examVisible,examResult
                                        )

                                        ) {
                                            Log.e("msg", "done");
                                        } else {
                                            Log.e("msg", "not done");

                                        }

                                        getExamQuestion(examId);
                                        getExamAnswer(examId);
                                    }


                                }
                                else {

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
            parms.put("schoolId", String.valueOf(schoolId));
            parms.put("classId", String.valueOf(classId));
            parms.put("sectionId", String.valueOf(sectionId));

            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getExamQuestion(final int examId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_EXAM_QUESTION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int questionId = data.getInt("questionId");
                        String questionImage = data.getString("questionImage");
                        String question = data.getString("question");
                        String option1 = data.getString("option1");
                        String option2 = data.getString("option2");
                        String option3 = data.getString("option3");
                        String option4 = data.getString("option4");
                        String answer = data.getString("correctAnswer");
                        String description = data.getString("description");


                        if(dbHelper.insertExamQuestion(schoolId, examId, questionId, questionImage, question, option1, option2, option3, option4, answer, description)){
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
                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("examId", String.valueOf(examId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getExamAnswer(final int examId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_EXAM_ANSWER, new com.android.volley.Response.Listener<String>() {
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
                                int classTestId = data.getInt("examId");
                                int userId = data.getInt("userId");
                                int questionId = data.getInt("questionId");
                                String answer = data.getString("answer");
                                int status = data.getInt("status");
                                String date = data.getString("date");
                                String time = data.getString("time");

                                if (dbHelper.insertExamAnswer(classTestId, userId, questionId, answer, status, date, time)) {
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

                        Toast.makeText(StudHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("examId", String.valueOf(examId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
