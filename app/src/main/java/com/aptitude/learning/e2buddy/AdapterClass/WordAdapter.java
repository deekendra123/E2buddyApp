package com.aptitude.learning.e2buddy.AdapterClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.ObjectClass.WordData;
import com.aptitude.learning.e2buddy.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {

    Context mCtx;
    List<WordData> list;
    ImageView imageView;
    String current_date;

    WordAdapter.OnItemClickListener listener;


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(WordAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public WordAdapter(Context mCtx, List<WordData> list, ImageView imageView, String current_date) {
        this.mCtx = mCtx;
        this.list = list;
        this.imageView = imageView;
        this.current_date = current_date;
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.word_list_items, parent, false);
        return new WordHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final WordHolder holder, final int position) {

        final WordData wordData = list.get(position);

        holder.tvWord.setText(""+wordData.getWord());
        holder.tvDate.setText(""+wordData.getDate());


       if (position==0){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard8);
        }
       else if (position==1){
           holder.relativelayout.setBackgroundResource(R.drawable.bgcard6);


       }else if (position==2){

            holder.relativelayout.setBackgroundResource(R.drawable.bgcard7);


        }else if (position==3){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard8);


        }else if (position==4){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard9);


        }
        else if (position==5){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard6);

        }
        else {
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard8);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WordHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvWord;
        RelativeLayout relativelayout;


        public WordHolder(@NonNull final View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvDate = itemView.findViewById(R.id.tvDate);
            relativelayout = itemView.findViewById(R.id.relativelayout);

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
}
