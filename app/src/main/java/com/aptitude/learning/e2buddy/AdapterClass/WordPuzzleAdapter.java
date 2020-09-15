package com.aptitude.learning.e2buddy.AdapterClass;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.ObjectClass.WordData;
import com.aptitude.learning.e2buddy.ObjectClass.WordPuzzleData;
import com.aptitude.learning.e2buddy.R;

import org.w3c.dom.Text;

import java.util.List;

public class WordPuzzleAdapter extends BaseAdapter {

    Context mCtx;
    List<WordPuzzleData> list;

    public WordPuzzleAdapter(Context mCtx, List<WordPuzzleData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)mCtx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.puzzle_list_items,null,true);
        }
        WordPuzzleData listdata= list.get(position);

        TextView tvLetter = convertView.findViewById(R.id.tvLetter);


        tvLetter.setText(listdata.getLetter());

        return convertView;
    }
}


