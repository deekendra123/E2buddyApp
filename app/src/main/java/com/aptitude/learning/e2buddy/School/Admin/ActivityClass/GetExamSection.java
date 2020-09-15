package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import android.content.Context;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetExamSection {
    private Context context;
    private int examId;
    private TextView tvSectionName;

    public GetExamSection(Context context, int examId, TextView tvSectionName) {
        this.context = context;
        this.examId = examId;
        this.tvSectionName = tvSectionName;
    }


    public void getSchoolSection(){
        final List<String> section = new ArrayList<>();
        section.clear();

        AdminDBHelper dbHelper = new AdminDBHelper(context);

        List<SectionData> sectionDataList = dbHelper.getExamSection(examId);

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);
            section.add(sectionData.getSectionName());

        }

        Collections.sort(section);
        for (int i=0; i<section.size();i++){

            tvSectionName.append(" "+section.get(i));
            tvSectionName.append(" ");

        }

    }


}
