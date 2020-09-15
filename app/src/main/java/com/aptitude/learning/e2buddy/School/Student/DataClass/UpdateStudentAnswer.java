package com.aptitude.learning.e2buddy.School.Student.DataClass;

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
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudentAnswerReviewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStudentAnswer {
    private Context mCtx;
    private int studentId,classTestId;

    public UpdateStudentAnswer(Context mCtx, int studentId, int classTestId) {
        this.mCtx = mCtx;
        this.studentId = studentId;
        this.classTestId = classTestId;
    }
    public void updateAnswer(){
        AdminDBHelper dbHelper = new AdminDBHelper(mCtx);
        List<AnswerData> answerDataList = dbHelper.getUserAnswer(classTestId, studentId);

        for (int j = 0; j<answerDataList.size(); j++){
            AnswerData answerData = answerDataList.get(j);

            String answer = answerData.getUserAnswer();
            int questionId = answerData.getQuestionId();
            String question = answerData.getQuestion();
            String correctAnswer = answerData.getCorrAnswer();

            if (answer.equals(correctAnswer)){
                if (dbHelper.updateAnswerStatus(classTestId, studentId, questionId,1)) {
                    Log.e("msg", "dk");
                } else {
                    Log.e("msg", "not done");

                }

            }
            else {

                if (dbHelper.updateAnswerStatus(classTestId, studentId, questionId,0)) {
                    Log.e("msg", "dk");
                } else {
                    Log.e("msg", "not done");

                }
            }

        }

    }

}
