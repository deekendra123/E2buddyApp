package com.aptitude.learning.e2buddy.AdapterClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.ActivityClass.ViewDescriptionActivity;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.ObjectClass.QuestionView;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.bumptech.glide.Glide;
import com.aptitude.learning.e2buddy.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Matrix on 12-02-2019.
 */

public class QuizAdapter extends PagerAdapter {

    private List<QuestionView> questionViewList;
    Context context;
    LayoutInflater layoutInflater;
    int userid;
    Button btn_unfocus;
    public String[] qnum = {"1/10","2/10","3/10","4/10","5/10","6/10","7/10","8/10","9/10","10/10"};

    ViewPager viewPager;

    public QuizAdapter(List<QuestionView> questionViewList, Context context, int userid, ViewPager viewPager) {
        this.questionViewList = questionViewList;
        this.context = context;
        this.userid = userid;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return questionViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        final Button[] btn = new Button[4];
        int[] btn_id = {R.id.rdops1, R.id.rdops2, R.id.rdops3, R.id.rdops4};

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.trick_question_layout, container, false);
        final ImageView questionImageview = view.findViewById(R.id.imgQuestion);
        final  TextView quenunber = view.findViewById(R.id.questionnumber);
        final Button opt1 = view.findViewById(R.id.rdops1);
        final Button opt2 = view.findViewById(R.id.rdops2);
        final  Button opt3 = view.findViewById(R.id.rdops3);
        final Button opt4 = view.findViewById(R.id.rdops4);
        final TextView tvSolution = view.findViewById(R.id.tvSolution);
        final TextView btNext = view.findViewById(R.id.btNext);
        final RelativeLayout relativelayout = view.findViewById(R.id.relativelayout);
        final TextView tvHeading = view.findViewById(R.id.tvHeading);

        btNext.setClickable(false);
        tvSolution.setClickable(false);

        final QuestionView questionView = questionViewList.get(position);


        for (int i=0;i<questionView.getQuestionid();i++) {


            quenunber.setText("" + questionView.getQuestionid()+" of 25");


            tvHeading.setText(""+questionView.getQuestionHeading());

            Glide.with(context)
                    .load(AppCinfig.BASE_IMAGE_URL+questionView.getQuestionImage())
                    .into(questionImageview);

            opt1.setText(questionView.getOption1());
            opt2.setText(questionView.getOption2());
            opt3.setText(questionView.getOption3());
            opt4.setText(questionView.getOption4());

        }

        btn_unfocus = btn[0];

