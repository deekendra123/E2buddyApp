package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminExamQuestionReviewActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminQuestionReviewActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminStudentExamResultActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminUpdateExamActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminUpdateTestActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminViewStudentActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.ViewExamStudentActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExamTestAdapter extends RecyclerView.Adapter<ExamTestAdapter.TestHolder> {


    private Context mCtx;
    private List<ExamData> list;
    int i = 0;

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private String subjectName,schoolImage,schoolName;
    private List<String> sectionList;
    private int schoolId;
    private AdminDBHelper dbHelper;

    boolean status = false;
    public ExamTestAdapter(Context mCtx, List<ExamData> list, AdminDBHelper dbHelper) {
        this.mCtx = mCtx;
        this.list = list;
        this.dbHelper = dbHelper;
    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.school_exam_layout, parent, false);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(final TestHolder holder, final int position) {
        final ExamData testData = list.get(position);

        SchoolData schoolData = SchoolPreference.getInstance(mCtx).getSchoolInfo();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();
        schoolName = schoolData.getSchoolName();

        AdminData user = SchoolAdminSessionManager.getInstance(mCtx).getUser();
        String adminName = user.getUsername();

        if (!schoolImage.isEmpty()){
            Glide.with(mCtx)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage)
                    .into(holder.imgSchoolLogo);
        }
        else {
            holder.imgSchoolLogo.setBackgroundResource(R.drawable.school);
        }

        holder.tvAddedBy.setText("Added by : "+adminName);
        holder.tvTestName.setText(""+testData.getExamName());
        holder.tvMaxMarks.setText("Max marks: "+testData.getMaxMark());
        holder.tvPassMarks.setText("Pass marks: "+testData.getPassMark());
        getSchoolSection(testData.getExamId(), holder.tvSectionName);
        getSubjectDetails(testData.getSubjectId(), holder.tvSubjectNmae);

        if (testData.getExamVisible() == 1){
            holder.tvVisibility.setText("Visibility: Yes");
            holder.tvCounter.setVisibility(View.VISIBLE);
            int count = dbHelper.getStudentCount(testData.getExamId());
            holder.tvCounter.setText(""+count);
        }
        else {
            holder.tvVisibility.setText("Visibility: No");
        }

        if (testData.getStatus()==0){
            holder.imgStatus.setVisibility(View.VISIBLE);
        }
        else {
            holder.imgStatus.setVisibility(View.GONE);
        }

        String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(testData.getExamDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd");
        date = spf.format(newDate);
        holder.tvDate.setText(""+date);

        spf= new SimpleDateFormat("MMM");
        date = spf.format(newDate);
        holder.tvMonth.setText(""+date);

        // System.out.println(date);
        if (testData.getExamVisible() ==1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cardView.setCardBackgroundColor(mCtx.getColor(R.color.bg));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSchoolSectionIds(testData,"1");

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (testData.getExamVisible() == 0) {
                    showAlertDialog(position, testData.getExamId(), holder.cardView, holder.tvVisibility, testData);
                    return true;
                }
                else {
                 showViewStudentAlert(testData);
                 return true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    private void showViewStudentAlert(final ExamData testData){
        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.view_student_alert, null);
        final Button btViewStudents = alertLayout.findViewById(R.id.btViewStudents);
        final TextView tvTestName = alertLayout.findViewById(R.id.tvTestName);

        final AlertDialog.Builder alert = new AlertDialog.Builder((mCtx), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        tvTestName.setText(""+testData.getExamName());

        btViewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                getSchoolSectionIds(testData,"2");

            }
        });

    }




    public class TestHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvClassName,tvTestName,tvVisibility,tvMaxMarks,tvPassMarks,tvSectionName,tvAddedBy,tvMonth,tvSubjectNmae,tvCounter;
        CardView cardView;
        CircleImageView imgSchoolLogo;
        ImageView imgStatus;

        private TestHolder(View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            tvVisibility = itemView.findViewById(R.id.tvVisibility);
            tvMaxMarks = itemView.findViewById(R.id.tvMaxMarks);
            tvPassMarks = itemView.findViewById(R.id.tvPassMarks);
            cardView = itemView.findViewById(R.id.cardView);
            tvSectionName = itemView.findViewById(R.id.tvSectionName);
            imgSchoolLogo = itemView.findViewById(R.id.imgSchoolLogo);
            tvAddedBy = itemView.findViewById(R.id.tvAddedBy);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvSubjectNmae = itemView.findViewById(R.id.tvSubjectNmae);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            tvCounter = itemView.findViewById(R.id.tvCounter);

        }
    }

    public void showAlertDialog(final int position, final int classTestID, final CardView cardView, final TextView tvVisibility, final ExamData testData){


        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.visibility_alert, null);
        final Button btDelete = alertLayout.findViewById(R.id.btDelete);
        final Button btEditTest = alertLayout.findViewById(R.id.btEditTest);



        final AlertDialog.Builder alert = new AlertDialog.Builder((mCtx), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTest(classTestID);
                dbHelper.deleteExam(classTestID);
                dbHelper.deleteExamQuestion(schoolId,classTestID);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());

                dialog.dismiss();
            }
        });

        btEditTest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                Intent intent = new Intent(mCtx, AdminUpdateExamActivity.class);
                intent.putExtra("examId", testData.getExamId());
                intent.putExtra("questionStatus", "0");
                intent.putExtra("questionNumber", testData.getTotalQuestion());
