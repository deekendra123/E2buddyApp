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
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminHomeActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminQuestionReviewActivity;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminViewStudentActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminUpdateTestActivity;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;
import com.aptitude.learning.e2buddy.Util.Utils;
import com.bumptech.glide.Glide;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class SchoolTestAdapter extends RecyclerView.Adapter<SchoolTestAdapter.TestHolder> {


    private Context mCtx;
    private List<TestData> list;
    int i = 0;

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private String subjectName,schoolImage,schoolName;
    private List<String> sectionList;
    private int schoolId;
    private AdminDBHelper dbHelper;

    boolean status = false;
    public SchoolTestAdapter(Context mCtx, List<TestData> list, AdminDBHelper dbHelper) {
        this.mCtx = mCtx;
        this.list = list;
        this.dbHelper = dbHelper;
    }


    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.school_test_item_list, parent, false);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(final TestHolder holder, final int position) {
        final TestData testData = list.get(position);

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
        holder.tvTestName.setText(""+testData.getClassTestName());
        holder.tvMaxMarks.setText("Max marks: "+testData.getMaxMark());
        holder.tvPassMarks.setText("Pass marks: "+testData.getPassMark());
        getSchoolSection(testData.getClassTestId(), holder.tvSectionName);
        getSubjectDetails(testData.getSubjectId(), holder.tvSubjectNmae);

        if (testData.getClassTestVisible() == 1){
            holder.tvVisibility.setText("Visibility: Yes");

        }
        else {
            holder.tvVisibility.setText("Visibility: No");
        }

        if (testData.getStatus().equals("0")){
            holder.imgStatus.setVisibility(View.VISIBLE);
        }

        String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(testData.getClassTestDate());
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

        if (testData.getClassTestVisible() ==1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cardView.setCardBackgroundColor(mCtx.getColor(R.color.bg));
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AdminQuestionReviewActivity.class);
                intent.putExtra("from",1);
                intent.putExtra("classTestId", testData.getClassTestId());
                intent.putExtra("className", testData.getClassId());
                intent.putExtra("TestName", testData.getClassTestName());
                mCtx.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (testData.getClassTestVisible() == 0){
                    showAlertDialog(position, testData.getClassTestId(), holder.cardView, holder.tvVisibility,testData);
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


    public class TestHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvClassName,tvTestName,tvVisibility,tvMaxMarks,tvPassMarks,tvSectionName,tvAddedBy,tvMonth,tvSubjectNmae;
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

        }
    }

    public void showAlertDialog(final int position, final int classTestID, final CardView cardView, final TextView tvVisibility, final TestData testData){


        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.visibility_alert, null);
        final Button btDelete = alertLayout.findViewById(R.id.btDelete);
        final Button btVisible = alertLayout.findViewById(R.id.btVisible);
        final Button btEditTest = alertLayout.findViewById(R.id.btEditTest);



        final AlertDialog.Builder alert = new AlertDialog.Builder((mCtx), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        if (testData.getStatus().equals("1")){
            btVisible.setVisibility(View.VISIBLE);
        }

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                deleteTest(classTestID);
                AdminDBHelper dbHelper = new AdminDBHelper(mCtx);
                dbHelper.deleteClassTest(classTestID);
                dbHelper.deleteQuestion(schoolId,classTestID);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());

                dialog.dismiss();
            }
        });
        btVisible.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                while (i < 15) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.GET_ADMIN_TOTAL_QUESTION,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("true")) {

                                            int totalQuestion = jsonObject.getInt("data");
                                            int tQ = Integer.parseInt(testData.getTotalQuestion());
                                            if (totalQuestion != tQ){
                                                List<QuestionData> questionDataList = dbHelper.getAllQuestion(schoolId,classTestID);
                                                for (int j = 0; j<questionDataList.size(); j++){
                                                    QuestionData questionData = questionDataList.get(j);
                                                    insertQuestionToServer(questionData.getQuestionid(), classTestID, questionData.getQuestion(), questionData.getOption1(), questionData.getOption2(), questionData.getOption3(), questionData.getOption4(), questionData.getAnswer(), questionData.getDescription());
                                                }
                                                status = false;
                                            }
                                            else {
                                                status = true;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                            parms.put("classTestId", String.valueOf(classTestID));

                            return parms;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                    requestQueue.add(stringRequest);


                    if (status){

                        break;
                    }
                    else {
                        i++;
                    }

                }

                    updateTest(classTestID, testData);
                    dialog.dismiss();
                    cardView.setCardBackgroundColor(mCtx.getColor(R.color.bg));
                    tvVisibility.setText("Visibility: Yes");


            }
        });

        btEditTest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(mCtx, AdminUpdateTestActivity.class);
                intent.putExtra("classTestId", testData.getClassTestId());
                intent.putExtra("questionStatus", "0");
                intent.putExtra("questionNumber", testData.getTotalQuestion());
