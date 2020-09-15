package com.aptitude.learning.e2buddy.School.Student.FragmentClass;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudHomeActivity;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentExamAdapter;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentTestAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentExamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StudentData studentData;
    private int id,schoolId,classId,sectionId,studentId;
    String studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName;
    private int systemTaken;
    String schoolLogo;
    private TextView tvTestMsg;
    private RecyclerView recyclerViewTests;
    private SwipeRefreshLayout swipeRefreshLayout;


    private AdminDBHelper dbHelper;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentExamFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentExamFragment newInstance(String param1, String param2) {
        StudentExamFragment fragment = new StudentExamFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_exam, container, false);

        recyclerViewTests = view.findViewById(R.id.recyclerViewTests);
        tvTestMsg = view.findViewById(R.id.tvTestMsg);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);

        dbHelper = new AdminDBHelper(getActivity());
        studentData = SchoolStudentSessionManager.getInstance(getActivity()).getUser();
        studentId = studentData.getId();
        studentName = studentData.getStudentName();
        schoolName = studentData.getSchoolName();
        schoolLogo = studentData.getSchoolLogo();
        studentImage = studentData.getStudentImage();
        schoolId = studentData.getSchoolId();
        classId = studentData.getClassId();
        sectionId = studentData.getSectionId();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkTest();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        checkTest();

    }
    private void checkTest(){
        final List<ExamData> list = new ArrayList<>();



        List<ExamData> examDataList = dbHelper.getAllExam(schoolId, classId, sectionId);

        if (examDataList.size()>0) {
            for (int j = 0; j < examDataList.size(); j++) {

                ExamData data = examDataList.get(j);

                list.add(new ExamData(data.getExamTypeId(),data.getExamId(),data.getExamName(),data.getClassId(),data.getSectionId(),data.getSubjectId(),data.getMaxMark(),data.getPassMark(),data.getExamDate(),data.getExamStartTime(),data.getExamStopTime(),data.getTimeAllotted(),data.getTotalQuestion(),
                        data.getCorrectAnswerMark(),data.getWrongAnswerMark(),data.getNotAttemptedMark(),data.getSchoolCode(),data.getStatus(),data.getExamAddedBy(),data.getExamVisible(),data.getExamResult()));

            }
            tvTestMsg.setVisibility(View.GONE);
            recyclerViewTests.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewTests.setLayoutManager(layoutManager);
            final StudentExamAdapter schoolTestAdapter = new StudentExamAdapter(getActivity(), list, classId,studentId,dbHelper);

            Collections.sort(list, new Comparator<ExamData>()
            {
                @Override
                public int compare(ExamData lhs, ExamData rhs) {

                    return Integer.valueOf(rhs.getExamId()).compareTo(lhs.getExamId());
                }
            });

            recyclerViewTests.setAdapter(schoolTestAdapter);
        }
        else {
            recyclerViewTests.setVisibility(View.GONE);
            tvTestMsg.setVisibility(View.VISIBLE);
        }


    }

}