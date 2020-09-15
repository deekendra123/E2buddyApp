package com.aptitude.learning.e2buddy.School.Admin.FragmentClass;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.CheckSchoolTest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Class5Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Class5Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewTests;
    private TextView tvTestMsg;
    private CheckSchoolTest checkSchoolTest;


    public Class5Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Class5Fragment newInstance(String param1, String param2) {
        Class5Fragment fragment = new Class5Fragment();
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
        View view = inflater.inflate(R.layout.fragment_nursery, container, false);
        recyclerViewTests = view.findViewById(R.id.recyclerViewTests);
        tvTestMsg = view.findViewById(R.id.tvTestMsg);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.refreshLayout);

        checkSchoolTest = new CheckSchoolTest(getActivity(), 9, recyclerViewTests, tvTestMsg);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkSchoolTest.checkTest();
                pullToRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkSchoolTest.checkTest();
    }
}
