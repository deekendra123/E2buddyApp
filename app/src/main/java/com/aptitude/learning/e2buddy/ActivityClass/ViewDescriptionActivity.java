package com.aptitude.learning.e2buddy.ActivityClass;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewDescriptionActivity extends AppCompatActivity {

    private int questionId, wordCoachId, userId, position, getcount, current_position;
    private String userAnswer, correctAnswer, questionImage, description;
    private TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_description);
        ImageView imgQuestion = findViewById(R.id.imgQuestion);
        Button btNext = findViewById(R.id.btNext);
        tvUserName = findViewById(R.id.tvUserName);

        final User user = SessionManager.getInstance(this).getUser();
        userId = user.getId();
        tvUserName.setText("Welcome "+user.getUsername());

        questionId=getIntent().getIntExtra("questionId",0);
        questionImage = getIntent().getStringExtra("questionImage");
        userAnswer=getIntent().getStringExtra("userAnswer");
        correctAnswer = getIntent().getStringExtra("correctAnswer");
        description = getIntent().getStringExtra("description");
        wordCoachId = getIntent().getIntExtra("wordCoachId", 0);
        position = getIntent().getIntExtra("current_position", 0);
        getcount = getIntent().getIntExtra("getcount", 0);


        Glide.with(ViewDescriptionActivity.this)
                .load(AppCinfig.BASE_IMAGE_URL+description)
                .into(imgQuestion);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                if (checkInternet.isOnline()==true){

                    current_position = position+1;

                    if (current_position==getcount){
                        showExitAlertDialog(position);

                    }
                    else {
                        if (wordCoachId  == 0){

                            SharedPreferences sharedPreferences = getSharedPreferences("nextquiz", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("status", "true");
                            editor.commit();

                            onBackPressed();
                        }
                        else {
                            getWordCoachQuestion(wordCoachId);
                        }

                    }

                                    }
                else {
                    Toast.makeText(ViewDescriptionActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }





            }
        });
    }

    private void getWordCoachQuestion(final int wordCoachId){

        View alertLayout = LayoutInflater.from(ViewDescriptionActivity.this).inflate(R.layout.word_coach_question_layout, null);
        final Button btOption1 = alertLayout.findViewById(R.id.btOption1);
        final Button btOption2 = alertLayout.findViewById(R.id.btOption2);

        final TextView tvQuestion = alertLayout.findViewById(R.id.tvQuestion);
        final LinearLayout linearResultLayout = alertLayout.findViewById(R.id.linearResultLayout);
        final ImageView imgCheck = alertLayout.findViewById(R.id.imgCheck);
        final TextView tvUserAnswer = alertLayout.findViewById(R.id.tvUserAnswer);
        final TextView tvCorrectAnswer = alertLayout.findViewById(R.id.tvCorrectAnswer);
        final TextView tvDescription = alertLayout.findViewById(R.id.tvDescription);
        final Button btNext = alertLayout.findViewById(R.id.btNext);
        final LinearLayout liner = alertLayout.findViewById(R.id.liner);


        final AlertDialog.Builder alert = new AlertDialog.Builder(ViewDescriptionActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_WORD_QUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("school", response);

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                final int questionid = data.getInt("id");
                                String wordQuestion = data.getString("wordQuestion");
                                final String option1 = data.getString("option1");
                                final String option2 = data.getString("option2");
                                final String answer = data.getString("answer");
                                final String description = data.getString("description");

                                Log.e("questions" ,wordQuestion);

                                tvQuestion.setText(""+wordQuestion);
                                btOption1.setText(""+option1);
                                btOption2.setText(""+option2);

                                btOption1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                                        if (checkInternet.isOnline()==true){


                                        if (btOption1.getText().toString().equals(answer)){
                                            insertWordCoachAnswer(questionid, btOption1.getText().toString(), "correct");

                                        }
                                        else {
                                            insertWordCoachAnswer(questionid, btOption1.getText().toString(), "wrong");

                                        }


                                        linearResultLayout.setVisibility(View.VISIBLE);
                                        if (btOption1.getText().toString().equals(answer)){
                                            imgCheck.setBackgroundResource(R.drawable.ic_checked);

                                            tvUserAnswer.setText(""+btOption1.getText().toString());
                                            tvCorrectAnswer.setVisibility(View.GONE);
                                            tvUserAnswer.setBackgroundResource(R.drawable.buttons2);


                                        }
                                        else {
                                            imgCheck.setBackgroundResource(R.drawable.ic_delete_cross);

                                            tvUserAnswer.setText(""+btOption1.getText().toString());
                                            tvUserAnswer.setBackgroundResource(R.drawable.buttons4);
                                            tvCorrectAnswer.setText(""+answer);
                                            tvCorrectAnswer.setBackgroundResource(R.drawable.buttons2);


                                        }

                                        tvDescription.setText(""+description);

                                        btNext.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                                                if (checkInternet.isOnline()==true){

                                                    SharedPreferences sharedPreferences = getSharedPreferences("nextquiz", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("status", "true");
                                                    editor.commit();

                                                    onBackPressed();                                                }
                                                else {
                                                    Toast.makeText(ViewDescriptionActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });

                                        liner.setVisibility(View.GONE);

                                        }
                                        else {
                                            Toast.makeText(ViewDescriptionActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                btOption2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                                        if (checkInternet.isOnline()==true){

                                            if (btOption2.getText().toString().equals(answer)){
                                            insertWordCoachAnswer(questionid, btOption2.getText().toString(), "correct");

                                        }
                                        else {
                                            insertWordCoachAnswer(questionid, btOption2.getText().toString(), "wrong");

                                        }

                                        linearResultLayout.setVisibility(View.VISIBLE);

                                        if (btOption2.getText().toString().equals(answer)){
                                            imgCheck.setBackgroundResource(R.drawable.ic_checked);

                                            tvUserAnswer.setText(""+btOption2.getText().toString());
                                            tvUserAnswer.setBackgroundResource(R.drawable.buttons2);
                                            tvCorrectAnswer.setVisibility(View.GONE);

                                        }
                                        else {
                                            imgCheck.setBackgroundResource(R.drawable.ic_delete_cross);

                                            tvUserAnswer.setText(""+btOption2.getText().toString());
                                            tvUserAnswer.setBackgroundResource(R.drawable.buttons4);
                                            tvCorrectAnswer.setText(""+answer);
                                            tvCorrectAnswer.setBackgroundResource(R.drawable.buttons2);

                                        }
                                        tvDescription.setText(""+description);

                                        btNext.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                CheckInternet checkInternet = new CheckInternet(getApplicationContext());

                                                if (checkInternet.isOnline()==true){

                                                    SharedPreferences sharedPreferences = getSharedPreferences("nextquiz", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("status", "true");
                                                    editor.commit();

                                                    onBackPressed();

                                                }
                                                else {
                                                    Toast.makeText(ViewDescriptionActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                        });

                                        liner.setVisibility(View.GONE);
                                        }
                                        else {
                                            Toast.makeText(ViewDescriptionActivity.this, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("wordCoachId", String.valueOf(wordCoachId));
                return parms;
            }
        };

        Volley.newRequestQueue(ViewDescriptionActivity.this).add(stringRequest);

    }

    private void insertWordCoachAnswer(final int questionid, final String userAnswer, final String status){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_WORD_ANSWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ViewDescriptionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parms = new HashMap<>();
                parms.put("userId", String.valueOf(userId));
                parms.put("wordCoachQuestionId", String.valueOf(questionid));
                parms.put("userAnswer", userAnswer);
                parms.put("status", status);
                parms.put("dateTime", dateTime);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewDescriptionActivity.this);
        requestQueue.add(stringRequest);

    }

    private void showExitAlertDialog(final int mCurrentPage){

        final User user = SessionManager.getInstance(ViewDescriptionActivity.this).getUser();


        View alertLayout = LayoutInflater.from(ViewDescriptionActivity.this).inflate(R.layout.view_message_layout, null);
        final Button btExit = alertLayout.findViewById(R.id.btExit);

        final TextView tvUserName = alertLayout.findViewById(R.id.tvUserName);
        final TextView tvMessage = alertLayout.findViewById(R.id.tvMessage);

        tvUserName.setText("Congratulations "+user.getUsername()+"!");

        tvMessage.setText("You have successfully finished today's challenge  of all the Puzzles");

        final AlertDialog.Builder alert = new AlertDialog.Builder(ViewDescriptionActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("resumequiz", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putInt("position", mCurrentPage);
                editor1.commit();
                finish();
                HomeActivity.homeActivity.finish();

            }
        });

    }

}
