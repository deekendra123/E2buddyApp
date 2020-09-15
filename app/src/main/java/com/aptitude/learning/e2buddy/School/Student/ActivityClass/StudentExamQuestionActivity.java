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
import android.view.KeyEvent;
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
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentExamQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.MarksData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.UpdateStudentAnswer;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StudentExamQuestionActivity extends AppCompatActivity {

    private List<QuestionData> list;
    private StudentExamQuestionAdapter studentQuestionAdapter;
    private RecyclerView recyclerViewQuestions;
    public static StudentExamQuestionActivity studentExamQuestionActivity;
    private int id,schoolId,classId,sectionId,classTestId,studentId,timeAllotted;
    String  studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName,schoolLogo,subjectName;
    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totalQuestion;

    private StudentData studentData;

    private TextView tvTimeAlloted;
    public static long timeLeft;

    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_exam_question);

        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        tvTimeAlloted = findViewById(R.id.tvTimeAlloted);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        subjectName = getIntent().getStringExtra("subjectName");
        classTestId = getIntent().getIntExtra("examId",-1);
        timeAllotted = getIntent().getIntExtra("timeAllotted",-1);

        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);



        studentExamQuestionActivity = this;
        dbHelper = new AdminDBHelper(this);
        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentId = studentData.getId();

        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        schoolLogo = studentData.getSchoolLogo();
        studentImage = studentData.getStudentImage();
        schoolId = studentData.getSchoolId();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentExamQuestionActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        studentQuestionAdapter = new StudentExamQuestionAdapter(StudentExamQuestionActivity.this, list, classTestId, studentId);
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
                    new AlertDialog.Builder(StudentExamQuestionActivity.this)
                            .setTitle("Submit Alert?")
                            .setMessage("You ran out of Time, Please submit your test.")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancel();
                                    Intent intent = new Intent(StudentExamQuestionActivity.this, StudentExamAnswerReviewActivity.class);
                                    intent.putExtra("subjectName", subjectName);
                                    intent.putExtra("examId", classTestId);
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

    private void getQuestion(){

        List<com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData> questionDataList = dbHelper.getExamRandomQuestion(schoolId, classTestId, 3);
        for (int j = 0; j<questionDataList.size(); j++){
            com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData questionData = questionDataList.get(j);
            list.add(new QuestionData(questionData.getQuestionid(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(),questionData.getAnswer(),questionData.getDescription()));
        }

        recyclerViewQuestions.setAdapter(studentQuestionAdapter);
        recyclerViewQuestions.setItemViewCacheSize(list.size());
        progressBar.setVisibility(View.GONE);

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
            Intent intent = new Intent(StudentExamQuestionActivity.this, StudentExamAnswerReviewActivity.class);
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("examId", classTestId);
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





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
           Utils.showToast("Please Submit the Exam");
        return false;
    }
}