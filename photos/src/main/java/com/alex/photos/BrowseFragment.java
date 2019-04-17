package com.alex.photos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.bean.PhotoBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<PhotoBean> list;
    private int index;
    private MyPagerAdapter mPagerAdapter;
    private ViewPager viewPager;


    public BrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photosList Parameter 1.
     * @param index      Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    public static BrowseFragment newInstance(ArrayList<PhotoBean> photosList, int index) {
        BrowseFragment fragment = new BrowseFragment();
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
            list = getArguments().getParcelableArrayList(ARG_PARAM1);
            index = getArguments().getInt(ARG_PARAM2);
            mPagerAdapter = new MyPagerAdapter(this.getActivity(), list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        view.findViewById(R.id.action_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof AppCompatActivity) {
                    ((AppCompatActivity) getContext()).onBackPressed();
                }
            }
        });
        // Inflate the layout for this fragment
        viewPager = view.findViewById(R.id.browseViewPager);

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(index);

        return view;
    }

}
