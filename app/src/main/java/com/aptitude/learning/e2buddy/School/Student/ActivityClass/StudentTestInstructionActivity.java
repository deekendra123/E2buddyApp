package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StudentTestInstructionActivity extends AppCompatActivity {

    public static StudentTestInstructionActivity studentTestInstructionActivity;
    private String subjectName, testName, timeAllotted;
    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,classTestId,totaltime,time;
    private TextView tvTotalQuestion, tvSubjectNmae,tvTestName,tvMaxMarks,tvPassMarks,tvCorrectAnswerMarks,tvNotAttemptedMarks,tvWrongAnsMarks,tvTimeAlloted,tvStudentName;
    private StudentData studentData;
    private int id,schoolId,classId,sectionId,studentId,totalQuestion,subjectId;
    String studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName,schoolLogo;
    private ProgressBar progressBar;
    private AdminDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test_instruction);

        tvSubjectNmae = findViewById(R.id.tvSubjectNmae);
        tvTestName = findViewById(R.id.tvTestName);
        tvMaxMarks = findViewById(R.id.tvMaxMarks);
        tvPassMarks = findViewById(R.id.tvPassMarks);
        tvCorrectAnswerMarks = findViewById(R.id.tvCorrectAnswerMarks);
        tvNotAttemptedMarks = findViewById(R.id.tvNotAttemptedMarks);
        tvWrongAnsMarks = findViewById(R.id.tvWrongAnsMarks);
        tvTimeAlloted = findViewById(R.id.tvTimeAlloted);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);
        progressBar = findViewById(R.id.progressBar);

        dbHelper = new AdminDBHelper(this);

        studentTestInstructionActivity =this;

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentName = studentData.getStudentName();
        studentId = studentData.getId();

        subjectId = getIntent().getIntExtra("subjectId",-1);
        testName = getIntent().getStringExtra("testName");
        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        timeAllotted = getIntent().getStringExtra("timeAllotted");
        classTestId = getIntent().getIntExtra("classTestId",-1);

        time = Integer.parseInt(timeAllotted);

        totaltime = (int) TimeUnit.MINUTES.toMillis(time);

        tvStudentName.setText(""+studentName);
        tvSubjectNmae.setText(""+subjectName);
        tvTestName.setText(""+testName);
        tvMaxMarks.setText("Max Marks: "+maxMarks);
        tvPassMarks.setText("Pass Marks: "+passMarks);
        tvCorrectAnswerMarks.setText(""+corrMarks+" for Correct Ans.");
        tvNotAttemptedMarks.setText(""+notAttemptMarks+" for not Attempted");
        tvWrongAnsMarks.setText("-"+wrongAnsMarks+" for wrong Answer");
        tvTimeAlloted.setText(""+timeAllotted+" minutes");
        checkInternet();

    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            progressBar.setVisibility(View.VISIBLE);
            getSubjectDetails();

            getTotalQuestion(classTestId);
            getTimeLeft();

            progressBar.setVisibility(View.GONE);

            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    public void checkNetworkConnection(){
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
    public void btStartTestOnClick(View view) {


       checkInternet1();
    }


    private boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {

            Intent intent = new Intent(StudentTestInstructionActivity.this, StudentQuestionsActivity.class);
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("classTestId", classTestId);
            intent.putExtra("timeAllotted", totaltime);
            intent.putExtra("maxMarks", maxMarks);
            intent.putExtra("passMarks", passMarks);
            intent.putExtra("corrMarks", corrMarks);
            intent.putExtra("notAttemptMarks", notAttemptMarks);
            intent.putExtra("wrongAnsMarks", wrongAnsMarks);
            intent.putExtra("totalQuestion", totalQuestion);
            startActivity(intent);

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

    private void getTimeLeft(){

        List<String> timeLeftList = dbHelper.getTestTimeLeft(studentId,classTestId);

        for (int j = 0; j<timeLeftList.size(); j++){

            String timeLeft = timeLeftList.get(j);

            if (!timeLeft.equals(null)){
                totaltime = Integer.parseInt(timeLeft);
            }

        }


    }

    private void getTotalQuestion(final int classTestId){

        List<String> list = dbHelper.getTestTotalQuestion(classTestId);

        for (int j = 0; j<list.size(); j++){

            totalQuestion = Integer.parseInt(list.get(j));
            tvTotalQuestion.setText("No. of Questions: "+list.get(j));
        }
    }

    private void getSubjectDetails(){

        String subjectName = dbHelper.getSubjectName(subjectId);
        tvSubjectNmae.setText(""+subjectName);
    }


}
