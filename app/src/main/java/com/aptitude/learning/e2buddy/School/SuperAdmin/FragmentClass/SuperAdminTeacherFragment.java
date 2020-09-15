package com.aptitude.learning.e2buddy.School.SuperAdmin.FragmentClass;

import android.content.Intent;
import android.os.Bundle;

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
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.SuperAdminAddTeacherActivity;
import com.aptitude.learning.e2buddy.School.SuperAdmin.Adapter.TeacherAdapter;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ObjectData.TeacherData;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperAdminTeacherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewTeacher;
    private TextView tvTestMsg;
    private RelativeLayout relativeLayoutAddTest;
    private SchoolData schoolData;
    private int schoolId;
    private ProgressBar progressBar;
  

    public SuperAdminTeacherFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SuperAdminTeacherFragment newInstance(String param1, String param2) {
        SuperAdminTeacherFragment fragment = new SuperAdminTeacherFragment();
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
        View view = inflater.inflate(R.layout.fragment_super_admin_teacher, container, false);
        recyclerViewTeacher = view.findViewById(R.id.recyclerViewTeacher);
        tvTestMsg = view.findViewById(R.id.tvTestMsg);
        progressBar = view.findViewById(R.id.progressBar);
        relativeLayoutAddTest = view.findViewById(R.id.relativeLayoutAddTest);
        schoolData = SchoolPreference.getInstance(getActivity()).getSchoolInfo();
        schoolId = schoolData.getSchoolId();
        onClick();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllTeacher();

    }

    private void getAllTeacher(){
        progressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewTeacher.setLayoutManager(layoutManager);

        final List<TeacherData> list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_TEACHER, new com.android.volley.Response.Listener<String>() {
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

                                int adminId = dataobj.getInt("adminId");
                                String adminName = dataobj.getString("adminName");
                                String adminEmail = dataobj.getString("adminEmail");
                                String adminPhone = dataobj.getString("adminPhone");
                                String dateTime = dataobj.getString("dateTime");

                                list.add(new TeacherData(adminId,adminName,adminEmail,adminPhone,dateTime));

                            }
                            tvTestMsg.setVisibility(View.GONE);
                            recyclerViewTeacher.setVisibility(View.VISIBLE);

                            TeacherAdapter teacherAdapter = new TeacherAdapter(getActivity(),list, schoolId);
                            recyclerViewTeacher.setAdapter(teacherAdapter);
                            progressBar.setVisibility(View.GONE);


                        }
                        else {

                            progressBar.setVisibility(View.GONE);
                            recyclerViewTeacher.setVisibility(View.GONE);
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

            return parms;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void onClick(){
        relativeLayoutAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SuperAdminAddTeacherActivity.class);
                intent.putExtra("from", "0");
                getActivity().startActivity(intent);
            }
        });
    }
}