package com.alex.photos.full;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class PreviewFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_MEDIA = "media";
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
        if (getArguments() != null) {
            mPhotoInfoBean = getArguments().getParcelable(KEY_MEDIA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, null);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (R.id.play_control == v.getId()) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, PlayFragment.newInstance(mPhotoInfoBean), "play")
                    .addToBackStack(null)
                    .commit();
        }
    }
}