package com.alex.photos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.bean.PhotoBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int index;
    private MyPagerAdapter mPagerAdapter;


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
            ArrayList<PhotoBean> list = getArguments().getParcelableArrayList(ARG_PARAM1);
            index = getArguments().getInt(ARG_PARAM2);
            mPagerAdapter = new MyPagerAdapter(this.getActivity(), list);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        // Inflate the layout for this fragment
        ViewPager viewPager = view.findViewById(R.id.browseViewPager);

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(index);

        return view;
    }

}
