package com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ClassTestData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.ExamTypeData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.MarkData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SectionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SubjectData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.UserData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.AnswerData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.ExamData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.MarksData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.TestStatus;
import java.util.ArrayList;
public class AdminDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "e2Buddy.db";
    public static final String TABLE_QUESTION = "classQuestion";
    public static final String QUESTION_ID = "questionId";
    public static final String QUESTION = "question";
    public static final String OPTION1 = "option1";
    public static final String OPTION2 = "option2";
    public static final String OPTION3 = "option3";
    public static final String OPTION4 = "option4";
    public static final String CORRECT_ANSWER = "correctAnswer";
    public static final String DESCRIPTION = "description";
    public static final String COLUMN_SECTION_ID = "id";
    public static final String TABLE_SCHOOL_CLASS = "schoolClass";
    public static final String TABLE_SCHOOL_SECTION = "schoolSection";
    public static final String TABLE_SCHOOL_SUBJECT = "schoolSubject";
    public static final String TABLE_SECTION = "section";
    public static final String TABLE_SCHOOL_CLASS_TEST = "schoolClassTest";
    public static final String TABLE_SCHOOL_CLASS_TEST_ANSWER = "schoolClassTestAnswer";
    public static final String TABLE_SCHOOL_CLASS_TEST_MARK = "schoolClassTestMark";
    public static final String TABLE_SCHOOL_CLASS_TEST_TIME_LEFT = "schoolClassTestTimeLeft";
    public static final String TABLE_SCHOOL_ASSIGN_CLASS_SETCION = "schoolAssignClassSection";
    public static final String TABLE_SCHOOL_ADMIN = "schoolAdmin";
    public static final String TABLE_SCHOOL_EXAM = "schoolExam";
    public static final String TABLE_SCHOOL_EXAM_QUESTION = "schoolExamQuestion";
    public static final String TABLE_SCHOOL_EXAM_ANSWER = "schoolExamAnswer";
    public static final String TABLE_SCHOOL_EXAM_TIME_LEFT = "schoolExamTimeLeft";
    public static final String TABLE_SCHOOL_EXAM_MARK = "schoolExamMark";
    public static final String TABLE_SCHOOL_EXAM_TYPE = "schoolExamType";

    public static final String TABLE_STUDENT_EXAM_STATUS = "schoolExamStudentStatus";


    public AdminDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ TABLE_QUESTION +
                        "(classTestQuestionId integer primary key, schoolCode integer, classTestId integer, questionId integer, question text, option1 text , option2 text , option3 text , option4 text , correctAnswer text , description text )"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_CLASS +
                        "(classId integer, className text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_SECTION +
                        "(sectionId integer, classId integer, sectionName text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_SUBJECT +
                        "(subjectId integer, classId integer, subjectName text, schoolCode integer)"
        );

        db.execSQL(
                "create table " + TABLE_SECTION +
                        "(id integer)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_CLASS_TEST +
                        "(id integer primary key, classTestId integer, classTestName text, classId integer, sectionId integer, subjectId integer, maxMark integer, passMark integer, classTestDate text, timeAllotted integer, totalQuestion integer , correctAnswerMark integer , wrongAnswerMark integer , notAttemptedMark integer , schoolCode integer , status integer, classTestAddedBy integer, classTestVisible integer )"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_CLASS_TEST_ANSWER +
                        "(classTestAnswerId integer primary key, classTestId integer, userId integer, questionId integer, answer text, status integer , dateTime text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_CLASS_TEST_MARK +
                        "(classTestMarkid integer primary key, classTestId integer, userId integer, correctAnswer integer, wrongAnswer integer, classTestMark integer , status integer, teacherRemark text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT +
                        "(id integer primary key, classTestId integer, userId integer, timeLeft text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_ASSIGN_CLASS_SETCION +
                        "(userId integer, userName text, dateofBirth text, userInfoId integer, classId integer, sectionId integer, schoolCode integer, admissionNumber integer, token text, rollNo text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_ADMIN +
                        "(adminId integer, adminName text, adminEmail text, adminPhone text, adminImage text, dateTime text, schoolCode integer)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM +
                        "(id integer primary key, examTypeId integer, examId integer, examName text, classId integer, sectionId integer, subjectId integer, maxMark integer, passMark integer, examDate text, examStartTime text, examStopTime text , timeAllotted integer , totalQuestion integer , correctAnswerMark integer , wrongAnswerMark integer , notAttemptedMark integer, schoolCode integer, status integer , examAddedBy integer, examVisible integer , examResult integer )"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM_QUESTION +
                        "(examQuestionId integer primary key, schoolCode integer, examId integer, questionId integer, questionImage text, question text, option1 text , option2 text , option3 text , option4 text , correctAnswer text , description text )"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM_ANSWER +
                        "(examAnswerId integer primary key, examId integer, userId integer, questionId integer, answer text, status integer , date text, time text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM_TIME_LEFT +
                        "(id integer primary key, examId integer, userId integer, timeLeft text)"
        );

        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM_MARK +
                        "(classExamMarkid integer primary key, examId integer, userId integer, correctAnswer integer, wrongAnswer integer, classTestMark integer , status integer, date text, time text, teacherRemark text)"
        );


        db.execSQL(
                "create table "+ TABLE_SCHOOL_EXAM_TYPE +
                        "(examTypeId integer, examName text, startTime text, endTime text, examAddedBy integer)"
        );

        db.execSQL(
                "create table "+ TABLE_STUDENT_EXAM_STATUS +
                        "(id integer, examId integer, schoolId integer, userId integer, status integer, date text, time text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTION);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_CLASS);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_SECTION);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_SUBJECT);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SECTION);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_CLASS_TEST);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_CLASS_TEST_ANSWER);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_CLASS_TEST_MARK);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_CLASS_TEST_TIME_LEFT);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_ASSIGN_CLASS_SETCION);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_ADMIN);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM_QUESTION);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM_ANSWER);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM_TIME_LEFT);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM_MARK);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL_EXAM_TYPE);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_STUDENT_EXAM_STATUS);


        onCreate(db);


    }

    public void deleteClassTest(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_CLASS_TEST);
    }
    public void deleteClassTestAnswer(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER);
    }
    public void deleteClassTestMarks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_CLASS_TEST_MARK);
    }
    public void deleteClassTestTimeLeft(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT);
    }

    public void deleteClassTestQuestion(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_QUESTION);
    }

    public void deleteUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_ASSIGN_CLASS_SETCION);
    }
    public void deleteSubjectData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_SUBJECT);
    }

    public void deleteAdminData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SCHOOL_ADMIN);
    }
    public void deleteStudentExamStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_STUDENT_EXAM_STATUS);
    }




    public boolean checkSchoolSubject(int subjectId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SUBJECT+" where subjectId="+subjectId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public boolean checkStudentClassTest(int classTestId,int schoolId, int classId, int sectionId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where classTestId="+classTestId+" and  schoolCode="+schoolId+" and classId="+classId+" and sectionId="+sectionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public ArrayList getAllStudentTest(int schoolId, int classId, int sectionId) {

        ArrayList<ClassTestData> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where schoolCode="+schoolId+" and classId="+classId+" and sectionId="+sectionId+" and classTestVisible="+1+" " , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassTestData(
                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex("classTestName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("classTestDate")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestAddedBy")),
                            res.getInt(res.getColumnIndex("classTestVisible"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllExam(int schoolId, int classId, int sectionId) {

        ArrayList<ExamData> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+" where schoolCode="+schoolId+" and classId="+classId+" and sectionId="+sectionId+" and examVisible="+1+" " , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ExamData(
                            res.getInt(res.getColumnIndex("examTypeId")),
                            res.getInt(res.getColumnIndex("examId")),
                            res.getString(res.getColumnIndex("examName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("examDate")),
                            res.getString(res.getColumnIndex("examStartTime")),
                            res.getString(res.getColumnIndex("examStopTime")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("examAddedBy")),
                            res.getInt(res.getColumnIndex("examVisible")),
                            res.getInt(res.getColumnIndex("examResult"))

                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean updateStudentRemark (int classTestId, int userId, String remark) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_MARK+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("teacherRemark", remark);
            db.update(TABLE_SCHOOL_CLASS_TEST_MARK, contentValues,    "classTestId = ? AND  userId  = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId)});

        }
        cursor.close();
        return true;
    }

    public int getStudentCount(int examId) {
        String countQuery = "SELECT  * FROM " + TABLE_STUDENT_EXAM_STATUS +" where examId="+examId+"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean insertQuestion (int schoolCode, int classTestId, int questionId, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_QUESTION+" where classTestId="+classTestId+" and questionId="+questionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("classTestId", classTestId);
            contentValues.put("questionId", questionId);
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.insert(TABLE_QUESTION, null, contentValues);
        }
        else {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.update(TABLE_QUESTION, contentValues,    "classTestId = ? AND  questionId  = ?" , new String[] {Integer.toString(classTestId), Integer.toString(questionId)});
        }
        cursor.close();
        return true;
    }
    public boolean insertExamQuestion (int schoolCode, int examId, int questionId, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_QUESTION+" where examId="+examId+" and questionId="+questionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("examId", examId);
            contentValues.put("questionId", questionId);
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.insert(TABLE_SCHOOL_EXAM_QUESTION, null, contentValues);
        }
        else {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.update(TABLE_SCHOOL_EXAM_QUESTION, contentValues,    "examId = ? AND  questionId  = ?" , new String[] {Integer.toString(examId), Integer.toString(questionId)});
        }
        cursor.close();
        return true;
    }

    public boolean insertExamQuestion (int schoolCode, int examId, int questionId, String questionImage, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_QUESTION+" where examId="+examId+" and questionId="+questionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("examId", examId);
            contentValues.put("questionId", questionId);
            contentValues.put("questionImage",questionImage);
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.insert(TABLE_SCHOOL_EXAM_QUESTION, null, contentValues);
        }
        else {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("questionImage",questionImage);
            contentValues.put("question", question);
            contentValues.put("option1", option1);
            contentValues.put("option2", option2);
            contentValues.put("option3", option3);
            contentValues.put("option4", option4);
            contentValues.put("correctAnswer", answer);
            contentValues.put("description", description);
            db.update(TABLE_SCHOOL_EXAM_QUESTION, contentValues,    "examId = ? AND  questionId  = ?" , new String[] {Integer.toString(examId), Integer.toString(questionId)});
        }
        cursor.close();
        return true;
    }

    public boolean insertUserData (int userId, String userName, String dateofBirth, int userInfoId, int classId, int sectionId, int schoolCode, int admissionNumber, String token,String rollNo) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_ASSIGN_CLASS_SETCION+" where userId="+userId+" and  userInfoId="+userInfoId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("userId", userId);
            contentValues.put("userName", userName);
            contentValues.put("dateofBirth", dateofBirth);
            contentValues.put("userInfoId", userInfoId);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("admissionNumber", admissionNumber);
            contentValues.put("token", token);
            contentValues.put("rollNo",rollNo);
            db.insert(TABLE_SCHOOL_ASSIGN_CLASS_SETCION, null, contentValues);
        }
        cursor.close();
        return true;
    }

    public boolean insertAdminData (int adminId, String adminName, String adminEmail, String adminPhone, String adminImage, String dateTime, int schoolCode) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_ADMIN+" where adminId="+adminId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists){
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("adminId", adminId);
            contentValues.put("adminName", adminName);
            contentValues.put("adminEmail", adminEmail);
            contentValues.put("adminPhone", adminPhone);
            contentValues.put("adminImage", adminImage);
            contentValues.put("dateTime", dateTime);
            contentValues.put("schoolCode", schoolCode);
            db.insert(TABLE_SCHOOL_ADMIN, null, contentValues);
        }
        cursor.close();
        return true;
    }
    public boolean insertAnswer (int classTestId, int userId, int questionId, String answer, int status, String dateTime) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId = "+userId+" and questionId="+questionId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists){
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("userId", userId);
            contentValues.put("questionId", questionId);
            contentValues.put("answer", answer);
            contentValues.put("status", status);
            contentValues.put("dateTime", dateTime);
            db.insert(TABLE_SCHOOL_CLASS_TEST_ANSWER, null, contentValues);
        }
        cursor.close();
        return true;
    }

    public boolean insertExamAnswer (int examId, int userId, int questionId, String answer, int status, String date, String time) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_ANSWER+" where examId="+examId+" and userId = "+userId+" and questionId="+questionId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists){
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examId", examId);
            contentValues.put("userId", userId);
            contentValues.put("questionId", questionId);
            contentValues.put("answer", answer);
            contentValues.put("status", status);
            contentValues.put("date", date);
            contentValues.put("time", time);

            db.insert(TABLE_SCHOOL_EXAM_ANSWER, null, contentValues);
        }
        cursor.close();

        return true;
    }


    public boolean insertStudentMarks (int classTestId, int userId, int correctAnswer, int wrongAnswer, int classTestMark, int status, String teacherRemark) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_MARK+" where classTestId="+classTestId+" and userId = "+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("userId", userId);
            contentValues.put("correctAnswer", correctAnswer);
            contentValues.put("wrongAnswer", wrongAnswer);
            contentValues.put("classTestMark", classTestMark);
            contentValues.put("status", status);
            contentValues.put("teacherRemark", teacherRemark);
            db.insert(TABLE_SCHOOL_CLASS_TEST_MARK, null, contentValues);

        }
        else {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("correctAnswer", correctAnswer);
            contentValues.put("wrongAnswer", wrongAnswer);
            contentValues.put("classTestMark", classTestMark);
            contentValues.put("status", status);
            contentValues.put("teacherRemark", teacherRemark);
            db.update(TABLE_SCHOOL_CLASS_TEST_MARK, contentValues,    "classTestId = ? AND userId = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId)});

        }
        cursor.close();
        return true;
    }


    public boolean insertStudentExamMarks (int examId, int userId, int correctAnswer, int wrongAnswer, int classTestMark, int status,String date, String time, String teacherRemark) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_MARK+" where examId="+examId+" and userId = "+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examId", examId);
            contentValues.put("userId", userId);
            contentValues.put("correctAnswer", correctAnswer);
            contentValues.put("wrongAnswer", wrongAnswer);
            contentValues.put("classTestMark", classTestMark);
            contentValues.put("status", status);
            contentValues.put("date", date);
            contentValues.put("time", time);
            contentValues.put("teacherRemark", teacherRemark);
            db.insert(TABLE_SCHOOL_EXAM_MARK, null, contentValues);

        }
        else {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("correctAnswer", correctAnswer);
            contentValues.put("wrongAnswer", wrongAnswer);
            contentValues.put("classTestMark", classTestMark);
            contentValues.put("status", status);
            contentValues.put("date", date);
            contentValues.put("time", time);
            contentValues.put("teacherRemark", teacherRemark);
            db.update(TABLE_SCHOOL_EXAM_MARK, contentValues,    "examId = ? AND userId = ?" , new String[] {Integer.toString(examId), Integer.toString(userId)});

        }
        cursor.close();
        return true;
    }

    public boolean updateStudentMarks (int classTestId, int userId, int correctAnswer, int wrongAnswer, int classTestMark, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("correctAnswer", correctAnswer);
        contentValues.put("wrongAnswer", wrongAnswer);
        contentValues.put("classTestMark", classTestMark);
        contentValues.put("status", status);

        db.update(TABLE_SCHOOL_CLASS_TEST_MARK, contentValues,    "classTestId = ? AND userId = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId)});

        return true;
    }


    public boolean insertTestTimeLeft (int classTestId, int userId, int timeLeft) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT+" where classTestId="+classTestId+" and userId = "+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("userId", userId);
            contentValues.put("timeLeft", timeLeft);
            db.insert(TABLE_SCHOOL_CLASS_TEST_TIME_LEFT, null, contentValues);
        }
