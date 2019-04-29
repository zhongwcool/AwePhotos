package com.alex.photos.piece;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.utils.DateUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoBean}
 */
public class MyGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;
    private ArrayList<PhotoBean> mShowItems = new ArrayList<>();
    private List<Integer> mHeadPositionList = new ArrayList<>();
    private Context mContext;

    public MyGalleryAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE) {
            return new HeadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv_head, null));
        } else if (viewType == BODY_TYPE) {
            return new BodyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv_media, null));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final PhotoBean mItem = mShowItems.get(position);
        if (mItem == null) {
            return;
        }

        if (holder instanceof BodyViewHolder) {
            if (mItem.getMediaType() == 3) {
                ((BodyViewHolder) holder).mRlGifInfo.setVisibility(View.INVISIBLE);
                ((BodyViewHolder) holder).mRlVideoInfo.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).mTvVideoTime.setText(mItem.getDuration());
            } else {
                ((BodyViewHolder) holder).mRlVideoInfo.setVisibility(View.INVISIBLE);
                ((BodyViewHolder) holder).mRlGifInfo.setVisibility(".gif".equalsIgnoreCase(mItem.getExtension()) ? View.VISIBLE : View.INVISIBLE);
            }

            Uri mediaUri = Uri.parse("file://" + mItem.getPath());
            Glide.with(mContext)
                    .load(mediaUri)
                    .centerCrop()
                    .into(((BodyViewHolder) holder).mContentView);

            ((BodyViewHolder) holder).mView.setOnClickListener(v -> {
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, PagerFragment.newInstance(mShowItems, position), "pager")
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            ((HeadViewHolder) holder).mTvTitle.setText(DateUtils.getSdfTime(mItem.getTime() + "", DateUtils.YMDHMS2));
        }
    }

    @Override
    public int getItemCount() {
        return mShowItems.size();
    }

    public void updateAdapterList(ArrayList<PhotoBean> list, List<Integer> headPositionList) {
        this.mHeadPositionList = headPositionList;
        if (list != null) {
            mShowItems = list;
            notifyDataSetChanged();
        }
    }

    public List<Integer> getHeadPositionList() {
        return mHeadPositionList;
    }

    /**
     * //获取当前相同时间的position
     *
     * @param position firstVisiblePosition
     * @return
     */
    public int getCurrentTimeItemCount(int position) {
        int count = -1;
        for (int i = 0; i < mHeadPositionList.size(); i++) {
            if (i + 1 < mHeadPositionList.size()) {
                if (position > mHeadPositionList.get(i) && position < mHeadPositionList.get(i + 1)) {
                    return mHeadPositionList.get(i + 1) - mHeadPositionList.get(i);
                }
            }
        }

        return count;
    }

    public List<PhotoBean> getAllDataNoHead() {
        List<PhotoBean> mAllItems = new ArrayList<>();
        for (int i = 0; i < mShowItems.size(); i++) {
            if (mShowItems.get(i).getDataType() == BODY_TYPE) {
                mAllItems.add(mShowItems.get(i));
            }
        }
        return mAllItems;
    }

    public List<PhotoBean> getAllData() {
        return mShowItems;
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final ImageView mContentView;
        private TextView mTvVideoTime;
        private RelativeLayout mRlGifInfo;
        private RelativeLayout mRlVideoInfo;

        private BodyViewHolder(View view) {
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

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;

        HeadViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