//
                mCtx.startActivity(intent);
            }
        });


    }



    private void deleteTest(final int examId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.DELETE_EXAM,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                parms.put("examId", String.valueOf(examId));

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);


    }



    private void getSchoolSection(final int classTestId, final TextView textView){
        final List<String> section = new ArrayList<>();
        sectionList = new ArrayList<>();

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);


        List<SectionData> sectionDataList = dbHelper.getExamSection(classTestId);

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);
            section.add(sectionData.getSectionName());

        }

        Collections.sort(section);
        for (int i=0; i<section.size();i++){

            textView.append(section.get(i));
            textView.append(" ");

        }

    }


    private void getSubjectDetails(final int subjectId, final TextView tvSubjectName){


        List<SubjectData> subjectDataList = dbHelper.getSubject(subjectId);

        for (int j = 0; j<subjectDataList.size(); j++){
            SubjectData subjectData = subjectDataList.get(j);

            subjectName = subjectData.getSubjectName();

            tvSubjectName.setText(""+subjectData.getSubjectName());


        }

    }


    private void getSchoolSectionIds(final ExamData testData,String value){
        sectionList = new ArrayList<>();

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);

        List<SectionData> sectionDataList = dbHelper.getExamSection(testData.getExamId());

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);
            String sectionId = String.valueOf(sectionData.getSectionId());
            sectionList.add(sectionId);
        }

        if (value.equals("1")){
            Intent intent = new Intent(mCtx, ViewExamStudentActivity.class);
            intent.putExtra("examId", testData.getExamId());
            intent.putExtra("classId", testData.getClassId());
            intent.putExtra("testName", testData.getExamName());
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("maxMarks", testData.getMaxMark());
            intent.putExtra("passMarks", testData.getPassMark());
            intent.putStringArrayListExtra("sectionId", (ArrayList<String>) sectionList);
            intent.putExtra("examDate", testData.getExamDate());

            mCtx.startActivity(intent);
        }else {
            Intent intent = new Intent(mCtx, AdminStudentExamResultActivity.class);
            intent.putExtra("examId", testData.getExamId());
            intent.putExtra("classId", testData.getClassId());
            intent.putExtra("testName", testData.getExamName());
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("maxMarks", testData.getMaxMark());
            intent.putExtra("passMarks", testData.getPassMark());
            intent.putStringArrayListExtra("sectionId", (ArrayList<String>) sectionList);
            intent.putExtra("examDate", testData.getExamDate());

            mCtx.startActivity(intent);
        }


    }




}