        for(int i = 0; i < btn.length; i++){
            btn[i] = view.findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setBackgroundResource(R.drawable.buttons);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckInternet checkInternet = new CheckInternet(context);

                    if (checkInternet.isOnline() == true) {

                    btNext.setClickable(true);
                    tvSolution.setClickable(true);
                    btNext.setBackgroundResource(R.drawable.circle1);
                    tvSolution.setBackgroundResource(R.drawable.circle1);

                    final String ans;
                    switch (v.getId()) {
                        case R.id.rdops1:
                            ans = btn[0].getText().toString();
                            if (ans.equals(questionView.getAnswer())) {

                                setGreen(btn[1], btn[0]);
                                setGreen(btn[2], btn[0]);
                                setGreen(btn[3], btn[0]);

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "correct");


                            } else {

                                setRed(btn[1], btn[0], questionView.getAnswer());
                                setRed(btn[2], btn[0], questionView.getAnswer());
                                setRed(btn[3], btn[0], questionView.getAnswer());

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "wrong");


                            }

                            btn[0].setClickable(false);
                            btn[1].setClickable(false);
                            btn[2].setClickable(false);
                            btn[3].setClickable(false);

                            handleAfterAnswering(questionView, relativelayout, tvSolution, btNext, ans, position);

                            break;

                        case R.id.rdops2:


                            ans = btn[1].getText().toString();
                            if (ans.equals(questionView.getAnswer())) {

                                setGreen(btn[0], btn[1]);
                                setGreen(btn[2], btn[1]);
                                setGreen(btn[3], btn[1]);


                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "correct");

                            } else {

                                setRed(btn[0], btn[1], questionView.getAnswer());
                                setRed(btn[2], btn[1], questionView.getAnswer());
                                setRed(btn[3], btn[1], questionView.getAnswer());

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "wrong");


                            }


                            btn[0].setClickable(false);
                            btn[1].setClickable(false);
                            btn[2].setClickable(false);
                            btn[3].setClickable(false);

                            handleAfterAnswering(questionView, relativelayout, tvSolution, btNext, ans, position);


                            break;

                        case R.id.rdops3:
                            ans = btn[2].getText().toString();

                            if (ans.equals(questionView.getAnswer())) {

                                setGreen(btn[0], btn[2]);
                                setGreen(btn[1], btn[2]);
                                setGreen(btn[3], btn[2]);

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "correct");


                            } else {
                                setRed(btn[0], btn[2], questionView.getAnswer());
                                setRed(btn[1], btn[2], questionView.getAnswer());
                                setRed(btn[3], btn[2], questionView.getAnswer());

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "wrong");


                            }

                            btn[0].setClickable(false);
                            btn[1].setClickable(false);
                            btn[2].setClickable(false);
                            btn[3].setClickable(false);

                            handleAfterAnswering(questionView, relativelayout, tvSolution, btNext, ans, position);


                            break;

                        case R.id.rdops4:
                            ans = btn[3].getText().toString();

                            if (ans.equals(questionView.getAnswer())) {

                                setGreen(btn[0], btn[3]);
                                setGreen(btn[1], btn[3]);
                                setGreen(btn[2], btn[3]);

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "correct");


                            } else {
                                setRed(btn[0], btn[3], questionView.getAnswer());
                                setRed(btn[1], btn[3], questionView.getAnswer());
                                setRed(btn[2], btn[3], questionView.getAnswer());

                                insertAnswer(questionView.getQuestionid(), ans, questionView.getAnswer(), questionView.getQuestionImage(), questionView.getDescription(), questionView.getWordCoachId(), "wrong");


                            }


                            btn[0].setClickable(false);
                            btn[1].setClickable(false);
                            btn[2].setClickable(false);
                            btn[3].setClickable(false);

                            handleAfterAnswering(questionView, relativelayout, tvSolution, btNext, ans, position);


                            break;
                    }
                }
                else {
                    Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
                }
            });


        }


        btn_unfocus = btn[0];


        container.addView(view);
        return view;
    }

    private void setGreen(Button btn_unfocus, Button btn_focus){
//        btn_unfocus.setVisibility(View.INVISIBLE);

        btn_unfocus.setBackgroundResource(R.drawable.buttons3);

        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundResource(R.drawable.buttons2);
        this.btn_unfocus = btn_focus;
    }

    private void setRed(Button btn_unfocus, Button btn_focus, String answer){

        if (btn_unfocus.getText().toString().equals(answer)){
            btn_focus.setBackgroundResource(R.drawable.buttons4);
        }

        else if (!btn_unfocus.getText().toString().equals(answer)){
           btn_unfocus.setBackgroundResource(R.drawable.buttons3);
           // btn_unfocus.setVisibility(View.INVISIBLE);
        }

        if (btn_unfocus.getText().toString().equals(answer)) {

            btn_unfocus.setBackgroundResource(R.drawable.buttons2);
            this.btn_unfocus = btn_focus;

        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    private void insertAnswer(final int questionid, final String answer, final String correctAnswer, final String questionImage, final String description, final int getWordCoachId, final String status){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_TRICKQUESTION_ANSWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parms = new HashMap<>();
                parms.put("userId", String.valueOf(userid));
                parms.put("trickQuestionId", String.valueOf(questionid));
                parms.put("answer", answer);
                parms.put("status", status);
                parms.put("dateTime", dateTime);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void handleAfterAnswering(final QuestionView questionView, RelativeLayout relativeLayout, TextView tvSolution, TextView btNext, final String ans, final int position){
        relativeLayout.setVisibility(View.VISIBLE);

        tvSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInternet checkInternet = new CheckInternet(context);

                if (checkInternet.isOnline()==true){
                    SharedPreferences preferences =context.getSharedPreferences("nextquiz", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    SharedPreferences preferences1 =context.getSharedPreferences("resumequiz", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.clear();
                    editor1.apply();

                    Intent intent = new Intent(context, ViewDescriptionActivity.class);
                    intent.putExtra("questionId", questionView.getQuestionid());
                    intent.putExtra("questionImage", questionView.getQuestionImage());
                    intent.putExtra("userAnswer",ans);
                    intent.putExtra("correctAnswer",questionView.getAnswer());
                    intent.putExtra("description", questionView.getDescription());
                    intent.putExtra("wordCoachId", questionView.getWordCoachId());
                    intent.putExtra("current_position", position);
                    intent.putExtra("getcount", getCount());
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }


            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInternet checkInternet = new CheckInternet(context);

                if (checkInternet.isOnline()==true){
                    int current_position = position+1;

                    if (current_position==getCount()){
                        showExitAlertDialog(position);

                    }
                    else {
                        SharedPreferences preferences1 = context.getSharedPreferences("resumequiz", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.clear();
                        editor1.apply();

                        if (questionView.getWordCoachId() == 0) {

                            viewPager.setCurrentItem(position + 1);

                        } else {
                            getWordCoachQuestion(questionView.getWordCoachId(), position);
                        }
                    }
                }
                else {
                    Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    private void getWordCoachQuestion(final int wordCoachId, final int position){

        View alertLayout = LayoutInflater.from(context).inflate(R.layout.word_coach_question_layout, null);
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


        final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

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

                                        CheckInternet checkInternet = new CheckInternet(context);

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

                                                CheckInternet checkInternet = new CheckInternet(context);

                                                if (checkInternet.isOnline()==true){
                                                    dialog.dismiss();
                                                    viewPager.setCurrentItem(position+1);
                                                }
                                                else {
                                                    Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });

                                        liner.setVisibility(View.GONE);

                                        }
                                        else {
                                            Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                btOption2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CheckInternet checkInternet = new CheckInternet(context);

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

                                                CheckInternet checkInternet = new CheckInternet(context);

                                                if (checkInternet.isOnline()==true) {
                                                    dialog.dismiss();

                                                    viewPager.setCurrentItem(position + 1);
                                                }
                                                else {
                                                    Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

                                                }


                                            }

                                        });

                                        liner.setVisibility(View.GONE);

                                        }
                                        else {
                                            Toast.makeText(context, "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

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

                Log.e("coachid",""+ wordCoachId);
                HashMap<String, String> parms = new HashMap<>();
                parms.put("wordCoachId", String.valueOf(wordCoachId));
                return parms;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }

    private void insertWordCoachAnswer(final int questionid, final String userAnswer, final String staus){

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

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parms = new HashMap<>();
                parms.put("userId", String.valueOf(userid));
                parms.put("wordCoachQuestionId", String.valueOf(questionid));
                parms.put("userAnswer", userAnswer);
                parms.put("status", staus);
                parms.put("dateTime",dateTime);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void showExitAlertDialog(final int mCurrentPage){

        final User user = SessionManager.getInstance(context).getUser();


        View alertLayout = LayoutInflater.from(context).inflate(R.layout.view_message_layout, null);
        final Button btExit = alertLayout.findViewById(R.id.btExit);

        final TextView tvUserName = alertLayout.findViewById(R.id.tvUserName);
        final TextView tvMessage = alertLayout.findViewById(R.id.tvMessage);

        tvUserName.setText("Congratulations "+user.getUsername()+"!");

        tvMessage.setText("You have successfully finished today's challenge  of all the Puzzles");

        final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("resumequiz", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putInt("position", mCurrentPage);
                editor1.commit();

                ((Activity)context).finish();
            }
        });

    }


}
