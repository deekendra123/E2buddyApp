package com.aptitude.learning.e2buddy.School.Student.ActivityClass;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.MarksData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.UpdateStudentAnswer;
import com.aptitude.learning.e2buddy.Util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StudentQuestionsActivity extends AppCompatActivity {

    private List<QuestionData> list;
    private StudentQuestionAdapter studentQuestionAdapter;
    private RecyclerView recyclerViewQuestions;
    public static StudentQuestionsActivity studentQuestionsActivity;

    private StudentData studentData;
    private int id,schoolId,classId,sectionId,classTestId,studentId,timeAllotted;
    String  studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName,schoolLogo,subjectName;

    private TextView tvTimeAlloted;
    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totalQuestion;
    public int counter;
    public static long timeLeft=98;

    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;

    private int i = 0;
    private boolean status = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        tvTimeAlloted = findViewById(R.id.tvTimeAlloted);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        studentQuestionsActivity = this;
        dbHelper = new AdminDBHelper(this);
        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentId = studentData.getId();

        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        schoolLogo = studentData.getSchoolLogo();
        studentImage = studentData.getStudentImage();
        schoolId = studentData.getSchoolId();
        classId = studentData.getClassId();
        sectionId = studentData.getSectionId();

        subjectName = getIntent().getStringExtra("subjectName");
        classTestId = getIntent().getIntExtra("classTestId",-1);
        timeAllotted = getIntent().getIntExtra("timeAllotted",-1);

        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);

        tvTimeAlloted.setText(""+timeAllotted);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentQuestionsActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        studentQuestionAdapter = new StudentQuestionAdapter(StudentQuestionsActivity.this, list, classTestId, studentId);
        getQuestion();
        startTimer();

    }


    private void startTimer(){

        new CountDownTimer(timeAllotted,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tvTimeAlloted.setText(text);

                timeLeft = millisUntilFinished;
            }
            @Override
            public void onFinish() {
                tvTimeAlloted.setText("00:00");

                if(!isFinishing())
                {
                    //show dialog
                    new AlertDialog.Builder(StudentQuestionsActivity.this)
                            .setTitle("Submit Alert?")
                            .setMessage("You ran out of Time, Please submit your test.")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancel();
                                    Intent intent = new Intent(StudentQuestionsActivity.this, StudentAnswerReviewActivity.class);
                                    intent.putExtra("subjectName", subjectName);
                                    intent.putExtra("classTestId", classTestId);
                                    intent.putExtra("timeAllotted", timeAllotted);
                                    intent.putExtra("maxMarks", maxMarks);
                                    intent.putExtra("passMarks", passMarks);
                                    intent.putExtra("corrMarks", corrMarks);
                                    intent.putExtra("notAttemptMarks", notAttemptMarks);
                                    intent.putExtra("wrongAnsMarks", wrongAnsMarks);
                                    intent.putExtra("totalQuestion",totalQuestion);
                                    intent.putExtra("timeLeft",0);
                                    startActivity(intent);
                                }
                            }).create().show();
                }
            }
        }.start();
    }

    public void btSubmitClick(View view) {

        checkInternet1();
    }

    private boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Intent intent = new Intent(StudentQuestionsActivity.this, StudentAnswerReviewActivity.class);
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("classTestId", classTestId);
            intent.putExtra("timeAllotted", timeAllotted);
            intent.putExtra("maxMarks", maxMarks);
            intent.putExtra("passMarks", passMarks);
            intent.putExtra("corrMarks", corrMarks);
            intent.putExtra("notAttemptMarks", notAttemptMarks);
            intent.putExtra("wrongAnsMarks", wrongAnsMarks);
            intent.putExtra("totalQuestion",totalQuestion);
            intent.putExtra("timeLeft",timeLeft);
            startActivity(intent);
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

    private void getQuestion(){

        List<com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData> questionDataList = dbHelper.getAllRandomQuestion(schoolId, classTestId, totalQuestion);
        for (int j = 0; j<questionDataList.size(); j++){
            com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData questionData = questionDataList.get(j);
            list.add(new QuestionData(questionData.getQuestionid(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(),questionData.getAnswer(),questionData.getDescription()));
        }

        recyclerViewQuestions.setAdapter(studentQuestionAdapter);
        recyclerViewQuestions.setItemViewCacheSize(list.size());
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(StudentQuestionsActivity.this)
                .setTitle("FINISH ALERT?")
                .setMessage("Are you sure to finish the Test? \n\n You will not be allowed to attempt this test again.")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                            checkInternet2();

                    }
                }).create().show();

    }

    private boolean checkInternet2(){

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            progressBar.setVisibility(View.VISIBLE);

            final String timeLeft = dbHelper.getStudentTimeLeft(studentId,classTestId);

            UpdateStudentAnswer updateStudentAnswer = new UpdateStudentAnswer(StudentQuestionsActivity.this,studentId,classTestId);
            updateStudentAnswer.updateAnswer();

            UpdateStudentTestMarks updateStudentTestMarks = new UpdateStudentTestMarks(StudentQuestionsActivity.this,studentId,classTestId,totalQuestion,maxMarks,passMarks,corrMarks,notAttemptMarks,wrongAnsMarks,dbHelper);
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
                                Toast.makeText(StudentQuestionsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                StudentQuestionsActivity.super.onBackPressed();
            return true;
        }
        else{
            checkNetworkConnection2();
            return false;
        }
    }

    public void checkNetworkConnection2(){
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

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(StudentQuestionsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void insertMarks(final MarksData marksData){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_STUDENT_MARKS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StudentQuestionsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentQuestionsActivity.this);
        requestQueue.add(stringRequest);

    }






}
