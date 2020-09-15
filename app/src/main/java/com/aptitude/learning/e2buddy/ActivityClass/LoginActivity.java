package com.aptitude.learning.e2buddy.ActivityClass;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AdapterClass.LoginAdapter;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminConfirmationActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.SuperAdminConfirmationActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button signInButton;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] mDots;

    private LoginActivity loginActivity;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    LoginAdapter loginAdapter;
    private static final int RC_SIGN_IN = 234;

    private static final String TAG = "e2buddyapp";

    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth.AuthStateListener listener;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.buttonSignIn);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);


        loginAdapter = new LoginAdapter(getApplicationContext());

        viewPager.setAdapter(loginAdapter);

        addDotIndicator(0);

        viewPager.addOnPageChangeListener(onPageChangeListener);

        FirebaseApp.initializeApp(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).
                        requestEmail().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                if (checkInternet.isOnline()==true){
                    signIn();
                }
                else {
                    Toast.makeText(LoginActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    public void addDotIndicator(int position){

        mDots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i =0; i<mDots.length; i++){
            mDots[i] = new TextView(this );
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.dot_inactives));
            dotsLayout.addView(mDots[i]);
        }

        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(listener);

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                fireBaseAuthwithGoogle(account);

                if (!isFinishing()) {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                }
            } catch (ApiException e) {
            }
        }
    }

    private void fireBaseAuthwithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {

                            addData();

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    private void addData(){

        final String username, userid, useremail,token_id,imageUri;
        final FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        useremail = user.getEmail();
        username = user.getDisplayName();
        imageUri = user.getPhotoUrl().toString();
        token_id = FirebaseInstanceId.getInstance().getToken();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());

        mGoogleSignInClient.revokeAccess();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SUPER_LOGIN, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("res", response);

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        String userType = data.getString("userType");



                        if (userType.equals("student")) {
                            int id = data.getInt("id");
                            String userName = data.getString("userName");
                            String emailId = data.getString("emailId");
                            String phoneNumber = data.getString("phoneNumber");
                            String userImage = data.getString("userImage");
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginActivity.this, School_Code_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("id", id);
                            intent.putExtra("userName", userName);
                            intent.putExtra("userEmail", emailId);
                            intent.putExtra("imageUrl", userImage);
                            startActivity(intent);
                            finish();

                        }
                        else if (userType.equals("admin")){
                            final int id = data.getInt("adminId");
                            String userName = data.getString("adminName");
                            final String emailId = data.getString("adminEmail");
                            String phoneNumber = data.getString("adminPhone");
                            String adminImage = data.getString("adminImage");
                            String dateTime = data.getString("dateTime");
                            final String schoolCode = data.getString("schoolCode");


                            AdminData user = new AdminData(
                                    id, userName, emailId, schoolCode, imageUri
                            );

                            SchoolAdminSessionManager.getInstance(LoginActivity.this).userLogin(user);

                            updateSchoolData(emailId,schoolCode);
                            getSchoolInfo(schoolCode,"admin");


                        }

                        else {
                            final int superAdminId = data.getInt("superAdminId");
                            String superAdminName = data.getString("superAdminName");
                            final String superAdminEmail = data.getString("superAdminEmail");
                            String superAdminPhone = data.getString("superAdminPhone");
                            String superAdminImage = data.getString("superAdminImage");
                            String dateTime = data.getString("dateTime");
                            final String schoolCode = data.getString("schoolCode");

                            SuperAdminData user = new SuperAdminData(
                                    superAdminId, superAdminName, superAdminEmail, superAdminPhone, imageUri, schoolCode
                            );

                            SchoolSuperAdminSessionManager.getInstance(LoginActivity.this).userLogin(user);

                            updateSchoolData(superAdminEmail,schoolCode);
                            getSchoolInfo(schoolCode,"superAdmin");


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parms = new HashMap<>();
                parms.put("userName",username);
                parms.put("emailId", useremail);
                parms.put("phoneNumber", "0");
                parms.put("userImage", imageUri);
                parms.put("dateTime", dateTime);
                parms.put("dateOfBirth", "0");
                parms.put("schoolCodeId", "0");
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Alert");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void updateSchoolData(final String emailId, final String schoolCode){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_ADMIN, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("emailId", emailId);
                parms.put("schoolCodeId", schoolCode);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getSchoolInfo(final String schoolId, final String userType){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SCHOOL_INFO, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response",response);

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int schoolCodeId = data.getInt("id");
                        String schoolName = data.getString("schoolName");
                        String schoolCode = data.getString("schoolCode");
                        String city = data.getString("city");
                        String state = data.getString("state");
                        String pincode = data.getString("pincode");
                        String type = data.getString("type");
                        String systemTaken = data.getString("systemTaken");
                        String schoolLogo = data.getString("schoolLogo");


                        SchoolData schoolData = new SchoolData(
                                schoolCodeId, schoolName, schoolCode,city,state,pincode,type,systemTaken,schoolLogo
                        );

                        SchoolPreference.getInstance(LoginActivity.this).putSchoolInfo(schoolData);

                        progressDialog.dismiss();

                        if (userType.equals("admin")){
                            Intent intent = new Intent(LoginActivity.this, AdminConfirmationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        if (userType.equals("superAdmin")) {
                            Intent intent = new Intent(LoginActivity.this, SuperAdminConfirmationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Utils.showToast("   "+ schoolId);

                HashMap<String, String> parms = new HashMap<>();
                parms.put("schoolId", schoolId);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
