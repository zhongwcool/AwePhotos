package com.alex.photos.full;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.OnToggleListener;
import com.alex.photos.widget.TinyPlayFragment;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class FullPagerFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_MEDIA = "media";
    private View mPlayVideo;
    private PhotoView mPhotoView;
    private PhotoBean bean;
    private OnToggleListener mListener;

    public static FullPagerFragment newInstance(PhotoBean media) {
        FullPagerFragment f = new FullPagerFragment();
        Bundle b = new Bundle();
        b.putParcelable(KEY_MEDIA, media);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = getArguments().getParcelable(KEY_MEDIA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_pager, null);
        mPlayVideo = view.findViewById(R.id.play_control);
        mPhotoView = view.findViewById(R.id.shot_view);
        mPlayVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            mPlayVideo.setVisibility(View.VISIBLE);
        } else {
            mPlayVideo.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(bean.getPath())
                .into(mPhotoView);

        mPhotoView.setMaximumScale(5);
        mPhotoView.setOnPhotoTapListener((OnPhotoTapListener) getActivity());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnToggleListener) {
            mListener = (OnToggleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (R.id.play_control == v.getId()) {
            mListener.onToggle();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, TinyPlayFragment.newInstance(bean, TinyPlayFragment.UI_NONE), "play")
                    .addToBackStack(null)
                    .commit();
        }
    }
}