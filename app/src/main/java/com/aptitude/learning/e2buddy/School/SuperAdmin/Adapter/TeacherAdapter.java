package com.aptitude.learning.e2buddy.School.SuperAdmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.SuperAdminAddTeacherActivity;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ObjectData.TeacherData;
import com.aptitude.learning.e2buddy.Util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.RequestHolder> {

    Context mCtx;
    List<TeacherData> list;
    int schoolId;


    public TeacherAdapter(Context mCtx, List<TeacherData> list, int schoolId) {
        this.mCtx = mCtx;
        this.list = list;
        this.schoolId = schoolId;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.teacher_list_item, viewGroup, false);

        return new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, final int position) {

        final TeacherData teacherData = list.get(position);
        holder.tvTeacherName.setText(""+teacherData.getTeacherName());
        holder.tvTeacherEmail.setText(""+teacherData.getTeacherEmail());
        holder.tvTeacherPhone.setText(""+teacherData.getTeacherPhone());

        if (teacherData.getTeacherPhone().equals("null")){
            holder.relativeLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlertDialog(teacherData, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RequestHolder extends RecyclerView.ViewHolder {

        TextView tvTeacherName, tvTeacherEmail,tvTeacherPhone;
        private RelativeLayout relativeLayout;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvTeacherEmail = itemView.findViewById(R.id.tvTeacherEmail);
            tvTeacherPhone = itemView.findViewById(R.id.tvTeacherPhone);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }

    public void showAlertDialog(final TeacherData teacherData, final int position){


        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.teacher_alert, null);
        final Button btDelete = alertLayout.findViewById(R.id.btDelete);
        final Button btEditTeacher = alertLayout.findViewById(R.id.btEditTeacher);

        final AlertDialog.Builder alert = new AlertDialog.Builder((mCtx), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();


        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTeacherDetails(teacherData, position);
                dialog.dismiss();
            }
        });


        btEditTeacher.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(mCtx, SuperAdminAddTeacherActivity.class);
                intent.putExtra("from", "1");
                intent.putExtra("teacherName", teacherData.getTeacherName());
                intent.putExtra("teacherEmail", teacherData.getTeacherEmail());
                intent.putExtra("teacherPhone", teacherData.getTeacherPhone());
                intent.putExtra("teacherId", teacherData.getTeacherId());

                mCtx.startActivity(intent);
            }
        });


    }

    private void deleteTeacherDetails(final TeacherData teacherData, final int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        final String dateTime = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_DELETE_TEACHER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utils.showToast("Teacher Deleted Successfully");

                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();
                parms.put("teacherId", String.valueOf(teacherData.getTeacherId()));
                parms.put("teacherName", teacherData.getTeacherName());
                parms.put("teacherEmail", teacherData.getTeacherEmail());
                parms.put("teacherPhone", teacherData.getTeacherPhone());
                parms.put("addedAt", dateTime);
                parms.put("schoolCodeId", String.valueOf(schoolId));
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

    }

}
