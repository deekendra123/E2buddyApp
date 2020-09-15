package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.Preference.SchoolStudentSessionManager;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.ExamInstructionActivity;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentExamAdapter extends RecyclerView.Adapter<StudentExamAdapter.QuestionHolder> {

    private Context mCtx;
    private List<ExamData> list;
    private int classId,studentId,totalQuestion;
    private String subjectName;
    private AdminDBHelper dbHelper;

    public StudentExamAdapter(Context mCtx, List<ExamData> list, int classId, int studentId, AdminDBHelper dbHelper) {
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
        final ExamData examData = list.get(position);

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


        getSubjectDetails(examData.getSubjectId(), holder.tvSubjectNmae);
        getAdminDetails(examData.getExamAddedBy(), holder.tvAddedBy);
        holder.tvTestName.setText(""+examData.getExamName());

        String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(examData.getExamDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd");
        date = spf.format(newDate);
        holder.tvDate.setText(""+date);

        spf= new SimpleDateFormat("MMM");
        date = spf.format(newDate);
        holder.tvMonth.setText(""+date);

        holder.tvMaxMarks.setText("Max marks: "+examData.getMaxMark());
        holder.tvPassMarks.setText("Pass marks: "+examData.getPassMark());

        if (dbHelper.checkExamMark(examData.getExamId(), studentId)){
            holder.tvStartTest.setVisibility(View.GONE);
        }
        else {
            checkTestStatus(examData.getExamId(), holder.tvMaxMarks, holder.tvPassMarks, holder, examData);

        }

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
       tvSubjectName.setText(subjectName);

    }

    private void checkTestStatus(final int classTestId, final TextView tvStatus, final TextView passMars, final QuestionHolder holder, final ExamData testData){

            holder.tvStartTest.setText("START TEST");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.itemView.setEnabled(false);
                    Intent intent = new Intent(mCtx, ExamInstructionActivity.class);
                    intent.putExtra("examId", testData.getExamId());
                    intent.putExtra("subjectId", testData.getSubjectId());
                    intent.putExtra("testName", testData.getExamName());
                    intent.putExtra("maxMarks", testData.getMaxMark());
                    intent.putExtra("passMarks", testData.getPassMark());
                    intent.putExtra("corrMarks", testData.getCorrectAnswerMark());
                    intent.putExtra("notAttemptMarks", testData.getNotAttemptedMark());
                    intent.putExtra("wrongAnsMarks", testData.getWrongAnswerMark());
                    intent.putExtra("timeAllotted", testData.getTimeAllotted());
                    intent.putExtra("startTime",testData.getExamStartTime());
                    intent.putExtra("endTime",testData.getExamStopTime());

                    mCtx.startActivity(intent);

                }
            });

        }

        }


