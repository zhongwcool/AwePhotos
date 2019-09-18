package com.alex.photos.piece;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.viewpager.widget.PagerAdapter;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.utils.DateUtils;
import com.alex.photos.utils.ExifUtil;
import com.alex.photos.utils.FileUtils;
import com.alex.photos.widget.TinyPlayFragment;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<PhotoBean> list = new ArrayList<>();
    private View view;
    private boolean isEnableShare = false;

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
        PhotoBean bean = list.get(position);

        View view = View.inflate(mContext, R.layout.item_pager, null);
        PhotoView imageView = view.findViewById(R.id.content);
        TextView detail = view.findViewById(R.id.tv_detail);
        detail.setText(getDetail(bean));
        if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            view.findViewById(R.id.play_control).setVisibility(View.VISIBLE);
            view.findViewById(R.id.action_play).setOnClickListener(v -> {
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, TinyPlayFragment.newInstance(bean, TinyPlayFragment.UI_DIALOG), "play")
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            view.findViewById(R.id.play_control).setVisibility(View.INVISIBLE);
        }

        if (isEnableShare) {
            view.findViewById(R.id.share_control).setVisibility(View.VISIBLE);
            view.findViewById(R.id.share_control).setOnClickListener(v -> {
                if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(bean.getPath()));
                    mContext.startActivity(Intent.createChooser(shareIntent, "分享视频到"));
                } else {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(bean.getPath()));
                    mContext.startActivity(Intent.createChooser(shareIntent, "分享图片到"));
                }
            });
        } else {
            view.findViewById(R.id.share_control).setVisibility(View.INVISIBLE);
        }

        //Uri mediaUri = Uri.parse("file://" + bean.getPath());
        Glide.with(mContext)
                .load(bean.getPath())
                .placeholder(R.drawable.img_place_holder)
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

    private String getDetail(PhotoBean bean) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.getFileTime(bean.getTime())).append("\n");
        sb.append(bean.getWidth()).append("x").append(bean.getHeight()).append("    ").append(FileUtils.getReadableSize(bean.getSize())).append("\n");

        if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            sb.append("时长: ").append(bean.getDuration());
        } else {
            try {
                InputStream is = mContext.getContentResolver().openInputStream(Uri.parse(bean.getPath()));
                if (null == is) return sb.toString();
                ExifInterface exifInterface = new ExifInterface(is);
                String device = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
                sb.append("设备: ").append(null == device ? "未记录" : ExifUtil.hexStr2Str(device)).append("\n");
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = TextUtils.concat(getHostAppId(), mContext.getResources().getString(R.string.app_authority_suffix)).toString();
            return FileProvider.getUriForFile(mContext, auth, new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    private String getHostAppId() {
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }

    public void setEnableShare(boolean enableShare) {
        isEnableShare = enableShare;
    }
}
