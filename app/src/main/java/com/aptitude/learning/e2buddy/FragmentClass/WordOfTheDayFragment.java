package com.aptitude.learning.e2buddy.FragmentClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.ActivityClass.HomeActivity;
import com.aptitude.learning.e2buddy.AdapterClass.WordAdapter;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.ObjectClass.QuestionView;
import com.aptitude.learning.e2buddy.ObjectClass.WordData;
import com.aptitude.learning.e2buddy.R;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WordOfTheDayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerViewword;
    private List<WordData> list;
    private ImageView imgWord;
    private WordAdapter wordAdapter;
    private String current_date;
    private ProgressDialog progressDialog;

    private String words, word_date, word_meaning, word_img;
    private int word_id;
    public WordOfTheDayFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WordOfTheDayFragment newInstance(String param1, String param2) {
        WordOfTheDayFragment fragment = new WordOfTheDayFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_of_the_day, container, false);
        recyclerViewword = view.findViewById(R.id.recyclerViewword);
        imgWord = view.findViewById(R.id.imgWord);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerViewword.setLayoutManager(layoutManager);
        list = new ArrayList<>();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        String daate = dateFormat.format(cal.getTime());

        Log.e("date", daate);


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        current_date = sdf.format(new Date());

        wordAdapter = new WordAdapter(getActivity(), list, imgWord, current_date);

        getWordoftheDay();



        wordAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                final WordData wordData = list.get(position);
                final String imaes = AppCinfig.BASE_WORD_IMAGE_URL + wordData.getImagUrl()+".jpg";

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.zoom_image_layout, null);
                        PhotoView photoView = mView.findViewById(R.id.imageView);

                        Glide.with(getActivity()).load(imaes).into(photoView);
                        mBuilder.setView(mView);
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();

            }
        });


        return view;
    }

    private void getWordoftheDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String current_date = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_WORD_IMAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("school", response);

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                int wordid = data.getInt("id");
                                String date = data.getString("date");
                                String word = data.getString("word");
                                String wordMeaning = data.getString("wordMeaning");
                                String wordImage = data.getString("wordImage");

                                if (current_date.equals(date)){
                                    final String imaes = AppCinfig.BASE_WORD_IMAGE_URL + wordImage+".jpg";
                                    words = word;
                                    word_id = wordid;
                                    word_img = wordImage;
                                    word_meaning = wordMeaning;
                                    word_date = date;

                                    Glide.with(getActivity())
                                            .load(imaes)
                                            .into(imgWord);

                                    imgWord.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                                            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.zoom_image_layout, null);
                                            PhotoView photoView = mView.findViewById(R.id.imageView);

                                            Glide.with(getActivity()).load(imaes).into(photoView);
                                            mBuilder.setView(mView);
                                            AlertDialog mDialog = mBuilder.create();
                                            mDialog.show();
                                        }
                                    });



                                }

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                Collections.sort(list, byDate);

                                if (simpleDateFormat.parse(date).before(simpleDateFormat.parse(current_date))){

                                    list.add(new WordData(wordid, date, word, wordMeaning, "", wordImage));

                                }

                            }

                            recyclerViewword.setAdapter(wordAdapter);
                            progressDialog.dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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

    static final Comparator<WordData> byDate = new Comparator<WordData>() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        public int compare(WordData ord1, WordData ord2) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(ord1.getDate());
                d2 = sdf.parse(ord2.getDate());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };

}
