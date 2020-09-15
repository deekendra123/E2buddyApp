package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSectionAdapter1;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSpinAdapter;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ExamSectionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ExamTypeData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.ExamTypeAdapter;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.SpinAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUpdateExamActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSection;
    private ExamSectionAdapter classSectionAdapter;
    private EditText etMaxMarks, etPassMarks, etTimeAlloted, etCorrectAnswerMark,etWrongAnswerMark,etNotAttempted;
    private TextView tvTestDate,tvSchoolName;
    private Spinner spinnerClass, spinnerSubject,spinnerExamName,spinnerStopTime,spinnerStartTime;
    private int classId, subjectId;
    private int sectionId = 1,examId,status,questionNumber;
    private Button btAddTest;
    private int adminId,schoolId,from;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage, className,testdate,subjectName,testName,timeAllotted,questionStatus,startTime,endTime;
    private AdminData user;
    private SchoolData schoolData;

    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totaltime,time,studentClassId,studentSubjectId,maxMark,passMark,
            timeAlloted,correctAnswerMark,wrongAnswerMark,notAttemptedMark,studentSectionId,classTestAddedBy,examTypeId;

    public static AdminUpdateExamActivity adminUpdateExamActivity;
    private AdminDBHelper dbHelper;

    private String examId1,classTestName,classTestDate;
    private CircleImageView imgAdmin,imgSchoolLogo;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SpinAdapter adapter;
    private ClassSpinAdapter classSpinAdapter;
    private ProgressBar progressBar;
    private ExamTypeAdapter examTypeAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_exam);

        recyclerViewSection = findViewById(R.id.recyclerViewSection);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etMaxMarks = findViewById(R.id.etMaxMarks);
        etPassMarks = findViewById(R.id.etPassMarks);
        tvTestDate = findViewById(R.id.tvTestDate);
        etTimeAlloted = findViewById(R.id.etTimeAlloted);
        etCorrectAnswerMark = findViewById(R.id.etCorrectAnswerMark);
        etWrongAnswerMark = findViewById(R.id.etWrongAnswerMark);
        btAddTest = findViewById(R.id.btAddTest);
        spinnerExamName = findViewById(R.id.spinnerExamName);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        etNotAttempted = findViewById(R.id.etNotAttempted);
        progressBar = findViewById(R.id.progressBar);

        spinnerStopTime = findViewById(R.id.spinnerStopTime);
        spinnerStartTime = findViewById(R.id.spinnerStartTime);


        progressBar.setVisibility(View.VISIBLE);

        adminUpdateExamActivity = this;
        dbHelper = new AdminDBHelper(this);
        dbHelper.deleteAllSection();

        user = SchoolAdminSessionManager.getInstance(this).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();
        tvSchoolName.setText(""+schoolName);

        examId = getIntent().getIntExtra("examId",-1);
        questionStatus = getIntent().getStringExtra("questionStatus");
        questionNumber = getIntent().getIntExtra("questionNumber",-1);


        Glide.with(AdminUpdateExamActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        getTestDetails(examId);


        tvTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUpdateExamActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = String.valueOf(dayOfMonth);
                                String month = String.valueOf(monthOfYear+1);


                                for (int i=1;i<=MONTHS.length;i++) {

                                    if (i==monthOfYear){
                                        tvTestDate.setText(dayOfMonth + " "
                                                + (MONTHS[i]) + " " + year);
                                    }
                                }

                                if(monthOfYear<9){

                                    month = "0"+month;
                                }
                                if (dayOfMonth<10){
                                    day="0"+day;

                                }

                                testdate = day+"-"+month+"-"+year;


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        btAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int maxMarks = Integer.parseInt(etMaxMarks.getText().toString());
                int passMarks = Integer.parseInt(etPassMarks.getText().toString());
                int corrAnsMarks = Integer.parseInt(etCorrectAnswerMark.getText().toString());
                int wrongAnsMarks = Integer.parseInt(etWrongAnswerMark.getText().toString());
                int notAttemptedMarks = Integer.parseInt(etNotAttempted.getText().toString());

                final ArrayList array_list = dbHelper.getAllSection();

               if (etMaxMarks.getText().toString().isEmpty()) {
                    etMaxMarks.setError("Max Marks is required");
                    etMaxMarks.requestFocus();
                    return;
                }

                else if (etPassMarks.getText().toString().isEmpty()) {
                    etPassMarks.setError("Pass Marks is required");
                    etPassMarks.requestFocus();
                    return;
                }

                else if (etTimeAlloted.getText().toString().isEmpty()) {
                    etTimeAlloted.setError("Time Allotted is required");
                    etTimeAlloted.requestFocus();
                    return;
                }

                else if (etCorrectAnswerMark.getText().toString().isEmpty()) {
                    etCorrectAnswerMark.setError("Correct Answer Marks is required");
                    etCorrectAnswerMark.requestFocus();
                    return;
                }

                else if (etWrongAnswerMark.getText().toString().isEmpty()) {
                    etWrongAnswerMark.setError("Wrong Answer Marks is required");
                    etWrongAnswerMark.requestFocus();
                    return;
                }

                else if (etNotAttempted.getText().toString().isEmpty()) {
                    etNotAttempted.setError("Not Attempted Marks is required");
                    etNotAttempted.requestFocus();
                    return;
                }
                else if (passMarks>maxMarks) {
                    Utils.showToast("Pass Marks should be lesser then Max Marks");

                }
                else if (corrAnsMarks>passMarks) {
                    Utils.showToast("Correct Answer Marks should be lesser then Pass Marks");
                }
                else if (wrongAnsMarks>corrAnsMarks) {

                    Utils.showToast("Wrong Answer Marks should be lesser then Correct Answer Marks");
                }
                else if (notAttemptedMarks>wrongAnsMarks) {
                    Utils.showToast("Not Attempted Marks should be lesser then Wrong Answer Marks");
                }
                else if (maxMarks<=0||passMarks<=0||corrAnsMarks<=0) {

                    Utils.showToast("Max Marks, Pass Marks and Correct Answer Marks can not be zero");

                }

                else if (array_list.size()<=0){
                    Utils.showToast("Please Select Class Section");
                }
                else {
                    if (from==0) {

                        checkInternet1();

                    }
                }
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
            updateTestData();
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



    private void getSchoolClass(){

        final List<ClassData> classes = new ArrayList<>();

        List<ClassData> classDataList = dbHelper.getAllSchoolClass();

        for (int j = 0; j<classDataList.size(); j++){
            ClassData classData = classDataList.get(j);
            className = classData.getClassName();
            classes.add(new ClassData(classData.getClassid(),className));

        }

        classSpinAdapter = new ClassSpinAdapter(AdminUpdateExamActivity.this,
                R.layout.spinner,
                classes);


        spinnerClass.setEnabled(false);
        spinnerClass.setClickable(false);
        spinnerClass.setAdapter(classSpinAdapter);
        spinnerClass.setSelection(studentClassId-1);

        getSchoolSection(studentClassId);
        getSchoolSubject(studentClassId);

        for (int i = 0; i<classes.size();i++){
            if (studentClassId==classes.get(i).getClassid()){
                className = classes.get(i).getClassName();
                break;
            }
        }

    }

    private void getExamName(){

        final List<ExamTypeData> data = new ArrayList<>();

        List<ExamTypeData> examTypeDataList = dbHelper.getExamType();

        for (int j = 0; j<examTypeDataList.size(); j++){
            ExamTypeData examTypeData = examTypeDataList.get(j);
            classTestName = examTypeData.getExamName();
            data.add(new ExamTypeData(examTypeData.getExamTypeId(),classTestName,examTypeData.getStartTime(), examTypeData.getEndTime(),examTypeData.getExamAddedBy()));
        }

        examTypeAdapter = new ExamTypeAdapter(AdminUpdateExamActivity.this,
                R.layout.spinner,
                data,1);


        spinnerExamName.setEnabled(false);
        spinnerExamName.setClickable(false);
        spinnerExamName.setAdapter(examTypeAdapter);
        spinnerExamName.setSelection(examTypeId-1);

        for (int i = 0; i<data.size();i++){
            if (examTypeId==data.get(i).getExamTypeId()){
                classTestName = data.get(i).getExamName();
                break;
            }
        }

        examTypeAdapter = new ExamTypeAdapter(AdminUpdateExamActivity.this,
                R.layout.spinner,
                data,2);

        spinnerStartTime.setAdapter(examTypeAdapter);

        String id = String.valueOf(examTypeId);
        spinnerStartTime.setSelection(((ArrayAdapter<String>)spinnerStartTime.getAdapter()).getPosition(id));

        for(int i = 0; i < examTypeAdapter.getCount(); i++)
        {
            ExamTypeData subjectdata = examTypeAdapter.getItem(i);

            if (subjectdata.getExamTypeId() == examTypeId )
            {
                spinnerStartTime.setSelection(i);
                break;
            }
        }

        spinnerStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ExamTypeData subjectdata = examTypeAdapter.getItem(position);

                examTypeId = subjectdata.getExamTypeId();

                startTime = subjectdata.getStartTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        examTypeAdapter = new ExamTypeAdapter(AdminUpdateExamActivity.this,
                R.layout.spinner,
                data,3);

        spinnerStopTime.setAdapter(examTypeAdapter);

        String ids = String.valueOf(examTypeId);
        spinnerStopTime.setSelection(((ArrayAdapter<String>)spinnerStopTime.getAdapter()).getPosition(ids));

        for(int i = 0; i < examTypeAdapter.getCount(); i++)
        {
            ExamTypeData subjectdata = examTypeAdapter.getItem(i);

            if (subjectdata.getExamTypeId() == examTypeId )
            {
                spinnerStopTime.setSelection(i);
                break;
            }
        }

        spinnerStopTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ExamTypeData subjectdata = examTypeAdapter.getItem(position);

                examTypeId = subjectdata.getExamTypeId();

                endTime = subjectdata.getEndTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void getSchoolSection(final int classId){

        dbHelper.deleteAllSection();
        final List<SectionData> list = new ArrayList<>();
        classSectionAdapter = new ExamSectionAdapter(AdminUpdateExamActivity.this, list,examId);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerViewSection.setLayoutManager(staggeredGridLayoutManager);

        List<SectionData> sectionDataList = dbHelper.getSchoolSection(classId);

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);

            list.add(new SectionData(sectionData.getSectionId(),sectionData.getClassId(),sectionData.getSectionName()));

        }
        recyclerViewSection.setAdapter(classSectionAdapter);
        progressBar.setVisibility(View.GONE);


    }


    private void getSchoolSubject(final int classId){


        final List<SectionSubjectdata> list = new ArrayList<>();


        List<SubjectData> subjectDataList = dbHelper.getSchoolSubject(schoolId,classId);

        for (int j = 0; j<subjectDataList.size(); j++){
            SubjectData subjectData = subjectDataList.get(j);

            list.add(new SectionSubjectdata(subjectData.getSubjectId(),subjectData.getSubjectName()));


        }

        adapter = new SpinAdapter(AdminUpdateExamActivity.this,
                R.layout.spinner,
                list);

        spinnerSubject.setAdapter(adapter);

        String id = String.valueOf(studentSubjectId);
        spinnerSubject.setSelection(((ArrayAdapter<String>)spinnerSubject.getAdapter()).getPosition(id));


        for(int i = 0; i < adapter.getCount(); i++)
        {
            SectionSubjectdata subjectdata = adapter.getItem(i);

            if (subjectdata.getId() == studentSubjectId )
            {
                spinnerSubject.setSelection(i);
                break;
            }
        }

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SectionSubjectdata subjectdata = adapter.getItem(position);

                subjectId = subjectdata.getId();

                subjectName = subjectdata.getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }




    private void getTestDetails(final int examId){


        List<ExamData> examDataList = dbHelper.getExam(examId);

        for (int j = 0; j < examDataList.size(); j++) {
            ExamData classTestData = examDataList.get(j);

            examTypeId = classTestData.getExamTypeId();
            classTestName = classTestData.getExamName();
            studentSectionId = classTestData.getSectionId();
            studentSubjectId = classTestData.getSubjectId();
            studentClassId = classTestData.getClassId();
            maxMark = classTestData.getMaxMark();
            passMark = classTestData.getPassMark();
            testdate = classTestData.getExamDate();
            timeAlloted = classTestData.getTimeAllotted();
            correctAnswerMark = classTestData.getCorrectAnswerMark();
            wrongAnswerMark = classTestData.getWrongAnswerMark();
            notAttemptedMark = classTestData.getNotAttemptedMark();
            classTestAddedBy = classTestData.getExamAddedBy();

            etMaxMarks.setText(""+classTestData.getMaxMark());
            etPassMarks.setText(""+classTestData.getPassMark());
            etCorrectAnswerMark.setText(""+classTestData.getCorrectAnswerMark());
            etNotAttempted.setText(""+classTestData.getNotAttemptedMark());
            etWrongAnswerMark.setText(""+classTestData.getWrongAnswerMark());
            etTimeAlloted.setText(""+classTestData.getTimeAllotted());



            String date="Mar 10, 2016 6:30:00 PM";
            SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
            Date newDate= null;
            try {
                newDate = spf.parse(testdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat("dd MMM yyyy");
            date = spf.format(newDate);

            tvTestDate.setText(""+date);

        }

        getSchoolClass();
        getExamName();

    }


    private void updateTestData(){

        String maxMarks = etMaxMarks.getText().toString();
        String passMarks = etPassMarks.getText().toString();
        String timealloted = etTimeAlloted.getText().toString();
        String correctAnsMarks = etCorrectAnswerMark.getText().toString();
        String wrongAnsMarks = etWrongAnswerMark.getText().toString();
        String notAttempted = etNotAttempted.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        int mMarks = Integer.parseInt(maxMarks);
        int pMarks = Integer.parseInt(passMarks);
        int tAllotted = Integer.parseInt(timealloted);
        int cAnsMarks = Integer.parseInt(correctAnsMarks);
        int wAnsMarks = Integer.parseInt(wrongAnsMarks);
        int nAttempted = Integer.parseInt(notAttempted);

        if(dbHelper.updateExamData(
                examId,
                subjectId,
                mMarks,
                pMarks,
                testdate,
                startTime,
                endTime,
                tAllotted,
                cAnsMarks,
                wAnsMarks,
                nAttempted
        )
        ){
            Log.e("msg","dk");
        } else{
            Log.e("msg","not done");
        }

        List<ExamData> examDataList = dbHelper.getExam(examId);

        for (int j = 0; j<examDataList.size(); j++){
            final ExamData examData = examDataList.get(j);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.UPDATE_EXAM_DATA, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(AdminUpdateExamActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("examTypeId", String.valueOf(examData.getExamTypeId()));
                    parms.put("examId", String.valueOf(examData.getExamId()));
                    parms.put("examName",examData.getExamName());
                    parms.put("classId", String.valueOf(examData.getClassId()));
                    parms.put("sectionId", String.valueOf(examData.getSectionId()));
                    parms.put("subjectId", String.valueOf(examData.getSubjectId()));
                    parms.put("maxMark", String.valueOf(examData.getMaxMark()));
                    parms.put("passMark", String.valueOf(examData.getPassMark()));
                    parms.put("examDate",examData.getExamDate());
                    parms.put("examStartTime",examData.getExamStartTime());
                    parms.put("examStopTime",examData.getExamStopTime());
                    parms.put("timeAlloted", String.valueOf(examData.getTimeAllotted()));
                    parms.put("totalQuestion", String.valueOf(examData.getTotalQuestion()));
                    parms.put("correctAnswerMark", String.valueOf(examData.getCorrectAnswerMark()));
                    parms.put("wrongAnswerMark", String.valueOf(examData.getWrongAnswerMark()));
                    parms.put("notAttemptedMark", String.valueOf(examData.getNotAttemptedMark()));
                    parms.put("schoolCode", String.valueOf(schoolId));
                    parms.put("status", String.valueOf(examData.getStatus()));
                    parms.put("examAddedBy", String.valueOf(examData.getExamAddedBy()));
                    parms.put("examVisible", "0");
                    parms.put("examResult", "0");

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(AdminUpdateExamActivity.this, AdminExamQuestionActivity.class);
        intent.putExtra("questionNumber", questionNumber);
        intent.putExtra("examId", examId);
        intent.putExtra("className", className);
        intent.putExtra("examName", classTestName);
        intent.putExtra("questionStatus", "0");
        startActivity(intent);
        finish();
    }
    
}