package com.aptitude.learning.e2buddy.School.Student.FragmentClass;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentNoticeAdapter;
import com.aptitude.learning.e2buddy.School.SuperAdmin.Adapter.NoticeAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.NoticeData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentNoticeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StudentData studentData;
    private int id,schoolId,classId,sectionId,studentId;
    String studentName, studentImage, schoolName,schoolLogo;
    private RecyclerView recyclerViewNotice;
    private TextView tvTestMsg;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentNoticeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentNoticeFragment newInstance(String param1, String param2) {
        StudentNoticeFragment fragment = new StudentNoticeFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_notice, container, false);
        recyclerViewNotice = view.findViewById(R.id.recyclerViewNotice);
        tvTestMsg = view.findViewById(R.id.tvTestMsg);


        studentData = SchoolStudentSessionManager.getInstance(getActivity()).getUser();
        studentId = studentData.getId();
        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        schoolLogo = studentData.getSchoolLogo();
        studentImage = studentData.getStudentImage();
        schoolId = studentData.getSchoolId();
        classId = studentData.getClassId();
        sectionId = studentData.getSectionId();

        checkInternet();
        return view;
    }

    public boolean checkInternet(){
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
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }


    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
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



    private void getNotice(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewNotice.setLayoutManager(layoutManager);

        final List<NoticeData> list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_NOTICE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("resp", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                int noticeId = dataobj.getInt("noticeId");
                                int schoolId = dataobj.getInt("schoolCode");
                                int adminId = dataobj.getInt("noticeAddedBy");
                                int classId = dataobj.getInt("classId");
                                int sectionId = dataobj.getInt("sectionId");
                                String title = dataobj.getString("title");
                                String description = dataobj.getString("description");
                                String addedAt = dataobj.getString("date");
                                String link = dataobj.getString("link");

                               list.add(new NoticeData(noticeId,schoolId,adminId,classId,title,description,addedAt,link, "0"));

                            }
                            tvTestMsg.setVisibility(View.GONE);
                            recyclerViewNotice.setVisibility(View.VISIBLE);

                            StudentNoticeAdapter noticeAdapter = new StudentNoticeAdapter(getActivity(),list);
                            recyclerViewNotice.setAdapter(noticeAdapter);

                        }
                        else {

                            recyclerViewNotice.setVisibility(View.GONE);
                            tvTestMsg.setVisibility(View.VISIBLE);
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

                        Utils.showToast("Something went wrong");
                    }
                })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> parms = new HashMap<>();
            parms.put("schoolId", String.valueOf(schoolId));
            parms.put("classId", String.valueOf(classId));
            parms.put("sectionId", String.valueOf(sectionId));
            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
