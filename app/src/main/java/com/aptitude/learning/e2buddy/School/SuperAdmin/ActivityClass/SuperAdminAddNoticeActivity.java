package com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.ClassSectionAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuperAdminAddNoticeActivity extends AppCompatActivity {
    private Spinner spinnerClass;

    private RecyclerView recyclerViewSection;
    private AdminDBHelper dbHelper;

    private int adminId,schoolId,from,classId,noticeId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage, className,testdate,subjectName,testName,timeAllotted;
    private SuperAdminData user;
    private SchoolData schoolData;
    private TextView tvSchoolName;
    private EditText etTitle,etDesc,etLink;
    private CircleImageView imgSchoolLogo;
    private ClassSectionAdapter classSectionAdapter;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notice);
        spinnerClass = findViewById(R.id.spinnerClass);
        recyclerViewSection = findViewById(R.id.recyclerViewSection);
        imgSchoolLogo =findViewById(R.id.imgSchoolLogo);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etLink = findViewById(R.id.etLink);
        checkbox = findViewById(R.id.checkbox);

        dbHelper = new AdminDBHelper(this);
        dbHelper.deleteAllSection();

        user = SchoolSuperAdminSessionManager.getInstance(this).getUser();
        adminId = user.getSuperAdminId();

        schoolData = SchoolPreference.getInstance(getApplicationContext()).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();
        tvSchoolName.setText(""+schoolName);

        Glide.with(SuperAdminAddNoticeActivity.this)
                .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                .into(imgSchoolLogo);

        getNoticeId();
        getSchoolClass();
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

                    spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            className = parent.getItemAtPosition(position).toString();
                            dbHelper.deleteAllSection();
                            classId = position+1;
                            getSchoolSection(classId);
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


                        Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getSchoolSection(final int classId){
        dbHelper.deleteAllSection();
        final List<SectionData> list = new ArrayList<>();
        classSectionAdapter = new ClassSectionAdapter(SuperAdminAddNoticeActivity.this, list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerViewSection.setLayoutManager(staggeredGridLayoutManager);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_SECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list.clear();

                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                int sectionId = data.getInt("sectionId");
                                int classId = data.getInt("classId");
                                String sectionName = data.getString("sectionName");

                                Log.e("sec", String.valueOf(sectionId));
                                list.add(new SectionData(sectionId,classId,sectionName));
                            }
                            recyclerViewSection.setAdapter(classSectionAdapter);


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
        RequestQueue requestQueue = Volley.newRequestQueue(SuperAdminAddNoticeActivity.this);
        requestQueue.add(stringRequest);

    }



    public void btAddTestOnClick(View view) {
        final ArrayList array_list = dbHelper.getAllSection();

        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError("Title is required");
            etTitle.requestFocus();
            return;
        }
        else if (etDesc.getText().toString().isEmpty()) {
            etDesc.setError("Description is required");
            etDesc.requestFocus();
            return;
        }

        else if (array_list.size()<=0 && !checkbox.isChecked()){
            Utils.showToast("Please Select Class Section");
        }
        else {
            if (checkbox.isChecked()){
                insertNoticeForAllSchool();
            }
            else {
                checkInternet();
            }
        }
    }

    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            insertNotice();
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


    private void insertNotice(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String dateTime = sdf.format(new Date());

        final ArrayList array_list = dbHelper.getAllSection();

        for (int i=0;i<array_list.size();i++){

            final int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_NOTICE, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sendNotification();
                    Toast.makeText(SuperAdminAddNoticeActivity.this, "Notice successfully added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String title = etTitle.getText().toString();
                    String description = etDesc.getText().toString();
                    String link = etLink.getText().toString();

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("noticeId", String.valueOf(noticeId));
                    parms.put("schoolId", String.valueOf(schoolId));
                    parms.put("adminId", String.valueOf(adminId));
                    parms.put("classId", String.valueOf(classId));
                    parms.put("sectionId", String.valueOf(array_list.get(finalI)));
                    parms.put("title", title);
                    parms.put("description", description);
                    parms.put("addedAt",dateTime);
                    parms.put("link",link);

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }

    }

    private void getNoticeId(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_NOTICE_ID, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    int value = jsonObject.getInt("noticeId");
                    noticeId = value+1;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void insertNoticeForAllSchool(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String dateTime = sdf.format(new Date());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_ADD_NOTICE, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   sendNotification1();

                    Toast.makeText(SuperAdminAddNoticeActivity.this, "Notice successfully added", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                    finish();

                }
            },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String title = etTitle.getText().toString();
                    String description = etDesc.getText().toString();
                    String link = etLink.getText().toString();

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("noticeId", String.valueOf(noticeId));
                    parms.put("schoolId", String.valueOf(schoolId));
                    parms.put("adminId", String.valueOf(adminId));
                    parms.put("classId", String.valueOf(0));
                    parms.put("sectionId", String.valueOf(0));
                    parms.put("title", title);
                    parms.put("description", description);
                    parms.put("addedAt",dateTime);
                    parms.put("link",link);
                    parms.put("noticeType","1");

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    private void sendNotification() {

        final ArrayList array_list = dbHelper.getAllSection();

        for (int i = 0; i < array_list.size(); i++) {

            final int finalI = i;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SEND_NOTICE_NOTIFICATION,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("deeketoken", response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("DKKKerror", "" + error.getMessage());
                            Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("schoolId", String.valueOf(schoolId));
                    parms.put("classId", String.valueOf(classId));
                    parms.put("sectionId", String.valueOf(array_list.get(finalI)));
                    parms.put("schoolName", schoolName);
                    parms.put("noticeType","0");

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SuperAdminAddNoticeActivity.this);
            requestQueue.add(stringRequest);

        }
    }

    private void sendNotification1() {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SEND_NOTICE_NOTIFICATION,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("deeketoken", response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("DKKKerror", "" + error.getMessage());
                            Toast.makeText(SuperAdminAddNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("schoolId", String.valueOf(schoolId));
                    parms.put("classId", String.valueOf(classId));
                    parms.put("sectionId", "0");
                    parms.put("schoolName", schoolName);
                    parms.put("noticeType","1");

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SuperAdminAddNoticeActivity.this);
            requestQueue.add(stringRequest);



    }

}
