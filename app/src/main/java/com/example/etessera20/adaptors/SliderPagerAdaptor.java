package com.example.etessera20.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.etessera20.R;
import com.example.etessera20.activities.HomeActivity;
import com.example.etessera20.activities.PlayerActivity;
import com.example.etessera20.models.Slide;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdaptor extends PagerAdapter {

    private Context mcontext;
    private List<Slide> mList;
    private Button play;

    public SliderPagerAdaptor(Context mcontext, List<Slide> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item, null);
        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);
        TextView slideTxt = slideLayout.findViewById(R.id.slide_textView);
        Picasso.get().load(mList.get(position).getImage()).into(slideImg);
        slideTxt.setText(mList.get(position).getTitle());

        container.addView(slideLayout);
        return slideLayout;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object);
    }
}
