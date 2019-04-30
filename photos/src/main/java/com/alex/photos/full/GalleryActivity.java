package com.alex.photos.full;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.widget.DataLoader;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements DataLoader.LoadCallback {
    private MyFullGalleryAdapter adapter;
    private RecyclerView recyclerView;

    public static void start(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_gallery);

        if (null == adapter) {
            adapter = new MyFullGalleryAdapter(this);
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

        LoaderManager.getInstance(this).restartLoader(1, null, new DataLoader(this, this));
    }

    @Override
    public void onData(ArrayList<PhotoBean> list) {
        adapter.setAdapterList(list);
    }
}
