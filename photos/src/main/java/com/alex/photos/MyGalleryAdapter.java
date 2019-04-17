package com.alex.photos;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoBean}
 */
public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.ViewHolder> {

    private ArrayList<PhotoBean> mValues = new ArrayList<>();
    private Context mContext;

    public MyGalleryAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterList(ArrayList<PhotoBean> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem.getMediaType() == 3) {
            holder.mRlGifInfo.setVisibility(View.INVISIBLE);
            holder.mRlVideoInfo.setVisibility(View.VISIBLE);
            holder.mTvVideoTime.setText(holder.mItem.getDuration());
        } else {
            holder.mRlVideoInfo.setVisibility(View.INVISIBLE);
            holder.mRlGifInfo.setVisibility(".gif".equalsIgnoreCase(holder.mItem.getExtension()) ? View.VISIBLE : View.INVISIBLE);
        }

        Uri mediaUri = Uri.parse("file://" + holder.mItem.getPath());
        Glide.with(mContext)
                .load(mediaUri)
                .centerCrop()
                .into(holder.mContentView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, BrowseFragment.newInstance(mValues, position), "pager")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final ImageView mContentView;
        private TextView mTvVideoTime;
        private RelativeLayout mRlGifInfo;
        private RelativeLayout mRlVideoInfo;
        private PhotoBean mItem;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
            mRlVideoInfo = view.findViewById(R.id.rl_video_info);
            mRlGifInfo = view.findViewById(R.id.rl_gif_info);
            mTvVideoTime = view.findViewById(R.id.tv_video_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvVideoTime.getText() + "'";
        }
    }
}
