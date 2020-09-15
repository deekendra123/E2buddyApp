package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.Util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAnswerReviewAdapter extends RecyclerView.Adapter<StudentAnswerReviewAdapter.QuestionHolder> {

    private Context mCtx;
    private List<AnswerData> list;

    private int correctAnswerCount=0;
    private int wrongAnswerCount=0;
    private int attemptedQuestion=0;
    private int notAttempted = 0;
    private int totalQuestion;
    private int studentId, classTestId, maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks;


    public StudentAnswerReviewAdapter(Context mCtx, List<AnswerData> list, int studentId, int classTestId, int maxMarks, int passMarks, int corrMarks, int notAttemptMarks, int wrongAnsMarks, int totalQuestion) {
        this.mCtx = mCtx;
        this.list = list;
        this.studentId = studentId;
        this.classTestId = classTestId;
        this.maxMarks = maxMarks;
        this.passMarks = passMarks;
        this.corrMarks = corrMarks;
        this.notAttemptMarks = notAttemptMarks;
        this.wrongAnsMarks = wrongAnsMarks;
        this.totalQuestion = totalQuestion;
    }



    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mCtx).inflate(R.layout.student_answer_layout, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {

        AnswerData answerData = list.get(position);
        holder.tvQuestionId.setText("Ques. "+answerData.getQuestionId());
        holder.tvQuestion.setText(""+answerData.getQuestion());
        holder.tvAnswer.setText(""+answerData.getUserAnswer());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion,tvAnswer;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);

        }
    }


}
