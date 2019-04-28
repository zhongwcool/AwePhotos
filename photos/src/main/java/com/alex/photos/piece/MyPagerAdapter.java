package com.alex.photos.piece;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.TinyPlayFragment;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<PhotoBean> list = new ArrayList<>();
    private View view;

    public MyPagerAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterList(ArrayList<PhotoBean> photosList) {
        list = photosList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_pager, null);
        PhotoView imageView = view.findViewById(R.id.content);

        if (list.get(position).getMediaType() == 3) {
            view.findViewById(R.id.control).setVisibility(View.VISIBLE);
            view.findViewById(R.id.action_play).setOnClickListener(v -> {
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, TinyPlayFragment.newInstance(list.get(position), true), "play")
                        .addToBackStack(null)
                        .commit();
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
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        view = (View) object;
    }

    public View getPrimaryItem() {
        return view;
    }
}
