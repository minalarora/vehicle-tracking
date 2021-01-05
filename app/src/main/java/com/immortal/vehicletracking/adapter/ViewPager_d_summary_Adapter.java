package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPager_d_summary_Adapter extends PagerAdapter {

    private Context context;
    private ArrayList<View> views = null;

    @Override
    public int getCount() {
        return views.size();
    }

    public ViewPager_d_summary_Adapter(Context context, ArrayList<View> views) {

        this.context = context;
        this.views = views;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private android.view.ViewGroup container;

    //    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}