package com.alex.photos.full;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     DetailBottomDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DetailBottomDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_ALBUM_INFO = "album_info";
    private PhotoBean bean;

    public static DetailBottomDialogFragment newInstance(PhotoBean album) {
        final DetailBottomDialogFragment fragment = new DetailBottomDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM_INFO, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bean = getArguments().getParcelable(ARG_ALBUM_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail, container, false);
        TextView dateView = view.findViewById(R.id.detail_date);
        TextView pathView = view.findViewById(R.id.detail_path);

        dateView.setText(DateUtils.getFileTime(bean.getTime()));
        pathView.setText(bean.getPath());

        return view;
    }
}
