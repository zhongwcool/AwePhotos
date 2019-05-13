package com.alex.photos.full;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoBean}
 */
public class MyFullGalleryAdapter extends RecyclerView.Adapter<MyFullGalleryAdapter.ViewHolder> {

    private ArrayList<PhotoBean> mValues = new ArrayList<>();
    private Context mContext;

    public MyFullGalleryAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterList(ArrayList<PhotoBean> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
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

        holder.mView.setOnClickListener(v -> {
            /*
            ((AppCompatActivity) mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, PagerFragment.newInstance(mValues, position), "pager")
                    .addToBackStack(null)
                    .commit();
                    */
            //PagerFragment.newInstance(mValues, position).show(((AppCompatActivity) mContext).getSupportFragmentManager(), "pager");
            FullPagerActivity.start(mContext, position, mValues);
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

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mTvVideoTime.getText() + "'";
        }
    }
}
