package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ReviewQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;

import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;

import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminQuestionReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    private List<QuestionData> list;
    private ReviewQuestionAdapter reviewQuestionAdapter;
    private Button btsubmit,btAddQuestion;
    private int adminId,schoolId,classTestId,from,classId,queNumber;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage, className,TestName,questionStatus;
    private AdminData user;
    private SchoolData schoolData;
    private TextView tvTestName, tvSectionName, tvClassName,tvSchoolName;
    private CircleImageView imgSchoolLogo;
    private LinearLayout relativelayout2;
    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question_review);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        btsubmit = findViewById(R.id.btsubmit);
        btAddQuestion = findViewById(R.id.btAddQuestion);
        tvTestName = findViewById(R.id.tvTestName);
        tvSectionName = findViewById(R.id.tvSectionName);
        tvClassName = findViewById(R.id.tvClassName);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        relativelayout2 = findViewById(R.id.relativelayout2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        dbHelper = new AdminDBHelper(this);

        from = getIntent().getIntExtra("from",-1);
        classTestId = getIntent().getIntExtra("classTestId",-1);
        TestName = getIntent().getStringExtra("TestName");
        queNumber = getIntent().getIntExtra("queNumber",-1);
        questionStatus = getIntent().getStringExtra("questionStatus");

        tvTestName.setText(""+TestName);

        user = SchoolAdminSessionManager.getInstance(AdminQuestionReviewActivity.this).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(AdminQuestionReviewActivity.this).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();

        if (from==0){
            className = getIntent().getStringExtra("className");
            tvClassName.setText(""+className);
           checkInternet1();

            onCLick();

        }
        else {
            classId = getIntent().getIntExtra("className",-1);
            checkInternet2();
            relativelayout2.setVisibility(View.GONE);
          //  addQuestion();
        }

        tvSchoolName.setText(""+schoolName);

        Glide.with(AdminQuestionReviewActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        GetSectionName getSectionName = new GetSectionName(AdminQuestionReviewActivity.this, classTestId,tvSectionName);
        getSectionName.getSchoolSection();

    }

    private boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            getQuestion();
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


    private boolean checkInternet2(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            getQuestion();
            getClassName();
            return true;
        }
        else{
            checkNetworkConnection2();
            Log.d("Network","Not Connected");
            return false;
        }
    }



    private void checkNetworkConnection2(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkInternet2();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void onCLick(){
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               checkInternet();
            }
        });

        btAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            updateTest();
            insertClassTestData();

            progressBar.setVisibility(View.VISIBLE);

            List<QuestionData> questionDataList = dbHelper.getAllQuestion(schoolId, classTestId);

            for (int j = 0; j<questionDataList.size(); j++){
                QuestionData questionData = questionDataList.get(j);
                insertQuestion(questionData.getQuestionid(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(), questionData.getAnswer(), questionData.getDescription());
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);

                    if (questionStatus.equals("1")){
                        AdminAddTestActivity.adminAddTestActivity.finish();
                    }
                    else {
                        AdminUpdateTestActivity.AdminUpdateTestActivity.finish();
                    }

                    AdminAddQuestionActivity.adminAddQuestionActivity.finish();

                    onBackPressed();
                }
            }, 3000);



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

    private void updateTest(){

        if(dbHelper.updateClassTest(
                classTestId,
                queNumber,
                1
        )

        ){
            Log.e("msg","dk");
        } else{
            Log.e("msg","not done");

        }

    }
    private void insertQuestion(final int questionId, final String question, final String option1, final String option2, final String option3, final String option4, final String answer, final String description){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_QUESTION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AdminQuestionReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AdminQuestionReviewActivity.this);
        requestQueue.add(stringRequest);

    }



    private void getQuestion(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        reviewQuestionAdapter = new ReviewQuestionAdapter(AdminQuestionReviewActivity.this, list);
        List<QuestionData> questionDataList = dbHelper.getAllQuestion(schoolId, classTestId);

        for (int j = 0; j<questionDataList.size(); j++){
            QuestionData questionData = questionDataList.get(j);
            list.add(new QuestionData(questionData.getQuestionid(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(), questionData.getAnswer(), questionData.getDescription()));

            Log.e("data", questionData.getQuestion());
        }
        recyclerViewQuestions.setAdapter(reviewQuestionAdapter);
        recyclerViewQuestions.setItemViewCacheSize(list.size());
        progressBar.setVisibility(View.GONE);

    }


    private void getClassName(){

        List<ClassData> classDataList = dbHelper.getSchoolClass(classId);

        for (int j = 0; j<classDataList.size(); j++){

            ClassData classData = classDataList.get(j);
            String  className= classData.getClassName();
            tvClassName.setText(className);

        }


    }


    private void insertClassTestData(){

        List<ClassTestData> classTestDataList = dbHelper.getClassTest(classTestId);

        for (int j = 0; j<classTestDataList.size(); j++){
            final ClassTestData classTestData = classTestDataList.get(j);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_ADMIN_TEST_DATA, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(AdminQuestionReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("classTestId", String.valueOf(classTestId));
                    parms.put("classTestName",classTestData.getClassTestName());
                    parms.put("classId", String.valueOf(classTestData.getClassId()));
                    parms.put("sectionId", String.valueOf(classTestData.getSectionId()));
                    parms.put("subjectId", String.valueOf(classTestData.getSubjectId()));
                    parms.put("maxMark", String.valueOf(classTestData.getMaxMark()));
                    parms.put("passMark", String.valueOf(classTestData.getPassMark()));
                    parms.put("classTestDate",classTestData.getClassTestDate());
                    parms.put("timeAlloted", String.valueOf(classTestData.getTimeAllotted()));
                    parms.put("totalQuestion", String.valueOf(classTestData.getTotalQuestion()));
                    parms.put("correctAnswerMark", String.valueOf(classTestData.getCorrectAnswerMark()));
                    parms.put("wrongAnswerMark", String.valueOf(classTestData.getWrongAnswerMark()));
                    parms.put("notAttemptedMark", String.valueOf(classTestData.getNotAttemptedMark()));
                    parms.put("schoolCode", String.valueOf(schoolId));
                    parms.put("status", String.valueOf(classTestData.getStatus()));
                    parms.put("classTestAddedBy", String.valueOf(adminId));
                    parms.put("classTestVisible", "0");

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }


    }


}