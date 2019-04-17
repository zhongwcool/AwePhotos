package com.alex.awephotos.core;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.awephotos.R;
import com.alex.awephotos.bean.AlbumInfoBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AlbumInfoBean}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.ViewHolder> {

    private List<AlbumInfoBean> mValues = new ArrayList<>();
    private Context mContext;

    public MyGalleryAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterList(List<AlbumInfoBean> list) {
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
        holder.mInfoView.setText(mValues.get(position).getName());
        Uri mediaUri = Uri.parse("file://" + holder.mItem.getPath());

        Glide.with(mContext)
                .load(mediaUri)
                .centerCrop()
                .into(holder.mContentView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mContentView;
        public final TextView mInfoView;
        public AlbumInfoBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
            mInfoView = view.findViewById(R.id.info);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mInfoView.getText() + "'";
        }
    }
}
