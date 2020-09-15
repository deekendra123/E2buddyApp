package com.aptitude.learning.e2buddy.School.Student.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass.ViewWebLinkActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewNoticeDialog extends DialogFragment {

    private Callback callback;
    private String description,title,link,addedAt;
    private TextView tvTitle,tvDescription,tvDate;
    private FloatingActionButton webLink;
    public static ViewNoticeDialog newInstance() {
        return new ViewNoticeDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_notice_dialog, container, false);

        ImageButton imgCloase = view.findViewById(R.id.fullscreen_dialog_close);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDate = view.findViewById(R.id.tvDate);
        webLink = view.findViewById(R.id.webLink);

        title=getArguments().getString("title");
        description = getArguments().getString("description");
        link = getArguments().getString("link");
        addedAt = getArguments().getString("date");

        String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(addedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd-MMM-yyyy");
        date = spf.format(newDate);
        tvDate.setText(""+date);

        if (link.equals("")){
            webLink.setVisibility(View.GONE);
        }

        webLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewWebLinkActivity.class);
                intent.putExtra("link",link);
                startActivity(intent);
            }
        });

        tvTitle.setText(title);
        tvDescription.setText(description);

        imgCloase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });



        return view;
    }

    public interface Callback {

        void onActionClick(String name);

    }



}
