package com.aptitude.learning.e2buddy.School.Admin.FragmentClass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.CheckSchoolTest;


import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Class1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Class1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CardView cardView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewTests;
    private TextView tvTestMsg;
    private CheckSchoolTest checkSchoolTest;

    public Class1Fragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static Class1Fragment newInstance(String param1, String param2) {
        Class1Fragment fragment = new Class1Fragment();
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

        checkSchoolTest = new CheckSchoolTest(getActivity(), 5, recyclerViewTests, tvTestMsg);

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
