package com.alex.photos.full;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.GalleryLoader;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements GalleryLoader.LoadCallback {
    private static final String ARG_IS_LANDSCAPE = "is_landscape";
    private MyFullGalleryAdapter adapter;
    private RecyclerView recyclerView;

    public static void startActivity(Activity activity, boolean isLandscape) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(ARG_IS_LANDSCAPE, isLandscape);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        boolean isLand = getIntent().getBooleanExtra(ARG_IS_LANDSCAPE, true);
        if (isLand) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (null == adapter) {
            adapter = new MyFullGalleryAdapter(this, isLand);
        }

        recyclerView = findViewById(R.id.list);
        //获取屏幕方向
        Configuration mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            //横屏
            recyclerView.setLayoutManager(new GridLayoutManager(this, 8));
        }
        //mDevices.add(Device.createDummy("192.168.0.1"));
        //mDevices.add(Device.createDummy("192.168.0.2"));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LoaderManager.getInstance(this).restartLoader(1, null, new GalleryLoader(this, this));
    }

    @Override
    public void onData(ArrayList<PhotoBean> list) {
        adapter.setAdapterList(list);
    }
}
