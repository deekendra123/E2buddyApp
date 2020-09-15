package com.aptitude.learning.e2buddy.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.aptitude.learning.e2buddy.AppConfig.AppCinfig;
import com.bumptech.glide.Glide;
import com.aptitude.learning.e2buddy.R;

public class ViewDescriptionDialog extends DialogFragment {

    private Callback callback;
    private int questionId, wordCoachId;
    private String userAnswer, correctAnswer, questionImage, description;
    public static ViewDescriptionDialog newInstance() {
        return new ViewDescriptionDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_answer_layout, container, false);

        ImageView imgQuestion = view.findViewById(R.id.imgQuestion);
        Button btNext = view.findViewById(R.id.btNext);

        questionId=getArguments().getInt("questionId");
        questionImage = getArguments().getString("questionImage");
        userAnswer=getArguments().getString("userAnswer");
        correctAnswer = getArguments().getString("correctAnswer");
        description = getArguments().getString("description");
        wordCoachId = getArguments().getInt("wordCoachId");

        Glide.with(getActivity())
                .load(AppCinfig.BASE_IMAGE_URL+description)
                .into(imgQuestion);

        btNext.setOnClickListener(new View.OnClickListener() {
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
