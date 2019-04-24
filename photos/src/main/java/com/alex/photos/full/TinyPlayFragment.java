package com.alex.photos.full;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TinyPlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TinyPlayFragment extends Fragment {
    private static final String KEY_MEDIA = "media";
    private PhotoBean mPhotoInfoBean;
    private OnToggleListener mListener;
    private MediaController mediaController;
    private VideoView mVideoView;
    private int lastSeek = 0;

    public TinyPlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param media Parameter 1.
     * @return A new instance of fragment TinyPlayFragment.
     */
    public static TinyPlayFragment newInstance(PhotoBean media) {
        TinyPlayFragment f = new TinyPlayFragment();
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
        mediaController = new MediaController(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tiny_play, container, false);

        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                }
                break;
                case MotionEvent.ACTION_UP: {
                    v.performClick();
                    mListener.onToggle();
                }
                break;
            }
            return true;
        });
        mVideoView = view.findViewById(R.id.video_view);
        //将VideoView与MediaController进行关联
        mVideoView.setVideoPath(mPhotoInfoBean.getPath());
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);
        return view;
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //让VideoView获取焦点
        mVideoView.requestFocus();
        mVideoView.start();
        mVideoView.seekTo(lastSeek);
        //mediaController.setVisibility(View.GONE);//隐藏进度条
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
        lastSeek = mVideoView.getCurrentPosition();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "退出播放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mediaController = null;
    }
}
