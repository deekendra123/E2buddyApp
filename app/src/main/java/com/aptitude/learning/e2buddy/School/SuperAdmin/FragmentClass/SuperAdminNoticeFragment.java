package com.aptitude.learning.e2buddy.School.SuperAdmin.FragmentClass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.Preference.SchoolSuperAdminSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;
import com.aptitude.learning.e2buddy.School.SuperAdmin.Adapter.NoticeAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.NoticeData;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.SuperAdminAddNoticeActivity;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperAdminNoticeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewNotice;
    private TextView tvTestMsg;
    private RelativeLayout relativeLayoutAddNotice;
    private SchoolData schoolData;
    private int schoolId,superAdminId;
    private ProgressBar progressBar;
    private SuperAdminData user;

    public SuperAdminNoticeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SuperAdminNoticeFragment newInstance(String param1, String param2) {
        SuperAdminNoticeFragment fragment = new SuperAdminNoticeFragment();
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
        View view = inflater.inflate(R.layout.fragment_super_admin_notice, container, false);
        recyclerViewNotice = view.findViewById(R.id.recyclerViewNotice);
        tvTestMsg = view.findViewById(R.id.tvTestMsg);
        progressBar = view.findViewById(R.id.progressBar);
        relativeLayoutAddNotice = view.findViewById(R.id.relativeLayoutAddNotice);
        schoolData = SchoolPreference.getInstance(getActivity()).getSchoolInfo();
        schoolId = schoolData.getSchoolId();

        user = SchoolSuperAdminSessionManager.getInstance(getActivity()).getUser();
        superAdminId = user.getSuperAdminId();

        onClick();

        return view;
    }

    private void onClick(){
        relativeLayoutAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), SuperAdminAddNoticeActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkInternet1();

    }

    public boolean checkInternet1(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            getNotice();

            return true;
        }
        else{
            checkNetworkConnection1();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    public void checkNetworkConnection1(){
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
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

    private void getNotice(){
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewNotice.setLayoutManager(layoutManager);

        final List<NoticeData> list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_NOTICE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("respds", response);

                try {
                    JSONArray array = new JSONArray(response);

                    if (array.length()>0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject dataobj = array.getJSONObject(i);

                            int noticeId = dataobj.getInt("noticeId");
                          //  int adminId = dataobj.getInt("noticeAddedBy");
                            int classId = dataobj.getInt("classId");
                            String title = dataobj.getString("title");
                            String description = dataobj.getString("description");
                            String addedAt = dataobj.getString("date");
                            String link = dataobj.getString("link");
                            String noticeType = dataobj.getString("noticeType");

                            list.add(new NoticeData(noticeId,schoolId,superAdminId,classId,title,description,addedAt,link,noticeType));

                        }

                        tvTestMsg.setVisibility(View.GONE);
                        recyclerViewNotice.setVisibility(View.VISIBLE);

                        NoticeAdapter noticeAdapter = new NoticeAdapter(getActivity(),list);
                        recyclerViewNotice.setAdapter(noticeAdapter);
                        progressBar.setVisibility(View.GONE);


                    }
                    else {
                        //  progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        recyclerViewNotice.setVisibility(View.GONE);
                        tvTestMsg.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utils.showToast("Something went wrong");
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("schoolId", String.valueOf(schoolId));
            parms.put("superAdminId", String.valueOf(superAdminId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}