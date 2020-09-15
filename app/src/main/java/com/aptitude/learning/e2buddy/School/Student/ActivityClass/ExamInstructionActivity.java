package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ExamInstructionActivity extends AppCompatActivity {

    public static ExamInstructionActivity examInstructionActivity;
    private String subjectName, testName,startTime,endTime;
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
        setContentView(R.layout.activity_exam_instruction);

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

        examInstructionActivity =this;

        studentData = SchoolStudentSessionManager.getInstance(this).getUser();
        studentName = studentData.getStudentName();
        studentId = studentData.getId();
        schoolId = studentData.getSchoolId();

        subjectId = getIntent().getIntExtra("subjectId",-1);
        testName = getIntent().getStringExtra("testName");
        maxMarks = getIntent().getIntExtra("maxMarks",-1);
        passMarks = getIntent().getIntExtra("passMarks",-1);
        corrMarks = getIntent().getIntExtra("corrMarks",-1);
        notAttemptMarks = getIntent().getIntExtra("notAttemptMarks",-1);
        wrongAnsMarks = getIntent().getIntExtra("wrongAnsMarks",-1);
        time = getIntent().getIntExtra("timeAllotted",-1);
        classTestId = getIntent().getIntExtra("examId",-1);
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        totaltime = (int) TimeUnit.MINUTES.toMillis(time);

        tvStudentName.setText(""+studentName);
        tvSubjectNmae.setText(""+subjectName);
        tvTestName.setText(""+testName);
        tvMaxMarks.setText("Max Marks: "+maxMarks);
        tvPassMarks.setText("Pass Marks: "+passMarks);
        tvCorrectAnswerMarks.setText(""+corrMarks+" for Correct Ans.");
        tvNotAttemptedMarks.setText(""+notAttemptMarks+" for not Attempted");
        tvWrongAnsMarks.setText("-"+wrongAnsMarks+" for wrong Answer");
        tvTimeAlloted.setText(""+time+" minutes");
        checkInternet();



        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable(){
            public void run(){
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
                String currentTime = dateFormat.format(c);

                if (currentTime.equals(startTime)){
                    handler.removeMessages(0);

                    Intent intent = new Intent(ExamInstructionActivity.this, StudentExamQuestionActivity.class);
                    intent.putExtra("subjectName", subjectName);
                    intent.putExtra("examId", classTestId);
                    intent.putExtra("timeAllotted", totaltime);
                    intent.putExtra("maxMarks", maxMarks);
                    intent.putExtra("passMarks", passMarks);
                    intent.putExtra("corrMarks", corrMarks);
                    intent.putExtra("notAttemptMarks", notAttemptMarks);
                    intent.putExtra("wrongAnsMarks", wrongAnsMarks);
                    intent.putExtra("totalQuestion", totalQuestion);
                    finish();
                    startActivity(intent);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);

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

    private void getTotalQuestion(final int classTestId){

        List<String> list = dbHelper.getExamTotalQuestion(classTestId);

        for (int j = 0; j<list.size(); j++){

            totalQuestion = Integer.parseInt(list.get(j));
            tvTotalQuestion.setText("No. of Questions: "+list.get(j));
        }
    }

    private void getSubjectDetails(){

        String subjectName = dbHelper.getSubjectName(subjectId);
        tvSubjectNmae.setText(""+subjectName);
    }


    public void updateStatus(final String status){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String date = sdf.format(new Date());
        final String time1 = sdfTime.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.UPDATE_EXAM_STATUS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Toast.makeText(ExamInstructionActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ExamInstructionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("examId", String.valueOf(classTestId));
                parms.put("schoolId", String.valueOf(schoolId));
                parms.put("userId", String.valueOf(studentId));
                parms.put("status", String.valueOf(status));
                parms.put("date", date);
                parms.put("time", time1);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateStatus("0");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        updateStatus("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus("1");
    }
}