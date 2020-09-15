package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.ActivityClass.AdminAddQuestionActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;

import java.util.List;

public class ReviewQuestionAdapter extends RecyclerView.Adapter<ReviewQuestionAdapter.MyViewHolder> {


    private Context mCtx;
    private List<QuestionData> mModelList;

    public ReviewQuestionAdapter(Context mCtx, List<QuestionData> mModelList) {
        this.mCtx = mCtx;
        this.mModelList = mModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.review_question_item_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final QuestionData model = mModelList.get(position);

        int id = position+1;
        holder.tvQuestionId.setText("Question "+ id);
        holder.tvQuestion.setText(""+ model.getQuestion());
        holder.tvOption1.setText(""+model.getOption1());
        holder.tvOption2.setText(""+model.getOption2());
        holder.tvOption3.setText(""+model.getOption3());
        holder.tvOption4.setText(""+model.getOption4());
        holder.tvAnswer.setText(""+model.getAnswer());
        holder.tvDescription.setText(""+model.getDescription());

        if (holder.tvOption3.getText().toString().isEmpty()){
            holder.linOpt3.setVisibility(View.GONE);
        }
        if (holder.tvOption4.getText().toString().isEmpty()){
            holder.linOpt4.setVisibility(View.GONE);
        }
        if (holder.tvDescription.getText().toString().isEmpty()){
            holder.linDes.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionId, tvQuestion, tvOption1, tvOption2, tvOption3, tvOption4,tvAnswer,tvDescription;
        LinearLayout linDes,linAns,linOpt4,linOpt3,linOpt2,linOpt1;
        private MyViewHolder(View itemView) {
            super(itemView);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvOption1 = itemView.findViewById(R.id.tvOption1);
            tvOption2 = itemView.findViewById(R.id.tvOption2);
            tvOption3 = itemView.findViewById(R.id.tvOption3);
            tvOption4 = itemView.findViewById(R.id.tvOption4);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            linDes = itemView.findViewById(R.id.linDes);
            linAns = itemView.findViewById(R.id.linAns);
            linOpt4 = itemView.findViewById(R.id.linOpt4);
            linOpt3 = itemView.findViewById(R.id.linOpt3);
            linOpt2 = itemView.findViewById(R.id.linOpt2);
            linOpt1 = itemView.findViewById(R.id.linOpt1);

        }
    }
}
