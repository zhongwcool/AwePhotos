package com.alex.photos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.bean.PhotoBean;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link GalleryLoader.LoadCallback}
 * interface.
 */
public class GalleryFragment extends Fragment implements GalleryLoader.LoadCallback {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 4;
    private MyGalleryAdapter adapter;

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
        }

        adapter = new MyGalleryAdapter(getActivity());
        LoaderManager.getInstance(this).initLoader(1, null, new GalleryLoader(getContext(), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        view.findViewById(R.id.action_close).setOnClickListener(v -> {
            if (getContext() instanceof AppCompatActivity) {
                ((AppCompatActivity) getContext()).onBackPressed();
            }
        });

        RecyclerView list = view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            list.setLayoutManager(new LinearLayoutManager(context));
        } else {
            list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onData(ArrayList<PhotoBean> list) {
        adapter.setAdapterList(list);
    }
}
