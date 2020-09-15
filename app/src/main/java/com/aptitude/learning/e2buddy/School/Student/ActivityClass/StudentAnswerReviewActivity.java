package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminQuestionReviewActivity;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ReviewQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentAnswerReviewAdapter;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentResultAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.MarksData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.UpdateStudentAnswer;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentQuestionsActivity.timeLeft;

public class StudentAnswerReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnswers;
    private Button btsubmit,btEdit;
    private StudentAnswerReviewAdapter adapter;
    private List<AnswerData> list;
    public static StudentAnswerReviewActivity studentAnswerReviewActivity;
    private StudentData studentData;
    private int id,schoolId,classId,sectionId,classTestId,studentId;
    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totalQuestion;
    String studentName, studentEmail, timeAllotted, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName,schoolLogo,subjectName;
    private TextView tvStudentName;
    private long timeLeft;

    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;
    boolean status = false;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_answer_review);
        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        btsubmit = findViewById(R.id.btsubmit);
        btEdit = findViewById(R.id.btEdit);
        tvStudentName = findViewById(R.id.tvStudentName);

        progressBar = findViewById(R.id.progressBar);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        studentAnswerReviewActivity = this;

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentId = studentData.getId();

        studentName = studentData.getStudentName();
        tvStudentName.setText(""+studentName);

        subjectName = getIntent().getStringExtra("subjectName");
        classTestId = getIntent().getIntExtra("classTestId",-1);

        timeAllotted = getIntent().getStringExtra("timeAllotted");

        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);

        timeLeft = getIntent().getLongExtra("timeLeft",-1);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentAnswerReviewActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewAnswers.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        adapter = new StudentAnswerReviewAdapter(StudentAnswerReviewActivity.this, list, studentId, classTestId, maxMarks,passMarks,corrMarks,notAttemptMarks,wrongAnsMarks,totalQuestion);

        dbHelper = new AdminDBHelper(this);

        progressBar.setVisibility(View.VISIBLE);
        getQuestion();
        onClick();

    }


    private void getQuestion(){

        List<AnswerData> answerDataList = dbHelper.getUserAnswer(classTestId, studentId);

        for (int j = 0; j<answerDataList.size(); j++){
            AnswerData answerData = answerDataList.get(j);
            list.add(new AnswerData(answerData.getQuestionId(), answerData.getQuestion(), answerData.getCorrAnswer(), answerData.getUserAnswer()));

        }
        recyclerViewAnswers.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

    }


    private void onClick(){

        if (timeLeft>0){

            btEdit.setVisibility(View.VISIBLE);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });
        }
        else {
            btEdit.setVisibility(View.GONE);
            //  Utils.showToast("Your time has been over. You can not modify your answer.");
        }

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(StudentAnswerReviewActivity.this)
                        .setTitle("CONGRATULATIONS!")
                        .setMessage("Your answers are Successfully submitted. \n\n Click DONE.")
                        .setCancelable(false)
                         .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                checkInternet1();
                            }
                        }).create().show();

            //   checkInternet1();
            }
        });
    }

    private boolean checkInternet1(){

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            progressBar.setVisibility(View.VISIBLE);

            final String timeLeft = dbHelper.getStudentTimeLeft(studentId,classTestId);

            UpdateStudentAnswer updateStudentAnswer = new UpdateStudentAnswer(StudentAnswerReviewActivity.this,studentId,classTestId);
            updateStudentAnswer.updateAnswer();

            UpdateStudentTestMarks updateStudentTestMarks = new UpdateStudentTestMarks(StudentAnswerReviewActivity.this,studentId,classTestId,totalQuestion,maxMarks,passMarks,corrMarks,notAttemptMarks,wrongAnsMarks,dbHelper);
            updateStudentTestMarks.updateStudentMarks();

            List<AnswerData> answerDataList = dbHelper.getUserAllAnswer(classTestId, studentId);

            for (int j = 0; j<answerDataList.size(); j++){
                AnswerData answerData = answerDataList.get(j);
                insertAnswer(answerData.getQuestionId(),answerData.getUserAnswer(), answerData.getStatus(), answerData.getDateTime(), timeLeft);
            }

            MarksData marksData = dbHelper.getStudentMarks(classTestId,studentId);
            insertMarks(marksData);

            while (i < 5) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_TOTAL_ANSWER,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {

                                        int totalAnswer = jsonObject.getInt("data");
                                        int tA = dbHelper.getTotalAnswerCount(classTestId,studentId);
                                        if (totalAnswer != tA){
                                            List<AnswerData> answerDataList = dbHelper.getUserAllAnswer(classTestId, studentId);

                                            for (int j = 0; j<answerDataList.size(); j++){
                                                AnswerData answerData = answerDataList.get(j);
                                                insertAnswer(answerData.getQuestionId(),answerData.getUserAnswer(), answerData.getStatus(), answerData.getDateTime(), timeLeft);
                                            }
                                            status = false;
                                        }
                                        else {
                                            status = true;
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

                                Log.e("DKKKerror", ""+error.getMessage());
                                Toast.makeText(StudentAnswerReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> parms = new HashMap<>();
                        parms.put("classTestId", String.valueOf(classTestId));
                        return parms;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

                if (status){
                    break;
                }
                else {
                    i++;
                }

            }

            progressBar.setVisibility(View.GONE);
            StudentTestInstructionActivity.studentTestInstructionActivity.finish();
            StudentQuestionsActivity.studentQuestionsActivity.finish();

            Intent intent = new Intent(StudentAnswerReviewActivity.this, StudentResultActivity.class);
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("classTestId", classTestId);
            intent.putExtra("timeAllotted", timeAllotted);
            intent.putExtra("maxMarks", maxMarks);
            intent.putExtra("passMarks", passMarks);
            intent.putExtra("corrMarks", corrMarks);
            intent.putExtra("notAttemptMarks", notAttemptMarks);
            intent.putExtra("wrongAnsMarks", wrongAnsMarks);
            intent.putExtra("totalQuestion",totalQuestion);
            intent.putExtra("teacherRemark","0");

            startActivity(intent);
            StudentAnswerReviewActivity.this.finish();
            return true;

        }
        else{
            checkNetworkConnection1();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    private void insertMarks(final MarksData marksData){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_STUDENT_MARKS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StudentAnswerReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("classTestId", String.valueOf(classTestId));
                parms.put("userId", String.valueOf(studentId));
                parms.put("correctAnswer", String.valueOf(marksData.getCorrectAnswer()));
                parms.put("wrongAnswer", String.valueOf(marksData.getWrongAnswer()));
                parms.put("classTestMark", String.valueOf(marksData.getClassTestMark()));
                parms.put("status", String.valueOf(marksData.getStatus()));
                return parms;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentAnswerReviewActivity.this);
        requestQueue.add(stringRequest);

    }
    public void checkNetworkConnection1(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Log.e("msg","");

        return false;
    }


    private void insertAnswer(final int questionId, final String answer, final int status, final String dateTime, final String timeLeft){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.INSERT_STUDENT_ANSWER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StudentAnswerReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("classTestId", String.valueOf(classTestId));
                parms.put("userId", String.valueOf(studentId));
                parms.put("questionId", String.valueOf(questionId));
                parms.put("answer",answer);
                parms.put("dateTime",dateTime);
                parms.put("timeLeft", timeLeft);
                parms.put("answerStatus", String.valueOf(status));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
