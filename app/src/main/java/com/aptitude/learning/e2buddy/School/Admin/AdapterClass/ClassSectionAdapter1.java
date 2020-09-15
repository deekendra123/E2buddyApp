package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassSectionAdapter1 extends RecyclerView.Adapter<ClassSectionAdapter1.MyViewHolder> {

    private Context mCtx;
    private List<SectionData> mModelList;
    private int classTestId;
    private int studentSectionId;

    public ClassSectionAdapter1(Context mCtx, List<SectionData> mModelList, int classTestId) {
        this.mCtx = mCtx;
        this.mModelList = mModelList;
        this.classTestId = classTestId;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SectionData model = mModelList.get(position);

        holder.textView.setText(model.getSectionName());

        getTestDetails(model,holder);

        holder.itemView.setClickable(false);

    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.tvSection);
        }
    }

    private void getTestDetails(final SectionData sectionData, final MyViewHolder holder){

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);
        List<ClassTestData> classTestDataList = dbHelper.getClassTest(classTestId);

        for (int j = 0; j < classTestDataList.size(); j++) {
            ClassTestData classTestData = classTestDataList.get(j);


            studentSectionId = classTestData.getSectionId();
            if (sectionData.getSectionId()==studentSectionId) {
                sectionData.setSelected(true);
                holder.textView.setBackgroundColor(sectionData.isSelected() ? R.drawable.buttons2 : Color.WHITE);

                AdminDBHelper mydb = new AdminDBHelper(mCtx);
                mydb.insertSection(sectionData.getSectionId());
            }

        }




//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_GET_TEST, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("resp",response);
//                try {
//                    JSONArray array = new JSONArray(response);
//
//                    if (array.length()>0) {
//
//                        for (int i = 0; i < array.length(); i++) {
//
//                            JSONObject data = array.getJSONObject(i);
//
//                            studentSectionId = data.getInt("sectionId");
//
//                                    if (sectionData.getSectionId()==studentSectionId) {
//                                        sectionData.setSelected(true);
//                                        holder.textView.setBackgroundColor(sectionData.isSelected() ? R.drawable.buttons2 : Color.WHITE);
//
//                                        AdminDBHelper mydb = new AdminDBHelper(mCtx);
//                                        mydb.insertSection(sectionData.getSectionId());
//                                    }
//                        }
//
//                    }
//                    else {
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        },
//                new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                        Log.e("errr", error.getMessage());
//                        Utils.showToast("Something went wrong");
//                    }
//                })
//        { @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//
//            HashMap<String, String> parms = new HashMap<>();
//            parms.put("classTestId", String.valueOf(classTestId));
//
//            return parms;
//        }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
//        requestQueue.add(stringRequest);

    }



}
