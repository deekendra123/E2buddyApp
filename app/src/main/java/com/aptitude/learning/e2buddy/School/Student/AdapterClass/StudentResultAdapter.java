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

import com.aptitude.learning.e2buddy.R;

import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;

import java.util.List;


public class StudentResultAdapter extends RecyclerView.Adapter<StudentResultAdapter.QuestionHolder> {

    private Context mCtx;
    private List<AnswerData> list;
    private TextView tvCorrectAnswer,tvAttemptedQuestion,tvWrongAns;
    private int totalQuestion;
    private int studentId, classTestId, maxMarks, passMarks, corrMarks, notAttemptMarks, wrongAnsMarks;


    public StudentResultAdapter(Context mCtx, List<AnswerData> list, TextView tvCorrectAnswer, TextView tvAttemptedQuestion, TextView tvWrongAns, int studentId, int classTestId, int maxMarks, int passMarks, int corrMarks, int notAttemptMarks, int wrongAnsMarks, int totalQuestion) {
        this.mCtx = mCtx;
        this.list = list;
        this.tvCorrectAnswer = tvCorrectAnswer;
        this.tvAttemptedQuestion = tvAttemptedQuestion;
        this.tvWrongAns = tvWrongAns;
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

        View view = LayoutInflater.from(mCtx).inflate(R.layout.studnet_result_layout, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {

        AnswerData answerData = list.get(position);
        holder.tvQuestionId.setText("Ques. "+answerData.getQuestionId());
        holder.tvQuestion.setText(""+answerData.getQuestion());
        holder.btUserAmswer.setText(""+answerData.getUserAnswer());
        holder.btCorrectAnswer.setText(""+answerData.getCorrAnswer());

        if (answerData.getUserAnswer().equals(answerData.getCorrAnswer())){
            holder.btUserAmswer.setBackgroundResource(R.drawable.buttons2);
            holder.btCorrectAnswer.setVisibility(View.INVISIBLE);

        }
        else {
            holder.btUserAmswer.setBackgroundResource(R.drawable.buttons4);
            holder.btCorrectAnswer.setBackgroundResource(R.drawable.buttons2);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion;
        Button btUserAmswer,btCorrectAnswer;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            btUserAmswer = itemView.findViewById(R.id.btUserAmswer);
            btCorrectAnswer = itemView.findViewById(R.id.btCorrectAnswer);

        }
    }


}
