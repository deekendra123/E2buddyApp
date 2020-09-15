package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.AddQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ExamQuestionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminExamQuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    private List<QuestionData> list;
    private ExamQuestionAdapter addQuestionAdapter;
    private Button btsubmit,btAddQuestion;
    private String className,TestName;
    private int examId,classId,schoolId,adminId,questionNumber;
    public static AdminExamQuestionActivity AdminExamQuestionActivity;
    private TextView tvTestName, tvSectionName, tvClassName,tvSchoolName;
    private CircleImageView imgSchoolLogo;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage,questionStatus;
    private AdminData user;
    private SchoolData schoolData;
    private boolean status = true;
    private AdminDBHelper dbHelper;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exam_question);

        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        btsubmit = findViewById(R.id.btsubmit);
        tvTestName = findViewById(R.id.tvTestName);
        tvSectionName = findViewById(R.id.tvSectionName);
        tvClassName = findViewById(R.id.tvClassName);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);

        dbHelper = new AdminDBHelper(this);

        user = SchoolAdminSessionManager.getInstance(this).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        schoolCode = user.getSchoolCode();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();

        tvSchoolName.setText(""+schoolName);

        Glide.with(AdminExamQuestionActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        AdminExamQuestionActivity = this;
        questionNumber = getIntent().getIntExtra("questionNumber",-1);
        examId = getIntent().getIntExtra("examId",-1);
        className = getIntent().getStringExtra("className");
        TestName = getIntent().getStringExtra("examName");
        questionStatus = getIntent().getStringExtra("questionStatus");


        tvClassName.setText(""+className);
        tvTestName.setText(""+TestName);
        checkInternet();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        addQuestionAdapter = new ExamQuestionAdapter(AdminExamQuestionActivity.this, list, btsubmit, examId,className,TestName,questionStatus);

        if (questionStatus.equals("1")){

            addQuestion();

        }
        else {

            getExistingQuestion();
        }

        onCLick();

    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            GetExamSection getSectionName = new GetExamSection(AdminExamQuestionActivity.this, examId,tvSectionName);
            getSectionName.getSchoolSection();
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


    private void addQuestion(){

        for (int i = 1; i<=questionNumber; i++){

            list.add(new QuestionData(i));

            if(dbHelper.insertExamQuestion(
                    schoolId,
                    examId,
                    i,
                    "null",
                    "null",
                    "null",
                    "null",
                    "null",
                    "null",
                    "null"
            )
            ){
                Log.e("msg","done");
            } else{
                Log.e("msg","not done");

            }
        }
        recyclerViewQuestions.setAdapter(addQuestionAdapter);
        recyclerViewQuestions.setItemViewCacheSize(list.size());
    }

    private void getExistingQuestion(){


        List<QuestionData> questionDataList = dbHelper.getAllExamQuestion(schoolId, examId);

        for (int j = 0; j<questionDataList.size(); j++){
            QuestionData questionData = questionDataList.get(j);
            list.add(new QuestionData(questionData.getQuestionid(), questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(), questionData.getAnswer(), questionData.getDescription()));

        }
        recyclerViewQuestions.setAdapter(addQuestionAdapter);
        recyclerViewQuestions.setItemViewCacheSize(list.size());

    }



    private void onCLick(){

        btsubmit.setOnClickListener(new View.OnClickListener() {
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
        final List<QuestionData> list1 = new ArrayList<>();
        status = true;

        List<QuestionData> questionDataList = dbHelper.getAllExamQuestion(schoolId, examId);

        for (int j = 0; j<questionDataList.size(); j++){
            QuestionData questionData = questionDataList.get(j);
            String question = questionData.getQuestion();
            String option1 = questionData.getOption1();
            String option2 = questionData.getOption2();
            String answer = questionData.getAnswer();
            list1.add(new QuestionData(question, option1, option2, answer));

        }

        if (list.size() == questionDataList.size()) {
            for (int i = 0; i < list1.size(); i++) {
                if (list1.get(i).getQuestion().equals("null") || list1.get(i).getOption1().equals("null") || list1.get(i).getOption2().equals("null") || list1.get(i).getAnswer().equals("Select Answer") || list1.get(i).getAnswer().equals("null")) {
                    Utils.showToast("Please check! " + "Fill all the mandatory fields");
                    status = false;
                    break;
                }
            }

            if (status==true) {

                Intent intent = new Intent(AdminExamQuestionActivity.this, AdminExamQuestionReviewActivity.class);
                intent.putExtra("from", 0);
                intent.putExtra("examId", examId);
                intent.putExtra("className", className);
                intent.putExtra("examName", TestName);
                intent.putExtra("queNumber",questionNumber);
                intent.putExtra("questionStatus",questionStatus);

                AdminExamQuestionActivity.this.startActivity(intent);
            }
        }
        else {
            Utils.showToast("Please check! Fill All the Questions and Answers");
        }

    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(AdminExamQuestionActivity.this)
                .setTitle("Close Alert?")
                .setMessage("Are you sure, You want to close? You can Edit this Test later")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        AdminExamQuestionActivity.super.onBackPressed();

                    }
                }).create().show();

    }

}