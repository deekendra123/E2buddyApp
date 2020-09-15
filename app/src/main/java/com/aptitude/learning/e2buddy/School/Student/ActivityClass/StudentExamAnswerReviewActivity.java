package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentAnswerReviewAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.MarksData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentExamAnswerReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnswers;
    private Button btsubmit,btEdit;
    private StudentAnswerReviewAdapter adapter;
    private List<AnswerData> list;
    public static StudentExamAnswerReviewActivity studentExamAnswerReviewActivity;
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
        setContentView(R.layout.activity_student_exam_question_review);

        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        btsubmit = findViewById(R.id.btsubmit);
        btEdit = findViewById(R.id.btEdit);
        tvStudentName = findViewById(R.id.tvStudentName);

        progressBar = findViewById(R.id.progressBar);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        studentExamAnswerReviewActivity = this;

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentId = studentData.getId();

        studentName = studentData.getStudentName();
        tvStudentName.setText(""+studentName);

        subjectName = getIntent().getStringExtra("subjectName");
        classTestId = getIntent().getIntExtra("examId",-1);
        timeAllotted = getIntent().getStringExtra("timeAllotted");
        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);
        timeLeft = getIntent().getLongExtra("timeLeft",-1);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentExamAnswerReviewActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewAnswers.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new StudentAnswerReviewAdapter(StudentExamAnswerReviewActivity.this, list, studentId, classTestId, maxMarks,passMarks,corrMarks,notAttemptMarks,wrongAnsMarks,totalQuestion);
        dbHelper = new AdminDBHelper(this);

        progressBar.setVisibility(View.VISIBLE);

        getQuestion();

        onClick();


    }


    private void getQuestion(){

        List<AnswerData> answerDataList = dbHelper.getExamUserAnswer(classTestId, studentId);

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
        }

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(StudentExamAnswerReviewActivity.this)
                        .setTitle("CONGRATULATIONS!")
                        .setMessage("Your answers are Successfully submitted. \n\n Click DONE.")
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                checkInternet1();
                            }
                        }).create().show();

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

            UpdateStudentExamAnswer updateStudentAnswer = new UpdateStudentExamAnswer(StudentExamAnswerReviewActivity.this,studentId,classTestId);
            updateStudentAnswer.updateAnswer();

            UpdateStudentExamTestMarks updateStudentTestMarks = new UpdateStudentExamTestMarks(StudentExamAnswerReviewActivity.this,studentId,classTestId,totalQuestion,maxMarks,passMarks  ,corrMarks,notAttemptMarks,wrongAnsMarks,dbHelper);
            updateStudentTestMarks.updateStudentMarks();

            List<AnswerData> answerDataList = dbHelper.getUserAllExamAnswer(classTestId, studentId);


            for (int j = 0; j<answerDataList.size(); j++){
                AnswerData answerData = answerDataList.get(j);
                insertAnswer(answerData.getQuestionId(),answerData.getUserAnswer(), answerData.getStatus(), answerData.getDate(), answerData.getTime(), timeLeft);
            }

            MarksData marksData = dbHelper.getStudentExamMarks(classTestId,studentId);
            insertMarks(marksData);

            progressBar.setVisibility(View.GONE);
            ExamInstructionActivity.examInstructionActivity.finish();
            StudentExamQuestionActivity.studentExamQuestionActivity.finish();
            finish();
            onBackPressed();
            return true;

        }
        else{
            checkNetworkConnection1();
            Log.d("Network","Not Connected");
            return false;
        }
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

    private void insertMarks(final MarksData marksData){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.INSERT_EXAM_MARKS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StudentExamAnswerReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("examId", String.valueOf(classTestId));
                parms.put("userId", String.valueOf(studentId));
                parms.put("correctAnswer", String.valueOf(marksData.getCorrectAnswer()));
                parms.put("wrongAnswer", String.valueOf(marksData.getWrongAnswer()));
                parms.put("classTestMark", String.valueOf(marksData.getClassTestMark()));
                parms.put("status", String.valueOf(marksData.getStatus()));
                parms.put("date", String.valueOf(marksData.getDate()));
                parms.put("time", String.valueOf(marksData.getTime()));

                return parms;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentExamAnswerReviewActivity.this);
        requestQueue.add(stringRequest);

    }

    private void insertAnswer(final int questionId, final String answer, final int status, final String date, final String time, final long timeLeft){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.INSERT_EXAM_ANSWER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StudentExamAnswerReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                HashMap<String, String> parms = new HashMap<>();
                parms.put("examId", String.valueOf(classTestId));
                parms.put("userId", String.valueOf(studentId));
                parms.put("questionId", String.valueOf(questionId));
                parms.put("answer",answer);
                parms.put("date",date);
                parms.put("time",time);
                parms.put("timeLeft", String.valueOf(timeLeft));
                parms.put("answerStatus", String.valueOf(status));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}