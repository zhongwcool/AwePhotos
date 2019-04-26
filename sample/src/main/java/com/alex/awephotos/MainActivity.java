package com.alex.awephotos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.alex.photos.full.GalleryActivity;
import com.alex.photos.piece.GalleryFragment;
import com.alex.photos.piece.SoloPagerFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] mPerms = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.tv_model_1).setOnClickListener(this);
        findViewById(R.id.tv_model_2).setOnClickListener(this);
        findViewById(R.id.tv_model_3).setOnClickListener(this);

        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, mPerms, 0x001);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_model_1: {
                GalleryActivity.start(this);
            }
            break;
            case R.id.tv_model_2: {
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, GalleryFragment.newInstance(4), "gallery")
                        .addToBackStack(null)
                        .commit();
            }
            break;
            case R.id.tv_model_3: {
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, SoloPagerFragment.newInstance(), "solo-pager")
                        .addToBackStack(null)
                        .commit();
            }
            break;
        }
    }
}
