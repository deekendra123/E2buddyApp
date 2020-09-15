package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.Preference.SchoolAdminSessionManager;
import com.aptitude.learning.e2buddy.Preference.SchoolPreference;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.QuestionData;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;
import com.aptitude.learning.e2buddy.School.Admin.SQLiteDatabase.AdminDBHelper;

import java.util.ArrayList;

import java.util.List;


public class AddQuestionAdapter extends RecyclerView.Adapter<AddQuestionAdapter.MyViewHolder> {


    private Context mCtx;
    private List<QuestionData> mModelList;
    private List<QuestionData> list;

    OnItemClickListener listener;
    Button btsubmit;
    private int adminId,schoolId,classTestId;
    private String adminName, adminEmail, schoolCode, adminImage, schoolName,schoolImage, className, testName,option="",questionStatus;
    private AdminData user;
    private AdminDBHelper mydb;
    private SchoolData schoolData;


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AddQuestionAdapter(Context mCtx, List<QuestionData> mModelList, Button btsubmit, int classTestId, String className, String testName, String questionStatus) {
        this.mCtx = mCtx;
        this.mModelList = mModelList;
        this.btsubmit = btsubmit;
        this.classTestId = classTestId;
        this.className = className;
        this.testName = testName;
        this.questionStatus = questionStatus;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.add_question_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final QuestionData model = mModelList.get(position);

        holder.tvQuestion.setText("Question "+ model.getQuestionid());

        holder.itemView.setClickable(false);
        user = SchoolAdminSessionManager.getInstance(mCtx).getUser();
        adminId = user.getId();
        adminName = user.getUsername();
        adminEmail = user.getEmail();
        adminImage = user.getAdminImage();

        schoolData = SchoolPreference.getInstance(mCtx).getSchoolInfo();
        schoolName = schoolData.getSchoolName();
        schoolImage = schoolData.getSchoolLogo();
        schoolId = schoolData.getSchoolId();
        mydb = new AdminDBHelper(mCtx);

        if (questionStatus.equals("0")){
            if (!model.getQuestion().equals("null")){
                holder.etQuestion.setText(""+model.getQuestion());
            }
            if (!model.getOption1().equals("null")){
                holder.etOption1.setText(""+model.getOption1());

            }
            if (!model.getOption2().equals("null")){
                holder.etOption2.setText(""+model.getOption2());
            }

            if (!model.getOption3().equals("null")){
                holder.etOption3.setText(""+model.getOption3());
            }
            if (!model.getOption4().equals("null")){
                holder.etOption4.setText(""+model.getOption4());
            }

            if (!model.getAnswer().equals("null")){
                getOption(model.getQuestionid(), holder.spinnerOptions, holder, model.getAnswer());

            }

            if (!model.getDescription().equals("null")){
                holder.etDescription.setText(""+model.getDescription());
            }

        }


        holder.etQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                if (list.size() <= 0) {
                    if(mydb.insertQuestion(
                            schoolId,
                            classTestId,
                            model.getQuestionid(),
                            s.toString(),
                            holder.etOption1.getText().toString(),
                            holder.etOption2.getText().toString(),
                            holder.etOption3.getText().toString(),
                            holder.etOption4.getText().toString(),
                            option,
                            holder.etDescription.getText().toString()
                    )

                    ){
                        Log.e("msg","done");
                    } else{
                        Log.e("msg","not done");

                    }

                }
                else {
                    if(mydb.updateQuestion(
                            schoolId,
                            classTestId,
                            model.getQuestionid(),
                            s.toString(),
                            holder.etOption1.getText().toString(),
                            holder.etOption2.getText().toString(),
                            holder.etOption3.getText().toString(),
                            holder.etOption4.getText().toString(),
                            option,
                            holder.etDescription.getText().toString()
                    )

                    ){
                        Log.e("msg","dk");
                    } else{
                        Log.e("msg","not done");

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etOption1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                if (list.size() <= 0) {
                    if(mydb.insertQuestion(
                            schoolId,
                            classTestId,
                            model.getQuestionid(),
                            holder.etQuestion.getText().toString(),
                            s.toString(),
                            holder.etOption2.getText().toString(),
                            holder.etOption3.getText().toString(),
                            holder.etOption4.getText().toString(),
                            option,
                            holder.etDescription.getText().toString()
                    )
                    ){
                        Log.e("msg","done");
                    } else{
                        Log.e("msg","not done");

                    }
                }
                else {
                    if(mydb.updateQuestion(
                            schoolId,
                            classTestId,
                            model.getQuestionid(),
                            holder.etQuestion.getText().toString(),
                            s.toString(),
                            holder.etOption2.getText().toString(),
                            holder.etOption3.getText().toString(),
                            holder.etOption4.getText().toString(),
                            option,
                            holder.etDescription.getText().toString()
                    )

                    ){
                        Log.e("msg","dk");
                    } else{
                        Log.e("msg","not done");

                    }
                }
                getOption(model.getQuestionid(), holder.spinnerOptions, holder,"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etOption2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                    if (list.size() <= 0) {
                        if(mydb.insertQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                s.toString(),
                                holder.etOption3.getText().toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");

                        }

                    }
                    else {
                        if(mydb.updateQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                s.toString(),
                                holder.etOption3.getText().toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","dk");
                        } else{
                            Log.e("msg","not done");

                        }
                    }

                    getOption(model.getQuestionid(), holder.spinnerOptions, holder,"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etOption3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                    if (list.size() <= 0) {
                        if(mydb.insertQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                s.toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");

                        }

                    }
                    else {
                        if(mydb.updateQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                s.toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","dk");
                        } else{
                            Log.e("msg","not done");

                        }
                    }

                    getOption(model.getQuestionid(), holder.spinnerOptions, holder,"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.etOption4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                    if (list.size() <= 0) {
                        if(mydb.insertQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                holder.etOption3.getText().toString(),
                                s.toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");

                        }

                    }
                    else {
                        if(mydb.updateQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                holder.etOption3.getText().toString(),
                                s.toString(),
                                option,
                                holder.etDescription.getText().toString()
                        )

                        ){
                            Log.e("msg","dk");
                        } else{
                            Log.e("msg","not done");
                        }
                    }

                    getOption(model.getQuestionid(), holder.spinnerOptions, holder,"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                list = mydb.getQuestion(schoolId, classTestId,model.getQuestionid());

                    if (list.size() <= 0) {
                        if(mydb.insertQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                holder.etOption3.getText().toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                s.toString()
                        )

                        ){
                            Log.e("msg","done");
                        } else{
                            Log.e("msg","not done");

                        }

                    }
                    else {
                        if(mydb.updateQuestion(
                                schoolId,
                                classTestId,
                                model.getQuestionid(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                holder.etOption3.getText().toString(),
                                holder.etOption4.getText().toString(),
                                option,
                                s.toString()
                        )

                        ){
                            Log.e("msg","dk");
                        } else{
                            Log.e("msg","not done");

                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion,tvDeleteQuestion;
        Spinner spinnerOptions;
        public EditText etQuestion, etOption1, etOption2, etOption3, etOption4,etDescription;

        private MyViewHolder(final View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            etQuestion = itemView.findViewById(R.id.etQuestion);
            etOption1 = itemView.findViewById(R.id.etOption1);
            etOption2 = itemView.findViewById(R.id.etOption2);
            etOption3 = itemView.findViewById(R.id.etOption3);
            etOption4 = itemView.findViewById(R.id.etOption4);
            spinnerOptions = itemView.findViewById(R.id.spinnerOptions);
            etDescription = itemView.findViewById(R.id.etDescription);
            tvDeleteQuestion = itemView.findViewById(R.id.tvDeleteQuestion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView,position);
                        }
                    }
                }
            });

        }
    }

    private void getOption(final int questionId, final Spinner spinner, final MyViewHolder holder, String answer){


        final ArrayList<QuestionData> options = mydb.getQuestion(schoolId, classTestId, questionId);
        // Utils.showToast("size"+ options.size());
        final List<String> stringList = new ArrayList<>();

        for (int i=0; i<options.size();i++){
            QuestionData questionData = options.get(i);
            Log.e("option1", questionData.getOption1());
            Log.e("option2", questionData.getOption2());
            Log.e("option3", questionData.getOption3());
            Log.e("option4", questionData.getOption4());

            stringList.add("Select Answer");
            stringList.add(questionData.getOption1());
            stringList.add(questionData.getOption2());
            stringList.add(questionData.getOption3());
            stringList.add(questionData.getOption4());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, stringList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        if (questionStatus.equals("0")){
            spinner.setSelection(getIndex(spinner, answer));

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option = parent.getItemAtPosition(position).toString();

                if(mydb.updateQuestion(
                        schoolId,
                        classTestId,
                        questionId,
                        holder.etQuestion.getText().toString(),
                        holder.etOption1.getText().toString(),
                        holder.etOption2.getText().toString(),
                        holder.etOption3.getText().toString(),
                        holder.etOption4.getText().toString(),
                        option,
                        holder.etDescription.getText().toString()
                )

                ){
                    Log.e("msg","dk");
                } else{
                    Log.e("msg","not done");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


}
