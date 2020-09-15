package com.aptitude.learning.e2buddy.FragmentClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.ActivityClass.LoginActivity;
import com.aptitude.learning.e2buddy.ActivityClass.School_Code_Activity;
import com.aptitude.learning.e2buddy.AdapterClass.LineView;
import com.aptitude.learning.e2buddy.AdapterClass.LineView1;
import com.aptitude.learning.e2buddy.AdapterClass.WordPuzzleAdapter;
import com.aptitude.learning.e2buddy.AdapterClass.WordSearchPuzzleAdapter;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.ObjectClass.QuestionView;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.ObjectClass.WordPuzzleData;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordPuzzleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    GridView gridView;
    private boolean mSelecting;
    private FrameLayout gridFrame;
    float startX, startY;
    private User user;
    String s1,s2,s3;
    private boolean status;

    private WordSearchPuzzleAdapter adapter;
    private List<WordPuzzleData> wordPuzzleDataList;

    private RecyclerView recyclerViewword;
    private TextView tvWord;

    public WordPuzzleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WordPuzzleFragment newInstance(String param1, String param2) {
        WordPuzzleFragment fragment = new WordPuzzleFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_word_puzzle, container, false);

        gridView =  view.findViewById(R.id.gridview);
        gridFrame = view.findViewById(R.id.gridFrame);
        tvWord = view.findViewById(R.id.tvWord);
        recyclerViewword =  view.findViewById(R.id.recyclerViewword);


        StaggeredGridLayoutManager layoutManager= new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewword.setLayoutManager(layoutManager);
        wordPuzzleDataList = new ArrayList<>();
        user = SessionManager.getInstance(getActivity()).getUser();

        adapter = new WordSearchPuzzleAdapter(getActivity(), wordPuzzleDataList);
        getWordPuzzlesWord();
        getWordPuzzles();

        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getActionMasked();
                tvWord.setText("");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        // data
                        final PaintViewHolder newPaint = new PaintViewHolder();
                        newPaint.DrawLine = new LineView(getActivity());
                        gridFrame.addView(newPaint.DrawLine);

                        LineView1 lineView1 = new LineView1(getActivity());
                        gridFrame.addView(lineView1);

                        StringBuilder buildWord = new StringBuilder();
                        final int x = (int) event.getX();
                        final int y = (int) event.getY();

                        int position = gridView.pointToPosition(x, y);

                        if (position != GridView.INVALID_POSITION) {

                            v.getParent().requestDisallowInterceptTouchEvent(true);

                            TextView cellView = (TextView) gridView.getChildAt(position);

                            String a;

                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    startX = event.getX();
                                    startY = event.getY();

                                    a = cellView.getText().toString();
                                    Log.v(">>>>><<<<<<<????????", a.toString());

                                    //tvWord.setText(tvWord.getText()+a);

                                    s1 = "";
                                    s1 = s1+a;

                                    break;
                                case MotionEvent.ACTION_MOVE:


                                    a = cellView.getText().toString();
                                    Log.v(">>>>><<<<<<<????????", a.toString());


                                    //tvWord.setText(tvWord.getText()+a);
                                    s1 = s1+a;
                                    break;
                                case MotionEvent.ACTION_UP:
                                    // Checking the list for formed word ;
                                    //if found that is painted
                                    a = cellView.getText().toString();
                                    Log.v(">>>>><<<<<<<????????", a.toString());

                                    s1 = s1 + a;
                                    Log.e("word", s1.replaceAll("(.)\\1{1,}", "$1"));

                                    String word = s1.replaceAll("(.)\\1{1,}", "$1");
                                    tvWord.setText(word);


                                    for (int i = 0; i < wordPuzzleDataList.size(); i++)
                                    {
                                        if (word.equals(wordPuzzleDataList.get(i).getWord())) {
                                            //insertPuzzleAnswer(wordPuzzleDataList.get(i).getWordSearchPuzzleId(), wordPuzzleDataList.get(i).getWord());

                                            final int finalI = i;
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSER_PUZZLE_ANSWER, new com.android.volley.Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);

                                                        String data = jsonObject.getString("status");

                                                        if (data.equals("true")){
                                                            newPaint.DrawLine.setPoints(startX, startY, x, y);

                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                                    new com.android.volley.Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                            Utils.showToast("Something went worng");
                                                        }
                                                    }){

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    HashMap<String, String> parms = new HashMap<>();
                                                    parms.put("userId", String.valueOf(user.getId()));
                                                    parms.put("wordSearchPuzzleId", String.valueOf(wordPuzzleDataList.get(finalI).getWordSearchPuzzleId()));
                                                    parms.put("answer", wordPuzzleDataList.get(finalI).getWord());
                                                    return parms;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                            requestQueue.add(stringRequest);

                                            status = true;
                                            wordPuzzleDataList.set(i, new WordPuzzleData(wordPuzzleDataList.get(i).getWordid(),
                                                    wordPuzzleDataList.get(i).getWordSearchPuzzleId(),
                                                    wordPuzzleDataList.get(i).getWord(), 1));

                                            adapter.notifyItemChanged(i);
                                            break;
                                        }
                                        else {
                                            status = false;
                                        }

                                    }

                                    if (!status){
                                        lineView1.setPoints(startX, startY, x, y);
                                    }

                                    break;
                            }
                        } else {
                            if (mSelecting) {
                                mSelecting = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mSelecting = false;
                        break;
                }
                return true;
            }
        });
        return view;
    }

    private void getWordPuzzles(){
        final List<WordPuzzleData> list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_WORD_PUZZLES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                int id = data.getInt("id");
                                int wordSearchPuzzleId = data.getInt("wordSearchPuzzleId");
                                String letter = data.getString("letter");
                                String wordSearchLetterImage = data.getString("wordSearchLetterImage");

                                list.add(new WordPuzzleData(id, wordSearchPuzzleId, letter, wordSearchLetterImage));

                                 }

                            WordPuzzleAdapter adapter = new WordPuzzleAdapter(getActivity(), list);
                            gridView.setAdapter(adapter);


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


    private void getWordPuzzlesWord(){
        final List<WordPuzzleData> list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_WORD_PLUZZLE_WORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                int id = data.getInt("id");
                                int wordSearchPuzzleId = data.getInt("wordSearchPuzzleId");
                                String word = data.getString("word");

                                wordPuzzleDataList.add(new WordPuzzleData(id, wordSearchPuzzleId, word, 0));
                            }

                            recyclerViewword.setAdapter(adapter);


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

    protected class PaintViewHolder{ protected LineView DrawLine; }

}
