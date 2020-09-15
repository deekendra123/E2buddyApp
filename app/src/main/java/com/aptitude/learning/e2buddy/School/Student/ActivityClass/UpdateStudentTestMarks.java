package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStudentTestMarks {
    private Context mCtx;
    private int studentId,classTestId;
    private int correctAnswerCount;
    private int wrongAnswerCount;
    private int attemptedQuestion;
    private int notAttempted;
    private int totalQuestion;

    private int maxMarks;
    private int passMarks;
    private int corrMarks;
    private int notAttemptMarks;
    private int wrongAnsMarks;

    private AdminDBHelper dbHelper;

    public UpdateStudentTestMarks(Context mCtx, int studentId, int classTestId, int totalQuestion, int maxMarks, int passMarks, int corrMarks, int notAttemptMarks, int wrongAnsMarks, AdminDBHelper dbHelper) {
        this.mCtx = mCtx;
        this.studentId = studentId;
        this.classTestId = classTestId;
        this.totalQuestion = totalQuestion;

        this.maxMarks = maxMarks;
        this.passMarks = passMarks;
        this.corrMarks = corrMarks;
        this.notAttemptMarks = notAttemptMarks;
        this.wrongAnsMarks = wrongAnsMarks;
        this.dbHelper = dbHelper;

    }
    public void updateStudentMarks(){
        dbHelper = new AdminDBHelper(mCtx);
        List<AnswerData> answerDataList = dbHelper.getUserAnswer(classTestId, studentId);

        for (int j = 0; j<answerDataList.size(); j++){
            AnswerData answerData = answerDataList.get(j);

            String answer = answerData.getUserAnswer();
            int questionId = answerData.getQuestionId();
            String question = answerData.getQuestion();
            String correctAnswer = answerData.getCorrAnswer();

            if (answer.equals(correctAnswer)){
                correctAnswerCount = correctAnswerCount+1;
            }
            else {
                wrongAnswerCount = wrongAnswerCount +1;
            }
        }

        attemptedQuestion = answerDataList.size();
        notAttempted = totalQuestion - attemptedQuestion;

        int classTestMark = (correctAnswerCount*corrMarks) - (wrongAnswerCount*wrongAnsMarks) - (notAttempted*notAttemptMarks);

        if (classTestMark>=passMarks){
            insertMarks(classTestMark, 1);
        }
        else {
            insertMarks(classTestMark, 0);
        }

        }

    private void insertMarks(final int classTestMark, final int status){
        if (dbHelper.insertStudentMarks(classTestId, studentId, correctAnswerCount, wrongAnswerCount, classTestMark, status, "0")) {
            Log.e("msg", "done");
        } else {
            Log.e("msg", "not done");
        }

    }


}
