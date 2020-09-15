package com.aptitude.learning.e2buddy.School.Admin.ActivityClass;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSectionName {
    private Context context;
    private int classTestId;
    private TextView tvSectionName;

    public GetSectionName(Context context, int classTestId, TextView tvSectionName) {
        this.context = context;
        this.classTestId = classTestId;
        this.tvSectionName = tvSectionName;
    }


    public void getSchoolSection(){
        final List<String> section = new ArrayList<>();
        section.clear();

        AdminDBHelper dbHelper = new AdminDBHelper(context);

        List<SectionData> sectionDataList = dbHelper.getClassTestSection(classTestId);

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