//        else {
//            db = this.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("classTestId", classTestId);
//            contentValues.put("userId", userId);
//            contentValues.put("timeLeft", timeLeft);
//            db.update(TABLE_SCHOOL_CLASS_TEST_TIME_LEFT, contentValues,    "classTestId = ? and userId = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId)});
//
//        }
        cursor.close();
        return true;
    }

    public boolean updateAnswer (int classTestId, int userId, int questionId, String answer, int status, String dateTime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("answer", answer);
        contentValues.put("status", status);
        contentValues.put("dateTime", dateTime);

        db.update(TABLE_SCHOOL_CLASS_TEST_ANSWER, contentValues,    "classTestId = ? AND userId = ? AND  questionId  = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId), Integer.toString(questionId)});

        return true;
    }

    public boolean updateExamAnswer (int examId, int userId, int questionId, String answer, int status, String date, String time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("answer", answer);
        contentValues.put("status", status);
        contentValues.put("date", date);
        contentValues.put("time", time);

        db.update(TABLE_SCHOOL_EXAM_ANSWER, contentValues,    "examId = ? AND userId = ? AND  questionId  = ?" , new String[] {Integer.toString(examId), Integer.toString(userId), Integer.toString(questionId)});

        return true;
    }

    public boolean updateAnswerStatus (int classTestId, int userId, int questionId, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);

        db.update(TABLE_SCHOOL_CLASS_TEST_ANSWER, contentValues,    "classTestId = ? AND userId = ? AND  questionId  = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId), Integer.toString(questionId)});

        return true;
    }

    public boolean updateExamAnswerStatus (int examId, int userId, int questionId, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);

        db.update(TABLE_SCHOOL_EXAM_ANSWER, contentValues,    "examId = ? AND userId = ? AND  questionId  = ?" , new String[] {Integer.toString(examId), Integer.toString(userId), Integer.toString(questionId)});

        return true;
    }




    public boolean insertSchoolClass (int classId, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("classId", classId);
        contentValues.put("className", className);
        db.insert(TABLE_SCHOOL_CLASS, null, contentValues);
        return true;
    }

    public boolean insertSchoolSection (int sectionId, int classId, String sectionName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SECTION+" where sectionId="+sectionId+" and classId = "+classId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("sectionId", sectionId);
            contentValues.put("classId", classId);
            contentValues.put("sectionName", sectionName);
            db.insert(TABLE_SCHOOL_SECTION, null, contentValues);
        }
        cursor.close();
        return true;
    }

    public boolean insertSchoolSubject (int subjectId, int classId, String subjectName, int schoolCode) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SUBJECT+" where subjectId="+subjectId+" and classId = "+classId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("subjectId", subjectId);
            contentValues.put("classId", classId);
            contentValues.put("subjectName", subjectName);
            contentValues.put("schoolCode", schoolCode);
            db.insert(TABLE_SCHOOL_SUBJECT, null, contentValues);
        }
        cursor.close();
        return true;
    }

    public boolean insertExamType (int examTypeId, String examName, String startTime, String endTime, int examAddedBy) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_TYPE+" where examTypeId="+examTypeId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examTypeId",examTypeId);
            contentValues.put("examName", examName);
            contentValues.put("startTime", startTime);
            contentValues.put("endTime", endTime);
            contentValues.put("examAddedBy", examAddedBy);

            db.insert(TABLE_SCHOOL_EXAM_TYPE, null, contentValues);

        }
        cursor.close();
        return true;
    }

    public boolean insertStudentExamStatus (int id, int examId, int schoolId, int userId, int status, String date, String time) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_STUDENT_EXAM_STATUS+" where examId="+examId+" and userId="+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",id);
            contentValues.put("examId",examId);
            contentValues.put("schoolId", schoolId);
            contentValues.put("userId", userId);
            contentValues.put("status", status);
            contentValues.put("date", date);
            contentValues.put("time", time);

            db.insert(TABLE_STUDENT_EXAM_STATUS, null, contentValues);

        }
        cursor.close();
        return true;
    }


    public boolean insertSection (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        db.insert(TABLE_SECTION, null, contentValues);
        return true;
    }

    public boolean insertTimeLeft (int classTestId, int userId, int timeLeft) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT+" where classTestId="+classTestId+" and  userId="+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists){
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("userId", userId);
            contentValues.put("timeLeft", timeLeft);

            db.insert(TABLE_SCHOOL_CLASS_TEST_TIME_LEFT, null, contentValues);
        }
        else {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("userId", userId);
            contentValues.put("timeLeft", timeLeft);
            db.update(TABLE_SCHOOL_CLASS_TEST_TIME_LEFT, contentValues,    "classTestId = ? and userId = ?" , new String[] {Integer.toString(classTestId), Integer.toString(userId)});

        }
        cursor.close();


        return true;
    }

    public boolean checkExamMark (int examId, int userId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_MARK+" where examId="+examId+" and  userId="+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public boolean insertExamTimeLeft (int examId, int userId, int timeLeft) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_TIME_LEFT+" where examId="+examId+" and  userId="+userId+"" , null );
        boolean exists = (cursor.getCount() > 0);

        if (!exists){
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examId", examId);
            contentValues.put("userId", userId);
            contentValues.put("timeLeft", timeLeft);

            db.insert(TABLE_SCHOOL_EXAM_TIME_LEFT, null, contentValues);
        }
        else {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("userId", userId);
            contentValues.put("timeLeft", timeLeft);
            db.update(TABLE_SCHOOL_EXAM_TIME_LEFT, contentValues,    "examId = ? and userId = ?" , new String[] {Integer.toString(examId), Integer.toString(userId)});

        }
        cursor.close();


        return true;
    }

    public boolean insertClassTest (int classTestId, String classTestName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String classTestDate, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int classTestAddedBy, int classTestVisible) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where classTestId="+classTestId+" and classId = "+classId+" and sectionId = "+sectionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("classTestName", classTestName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("classTestDate", classTestDate);
            contentValues.put("timeAllotted", timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("classTestAddedBy", classTestAddedBy);
            contentValues.put("classTestVisible", classTestVisible);
            db.insert(TABLE_SCHOOL_CLASS_TEST, null, contentValues);
        }

        cursor.close();
        return true;
    }

    public boolean insertClassTest2 (int classTestId, String classTestName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String classTestDate, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int classTestAddedBy, int classTestVisible) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where classTestId="+classTestId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("classTestName", classTestName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("classTestDate", classTestDate);
            contentValues.put("timeAllotted", timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("classTestAddedBy", classTestAddedBy);
            contentValues.put("classTestVisible", classTestVisible);
            db.insert(TABLE_SCHOOL_CLASS_TEST, null, contentValues);

        }
        else {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestName", classTestName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("classTestDate", classTestDate);
            contentValues.put("timeAllotted", timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("classTestAddedBy", classTestAddedBy);
            contentValues.put("classTestVisible", classTestVisible);
            db.update(TABLE_SCHOOL_CLASS_TEST, contentValues,    "classTestId = ?" , new String[] {Integer.toString(classTestId)});
        }
        cursor.close();
        return true;
    }

    public boolean insertExam (int examTypeId, int examId, String examName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String examDate, String examStartTime, String examStopTime, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int examAddedBy, int examVisible, int examResult) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+" where examId="+examId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examTypeId", examTypeId);
            contentValues.put("examId", examId);
            contentValues.put("examName",examName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("examDate", examDate);
            contentValues.put("examStartTime", examStartTime);
            contentValues.put("examStopTime",examStopTime);
            contentValues.put("timeAllotted",timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("examAddedBy", examAddedBy);
            contentValues.put("examVisible", examVisible);
            contentValues.put("examResult", examResult);

            db.insert(TABLE_SCHOOL_EXAM, null, contentValues);

        }
        else {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examName",examName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("examDate", examDate);
            contentValues.put("examStartTime", examStartTime);
            contentValues.put("examStopTime",examStopTime);
            contentValues.put("timeAllotted",timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("examAddedBy", examAddedBy);
            contentValues.put("examVisible", examVisible);
            contentValues.put("examResult", examResult);
            db.update(TABLE_SCHOOL_EXAM, contentValues,    "examId = ?" , new String[] {Integer.toString(examId)});
        }
        cursor.close();
        return true;
    }

    public boolean updateExamData (int examId,int subjectId, int maxMark, int passMark, String examDate, String examStartTime, String examStopTime, int timeAllotted, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark) {

        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("examDate", examDate);
            contentValues.put("examStartTime", examStartTime);
            contentValues.put("examStopTime",examStopTime);
            contentValues.put("timeAllotted",timeAllotted);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            db.update(TABLE_SCHOOL_EXAM, contentValues,    "examId = ?" , new String[] {Integer.toString(examId)});

        return true;
    }

    public boolean insertExam1 (int examTypeId, int examId, String examName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String examDate, String examStartTime, String examStopTime, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int examAddedBy, int examVisible, int examResult) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+" where examId="+examId+" and classId="+classId+" and sectionId="+sectionId+"" , null );
        boolean exists = (cursor.getCount() > 0);
        if (!exists) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("examTypeId", examTypeId);
            contentValues.put("examId", examId);
            contentValues.put("examName",examName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("examDate", examDate);
            contentValues.put("examStartTime", examStartTime);
            contentValues.put("examStopTime",examStopTime);
            contentValues.put("timeAllotted",timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("examAddedBy", examAddedBy);
            contentValues.put("examVisible", examVisible);
            contentValues.put("examResult", examResult);

            db.insert(TABLE_SCHOOL_EXAM, null, contentValues);

        }
        cursor.close();
        return true;
    }

    public boolean insertClassTest1 (int classTestId, String classTestName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String classTestDate, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int classTestAddedBy, int classTestVisible) {

        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("classTestId", classTestId);
            contentValues.put("classTestName", classTestName);
            contentValues.put("classId", classId);
            contentValues.put("sectionId", sectionId);
            contentValues.put("subjectId", subjectId);
            contentValues.put("maxMark", maxMark);
            contentValues.put("passMark", passMark);
            contentValues.put("classTestDate", classTestDate);
            contentValues.put("timeAllotted", timeAllotted);
            contentValues.put("totalQuestion", totalQuestion);
            contentValues.put("correctAnswerMark", correctAnswerMark);
            contentValues.put("wrongAnswerMark", wrongAnswerMark);
            contentValues.put("notAttemptedMark", notAttemptedMark);
            contentValues.put("schoolCode", schoolCode);
            contentValues.put("status", status);
            contentValues.put("classTestAddedBy", classTestAddedBy);
            contentValues.put("classTestVisible", classTestVisible);
            db.insert(TABLE_SCHOOL_CLASS_TEST, null, contentValues);
        return true;
    }



    public boolean updateClassTest (int classTestId, String classTestName, int subjectId, int maxMark, int passMark, String classTestDate, int timeAllotted, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("classTestName", classTestName);
        contentValues.put("subjectId", subjectId);
        contentValues.put("maxMark", maxMark);
        contentValues.put("passMark", passMark);
        contentValues.put("classTestDate", classTestDate);
        contentValues.put("timeAllotted", timeAllotted);
        contentValues.put("correctAnswerMark", correctAnswerMark);
        contentValues.put("wrongAnswerMark", wrongAnswerMark);
        contentValues.put("notAttemptedMark",notAttemptedMark);
        db.update(TABLE_SCHOOL_CLASS_TEST, contentValues,    "classTestId = ?" , new String[] {Integer.toString(classTestId)});

        return true;
    }

    public boolean updateClassTestVisibility (int classTestId, int classTestVisible) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("classTestVisible", classTestVisible);
        db.update(TABLE_SCHOOL_CLASS_TEST, contentValues,    "classTestId = ?" , new String[] {Integer.toString(classTestId)});
        return true;
    }



    public Integer deleteSection (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SECTION,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteClassTest (Integer classTestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL_CLASS_TEST,
                "classTestId = ? ",
                new String[] { Integer.toString(classTestId) });
    }

    public Integer deleteExam (int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL_EXAM,
                "examId = ? ",
                new String[] { Integer.toString(examId) });
    }

    public void deleteAllSection(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SECTION);
    }

    public ArrayList<String> getAllSection() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_SECTION, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_SECTION_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    public String getAdminDetails(int adminId) {
        String adminName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_ADMIN+" where adminId="+adminId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            adminName = res.getString(res.getColumnIndex("adminName"));
            res.moveToNext();
        }
        return adminName;
    }

    public ArrayList getQuestion(int schoolCode, int classTestId, int questionId) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_QUESTION+" where schoolCode="+schoolCode+" and classTestId="+classTestId+" and questionId="+questionId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                    res.getInt(res.getColumnIndex(QUESTION_ID)),
                    res.getString(res.getColumnIndex(QUESTION)),
                    res.getString(res.getColumnIndex(OPTION1)),
                    res.getString(res.getColumnIndex(OPTION2)),
                    res.getString(res.getColumnIndex(OPTION3)),
                    res.getString(res.getColumnIndex(OPTION4)),
                    res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                    res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamQuestion(int schoolCode, int examId, int questionId) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_QUESTION+" where schoolCode="+schoolCode+" and examId="+examId+" and questionId="+questionId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getTestTotalQuestion(int classTestId) {

        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where classTestId="+classTestId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("totalQuestion")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamTotalQuestion(int examId) {

        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+" where examId="+examId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("totalQuestion")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getTestTimeLeft(int userId, int classTestId) {

        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT+" where userId="+userId+" and classTestId="+classTestId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("timeLeft"))
            );
            res.moveToNext();
        }
        return array_list;
    }

    public String getStudentTimeLeft(int userId, int classTestId) {


        String timeLeft = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_TIME_LEFT+" where userId="+userId+" and classTestId="+classTestId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            timeLeft = res.getString(res.getColumnIndex("timeLeft"));
            res.moveToNext();
        }
        return timeLeft;
    }

    public String getStudentExamTimeLeft(int userId, int examId) {


        String timeLeft = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_TIME_LEFT+" where userId="+userId+" and examId="+examId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            timeLeft = res.getString(res.getColumnIndex("timeLeft"));
            res.moveToNext();
        }
        return timeLeft;
    }


    public ArrayList getAnswer(int classTestId, int userId, int questionId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId="+userId+" and questionId="+questionId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("answer"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamAnswer(int examId, int userId, int questionId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_ANSWER+" where examId="+examId+" and userId="+userId+" and questionId="+questionId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("answer"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getUserExistingAnswer(int classTestId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("answer"))

                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getStudentTestStatus(int classTestId, int userId) {

        ArrayList<TestStatus> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_MARK+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new TestStatus(

                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestMark")),
                            res.getString(res.getColumnIndex("teacherRemark"))

                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public String getSubjectName(int subjectId) {

       String subjectName = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SUBJECT+" where subjectId="+subjectId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

                subjectName = res.getString(res.getColumnIndex("subjectName"));

                res.moveToNext();
            }

        return subjectName;
    }


    public ArrayList getAllQuestion(int schoolCode, int classTestId) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_QUESTION+" where schoolCode="+schoolCode+" and classTestId="+classTestId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllExamQuestion(int schoolCode, int examId) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_QUESTION+" where schoolCode="+schoolCode+" and examId="+examId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllRandomQuestion(int schoolCode, int classTestId, int totalQuestion) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_QUESTION+" where schoolCode="+schoolCode+" and classTestId="+classTestId+" ORDER BY RANDOM() LIMIT "+ totalQuestion , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamRandomQuestion(int schoolCode, int examId, int totalQuestion) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_QUESTION+" where schoolCode="+schoolCode+" and examId="+examId+" ORDER BY RANDOM() LIMIT "+ totalQuestion , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(
                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public MarksData getStudentMarks(int classTestId, int userId) {

       MarksData marksData = new MarksData();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_MARK+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            marksData.setClassTestId(res.getInt(res.getColumnIndex("classTestId")));
            marksData.setUserId(res.getInt(res.getColumnIndex("userId")));
            marksData.setCorrectAnswer(res.getInt(res.getColumnIndex("correctAnswer")));
            marksData.setWrongAnswer(res.getInt(res.getColumnIndex("wrongAnswer")));
            marksData.setClassTestMark(res.getInt(res.getColumnIndex("classTestMark")));
            marksData.setStatus(res.getInt(res.getColumnIndex("status")));
            marksData.setTeacherRemark(res.getInt(res.getColumnIndex("teacherRemark")));
            res.moveToNext();
        }
        return marksData;
    }

    public MarksData getStudentExamMarks(int examId, int userId) {

        MarksData marksData = new MarksData();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_MARK+" where examId="+examId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            marksData.setClassTestId(res.getInt(res.getColumnIndex("examId")));
            marksData.setUserId(res.getInt(res.getColumnIndex("userId")));
            marksData.setCorrectAnswer(res.getInt(res.getColumnIndex("correctAnswer")));
            marksData.setWrongAnswer(res.getInt(res.getColumnIndex("wrongAnswer")));
            marksData.setClassTestMark(res.getInt(res.getColumnIndex("classTestMark")));
            marksData.setStatus(res.getInt(res.getColumnIndex("status")));
            marksData.setDate(res.getString(res.getColumnIndex("date")));
            marksData.setTime(res.getString(res.getColumnIndex("time")));
            marksData.setTeacherRemark(res.getInt(res.getColumnIndex("teacherRemark")));

            res.moveToNext();
        }
        return marksData;
    }

    public ArrayList getAllAdminQuestion(int schoolCode) {

        ArrayList<QuestionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_QUESTION+" where schoolCode="+schoolCode+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new QuestionData(

                            res.getInt(res.getColumnIndex(QUESTION_ID)),
                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex(QUESTION)),
                            res.getString(res.getColumnIndex(OPTION1)),
                            res.getString(res.getColumnIndex(OPTION2)),
                            res.getString(res.getColumnIndex(OPTION3)),
                            res.getString(res.getColumnIndex(OPTION4)),
                            res.getString(res.getColumnIndex(CORRECT_ANSWER)),
                            res.getString(res.getColumnIndex(DESCRIPTION))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public MarkData getStudentResult(int classTestId, int userId) {

        MarkData markData = new MarkData();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_MARK+" where userId="+userId+" and classTestId="+classTestId+"" , null );

        boolean exists = (res.getCount() > 0);

        if (exists) {

            res.moveToFirst();

            while (res.isAfterLast() == false) {
                markData.setClassTestMark(res.getInt(res.getColumnIndex("classTestMark")));
                markData.setTeacherRemark(res.getString(res.getColumnIndex("teacherRemark")));
                res.moveToNext();
            }
        }
        else {
            markData.setClassTestMark(-100);
            markData.setTeacherRemark("0");
        }
        res.close();
        return markData;
    }

    public MarkData getStudentExamResult(int examId, int userId) {

        MarkData markData = new MarkData();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_MARK+" where userId="+userId+" and examId="+examId+"" , null );

        boolean exists = (res.getCount() > 0);

        if (exists) {

            res.moveToFirst();

            while (res.isAfterLast() == false) {
                markData.setClassTestMark(res.getInt(res.getColumnIndex("classTestMark")));
                markData.setTeacherRemark(res.getString(res.getColumnIndex("teacherRemark")));
                res.moveToNext();
            }
        }
        else {
            markData.setClassTestMark(-100);
            markData.setTeacherRemark("0");
        }
        res.close();
        return markData;
    }

    public String getStudentExamStatus(int examId, int userId) {

        String status="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_STUDENT_EXAM_STATUS+" where userId="+userId+" and examId="+examId+"" , null );

        boolean exists = (res.getCount() > 0);

        if (exists) {

            res.moveToFirst();

                status = res.getString(res.getColumnIndex("status"));

        }
        else {
            status = "A";

        }
        res.close();
        return status;
    }


    public String getUserAllAnswer(int classTestId, int userId, int questionId) {

       // AnswerData answerData = new AnswerData();
        String userAnswer = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId="+userId+" and questionId="+questionId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
//            answerData.setQuestionId(res.getInt(res.getColumnIndex("questionId")));
//            answerData.setUserAnswer(res.getString(res.getColumnIndex("answer")));

            userAnswer = res.getString(res.getColumnIndex("answer"));
            res.moveToNext();
        }
        return userAnswer;
    }



    public int getTotalQuestion(int classTestId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(
                "SELECT COUNT (*) FROM " + TABLE_QUESTION + " WHERE classTestId=?",
                new String[] { String.valueOf(classTestId) }
        );
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();
        return count;
    }


    public Integer deleteAnswer (int classTestId, int userId, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL_CLASS_TEST_ANSWER,
                "classTestId = ? AND userId = ? AND  questionId  = ?",
                new String[] {Integer.toString(classTestId), Integer.toString(userId), Integer.toString(questionId)});


    }

    public Integer deleteExamAnswer (int examId, int userId, int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL_EXAM_ANSWER,
                "examId = ? AND userId = ? AND  questionId  = ?",
                new String[] {Integer.toString(examId), Integer.toString(userId), Integer.toString(questionId)});


    }



    public ArrayList getUserAnswer(int classTestId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String MY_QUERY = "SELECT * FROM schoolClassTestAnswer a INNER JOIN classQuestion b ON a.classTestId=b.classTestId and a.questionId=b.questionId WHERE a.classTestId=? and a.userId=? ORDER BY a.questionId ASC";

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(classTestId), String.valueOf(userId)});

            res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("question")),
                            res.getString(res.getColumnIndex("correctAnswer")),
                            res.getString(res.getColumnIndex("answer")),
                            res.getString(res.getColumnIndex("dateTime"))
                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamUserAnswer(int examId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String MY_QUERY = "SELECT * FROM schoolExamAnswer a INNER JOIN schoolExamQuestion b ON a.examId=b.examId and a.questionId=b.questionId WHERE a.examId=? and a.userId=? ORDER BY a.questionId ASC";

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(examId), String.valueOf(userId)});

        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("question")),
                            res.getString(res.getColumnIndex("correctAnswer")),
                            res.getString(res.getColumnIndex("answer")),
                            res.getString(res.getColumnIndex("date"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getUserExamAnswer(int examId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String MY_QUERY = "SELECT * FROM schoolExamAnswer a INNER JOIN schoolExamQuestion b ON a.examId=b.examId and a.questionId=b.questionId WHERE a.examId=? and a.userId=? ORDER BY a.questionId ASC";

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(examId), String.valueOf(userId)});

        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getString(res.getColumnIndex("question")),
                            res.getString(res.getColumnIndex("correctAnswer")),
                            res.getString(res.getColumnIndex("answer")),
                            res.getString(res.getColumnIndex("date"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getUserInfo(int schoolCode, int classId, int sectionId) {

        ArrayList<UserData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String MY_QUERY = "SELECT * FROM schoolAssignClassSection a INNER JOIN schoolSection b ON a.sectionId = b.sectionId WHERE a.schoolCode=? and a.classId=? and a.sectionId=?";

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(schoolCode), String.valueOf(classId), String.valueOf(sectionId)});

        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new UserData(
                            res.getInt(res.getColumnIndex("userId")),
                            res.getString(res.getColumnIndex("userName")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("sectionName")),
                            res.getString(res.getColumnIndex("admissionNumber")),
                            res.getString(res.getColumnIndex("rollNo"))

                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getSection(int classTestId) {

        ArrayList<SectionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String MY_QUERY = "SELECT * FROM schoolSection a INNER JOIN schoolClassTest b ON a.sectionId = b.sectionId WHERE b.classTestId=?";

        Cursor res = db.rawQuery(MY_QUERY, new String[]{String.valueOf(classTestId)});

        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new SectionData(
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("sectionName"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }




    public ArrayList getClassTest(int classTestId) {

        ArrayList<ClassTestData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where classTestId="+classTestId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassTestData(

                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex("classTestName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("classTestDate")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestAddedBy")),
                            res.getInt(res.getColumnIndex("classTestVisible"))

                            ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getExam(int examId) {

        ArrayList<ExamData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+" where examId="+examId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ExamData(
                            res.getInt(res.getColumnIndex("examTypeId")),
                            res.getInt(res.getColumnIndex("examId")),
                            res.getString(res.getColumnIndex("examName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("examDate")),
                            res.getString(res.getColumnIndex("examStartTime")),
                            res.getString(res.getColumnIndex("examStopTime")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("examAddedBy")),
                            res.getInt(res.getColumnIndex("examVisible")),
                            res.getInt(res.getColumnIndex("examResult"))



                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllExam() {

        ArrayList<ExamData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM+ " GROUP BY examId ORDER BY examId DESC " , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ExamData(
                            res.getInt(res.getColumnIndex("examTypeId")),
                            res.getInt(res.getColumnIndex("examId")),
                            res.getString(res.getColumnIndex("examName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("examDate")),
                            res.getString(res.getColumnIndex("examStartTime")),
                            res.getString(res.getColumnIndex("examStopTime")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("examAddedBy")),
                            res.getInt(res.getColumnIndex("examVisible")),
                            res.getInt(res.getColumnIndex("examResult"))


                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getUserAllAnswer(int classTestId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getString(res.getColumnIndex("answer")),
                            res.getString(res.getColumnIndex("dateTime"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getUserAllExamAnswer(int examId, int userId) {

        ArrayList<AnswerData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_ANSWER+" where examId="+examId+" and userId="+userId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new AnswerData(
                            res.getInt(res.getColumnIndex("questionId")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getString(res.getColumnIndex("answer")),
                            res.getString(res.getColumnIndex("date")),
                            res.getString(res.getColumnIndex("time"))
                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public int getTotalAnswerCount(int classTestId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST_ANSWER+" where classTestId="+classTestId+" and userId="+userId+"" , null );
        int count = res.getCount();
        res.close();
        return count;
    }
    public ArrayList getAllClassTest(int schoolId, int adminId) {

        ArrayList<ClassTestData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where schoolCode="+schoolId+" and classTestAddedBy="+adminId+" GROUP BY classTestId" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassTestData(
                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex("classTestName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("classTestDate")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestAddedBy")),
                            res.getInt(res.getColumnIndex("classTestVisible"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllAdminClassTest(int schoolId, int adminId) {

        ArrayList<ClassTestData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where schoolCode="+schoolId+" and classTestAddedBy="+adminId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassTestData(
                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex("classTestName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("classTestDate")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestAddedBy")),
                            res.getInt(res.getColumnIndex("classTestVisible"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getAllClassTest(int schoolId, int classId, int adminId) {

        ArrayList<ClassTestData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS_TEST+" where schoolCode="+schoolId+" and classId="+classId+" and classTestAddedBy="+adminId+" GROUP BY classTestId ORDER BY classTestId DESC" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassTestData(
                            res.getInt(res.getColumnIndex("classTestId")),
                            res.getString(res.getColumnIndex("classTestName")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("maxMark")),
                            res.getInt(res.getColumnIndex("passMark")),
                            res.getString(res.getColumnIndex("classTestDate")),
                            res.getInt(res.getColumnIndex("timeAllotted")),
                            res.getInt(res.getColumnIndex("totalQuestion")),
                            res.getInt(res.getColumnIndex("correctAnswerMark")),
                            res.getInt(res.getColumnIndex("wrongAnswerMark")),
                            res.getInt(res.getColumnIndex("notAttemptedMark")),
                            res.getInt(res.getColumnIndex("schoolCode")),
                            res.getInt(res.getColumnIndex("status")),
                            res.getInt(res.getColumnIndex("classTestAddedBy")),
                            res.getInt(res.getColumnIndex("classTestVisible"))


                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllSchoolClass() {

        ArrayList<ClassData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassData(
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("className"))
                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getSchoolSubject(int schoolCodeId, int classId) {

        ArrayList<SubjectData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SUBJECT+" where schoolCode="+schoolCodeId+" and classId="+classId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new SubjectData(
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("subjectName"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getExamType() {

        ArrayList<ExamTypeData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_EXAM_TYPE , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ExamTypeData(
                            res.getInt(res.getColumnIndex("examTypeId")),
                            res.getString(res.getColumnIndex("examName")),
                            res.getString(res.getColumnIndex("startTime")),
                            res.getString(res.getColumnIndex("endTime")),
                            res.getInt(res.getColumnIndex("examAddedBy"))
                            ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getSubject(int subjectId) {

        ArrayList<SubjectData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SUBJECT+" where subjectId="+subjectId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new SubjectData(
                            res.getInt(res.getColumnIndex("subjectId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("subjectName"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getSchoolClass(int classId) {

        ArrayList<ClassData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_CLASS+" where classId="+classId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new ClassData(

                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("className"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getSchoolSection(int classId) {

        ArrayList<SectionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_SCHOOL_SECTION+" where classId="+classId+"" , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    new SectionData(
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("sectionName"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getClassTestSection(int classTestId) {

        ArrayList<SectionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select s.sectionId, s.classId, s.sectionName from schoolSection s, schoolClassTest t  where s.sectionId = t.sectionId and classTestId="+classTestId+"" , null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(
                    new SectionData(
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("sectionName"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getExamSection(int examId) {

        ArrayList<SectionData> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select s.sectionId, s.classId, s.sectionName from schoolSection s, schoolExam t  where s.sectionId = t.sectionId and examId="+examId+"" , null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(
                    new SectionData(
                            res.getInt(res.getColumnIndex("sectionId")),
                            res.getInt(res.getColumnIndex("classId")),
                            res.getString(res.getColumnIndex("sectionName"))

                    ));
            res.moveToNext();
        }
        return array_list;
    }


    public boolean updateQuestion (int schoolCode, int classTestId, int questionId, String question, String option1, String option2, String option3, String option4, String correctAnswer, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("option1", option1);
        contentValues.put("option2", option2);
        contentValues.put("option3", option3);
        contentValues.put("option4", option4);
        contentValues.put("correctAnswer", correctAnswer);
        contentValues.put("description", description);

        db.update(TABLE_QUESTION, contentValues,    "schoolCode = ? AND classTestId = ? AND  questionId  = ?" , new String[] {Integer.toString(schoolCode), Integer.toString(classTestId), Integer.toString(questionId)});

        return true;
    }

    public boolean updateExamQuestion (int schoolCode, int examId, int questionId, String question, String option1, String option2, String option3, String option4, String correctAnswer, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("option1", option1);
        contentValues.put("option2", option2);
        contentValues.put("option3", option3);
        contentValues.put("option4", option4);
        contentValues.put("correctAnswer", correctAnswer);
        contentValues.put("description", description);

        db.update(TABLE_SCHOOL_EXAM_QUESTION, contentValues,    "schoolCode = ? AND examId = ? AND  questionId  = ?" , new String[] {Integer.toString(schoolCode), Integer.toString(examId), Integer.toString(questionId)});

        return true;
    }

    public boolean updateClassTest (int classTestId, int totalQuestion, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("totalQuestion", totalQuestion);
        contentValues.put("status", status);

        db.update(TABLE_SCHOOL_CLASS_TEST, contentValues,    "classTestId = ?" , new String[] {Integer.toString(classTestId)});

        return true;
    }

    public boolean updateExam (int examId, int totalQuestion, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("totalQuestion", totalQuestion);
        contentValues.put("status", status);

        db.update(TABLE_SCHOOL_EXAM, contentValues,    "examId = ?" , new String[] {Integer.toString(examId)});

        return true;
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_QUESTION);
    }

    public Integer deleteQuestion (int schoolCode, int classTestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_QUESTION,
                "schoolCode = ? AND classTestId = ?",
                new String[] { Integer.toString(schoolCode), Integer.toString(classTestId)});

    }

    public Integer deleteExamQuestion (int schoolCode, int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL_EXAM_QUESTION,
                "schoolCode = ? AND examId = ?",
                new String[] { Integer.toString(schoolCode), Integer.toString(examId)});

    }

}
