package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ExamStatus;

import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import java.util.List;

public class ViewExamStudentAdapter extends RecyclerView.Adapter<ViewExamStudentAdapter.MyViewHolder> {

    private Context mCtx;
    private List<ExamStatus> mModelList;
    private int passMarks,classTestId;
    private AdminDBHelper dbHelper;

    public ViewExamStudentAdapter(Context mCtx, List<ExamStatus> mModelList, int passMarks, int classTestId, AdminDBHelper dbHelper) {
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
        final ExamStatus studentTestData = mModelList.get(position);
        holder.tvStudentName.setText(""+ studentTestData.getName());
        holder.tvSectionName.setText(""+ studentTestData.getSectionName());
        holder.tvMarks.setText(""+ studentTestData.getAdmissionNo());
        holder.tvRollNo.setText(""+ studentTestData.getRollNo());

    }
    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvSectionName, tvMarks, tvRollNo;
        ImageView image;
        private MyViewHolder(View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvSectionName = itemView.findViewById(R.id.tvSectionName);
            tvMarks = itemView.findViewById(R.id.tvMarks);
            image = itemView.findViewById(R.id.image);
            tvRollNo = itemView.findViewById(R.id.tvRollNo);
        }
    }

}
