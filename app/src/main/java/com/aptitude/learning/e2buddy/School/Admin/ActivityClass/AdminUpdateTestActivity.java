package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DatePickerDialog;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSectionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSectionAdapter1;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSpinAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.SpinAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUpdateTestActivity extends AppCompatActivity {


    private RecyclerView recyclerViewSection;
    private ClassSectionAdapter1 classSectionAdapter;
    private EditText etTestName, etMaxMarks, etPassMarks, etTimeAlloted, etCorrectAnswerMark,etWrongAnswerMark,etNotAttempted;
    private TextView tvTestDate,tvSchoolName;
    private Spinner spinnerClass, spinnerSubject;
    private int classId, subjectId;
    private int sectionId = 1,classTestId,status;
    private Button btAddTest;
    private int adminId,schoolId,from;
    private String adminName, adminEmail, schoolCode, adminImage, questionNumber, schoolName,schoolImage, className,testdate,subjectName,testName,timeAllotted,questionStatus;
    private AdminData user;
    private SchoolData schoolData;

    private int maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks,totaltime,time,studentClassId,studentSubjectId,maxMark,passMark,
    timeAlloted,correctAnswerMark,wrongAnswerMark,notAttemptedMark,studentSectionId,classTestAddedBy;

    public static AdminUpdateTestActivity AdminUpdateTestActivity;
    private AdminDBHelper dbHelper;

    private String classTestId1,classTestName,classTestDate;
    private CircleImageView imgAdmin,imgSchoolLogo;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SpinAdapter adapter;
    private ClassSpinAdapter classSpinAdapter;

    private ProgressBar progressBar;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_test);
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
        etTestName = findViewById(R.id.etTestName);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        etNotAttempted = findViewById(R.id.etNotAttempted);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        AdminUpdateTestActivity = this;
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

        classTestId = getIntent().getIntExtra("classTestId",-1);
        questionStatus = getIntent().getStringExtra("questionStatus");
        questionNumber = getIntent().getStringExtra("questionNumber");


        Glide.with(AdminUpdateTestActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        getTestDetails(classTestId);


        tvTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUpdateTestActivity.this,
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

                if (etTestName.getText().toString().isEmpty()) {
                    etTestName.setError("TestName is required");
                    etTestName.requestFocus();
                    return;
                }
                else if (etMaxMarks.getText().toString().isEmpty()) {
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

        classSpinAdapter = new ClassSpinAdapter(AdminUpdateTestActivity.this,
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


    private void getSchoolSection(final int classId){

        dbHelper.deleteAllSection();
        final List<SectionData> list = new ArrayList<>();
        classSectionAdapter = new ClassSectionAdapter1(AdminUpdateTestActivity.this, list,classTestId);
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

        adapter = new SpinAdapter(AdminUpdateTestActivity.this,
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



    private void getTestDetails(final int classTestId){


        List<ClassTestData> classTestDataList = dbHelper.getClassTest(classTestId);

            for (int j = 0; j < classTestDataList.size(); j++) {
                ClassTestData classTestData = classTestDataList.get(j);


                classTestName = classTestData.getClassTestName();
                studentSectionId = classTestData.getSectionId();
                studentSubjectId = classTestData.getSubjectId();
                studentClassId = classTestData.getClassId();
                maxMark = classTestData.getMaxMark();
                passMark = classTestData.getPassMark();
                testdate = classTestData.getClassTestDate();
                timeAlloted = classTestData.getTimeAllotted();
                correctAnswerMark = classTestData.getCorrectAnswerMark();
                wrongAnswerMark = classTestData.getWrongAnswerMark();
                notAttemptedMark = classTestData.getNotAttemptedMark();
                classTestAddedBy = classTestData.getClassTestAddedBy();

                etTestName.setText(""+classTestData.getClassTestName());
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

    }


    private void updateTestData(){


        String testName = etTestName.getText().toString();
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


        if(dbHelper.updateClassTest(
                classTestId,
                testName,
                subjectId,
                mMarks,
                pMarks,
                testdate,
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

                            Toast.makeText(AdminUpdateTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {

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
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminUpdateTestActivity.this, AdminAddQuestionActivity.class);
        intent.putExtra("questionNumber", questionNumber);
        intent.putExtra("classTestId", classTestId);
        intent.putExtra("className", className);
        intent.putExtra("TestName", etTestName.getText().toString());
        intent.putExtra("questionStatus", "0");
        startActivity(intent);
        finish();
    }




}
