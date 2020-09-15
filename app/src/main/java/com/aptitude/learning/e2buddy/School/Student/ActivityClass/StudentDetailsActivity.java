package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.SpinAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDetailsActivity extends AppCompatActivity {

    private int schoollId;
    String schoolName,schoolCode,city, state, pincode,type, imageUrl, userName, userEmail,schoolLogo,className,sectioName,dateOfBirth,admissionNo,token;
    private int systemTaken,classId,sectionId,studetnClassId,studentSectionId,sSetcionId;

    EditText etStudentName,etAdmissionNo;
    TextView etDateofBirth,tvSchoolName;
    ImageView btDone,imgadate;
    CircleImageView imageView;
  //  private RelativeLayout chooseImage;

    private Spinner spinnerClass,spinnerSection;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth auth;
    DatePickerDialog datePickerDialog;
    int userid;
   // private CircleImageView imgStudent;
    private Bitmap bitmap;
    private int GALLERY = 1, CAMERA = 2;
    private Uri filePath;
    private SpinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__details_);

        etStudentName = findViewById(R.id.editText11);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        etDateofBirth = findViewById(R.id.editText12);
        btDone = findViewById(R.id.button8);
        imageView = findViewById(R.id.imgSchoolLogo);
        imgadate = findViewById(R.id.imageView1);
        etAdmissionNo = findViewById(R.id.etAdmissionNo);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSection = findViewById(R.id.spinnerSection);

        etAdmissionNo.setEnabled(false);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        Log.e("dktoken",token);
                    }
                });

        schoollId = getIntent().getIntExtra("id", 0);
        schoolName = getIntent().getStringExtra("schoolName");
        schoolCode = getIntent().getStringExtra("schoolCode");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        pincode = getIntent().getStringExtra("pincode");
        type = getIntent().getStringExtra("type");
        imageUrl = getIntent().getStringExtra("imageUrl");
        userid = getIntent().getIntExtra("userId", 0);
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        systemTaken = getIntent().getIntExtra("systemTaken",-1);
        schoolLogo = getIntent().getStringExtra("schoolLogo");

        dateOfBirth = getIntent().getStringExtra("dateOfBirth");
        studetnClassId = getIntent().getIntExtra("classId",-1);
        sSetcionId = getIntent().getIntExtra("sectionId",-1);
        admissionNo = getIntent().getStringExtra("admissionNo");

        tvSchoolName.setText(""+schoolName);
        getSchoolClass();

        etAdmissionNo.setText(admissionNo+"");

        if (!userName.equals("null")){
            etStudentName.setText(""+userName);
        }
        if (!dateOfBirth.isEmpty()){
            etDateofBirth.setText(""+dateOfBirth);
            etAdmissionNo.setEnabled(false);

        }

        if (!schoolLogo.isEmpty()){

            Glide.with(StudentDetailsActivity.this)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolLogo)
                    .into(imageView);

        }
        else {

            imageView.setBackgroundResource(R.drawable.science);

        }


        etDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(StudentDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = String.valueOf(dayOfMonth);
                                String month = String.valueOf(monthOfYear+1);


                                for (int i=1;i<=MONTHS.length;i++) {

                                    if (i==monthOfYear){
                                        etDateofBirth.setText(dayOfMonth + " "
                                                + (MONTHS[i]) + " " + year);
                                    }
                                }

                                if(monthOfYear<9){

                                    month = "0"+month;
                                }
                                if (dayOfMonth<10){
                                    day="0"+day;

                                }

                                dateOfBirth = day+"-"+month+"-"+year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });

//        chooseImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPictureDialog();
//            }
//        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternet();

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
            if (etStudentName.getText().toString().isEmpty()) {
                etStudentName.setError("Name is required");
                etStudentName.requestFocus();
            }
            else if (etAdmissionNo.getText().toString().isEmpty()) {
                etAdmissionNo.setError("Admission No. is required");
                etAdmissionNo.requestFocus();
            }
            else {
                updateUserInfo(schoollId, userid);
            }
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



    private void updateUserInfo(final int schoollId, final int userid){

       final ProgressDialog progressDialog = new ProgressDialog(StudentDetailsActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

//        final String image = getStringImage(bitmap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_USER_INFO, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    Log.e("data", imageUrl+  "   "+etStudentName.getText().toString()  );

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String studentId = jsonObject.getString("userId");

                    int studentid = Integer.parseInt(studentId);

                    StudentData studentData = new StudentData(
                            studentid,
                            etStudentName.getText().toString(),
                            userEmail,
                            etDateofBirth.getText().toString(),
                            imageUrl,
                            schoollId,
                            schoolName,
                            schoolCode,
                            city,
                            state,
                            pincode,
                            type,
                            systemTaken,
                            schoolLogo,
                            studetnClassId,
                            studentSectionId
                    );

                    SchoolStudentSessionManager.getInstance(StudentDetailsActivity.this).userLogin(studentData);

                    progressDialog.dismiss();
                    Intent intent = new Intent(StudentDetailsActivity.this, StudentConfirmationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


              }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(StudentDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String studentName = etStudentName.getText().toString();
                String admissionNo = etAdmissionNo.getText().toString();

                Log.e("data",dateOfBirth);

                HashMap<String, String> parms = new HashMap<>();
                parms.put("userId", String.valueOf(userid));
                parms.put("userName",studentName);
                parms.put("dateOfBirth", dateOfBirth);
                parms.put("schoolCodeId", String.valueOf(schoollId));
                parms.put("admissionNo", admissionNo);
                parms.put("classId", String.valueOf(studetnClassId));
                parms.put("sectionId", String.valueOf(studentSectionId));
                parms.put("schoolCodeId", String.valueOf(schoollId));
                parms.put("token",token);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getSchoolClass(){

        final List<String> classes = new ArrayList<String>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_CLASS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int classId = data.getInt("classId");
                        String className = data.getString("className");
                        classes.add(className);

                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, classes);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerClass.setAdapter(dataAdapter);

                    if (studetnClassId!=0){

                        spinnerClass.setSelection(studetnClassId-1);
                    }


                    spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                className = parent.getItemAtPosition(position).toString();
                                studetnClassId = position+1;

                                getSchoolSection(studetnClassId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(StudentDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getSchoolSection(final int classId){


        final List<SectionSubjectdata> list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_SECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list.clear();

                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                sectionId = data.getInt("sectionId");
                                int classId = data.getInt("classId");
                                String sectionName = data.getString("sectionName");
                                list.add(new SectionSubjectdata(sectionId,sectionName));

                            }


                            adapter = new SpinAdapter(StudentDetailsActivity.this,
                                    R.layout.spinner,
                                    list);

                         spinnerSection.setAdapter(adapter);

                            if (sSetcionId!=0){

                                for(int i = 0; i < adapter.getCount(); i++)
                                {
                                    SectionSubjectdata subjectdata = adapter.getItem(i);

                                    if (subjectdata.getId() == sSetcionId)
                                    {
                                        spinnerSection.setSelection(i);
                                        break;
                                    }
                                }

                            }

                            spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    SectionSubjectdata user = adapter.getItem(position);
                                    studentSectionId = user.getId();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("classId", String.valueOf(classId));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(StudentDetailsActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //  imgStudent.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

}
