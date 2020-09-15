package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.aptitude.learning.e2buddy.School.Admin.DataClass.StudentTestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTestAdapter extends RecyclerView.Adapter<StudentTestAdapter.MyViewHolder> {


    private Context mCtx;
    private List<StudentTestData> mModelList;
    private int passMarks,classTestId;
    private AdminDBHelper dbHelper;

    public StudentTestAdapter(Context mCtx, List<StudentTestData> mModelList,int passMarks, int classTestId, AdminDBHelper dbHelper) {
        this.mCtx = mCtx;
        this.mModelList = mModelList;
        this.passMarks = passMarks;
        this.classTestId = classTestId;
        this.dbHelper = dbHelper;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.student_test_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StudentTestData studentTestData = mModelList.get(position);

        holder.tvStudentName.setText(""+ studentTestData.getStudentName());
        holder.tvSectionName.setText(""+ studentTestData.getSectionName());
        holder.tvRollNo.setText(""+studentTestData.getRollNo());

        holder.tvRollNo.setText(""+studentTestData.getRollNo());
        if (studentTestData.getMarks()==-100){
            holder.tvMarks.setText("A");
            holder.image.setVisibility(View.INVISIBLE);

            holder.tvRemark.setVisibility(View.GONE);
            holder.imageRemark.setVisibility(View.GONE);
        }
       else {
            holder.tvMarks.setText(""+studentTestData.getMarks());
            holder.image.setVisibility(View.VISIBLE);

            if (studentTestData.getMarks()>=passMarks){
                    holder.image.setBackgroundResource(R.drawable.ic_correct);
                }
                else {
                    holder.image.setBackgroundResource(R.drawable.ic_wrong);
                }

                if (studentTestData.getMarks()==-100 && studentTestData.getTeacherRemark().equals("0")){
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.imageRemark.setVisibility(View.GONE);
                }
                else if (studentTestData.getMarks()!=-100 && studentTestData.getTeacherRemark().equals("0")){

                    holder.tvRemark.setVisibility(View.GONE);
                    holder.imageRemark.setVisibility(View.VISIBLE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAlertDialog(studentTestData, holder, "0", position);
                        }
                    });
                }

                else if (studentTestData.getMarks()!=-100 && !studentTestData.getTeacherRemark().equals("0")){

                    holder.tvRemark.setVisibility(View.VISIBLE);
                    holder.imageRemark.setVisibility(View.VISIBLE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAlertDialog(studentTestData, holder, "1",position);
                        }
                    });
                }

            }

        }
    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvSectionName, tvMarks,tvRemark,tvRollNo;
        ImageView image,imageRemark;
        private MyViewHolder(View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvSectionName = itemView.findViewById(R.id.tvSectionName);
            tvMarks = itemView.findViewById(R.id.tvMarks);
            image = itemView.findViewById(R.id.image);
            imageRemark = itemView.findViewById(R.id.imageRemark);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            tvRollNo = itemView.findViewById(R.id.tvRollNo);

        }
    }

    private void showAlertDialog(final StudentTestData studentTestData, final MyViewHolder holder, final String status, final int position){

        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.remark_alert_dialog, null);
        final Button btAddRemark = alertLayout.findViewById(R.id.btAddRemark);
        final TextInputEditText etRemark = alertLayout.findViewById(R.id.etRemark);
        final Button btDeleteRemark = alertLayout.findViewById(R.id.btDeleteRemark);

        final AlertDialog.Builder alert = new AlertDialog.Builder(mCtx, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        if (status.equals("1")){
            etRemark.setText(studentTestData.getTeacherRemark());
            btAddRemark.setText("Update");
            btDeleteRemark.setVisibility(View.VISIBLE);
        }

        btAddRemark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (etRemark.getText().toString().isEmpty()) {
                            etRemark.setError("Enter Remark");
                            etRemark.requestFocus();
                            return;
                        }
                        else {

                            if(dbHelper.updateStudentRemark(
                                    classTestId,
                                    studentTestData.getStudentId(),
                                    etRemark.getText().toString()
                            )
                            ){
                                Log.e("msg","done");
                            } else{
                                Log.e("msg","not done");

                            }

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_REMARK, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    mModelList.set(position, new StudentTestData(studentTestData.getStudentId(),studentTestData.getStudentName(),studentTestData.getSectionName(), studentTestData.getMarks(), etRemark.getText().toString(), studentTestData.getRollNo()));
                                    notifyItemChanged(position);

                                    holder.tvRemark.setVisibility(View.VISIBLE);
                                    holder.imageRemark.setVisibility(View.VISIBLE);

                                }
                            },
                                    new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Log.e("errr", error.getMessage());
                                            Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            { @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Log.e("error", classTestId+"   "+ studentTestData.getStudentId());
                                HashMap<String, String> parms = new HashMap<>();
                                parms.put("classTestId", String.valueOf(classTestId));
                                parms.put("userId", String.valueOf(studentTestData.getStudentId()));
                                parms.put("remark", etRemark.getText().toString());
                                return parms;
                            }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                            requestQueue.add(stringRequest);
                        }
                    }
                });

        btDeleteRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(dbHelper.updateStudentRemark(
                            classTestId,
                            studentTestData.getStudentId(),
                            "0"
                    )
                    ){
                        Log.e("msg","done");
                    } else{
                        Log.e("msg","not done");

                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_REMARK, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();

                            mModelList.set(position, new StudentTestData(studentTestData.getStudentId(),studentTestData.getStudentName(),studentTestData.getSectionName(), studentTestData.getMarks(), "0",studentTestData.getRollNo()));
                            notifyItemChanged(position);

                            holder.tvRemark.setVisibility(View.VISIBLE);
                            holder.imageRemark.setVisibility(View.VISIBLE);

                        }
                    },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.e("errr", error.getMessage());
                                    Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            })
                    { @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Log.e("error", classTestId+"   "+ studentTestData.getStudentId());
                        HashMap<String, String> parms = new HashMap<>();
                        parms.put("classTestId", String.valueOf(classTestId));
                        parms.put("userId", String.valueOf(studentTestData.getStudentId()));
                        parms.put("remark", "0");
                        return parms;
                    }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                    requestQueue.add(stringRequest);

            }
        });
    }
}
