package com.alex.photos.full;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.alex.photos.BuildConfig;
import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

public class PreviewFragment extends BaseFragment implements View.OnClickListener {
    public static final String KEY_MEDIA = "media";
    private View mPlayVideo;
    private PhotoView mPhotoView;
    private PhotoBean mPhotoInfoBean;

    public static PreviewFragment newInstance(PhotoBean media) {
        PreviewFragment f = new PreviewFragment();
        Bundle b = new Bundle();
        b.putParcelable(KEY_MEDIA, media);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, null);
        mPhotoInfoBean = getArguments().getParcelable(KEY_MEDIA);
        mPlayVideo = view.findViewById(R.id.play_control);
        mPhotoView = view.findViewById(R.id.shot_view);
        mPlayVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mPhotoInfoBean.getMediaType() == 3) {
            mPlayVideo.setVisibility(View.VISIBLE);
        } else {
            mPlayVideo.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(mPhotoInfoBean.getPath())
                .into(mPhotoView);

        mPhotoView.setMaximumScale(5);
        mPhotoView.setOnPhotoTapListener((OnPhotoTapListener) getActivity());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (R.id.play_control == v.getId()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(getUri(mPhotoInfoBean.getPath()), "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (isIntentAvailable(getContext(), intent)) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this.getContext(), "没有可以播放的程序", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this.getContext(), "播放出错", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = TextUtils.concat(getHostAppId(), getString(R.string.app_authority_suffix)).toString();
            return FileProvider.getUriForFile(getContext(), auth, new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    private String getHostAppId() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
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