//
                mCtx.startActivity(intent);
            }
        });


    }



    private void deleteTest(final int classTestId){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_DELETE_TEST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.e("dkdata", classTestId+ "  ");
                HashMap<String, String> parms = new HashMap<>();
                parms.put("classTestId", String.valueOf(classTestId));

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);


    }

    private void updateTest(final int classTestId, final TestData testData){

        if(dbHelper.updateClassTestVisibility(
                classTestId,
                1

        )

        ){
            Log.e("msg","dk");
        } else{
            Log.e("msg","not done");

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_UPDATE_TEST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendNotification(classTestId, testData);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("DKKKerror", ""+error.getMessage());
                        Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.e("dkdata", classTestId+ "  ");
                HashMap<String, String> parms = new HashMap<>();
                parms.put("classTestId", String.valueOf(classTestId));
                parms.put("classTestVisible", String.valueOf(1));

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);


    }

    private void sendNotification(final int classTestId, final TestData testData) {

        final String schoolLogo = AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolImage;
        List<SectionData> sectionDataList = dbHelper.getClassTestSection(classTestId);
        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);

            final int sectionId = sectionData.getSectionId();
            int classId = sectionData.getClassId();
            final String sectionName = sectionData.getSectionName();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_SEND_NOTIFICATION,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("deeketoken",response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("DKKKerror", ""+error.getMessage());
                            Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> parms = new HashMap<>();
                    parms.put("schoolId", String.valueOf(schoolId));
                    parms.put("classId", String.valueOf(testData.getClassId()));
                    parms.put("sectionId", String.valueOf(sectionId));
                    parms.put("subjectId", String.valueOf(testData.getSubjectId()));
                    parms.put("schoolLogo", schoolLogo);
                    parms.put("sectionName",sectionName);
                    parms.put("schoolName",schoolName);

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
            requestQueue.add(stringRequest);
        }

    }

    private void getSchoolSection(final int classTestId, final TextView textView){
        final List<String> section = new ArrayList<>();
        sectionList = new ArrayList<>();

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);


        List<SectionData> sectionDataList = dbHelper.getClassTestSection(classTestId);

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


    private void showViewStudentAlert(final TestData testData){
        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.view_student_alert, null);
        final Button btViewStudents = alertLayout.findViewById(R.id.btViewStudents);
        final TextView tvTestName = alertLayout.findViewById(R.id.tvTestName);

        final AlertDialog.Builder alert = new AlertDialog.Builder((mCtx), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        tvTestName.setText(""+testData.getClassTestName());

        btViewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                getSchoolSectionIds(testData);

            }
       });

    }

    private void getSubjectDetails(final int subjectId, final TextView tvSubjectName){


        List<SubjectData> subjectDataList = dbHelper.getSubject(subjectId);

        for (int j = 0; j<subjectDataList.size(); j++){
            SubjectData subjectData = subjectDataList.get(j);

            subjectName = subjectData.getSubjectName();

            tvSubjectName.setText(""+subjectData.getSubjectName());


        }

    }


    private void getSchoolSectionIds(final TestData testData){
        sectionList = new ArrayList<>();

        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);

        List<SectionData> sectionDataList = dbHelper.getClassTestSection(testData.getClassTestId());

        for (int j = 0; j<sectionDataList.size(); j++){
            SectionData sectionData = sectionDataList.get(j);
            String sectionId = String.valueOf(sectionData.getSectionId());
            sectionList.add(sectionId);
        }

        Intent intent = new Intent(mCtx, AdminViewStudentActivity.class);
        intent.putExtra("classTestId", testData.getClassTestId());
        intent.putExtra("classId", testData.getClassId());
        intent.putExtra("testName", testData.getClassTestName());
        intent.putExtra("subjectName", subjectName);
        intent.putExtra("maxMarks", testData.getMaxMark());
        intent.putExtra("passMarks", testData.getPassMark());
        intent.putStringArrayListExtra("sectionId", (ArrayList<String>) sectionList);

        mCtx.startActivity(intent);

    }

    private void insertQuestionToServer(final int questionId, final int classTestId, final String question, final String option1, final String option2, final String option3, final String option4, final String answer, final String description) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppCinfig.URL_INSERT_QUESTION,
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
                parms.put("schoolCodeId", String.valueOf(schoolId));
                parms.put("classTestId", String.valueOf(classTestId));
                parms.put("questionId", String.valueOf(questionId));
                parms.put("question", question);
                parms.put("option1", option1);
                parms.put("option2", option2);
                parms.put("option3", option3);
                parms.put("option4", option4);
                parms.put("answer", answer);
                parms.put("description", description);

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

    }



}
