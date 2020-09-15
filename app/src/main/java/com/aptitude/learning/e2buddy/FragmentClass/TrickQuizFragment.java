package com.aptitude.learning.e2buddy.FragmentClass;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.ActivityClass.HomeActivity;
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


public class TrickQuizFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    QuizAdapter questionAdapter;
    public static int mCurrentPage;
    private TextView tvUserName;
    private List<QuestionView> questionViewList;
    private int userId;
    CustomViewPager viewPager;
    public static Activity homeActivity;
    private String userName;
    private ProgressDialog dialog;

    public TrickQuizFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrickQuizFragment newInstance(String param1, String param2) {
        TrickQuizFragment fragment = new TrickQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trick_quiz, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        viewPager = view.findViewById(R.id.slidequestion);
        homeActivity = getActivity();

        dialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog){
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



        questionAdapter = new QuizAdapter(questionViewList, getActivity(), userId, viewPager);

        CheckInternet checkInternet = new CheckInternet(getActivity());

        if (checkInternet.isOnline()==true){
            getQuizQuestions();
        }
        else {
            Toast.makeText(getActivity(), "No Network. Please check your internet connection", Toast.LENGTH_SHORT).show();

        }
        return view;
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

        Volley.newRequestQueue(getActivity()).add(stringRequest);

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
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("nextquiz", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "false");


        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("resumequiz", Context.MODE_PRIVATE);

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



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
