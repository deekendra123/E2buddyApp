package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.Util.Utils;

import java.util.List;

public class ClassSectionAdapter extends RecyclerView.Adapter<ClassSectionAdapter.MyViewHolder> {

    private Context mCtx;
    private List<SectionData> mModelList;

    public ClassSectionAdapter(Context mCtx, List<SectionData> mModelList) {
        this.mCtx = mCtx;
        this.mModelList = mModelList;
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

        holder.view.setBackgroundColor(model.isSelected() ? R.drawable.buttons2 : Color.WHITE);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int sectionId = model.getSectionId();
                model.setSelected(!model.isSelected());
                holder.textView.setBackgroundColor(model.isSelected() ? R.drawable.buttons2 : Color.WHITE);

                if (model.isSelected()){

                    AdminDBHelper mydb = new AdminDBHelper(mCtx);
                    mydb.insertSection(model.getSectionId());
                }
                else {
                    AdminDBHelper mydb = new AdminDBHelper(mCtx);
                    mydb.deleteSection(model.getSectionId());
                }
            }
        });
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



}
