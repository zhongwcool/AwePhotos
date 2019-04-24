package com.alex.photos.piece;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<PhotoBean> list;
    private View view;

    public MyPagerAdapter(Context context, ArrayList<PhotoBean> photosList) {
        this.mContext = context;
        this.list = photosList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_view, null);
        PhotoView imageView = view.findViewById(R.id.content);

        if (list.get(position).getMediaType() == 3) {
            view.findViewById(R.id.control).setVisibility(View.VISIBLE);
            view.findViewById(R.id.action_play).setOnClickListener(v -> {
                //TODO 播放视频
            });
        } else {
            view.findViewById(R.id.control).setVisibility(View.INVISIBLE);
        }

        Uri mediaUri = Uri.parse("file://" + list.get(position).getPath());
        Glide.with(mContext)
                .load(mediaUri)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        view = (View) object;
    }

    public View getPrimaryItem() {
        return view;
    }
}
