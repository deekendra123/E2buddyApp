package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.TestData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentResultActivity;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentTestInstructionActivity;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.TestStatus;
import com.bumptech.glide.Glide;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentTestAdapter extends RecyclerView.Adapter<StudentTestAdapter.QuestionHolder> {

    private Context mCtx;
    private List<TestData> list;
    private int classId,studentId,totalQuestion;
    private String subjectName;
    private AdminDBHelper dbHelper;

    public StudentTestAdapter(Context mCtx, List<TestData> list, int classId, int studentId, AdminDBHelper dbHelper) {
        this.mCtx = mCtx;
        this.list = list;
        this.classId = classId;
        this.studentId = studentId;
        this.dbHelper = dbHelper;
    }


    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.test_list_items, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        final TestData testData = list.get(position);

       StudentData studentData = SchoolStudentSessionManager.getInstance(mCtx).getUser();

       String schoolLogo = studentData.getSchoolLogo();

        if (!schoolLogo.isEmpty()){
            Glide.with(mCtx)
                    .load(AppCinfig.BASE_SCHOOL_IMAGE_URL+schoolLogo)
                    .into(holder.imgSchoolLogo);
        }
        else {

            holder.imgSchoolLogo.setBackgroundResource(R.drawable.science);
        }

        checkTestStatus(testData.getClassTestId(), holder.tvMaxMarks, holder.tvPassMarks, holder, testData);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {
        TextView tvTestName,tvDate,tvMonth,tvSubjectNmae,tvMaxMarks,tvPassMarks,tvAddedBy,tvStartTest;
        CircleImageView imgSchoolLogo;
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvSubjectNmae = itemView.findViewById(R.id.tvSubjectNmae);
            tvMaxMarks = itemView.findViewById(R.id.tvMaxMarks);
            tvPassMarks = itemView.findViewById(R.id.tvPassMarks);

            tvAddedBy = itemView.findViewById(R.id.tvAddedBy);
            imgSchoolLogo = itemView.findViewById(R.id.imgSchoolLogo);
            tvStartTest = itemView.findViewById(R.id.tvStartTest);
        }
    }

    private void getAdminDetails(final int adminId, final TextView tvAddedBy){
        String adminName = dbHelper.getAdminDetails(adminId);
        tvAddedBy.setText("Added by: "+adminName);
    }

    private void getSubjectDetails(final int subjectId, final TextView tvSubjectName){

        String subjectName = dbHelper.getSubjectName(subjectId);

            tvSubjectName.setText(""+subjectName);

    }


    private void checkTestStatus(final int classTestId, final TextView tvStatus, final TextView passMars, final QuestionHolder holder, final TestData testData){


        getSubjectDetails(testData.getSubjectId(), holder.tvSubjectNmae);
        getAdminDetails(testData.getClassTestAddedBy(), holder.tvAddedBy);
        holder.tvTestName.setText(""+testData.getClassTestName());

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

        List<TestStatus> testStatusList = dbHelper.getStudentTestStatus(classTestId,studentId);

        if (testStatusList.size()>0) {
            for (int i = 0; i < testStatusList.size(); i++) {
                TestStatus testStatus = testStatusList.get(i);

                int status = testStatus.getStatus();
                int classTestMark = testStatus.getClassTestMark();
                final String teacherRemark = testStatus.getTeacherRemark();

                if (status == 1) {
                    tvStatus.setText("Status: " + "Pass");
                    passMars.setText("Obtained Marks: " + classTestMark + "/" + testData.getMaxMark());

                } else {
                    tvStatus.setText("Status: " + "Fail");
                    passMars.setText("Obtained Marks: " + classTestMark + "/" + testData.getMaxMark());
                }


                holder.tvStartTest.setText("VIEW ANSWER");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mCtx, StudentResultActivity.class);

                        intent.putExtra("subjectName", subjectName);
                        intent.putExtra("classTestId", classTestId);
                        intent.putExtra("maxMarks", testData.getMaxMark());
                        intent.putExtra("passMarks", testData.getPassMark());
                        intent.putExtra("corrMarks", testData.getCorrectAnswerMark());
                        intent.putExtra("notAttemptMarks", testData.getNotAttemptedMark());
                        intent.putExtra("wrongAnsMarks", testData.getWrongAnswerMark());
                        intent.putExtra("timeAllotted", testData.getTimeAlloted());
                        intent.putExtra("totalQuestion", totalQuestion);
                        intent.putExtra("teacherRemark",teacherRemark);
                        mCtx.startActivity(intent);

                    }
                });

                if (testStatus.getClassTestId()==testData.getClassTestId()){
                    break;
                }
            }
        }
        else {
            holder.tvMaxMarks.setText("Max marks: "+testData.getMaxMark());
            holder.tvPassMarks.setText("Pass marks: "+testData.getPassMark());


            holder.tvStartTest.setText("START TEST");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.itemView.setEnabled(false);
                    Intent intent = new Intent(mCtx, StudentTestInstructionActivity.class);
                    intent.putExtra("classTestId", testData.getClassTestId());
                    intent.putExtra("subjectId", testData.getSubjectId());
                    intent.putExtra("testName", testData.getClassTestName());
                    intent.putExtra("maxMarks", testData.getMaxMark());
                    intent.putExtra("passMarks", testData.getPassMark());
                    intent.putExtra("corrMarks", testData.getCorrectAnswerMark());
                    intent.putExtra("notAttemptMarks", testData.getNotAttemptedMark());
                    intent.putExtra("wrongAnsMarks", testData.getWrongAnswerMark());
                    intent.putExtra("timeAllotted", testData.getTimeAlloted());

                    mCtx.startActivity(intent);

                }
            });

        }

        }

}
