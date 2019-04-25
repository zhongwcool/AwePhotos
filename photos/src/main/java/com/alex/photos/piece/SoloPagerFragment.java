package com.alex.photos.piece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.GalleryLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoloPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoloPagerFragment extends Fragment implements GalleryLoader.LoadCallback {
    private MyPagerAdapter mPagerAdapter;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPagerAdapter = new MyPagerAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solo_pager, container, false);

        // Inflate the layout for this fragment
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.getInstance(this).restartLoader(1, null, new GalleryLoader(getContext(), this));
    }

    @Override
    public void onData(ArrayList<PhotoBean> photos) {
        mPagerAdapter.setAdapterList(photos);
    }
}
