package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

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
import com.aptitude.learning.e2buddy.Util.Utils;

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

public class StudentNoticeAdapter extends RecyclerView.Adapter<StudentNoticeAdapter.RequestHolder> {

    Context mCtx;
    List<NoticeData> list;
    String date="Mar 10, 2016 6:30:00 PM";

    public StudentNoticeAdapter(Context mCtx, List<NoticeData> list) {
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

        holder.tvSection.setVisibility(View.GONE);
        holder.tvSectionName.setVisibility(View.GONE);
        final NoticeData noticeData = list.get(position);
        holder.tvDescription.setText(""+noticeData.getNoticeDescription());
        holder.tvTitle.setText(""+noticeData.getNoticeTitle());

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

        TextView tvDescription, tvTitle,tvDate,tvSection,tvMonth,tvSectionName;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvSection = itemView.findViewById(R.id.tvSection);
            tvSectionName = itemView.findViewById(R.id.tvSectionName);

        }
    }

}
