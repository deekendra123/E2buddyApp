package com.aptitude.learning.e2buddy.School.Student.ActivityClass;

import android.content.Context;
import android.util.Log;

import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;

import java.util.List;

public class UpdateStudentExamAnswer {
    private Context mCtx;
    private int studentId,classTestId;

    public UpdateStudentExamAnswer(Context mCtx, int studentId, int classTestId) {
        this.mCtx = mCtx;
        this.studentId = studentId;
        this.classTestId = classTestId;
    }
    public void updateAnswer(){
        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);
        List<AnswerData> answerDataList = dbHelper.getUserExamAnswer(classTestId, studentId);

        for (int j = 0; j<answerDataList.size(); j++){
            AnswerData answerData = answerDataList.get(j);

            String answer = answerData.getUserAnswer();
            int questionId = answerData.getQuestionId();
            String question = answerData.getQuestion();
            String correctAnswer = answerData.getCorrAnswer();

            if (answer.equals(correctAnswer)){
                if (dbHelper.updateExamAnswerStatus(classTestId, studentId, questionId,1)) {
                    Log.e("msg", "dk");
                } else {
                    Log.e("msg", "not done");

                }

            }
            else {

                if (dbHelper.updateExamAnswerStatus(classTestId, studentId, questionId,0)) {
                    Log.e("msg", "dk");
                } else {
                    Log.e("msg", "not done");

                }
            }

        }

    }

}
