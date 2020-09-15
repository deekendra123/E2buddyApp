package com.aptitude.learning.e2buddy.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.ObjectClass.WordPuzzleData;
import com.aptitude.learning.e2buddy.R;

import java.util.List;

public class WordSearchPuzzleAdapter extends RecyclerView.Adapter<WordSearchPuzzleAdapter.WordHolder> {

    Context mCtx;
    List<WordPuzzleData> list;

    public WordSearchPuzzleAdapter(Context mCtx, List<WordPuzzleData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }



    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.word_list, parent, false);
        return new WordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {

        WordPuzzleData data = list.get(position);

        holder.tvWord.setText(""+data.getWord());

        if (data.getStatus()==1){
            holder.imgCheck.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WordHolder extends RecyclerView.ViewHolder {

        TextView tvWord;
        ImageView imgCheck;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            imgCheck = itemView.findViewById(R.id.imgCheck);

        }
    }
}
