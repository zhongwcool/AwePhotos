package com.alex.photos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
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
                /*
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(getUri(list.get(position).getPath()), "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (isIntentAvailable(mContext, intent)) {
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "没有可以播放的程序", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "播放出错", Toast.LENGTH_SHORT).show();
                }
                */
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

    /**
     * 检查是否有可以处理的程序
     *
     * @param context 上下文
     * @param intent  能播放的
     * @return
     */
    private boolean isIntentAvailable(Context context, Intent intent) {
        List resolves = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolves.size() > 0;
    }

    private Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = TextUtils.concat(getHostAppId(), mContext.getString(R.string.app_authority_suffix)).toString();
            return FileProvider.getUriForFile(mContext, auth, new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    private String getHostAppId() {
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (null != info) {
                return info.packageName;
            } else {
                return BuildConfig.APPLICATION_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }
}
