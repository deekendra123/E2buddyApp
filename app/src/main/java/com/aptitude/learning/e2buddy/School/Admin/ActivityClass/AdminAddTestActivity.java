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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.SpinAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAddTestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSection;
    private ClassSectionAdapter classSectionAdapter;
    private EditText etTestName, etMaxMarks, etPassMarks, etTimeAlloted, etCorrectAnswerMark,etWrongAnswerMark,etNotAttempted;
    private TextView tvTestDate,tvSchoolName;
    private Spinner spinnerClass, spinnerSubject;
    private int classId, subjectId;
    private int sectionId = 1,classTestId;
    private Button btAddTest;
    private int adminId,schoolId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage, className,testdate,subjectName,testName,timeAllotted;
    private AdminData user;
    private SchoolData schoolData;

    public static AdminAddTestActivity adminAddTestActivity;
    private AdminDBHelper dbHelper;

    private String classTestId1;
    private CircleImageView imgAdmin,imgSchoolLogo;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SpinAdapter adapter;
    private List<SectionSubjectdata> list;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);
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


        adminAddTestActivity = this;
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


        Glide.with(AdminAddTestActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);


            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            String current_date = sdf.format(new Date());
            tvTestDate.setText(""+current_date);

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            testdate = sdf1.format(new Date());

            getTestId();
            getSchoolClass();

        tvTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminAddTestActivity.this,
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
                else {

                    int maxMarks = Integer.parseInt(etMaxMarks.getText().toString());
                    int passMarks = Integer.parseInt(etPassMarks.getText().toString());
                    int corrAnsMarks = Integer.parseInt(etCorrectAnswerMark.getText().toString());
                    int wrongAnsMarks = Integer.parseInt(etWrongAnswerMark.getText().toString());
                    int notAttemptedMarks = Integer.parseInt(etNotAttempted.getText().toString());

                    if (passMarks > maxMarks) {
                        Utils.showToast("Pass Marks should be lesser then Max Marks");

                    } else if (corrAnsMarks > passMarks) {
                        Utils.showToast("Correct Answer Marks should be lesser then Pass Marks");
                    } else if (wrongAnsMarks > corrAnsMarks) {

                        Utils.showToast("Wrong Answer Marks should be lesser then Correct Answer Marks");
                    } else if (notAttemptedMarks > wrongAnsMarks) {
                        Utils.showToast("Not Attempted Marks should be lesser then Wrong Answer Marks");
                    } else if (maxMarks <= 0 || passMarks <= 0 || corrAnsMarks <= 0) {

                        Utils.showToast("Max Marks, Pass Marks and Correct Answer Marks can not be zero");

                    } else if (array_list.size() <= 0) {
                        Utils.showToast("Please Select Class Section");
                    } else if (list.size() <= 0) {
                        Utils.showToast("Please Select Subject");

                    }
                    else {


                        checkInternet();

                    }
                }
            }
        });
    }

    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            insertTestData();
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


    private void showAlertDialog(){

        View alertLayout = LayoutInflater.from(AdminAddTestActivity.this).inflate(R.layout.test_alert_dialog, null);
        final Button btAddQuestion = alertLayout.findViewById(R.id.btAddQuestion);
        final LinearLayout linerLayout = alertLayout.findViewById(R.id.linerLayout);
        final EditText etQuestionNumber = alertLayout.findViewById(R.id.etQuestionNumber);
        final ImageView btNext = alertLayout.findViewById(R.id.btNext);
        final TextView tvClassName = alertLayout.findViewById(R.id.tvClassName);
        final TextView tvTestName = alertLayout.findViewById(R.id.tvTestName);
        final TextView tvSectionName = alertLayout.findViewById(R.id.tvSectionName);
        final TextView tvSubjectNmae = alertLayout.findViewById(R.id.tvSubjectNmae);
        final AlertDialog.Builder alert = new AlertDialog.Builder(AdminAddTestActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();

        tvSubjectNmae.setText(""+subjectName);


        GetSectionName getSectionName = new GetSectionName(AdminAddTestActivity.this, classTestId,tvSectionName);
        getSectionName.getSchoolSection();

        tvClassName.setText("Class - "+className);
        tvTestName.setText(""+etTestName.getText().toString()+ " added successfully");

        btAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btAddQuestion.setBackgroundResource(R.drawable.buttons3);
                linerLayout.setVisibility(View.VISIBLE);

                btNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (etQuestionNumber.getText().toString().isEmpty()) {
                            etQuestionNumber.setError("No. of Question is required");
                            etQuestionNumber.requestFocus();
                            return;
                        }
                        else  if (etQuestionNumber.getText().toString().equals("0")) {
                            etQuestionNumber.setError("Enter a valid No. of Question");
                            etQuestionNumber.requestFocus();
                            return;
                        }

                        else {
                            updateTest(classTestId, Integer.parseInt(etQuestionNumber.getText().toString()));
                            dialog.dismiss();
                            Intent intent = new Intent(AdminAddTestActivity.this, AdminAddQuestionActivity.class);
                            intent.putExtra("questionNumber", etQuestionNumber.getText().toString());
                            intent.putExtra("classTestId", classTestId);
                            intent.putExtra("className", className);
                            intent.putExtra("TestName", etTestName.getText().toString());
                            intent.putExtra("questionStatus", "1");
                            startActivity(intent);
                            finish();
                        }



                    }
                });


            }
        });


    }

    private void getSchoolClass(){

        final List<String> classes = new ArrayList<String>();

        List<ClassData> classDataList = dbHelper.getAllSchoolClass();

        for (int j = 0; j<classDataList.size(); j++){
            ClassData classData = classDataList.get(j);
            className = classData.getClassName();
            classes.add(className);

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, classes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(dataAdapter);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className = parent.getItemAtPosition(position).toString();
                dbHelper.deleteAllSection();
                classId = position+1;

                getSchoolSection(classId);
                getSchoolSubject(classId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getSchoolSection(final int classId){
        dbHelper.deleteAllSection();
        final List<SectionData> list = new ArrayList<>();
        classSectionAdapter = new ClassSectionAdapter(AdminAddTestActivity.this, list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerViewSection.setLayoutManager(staggeredGridLayoutManager);

        List<SectionData> sectionDataList = dbHelper.getSchoolSection(classId);

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);
            list.add(new SectionData(sectionData.getSectionId(),sectionData.getClassId(),sectionData.getSectionName()));
        }
        recyclerViewSection.setAdapter(classSectionAdapter);
    }

    private void getSchoolSubject(final int classId){
        list = new ArrayList<>();


        List<SubjectData> subjectDataList = dbHelper.getSchoolSubject(schoolId,classId);

        for (int j = 0; j<subjectDataList.size(); j++){
            SubjectData subjectData = subjectDataList.get(j);

            list.add(new SectionSubjectdata(subjectData.getSubjectId(),subjectData.getSubjectName()));

        }

        adapter = new SpinAdapter(AdminAddTestActivity.this,
                R.layout.spinner,
                list);

        spinnerSubject.setAdapter(adapter);

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


    private void insertTestData(){

        String testName = etTestName.getText().toString();
        int maxMarks = Integer.parseInt(etMaxMarks.getText().toString());
        int passMarks = Integer.parseInt(etPassMarks.getText().toString());
        int timealloted = Integer.parseInt(etTimeAlloted.getText().toString());
        int correctAnsMarks = Integer.parseInt(etCorrectAnswerMark.getText().toString());
        int wrongAnsMarks = Integer.parseInt(etWrongAnswerMark.getText().toString());
        int notAttempted = Integer.parseInt(etNotAttempted.getText().toString());


        final ArrayList array_list = dbHelper.getAllSection();


        for (int i=0;i<array_list.size();i++) {

                int sectionId = Integer.parseInt(array_list.get(i).toString());

                if (dbHelper.insertClassTest1(
                        classTestId, testName, classId, sectionId, subjectId, maxMarks, passMarks, testdate,
                        timealloted, 0, correctAnsMarks, wrongAnsMarks, notAttempted, schoolId, 0, adminId, 0
                )
                ) {
                    Log.e("msg", "done");
                } else {
                    Log.e("msg", "not done");

                }
            }
        showAlertDialog();
    }

    private void getTestId(){
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy::hh:mm:ss");
        final String timestamp = s.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_CLASS_TEST_ID, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    classTestId = jsonObject.getInt("classTestId");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(AdminAddTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("timestamp", timestamp);
                parms.put("adminId", String.valueOf(adminId));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void updateTest(final int classTestId, final int totalQuestion){

        if(dbHelper.updateClassTest(

                classTestId,
               totalQuestion,
                0
               )

        ){
            Log.e("msg","dk");
        } else{
            Log.e("msg","not done");

        }
    }


}
