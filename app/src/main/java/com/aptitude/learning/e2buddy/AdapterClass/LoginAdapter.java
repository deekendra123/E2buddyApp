package com.aptitude.learning.e2buddy.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.aptitude.learning.e2buddy.R;

/**
 * Created by Matrix on 04-01-2019.
 */

public class LoginAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public LoginAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {

            R.drawable.img1png,
            R.drawable.img2png,
            R.drawable.img3png,

    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_slider_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imageView2);

       imageView.setImageResource(slide_images[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);
    }
}
