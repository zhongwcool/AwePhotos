package com.alex.photos.piece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.load.DataLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoloPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoloPagerFragment extends Fragment implements DataLoader.LoadCallback {
    private static final String SAVED_INDEX = "android:index";

    private MyPagerAdapter mPagerAdapter;
    private int currentIndex = 0;
    private int mSize = 1;
    private ViewPager mViewPager;
    private TextView tvPagerIndex;
    private boolean isEnableShare = false;

    public SoloPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SoloPagerFragment.
     */
    public static SoloPagerFragment newInstance() {
        SoloPagerFragment fragment = new SoloPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SoloPagerFragment withShare(boolean withShare) {
        isEnableShare = withShare;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(SAVED_INDEX, 0);
        }

        mPagerAdapter = new MyPagerAdapter(this.getActivity());
        mPagerAdapter.setEnableShare(isEnableShare);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solo_pager, container, false);
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setPressed(true);
                }
                break;

                case MotionEvent.ACTION_UP: {
                    v.setPressed(false);
                    v.performClick();
                    if (null != getFragmentManager()) getFragmentManager().popBackStack();
                }
                break;
            }
            return true;
        });

        // Inflate the layout for this fragment
        tvPagerIndex = view.findViewById(R.id.tv_pager_index);
        mViewPager = view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                tvPagerIndex.setText(getString(R.string.tips_pager_index, (position + 1), mSize));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.getInstance(this).restartLoader(1, null, new DataLoader(getContext(), this));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        currentIndex = mViewPager.getCurrentItem();
        outState.putInt(SAVED_INDEX, currentIndex);
    }

    @Override
    public void onData(ArrayList<PhotoBean> photos) {
        mSize = photos.size();
        mPagerAdapter.setAdapterList(photos);
        if (currentIndex < mSize) mViewPager.setCurrentItem(currentIndex);
        tvPagerIndex.setText(getString(R.string.tips_pager_index, (currentIndex + 1), mSize));
    }
}
