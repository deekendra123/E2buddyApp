package com.aptitude.learning.e2buddy.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity {

    private int schoollId;
    String schoolName,schoolCode,city, state, pincode,type, imageUrl, userName, userEmail,birthDate;

    EditText etStudentName;
    TextView etDateofBirth,etSchoolName;
    ImageView btDone,imgadate;
    CircleImageView imageView;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth auth;
    DatePickerDialog datePickerDialog;
    int userid;
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        etStudentName = findViewById(R.id.editText11);
        etSchoolName = findViewById(R.id.editText13);
        etDateofBirth = findViewById(R.id.editText12);
        btDone = findViewById(R.id.button8);
        imageView = findViewById(R.id.userimage);
        imgadate = findViewById(R.id.imageView1);


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

        etSchoolName.setText(""+schoolName);

        Glide.with(UserDetailsActivity.this)
                .load(imageUrl)
                .into(imageView);


        etDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(UserDetailsActivity.this,
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

                                birthDate = day+"-"+month+"-"+year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                if (checkInternet.isOnline()==true){

                    if (etStudentName.getText().toString().equals("")){
                        etStudentName.setError("Name is required");

                    }
                    else {
                        updateUserInfo(schoollId, userid);
                    }                }
                else {

                    Toast.makeText(UserDetailsActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void updateUserInfo(final int schoollId, final int userid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_USER_INFO, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                User user = new User(
                        userid, etStudentName.getText().toString(), userEmail
                );

                SessionManager.getInstance(UserDetailsActivity.this).userLogin(user);

                Intent intent = new Intent(UserDetailsActivity.this, HomeNavActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(UserDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String studentName = etStudentName.getText().toString();
                String dateOfBirth = etDateofBirth.getText().toString();

                Log.e("DKKKerror", ""+studentName+ "  "+ dateOfBirth + "   "+ userid +"   "+ schoollId);


                HashMap<String, String> parms = new HashMap<>();

                parms.put("userId", String.valueOf(userid));
                parms.put("userName",studentName);
                parms.put("dateOfBirth", birthDate);
                parms.put("schoolCodeId", String.valueOf(schoollId));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
