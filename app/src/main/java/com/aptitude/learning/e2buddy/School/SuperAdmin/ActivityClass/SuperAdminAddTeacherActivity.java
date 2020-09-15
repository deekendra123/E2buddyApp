package com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentQuestionsActivity.timeLeft;

public class SuperAdminAddTeacherActivity extends AppCompatActivity {

    EditText etTeacherName,etEmail,etPhoneNo;
    private SchoolData schoolData;
    private String schoolImage,schoolName,from,teacherName,teacherEmail,teacherPhone;
    private int schoolId,teacherId;
    private CircleImageView imgSchoolLogo;
    private TextView tvSchoolName;
    private Button btTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_add_teacher);
        etTeacherName = findViewById(R.id.etTeacherName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        imgSchoolLogo = findViewById(R.id.imgSchoolLogo);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        btTeacher = findViewById(R.id.btTeacher);

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolId = schoolData.getSchoolId();
        schoolImage = schoolData.getSchoolLogo();
        schoolName = schoolData.getSchoolName();

        tvSchoolName.setText(""+schoolName);

        if (!schoolImage.isEmpty()){

            Glide.with(SuperAdminAddTeacherActivity.this)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                    .into(imgSchoolLogo);
        }
        else {
            imgSchoolLogo.setBackgroundResource(R.drawable.school);
        }

        from = getIntent().getStringExtra("from");
        if (from.equals("1")){
            teacherName = getIntent().getStringExtra("teacherName");
            teacherEmail = getIntent().getStringExtra("teacherEmail");
            teacherPhone = getIntent().getStringExtra("teacherPhone");
            teacherId = getIntent().getIntExtra("teacherId",-1);
            etTeacherName.setText(""+teacherName);
            etEmail.setText(""+teacherEmail);

            if (!teacherPhone.equals("null")){
                etPhoneNo.setText(""+teacherPhone);

            }

            btTeacher.setText("Update Teacher");
        }
        btAddTeacherOnClick();

    }

    public void btAddTeacherOnClick() {
        btTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTeacherName.getText().toString().isEmpty()) {
                    etTeacherName.setError("Name is required");
                    etTeacherName.requestFocus();
                    return;
                }
                else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }
                else if (!isValidEmailId(etEmail.getText().toString().trim())){
                    Utils.showToast("InValid Email Address.");
                }
                else {
                    if (from.equals("1")){
                       checkInternet();
                    }else {
                        checkInternet1();
                    }
                }
            }
        });

    }

    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            updateTeacherDetails();
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
    public boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            insertTeacherDetails();

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

    private void insertTeacherDetails() {
        final String name = etTeacherName.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone = etPhoneNo.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_ADD_TEACHER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("true")){
                                Utils.showToast("Teacher Added Successfully");
                                onBackPressed();
                            }
                            else {
                                Utils.showToast("Teacher Already Added");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SuperAdminAddTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("teacherName", name);
                parms.put("teacherEmail", email);
                parms.put("teacherPhone", phone);
                parms.put("addedAt", dateTime);
                parms.put("schoolCodeId", String.valueOf(schoolId));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void updateTeacherDetails() {
        final String name = etTeacherName.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone = etPhoneNo.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_TEACHER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utils.showToast("Teacher Updated Successfully");
                        onBackPressed();
                        finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SuperAdminAddTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("teacherId", String.valueOf(teacherId));
                parms.put("teacherName", name);
                parms.put("teacherEmail", email);
                parms.put("teacherPhone", phone);
                parms.put("addedAt", dateTime);
                parms.put("schoolCodeId", String.valueOf(schoolId));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}