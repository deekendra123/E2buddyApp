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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.AdapterClass.StudentTestAdapter;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentTestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StudentData studentData;
    private int id,schoolId,classId,sectionId,studentId;
    String studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type,className,sectionName;
    private int systemTaken;
    String schoolLogo;
    private TextView tvTestMsg;
    private RecyclerView recyclerViewTests;
    private SwipeRefreshLayout swipeRefreshLayout;


    private AdminDBHelper dbHelper;

    public StudentTestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentTestFragment newInstance(String param1, String param2) {
        StudentTestFragment fragment = new StudentTestFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_test, container, false);

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
        final List<TestData> list = new ArrayList<>();

        List<ClassTestData> classTestDataList = dbHelper.getAllStudentTest(schoolId, classId, sectionId);

        if (classTestDataList.size()>0) {
            for (int j = 0; j < classTestDataList.size(); j++) {

                ClassTestData data = classTestDataList.get(j);

                String timeAllotted = String.valueOf(data.getTimeAllotted());

                list.add(new TestData(data.getClassTestId(), data.getClassId(), data.getSubjectId(), data.getMaxMark(), data.getPassMark(), data.getClassTestAddedBy(), data.getCorrectAnswerMark(),
                        data.getWrongAnswerMark(), data.getNotAttemptedMark(), data.getClassTestName(), data.getClassTestDate(), timeAllotted));

            }
            tvTestMsg.setVisibility(View.GONE);
            recyclerViewTests.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewTests.setLayoutManager(layoutManager);
            StudentTestAdapter schoolTestAdapter = new StudentTestAdapter(getActivity(), list, classId,studentId,dbHelper);

            Collections.sort(list, new Comparator<TestData>()
            {
                @Override
                public int compare(TestData lhs, TestData rhs) {

                    return Integer.valueOf(rhs.getClassTestId()).compareTo(lhs.getClassTestId());
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
