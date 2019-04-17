package com.alex.photos;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoBean> list;
    private ImageView imageView;

    public MyPagerAdapter(Context context, ArrayList<PhotoBean> photosList) {
        this.context = context;
        this.list = photosList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ImageView imageView = (ImageView) View.inflate(context, R.layout.fragment_viewer, null);

        Uri mediaUri = Uri.parse("file://" + list.get(position).getPath());
        Glide.with(context)
                .load(mediaUri)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        imageView = (ImageView) object;
    }

    public ImageView getPrimaryItem() {
        return imageView;
    }
}
