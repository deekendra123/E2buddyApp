package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentAnswerReviewActivity;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.QuestionModel;
import com.aptitude.learning.e2buddy.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentQuestionsActivity.timeLeft;

public class StudentQuestionAdapter extends RecyclerView.Adapter<StudentQuestionAdapter.QuestionHolder> {


    private Context mCtx;
    private List<QuestionData> list;
    private int classTestId,userId;
    private String ans;
    private AdminDBHelper dbHelper;
    private String dateTime;
    private SimpleDateFormat sdf;
    private List<AnswerData> answerDataList;
    private int answerTimeLeft;
    public StudentQuestionAdapter(Context mCtx, List<QuestionData> list, int classTestId, int userId) {
        this.mCtx = mCtx;
        this.list = list;
        this.classTestId = classTestId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.question_layout, parent, false);
        return new QuestionHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionHolder holder, int position) {

        final QuestionData questionData = list.get(position);
        int queId = position+1;
        holder.tvQuestionId.setText("Ques. "+queId);
        holder.tvQuestion.setText(""+questionData.getQuestion());

        holder.op1.setText(""+questionData.getOption1());
        holder.op2.setText(""+questionData.getOption2());
        holder.op3.setText(""+questionData.getOption3());
        holder.op4.setText(""+questionData.getOption4());

        dbHelper = new AdminDBHelper(mCtx);

        if (holder.op1.getText().toString().isEmpty()){
            holder.op1.setVisibility(View.GONE);
        }
        if (holder.op2.getText().toString().isEmpty()){
            holder.op2.setVisibility(View.GONE);
        }
        if (holder.op3.getText().toString().isEmpty()){
            holder.op3.setVisibility(View.GONE);
        }
        if (holder.op4.getText().toString().isEmpty()){
            holder.op4.setVisibility(View.GONE);
        }

        getUserAnswer(holder,questionData);


        sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());


        holder.op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.op2.setChecked(false);
                holder.op3.setChecked(false);
                holder.op4.setChecked(false);

                dateTime = sdf.format(new Date());
                answerDataList = dbHelper.getAnswer(classTestId,userId, questionData.getQuestionid());


                if (holder.op1.isChecked()) {
                        ans = holder.op1.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "1");
                    answerTimeLeft = (int) timeLeft;

                        if (answerDataList.size() <= 0) {
                            if(dbHelper.insertAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)){
                                Log.e("msg","done");
                            } else{
                                Log.e("msg","not done");
                            }

                        }
                        else {
                            if (dbHelper.updateAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)) {
                                Log.e("msg", "dk");
                            } else {
                                Log.e("msg", "not done");

                            }
                        }
                    if(dbHelper.insertTimeLeft(classTestId, userId, answerTimeLeft)){
                        Log.e("msg","done");
                    } else{
                        Log.e("msg","not done");
                    }

                    } else {
                    ans = holder.op1.getText().toString();
                    // insertAnswer(questionData.getQuestionid(), ans, "0");

                    dbHelper.deleteAnswer(classTestId, userId, questionData.getQuestionid());
                }


                }
        });

        holder.op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op3.setChecked(false);
                holder.op4.setChecked(false);
                dateTime = sdf.format(new Date());

                answerDataList = dbHelper.getAnswer(classTestId,userId, questionData.getQuestionid());


                if (holder.op2.isChecked()) {

                        ans = holder.op2.getText().toString();
                    answerTimeLeft = (int) timeLeft;

                    //  insertAnswer(questionData.getQuestionid(), ans, "1");

                    if (answerDataList.size() <= 0) {
                        if(dbHelper.insertAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");
                        }

                    }
                    else {
                        if (dbHelper.updateAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)) {
                            Log.e("msg", "dk");
                        } else {
                            Log.e("msg", "not done");
                        }
                    }

                    if(dbHelper.insertTimeLeft(classTestId, userId, answerTimeLeft)){
                        Log.e("msg","done");
                    } else{
                        Log.e("msg","not done");
                    }


                } else {
                        ans = holder.op2.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "0");
                    dbHelper.deleteAnswer(classTestId,userId, questionData.getQuestionid());

                }
                }

        });

        holder.op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op2.setChecked(false);
                holder.op4.setChecked(false);
                dateTime = sdf.format(new Date());

                answerDataList = dbHelper.getAnswer(classTestId,userId, questionData.getQuestionid());

                    if (holder.op3.isChecked()) {
                        ans = holder.op3.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "1");
                        answerTimeLeft = (int) timeLeft;

                        if (answerDataList.size() <= 0) {
                            if(dbHelper.insertAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)){
                                Log.e("msg","done");
                            } else{
                                Log.e("msg","not done");
                            }

                        }
                        else {
                            if (dbHelper.updateAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)) {
                                Log.e("msg", "dk");
                            } else {
                                Log.e("msg", "not done");

                            }
                        }

                        if(dbHelper.insertTimeLeft(classTestId, userId, answerTimeLeft)){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");
                        }

                    } else {
                        ans = holder.op3.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "0");
                        dbHelper.deleteAnswer(classTestId,userId, questionData.getQuestionid());

                    }


            }
        });

        holder.op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op2.setChecked(false);
                holder.op3.setChecked(false);
                dateTime = sdf.format(new Date());

                answerDataList = dbHelper.getAnswer(classTestId,userId, questionData.getQuestionid());


                    if (holder.op4.isChecked()) {
                        ans = holder.op4.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "1");
                        answerTimeLeft = (int) timeLeft;


                        if (answerDataList.size() <= 0) {
                            if(dbHelper.insertAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)){
                                Log.e("msg","done");
                            } else{
                                Log.e("msg","not done");
                            }

                        }
                        else {
                            if (dbHelper.updateAnswer(classTestId, userId, questionData.getQuestionid(), ans, 1, dateTime)) {
                                Log.e("msg", "dk");
                            } else {
                                Log.e("msg", "not done");

                            }
                        }

                        if(dbHelper.insertTimeLeft(classTestId, userId, answerTimeLeft)){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");
                        }

                    } else {
                        ans = holder.op4.getText().toString();
                       // insertAnswer(questionData.getQuestionid(), ans, "0");

                        dbHelper.deleteAnswer(classTestId,userId, questionData.getQuestionid());

                    }

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion;
        CheckBox op1, op2, op3, op4;
        private RadioGroup radioGroup;
        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.myRadioGroup);

            op1 =  itemView.findViewById(R.id.option1);
            op2 =  itemView.findViewById(R.id.option2);
            op3 =  itemView.findViewById(R.id.option3);
            op4 =  itemView.findViewById(R.id.option4);

        }


    }


    private void getUserAnswer(final QuestionHolder holder, final QuestionData questionData){

        List<AnswerData> answerDataList = dbHelper.getUserExistingAnswer(classTestId, userId);

        for (int j = 0; j<answerDataList.size(); j++){

            AnswerData answerData = answerDataList.get(j);
            String answer = answerData.getUserAnswer();

            if (answer.equals(questionData.getOption1())){
                holder.op1.setChecked(true);
            }
            else  if (answer.equals(questionData.getOption2())){
                holder.op2.setChecked(true);
            }
            else  if (answer.equals(questionData.getOption3())){
                holder.op3.setChecked(true);
            }
            else  if (answer.equals(questionData.getOption4())){
                holder.op4.setChecked(true);
            }
        }

    }

}
