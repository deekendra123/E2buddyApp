package com.aptitude.learning.e2buddy.School.SuperAdmin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.DataClass.NoticeData;
import com.aptitude.learning.e2buddy.School.Student.DialogFragment.ViewNoticeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.RequestHolder> {

    Context mCtx;
    List<NoticeData> list;
    String date="Mar 10, 2016 6:30:00 PM";

    public NoticeAdapter(Context mCtx, List<NoticeData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.notice_list_item, viewGroup, false);

        return new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {

        final NoticeData noticeData = list.get(position);
        holder.tvDescription.setText(""+noticeData.getNoticeDescription());
        holder.tvTitle.setText(""+noticeData.getNoticeTitle());

        int classId = noticeData.getClassId();
        if (classId!=0) {
            holder.tvClassName.setVisibility(View.VISIBLE);
            if (classId == 1) {
                holder.tvClassName.setText("Class: Pre Nursery");
            } else if (classId == 2) {
                holder.tvClassName.setText("Class: Nursery");
            } else if (classId == 3) {
                holder.tvClassName.setText("Class: LKG");
            } else if (classId == 4) {
                holder.tvClassName.setText("Class: UKG");
            } else if (classId == 5) {
                holder.tvClassName.setText("Class: First");
            } else if (classId == 6) {
                holder.tvClassName.setText("Class: Second");
            } else if (classId == 7) {
                holder.tvClassName.setText("Class: Third");
            } else if (classId == 8) {
                holder.tvClassName.setText("Class: Fourth");
            } else if (classId == 9) {
                holder.tvClassName.setText("Class: Fifth");
            } else if (classId == 10) {
                holder.tvClassName.setText("Class: Sixth");
            } else if (classId == 11) {
                holder.tvClassName.setText("Class: Seventh");
            } else if (classId == 12) {
                holder.tvClassName.setText("Class: Eighth");
            } else if (classId == 13) {
                holder.tvClassName.setText("Class: Ninth");
            } else if (classId == 14) {
                holder.tvClassName.setText("Class: Tenth");
            } else if (classId == 15) {
                holder.tvClassName.setText("Class: Eleventh");
            } else if (classId == 16) {
                holder.tvClassName.setText("Class: Twelfth");
            }
        }
        String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(noticeData.getAddedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd");
        date = spf.format(newDate);
        holder.tvDate.setText(""+date);

        spf= new SimpleDateFormat("MMM");
        date = spf.format(newDate);
        holder.tvMonth.setText(""+date);

        if (noticeData.getNoticeType().equals("1")){
            holder.tvClassName.setVisibility(View.VISIBLE);
            holder.tvClassName.setText("All Classes");
            holder.tvSectionName.setVisibility(View.INVISIBLE);
            holder.tvSection.setVisibility(View.INVISIBLE);
        }
        else {
            getSchoolSection(noticeData.getNoticeId(), holder.tvSection);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = ((AppCompatActivity)mCtx).getSupportFragmentManager();
                DialogFragment dialog = ViewNoticeDialog.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("title", noticeData.getNoticeTitle());
                bundle.putString("description", noticeData.getNoticeDescription());
                bundle.putString("link", noticeData.getLink());
                bundle.putString("date",noticeData.getAddedAt());
                dialog.setArguments(bundle);
                dialog.show(fm,"tag");
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RequestHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvTitle,tvDate,tvSection,tvMonth,tvSectionName,tvClassName;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvSection = itemView.findViewById(R.id.tvSection);
            tvSectionName = itemView.findViewById(R.id.tvSectionName);
            tvClassName = itemView.findViewById(R.id.tvClassName);

        }
    }

    private void getSchoolSection(final int noticeId, final TextView textView){
        final List<String> section = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_NOTICE_SECTION_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("dksr",response);

                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);

                                String sectionId = data.getString("sectionId");
                                int classId = data.getInt("classId");
                                String sectionName = data.getString("sectionName");

                                section.add(sectionName);

                            }


                            Collections.sort(section);
                            for (int i=0; i<section.size();i++){

                                textView.append(""+section.get(i));
                                textView.append(" ");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("noticeId", String.valueOf(noticeId));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

    }

}
