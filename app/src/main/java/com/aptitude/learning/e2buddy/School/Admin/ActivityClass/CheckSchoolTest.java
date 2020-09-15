package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.School.Admin.AdapterClass.SchoolTestAdapter;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import java.util.ArrayList;
import java.util.List;

public class CheckSchoolTest {
    private Context mCtx;
    private TextView tvTestMsg;
    private int schoolId, classId;
    private RecyclerView recyclerViewTests;


    public CheckSchoolTest(Context mCtx, int classId, RecyclerView  recyclerViewTests, TextView tvTestMsg) {
        this.mCtx = mCtx;
        this.classId = classId;
        this.recyclerViewTests = recyclerViewTests;
        this.tvTestMsg = tvTestMsg;
    }

    public void checkTest(){

        SchoolData schoolData = SchoolPreference.getInstance(mCtx).getSchoolInfo();
        schoolId = schoolData.getSchoolId();

        AdminData user = SchoolAdminSessionManager.getInstance(mCtx).getUser();
        final int adminId = user.getId();

        final List<TestData> list = new ArrayList<>();

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);

        List<ClassTestData> classTestDataList = dbHelper.getAllClassTest(schoolId, classId, adminId);

        if (classTestDataList.size()>0) {

            for (int j = 0; j < classTestDataList.size(); j++) {
                ClassTestData classTestData = classTestDataList.get(j);
                String totalQuestion = String.valueOf(classTestData.getTotalQuestion());
                String status = String.valueOf(classTestData.getStatus());

                list.add(new TestData(classTestData.getClassTestId(), classId, classTestData.getSubjectId(), classTestData.getMaxMark(), classTestData.getPassMark(), classTestData.getClassTestDate(), classTestData.getClassTestVisible(), classTestData.getClassTestName(), classTestData.getClassTestAddedBy(), totalQuestion, status));
            }

            tvTestMsg.setVisibility(View.GONE);
            recyclerViewTests.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx, RecyclerView.VERTICAL, false);
            recyclerViewTests.setLayoutManager(layoutManager);
            SchoolTestAdapter schoolTestAdapter = new SchoolTestAdapter(mCtx, list, dbHelper);
            recyclerViewTests.setAdapter(schoolTestAdapter);
            recyclerViewTests.setItemViewCacheSize(list.size());


        }
        else {
            recyclerViewTests.setVisibility(View.GONE);
            tvTestMsg.setVisibility(View.VISIBLE);
        }

    }


}
