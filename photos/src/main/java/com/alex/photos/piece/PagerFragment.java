package com.alex.photos.piece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFragment extends Fragment {
    private static final String SAVED_INDEX = "android:index";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int currentIndex = 0;
    private int mSize = 1;
    private MyPagerAdapter mPagerAdapter;
    private TextView tvPagerIndex;
    private ViewPager mViewPager;
    private ArrayList<PhotoBean> photos;

    public PagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photosList Parameter 1.
     * @param index      Parameter 2.
     * @return A new instance of fragment PagerFragment.
     */
    public static PagerFragment newInstance(ArrayList<PhotoBean> photosList, int index) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, photosList);
        args.putInt(ARG_PARAM2, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photos = getArguments().getParcelableArrayList(ARG_PARAM1);
            currentIndex = getArguments().getInt(ARG_PARAM2);
            mPagerAdapter = new MyPagerAdapter(this.getActivity());
            mPagerAdapter.setAdapterList(photos);
        } else {
            getActivity().onBackPressed();
        }

        if (null != photos) {
            mSize = photos.size();
        } else {
            getActivity().onBackPressed();
        }

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(SAVED_INDEX, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        // Inflate the layout for this fragment
        tvPagerIndex = view.findViewById(R.id.tv_pager_index);
        mViewPager = view.findViewById(R.id.browseViewPager);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentIndex);

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
        tvPagerIndex.setText(getString(R.string.tips_pager_index, (currentIndex + 1), mSize));

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        currentIndex = mViewPager.getCurrentItem();
        outState.putInt(SAVED_INDEX, currentIndex);
    }
}
