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

import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentResultAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentResultActivity extends AppCompatActivity {

    private RecyclerView recyclerViewResult;
    private StudentResultAdapter resultAdapter;
    private Button btDone;
    private List<AnswerData> list;
    private StudentData studentData;
    private int id,schoolId,classId,sectionId,classTestId,studentId;
    private TextView tvTotalQuestion,tvCorrectAnswer,tvAttemptedQuestion,tvWrongAns,tvRemarks;
    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totalQuestion;

    String answer;
    private int correctAnswerCount=0;
    private int wrongAnswerCount=0;
    private int attemptedQuestion=0;
    private int notAttempted = 0;
    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;


    private String teacherRemark, studentName, studentEmail, studentDob, studentImage, timeAllotted, schoolName, schoolCode,city,state,pincode,type,className,sectionName,schoolLogo,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);
        recyclerViewResult = findViewById(R.id.recyclerViewResult);
        btDone = findViewById(R.id.btDone);
        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        tvAttemptedQuestion = findViewById(R.id.tvAttemptedQuestion);
        tvWrongAns = findViewById(R.id.tvWrongAns);
        progressBar = findViewById(R.id.progressBar);
        tvRemarks = findViewById(R.id.tvRemarks);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        dbHelper = new AdminDBHelper(this);

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentId = studentData.getId();

        studentName = studentData.getStudentName();
        schoolId = studentData.getSchoolId();

        subjectName = getIntent().getStringExtra("subjectName");
        classTestId = getIntent().getIntExtra("classTestId",-1);

        timeAllotted = getIntent().getStringExtra("timeAllotted");

        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        teacherRemark = getIntent().getStringExtra("teacherRemark");

        if (!teacherRemark.equals("0")){
         tvRemarks.setText("Remark: "+teacherRemark);
        }
        else {
            tvRemarks.setVisibility(View.GONE);
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentResultActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewResult.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        resultAdapter = new StudentResultAdapter(StudentResultActivity.this, list, tvCorrectAnswer,tvAttemptedQuestion,tvWrongAns, studentId, classTestId, maxMarks,passMarks,corrMarks,notAttemptMarks,wrongAnsMarks,totalQuestion);

        checkInternet();
        onClick();


    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            progressBar.setVisibility(View.VISIBLE);

            getTotalQuestion(classTestId);
            getQuestion();
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

    private void onClick(){
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternet1();
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
            onBackPressed();
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

        List<com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData> questionDataList = dbHelper.getAllQuestion(schoolId, classTestId);
        for (int j = 0; j<questionDataList.size(); j++){
            com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData questionData = questionDataList.get(j);

            getUserAnswer(questionData.getQuestionid(),questionData.getQuestion(),questionData.getAnswer());

        }

    }


    private void getUserAnswer(final int questionId, final String question, final String correctAnswer){

        String userAnswer = dbHelper.getUserAllAnswer(classTestId,studentId,questionId);
        if (!userAnswer.isEmpty()) {
            list.add(new AnswerData(questionId, question, correctAnswer, userAnswer));

            if (userAnswer.equals(correctAnswer)) {

                correctAnswerCount = correctAnswerCount + 1;

            } else {

                wrongAnswerCount = wrongAnswerCount + 1;

            }

            attemptedQuestion = attemptedQuestion + 1;
        }
        else {
            list.add(new AnswerData(questionId, question, correctAnswer, "Not Attempted"));
        }

        tvAttemptedQuestion.setText("Attempted: "+attemptedQuestion);

        tvTotalQuestion.setText("Total Question: "+ totalQuestion);
        tvCorrectAnswer.setText("Correct: "+correctAnswerCount);
        tvWrongAns.setText("Wrong: "+wrongAnswerCount);

        Collections.sort(list, new Comparator<AnswerData>() {
            @Override
            public int compare(AnswerData lhs, AnswerData rhs) {
                return lhs.getQuestionId()- rhs.getQuestionId();
            }
        });

        recyclerViewResult.setAdapter(resultAdapter);
        progressBar.setVisibility(View.GONE);


    }

    private void getTotalQuestion(final int classTestId){

        totalQuestion = dbHelper.getTotalQuestion(classTestId);
        tvTotalQuestion.setText("Total Question: "+totalQuestion);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
