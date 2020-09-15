package com.aptitude.learning.e2buddy.School.Student.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.School.Admin.DataClass.ExamTypeData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.SectionSubjectdata;

import java.util.List;

public class ExamTypeAdapter
        extends ArrayAdapter<ExamTypeData> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<ExamTypeData> values;
    private int data;

    public ExamTypeAdapter(Context context, int textViewResourceId,
                           List<ExamTypeData> values, int data) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.data = data;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public ExamTypeData getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)

        if (data == 1){
            label.setText(values.get(position).getExamName());
        }
        else if (data == 2){
            label.setText(values.get(position).getStartTime());

        }
        else if (data == 3){
            label.setText(values.get(position).getEndTime());

        }

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        if (data == 1){
            label.setText(values.get(position).getExamName());
        }
        else if (data == 2){
            label.setText(values.get(position).getStartTime());

        }
        else if (data == 3){
            label.setText(values.get(position).getEndTime());
        }

        return label;
    }

}
