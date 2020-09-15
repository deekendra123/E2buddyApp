package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;

import java.util.List;

public class ExamSectionAdapter extends RecyclerView.Adapter<ExamSectionAdapter.MyViewHolder> {

    private Context mCtx;
    private List<SectionData> mModelList;
    private int classTestId;
    private int studentSectionId;

    public ExamSectionAdapter(Context mCtx, List<SectionData> mModelList, int classTestId) {
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
        List<ExamData> classTestDataList = dbHelper.getExam(classTestId);

        for (int j = 0; j < classTestDataList.size(); j++) {
            ExamData classTestData = classTestDataList.get(j);


            studentSectionId = classTestData.getSectionId();
            if (sectionData.getSectionId()==studentSectionId) {
                sectionData.setSelected(true);
                holder.textView.setBackgroundColor(sectionData.isSelected() ? R.drawable.buttons2 : Color.WHITE);

                AdminDBHelper mydb = new AdminDBHelper(mCtx);
                mydb.insertSection(sectionData.getSectionId());
            }

        }

    }



}
