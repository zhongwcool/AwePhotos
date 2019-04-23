package com.alex.photos.full;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment {
    private static final String KEY_MEDIA = "media";
    private PhotoBean mPhotoInfoBean;

    public PlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param media Parameter 1.
     * @return A new instance of fragment PlayFragment.
     */
    public static PlayFragment newInstance(PhotoBean media) {
        PlayFragment f = new PlayFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        VideoView mVideoView = view.findViewById(R.id.video_view);
        mVideoView.requestFocus();
        MediaController media = new MediaController(getContext());
        //将VideoView与MediaController进行关联
        mVideoView.setVideoPath(mPhotoInfoBean.getPath());
        mVideoView.setMediaController(media);
        media.setMediaPlayer(mVideoView);
        //让VideoView获取焦点
        mVideoView.requestFocus();
        mVideoView.start();
        return view;
    }
}
