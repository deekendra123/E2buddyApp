package com.aptitude.learning.e2buddy.ActivityClass;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AdapterClass.CustomViewPager;
import com.aptitude.learning.e2buddy.AdapterClass.QuizAdapter;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.ObjectClass.QuestionView;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    QuizAdapter questionAdapter;
    int mCurrentPage;
    private TextView tvUserName;
    private List<QuestionView> questionViewList;
    private int userId;
    CustomViewPager viewPager;
    public static Activity homeActivity;
    private String userName;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvUserName = findViewById(R.id.tvUserName);
        viewPager = findViewById(R.id.slidequestion);
        homeActivity = this;





        dialog = new ProgressDialog(HomeActivity.this, R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.full_progress_dialog);
                getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.FILL_PARENT);
            }
        };

        dialog.setTitle("Please Wait...");
        dialog.setMessage("Initiating the game");
        dialog.setCancelable(false);
        dialog.show();



        questionViewList = new ArrayList<>();
        viewPager.addOnPageChangeListener(viewListener);

        viewPager.setPagingEnabled(false);

        final User user = SessionManager.getInstance(this).getUser();
        userId = user.getId();
        tvUserName.setText("Welcome "+user.getUsername());
        userName = user.getUsername();



        questionAdapter = new QuizAdapter(questionViewList, HomeActivity.this, userId, viewPager);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());

        if (checkInternet.isOnline()==true){
            getQuizQuestions();
        }
        else {
            Toast.makeText(HomeActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

        }


    }


    private void getQuizQuestions() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_TRICK_QUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("school", response);

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                int questionid = data.getInt("id");
                                String questionImage = data.getString("questionImage");
                                String option1 = data.getString("option1");
                                String option2 = data.getString("option2");
                                String option3 = data.getString("option3");
                                String option4 = data.getString("option4");
                                String answer = data.getString("answer");
                                String description = data.getString("description");
                                int wordCoachId = data.getInt("wordCoachId");
                                String questionHeading = data.getString("questionHeading");

                                Log.e("questions" ,questionImage);

                                questionViewList.add(new QuestionView(questionid, questionImage, option1, option2, option3, option4, answer, description, wordCoachId, questionHeading));
                            }

                            viewPager.setAdapter(questionAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", ""+ error.getMessage());
                    }
                });

        Volley.newRequestQueue(HomeActivity.this).add(stringRequest);

    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurrentPage = position;


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("nextquiz", MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "false");


        SharedPreferences sharedPreferences1 = getSharedPreferences("resumequiz", MODE_PRIVATE);

        final int pos = sharedPreferences1.getInt("position",-1);

        if (pos>-1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  //  Utils.makeText(getApplicationContext(), "" + pos, Utils.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(pos);
                    dialog.dismiss();

                }
            }, 3000);

        }
        else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    dialog.dismiss();

                }
            }, 3000);

        }

        if (status.equals("false")){

        }
        else if (status.equals("true")){
            viewPager.setCurrentItem(mCurrentPage+1);

        }


    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences preferences =getSharedPreferences("nextquiz", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        SharedPreferences sharedPreferences = getSharedPreferences("resumequiz", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putInt("position", mCurrentPage);
                        editor1.commit();

                        HomeActivity.super.onBackPressed();
                    }
                }).create().show();

    }






}
