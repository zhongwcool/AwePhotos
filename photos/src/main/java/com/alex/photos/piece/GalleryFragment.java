package com.alex.photos.piece;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.DataWithHeadLoader;
import com.alex.photos.widget.FlowHeadItemDecoration;
import com.alex.photos.widget.HeadItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link DataWithHeadLoader.LoadCallback}
 * interface.
 */
public class GalleryFragment extends Fragment implements DataWithHeadLoader.LoadCallback {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 4;
    private MyGalleryAdapter adapter;
    private View loading;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GalleryFragment() {
    }

    @SuppressWarnings("unused")
    public static GalleryFragment newInstance(int columnCount) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            if (mColumnCount < 1 | mColumnCount > 5) {
                mColumnCount = 4;
            }
        }

        if (null == adapter) {
            adapter = new MyGalleryAdapter(getActivity());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        loading = view.findViewById(R.id.progress);
        recyclerView = view.findViewById(R.id.list);
        // Set the adapter
        //获取屏幕方向
        Configuration mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        GridLayoutManager gridLayoutManager;
        if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            gridLayoutManager = new GridLayoutManager(getContext(), mColumnCount);
        } else {
            //横屏
            gridLayoutManager = new GridLayoutManager(getContext(), mColumnCount * 2);
        }
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == PhotoBean.TYPE_HEAD) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new HeadItemDecoration(getActivity()));
        recyclerView.addItemDecoration(new FlowHeadItemDecoration(getActivity(), adapter));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        LoaderManager.getInstance(this).restartLoader(1, null, new DataWithHeadLoader(getContext(), this));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onData(ArrayList<PhotoBean> list, List<Integer> headPositions) {
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.updateAdapterList(list, headPositions);
    }
}
