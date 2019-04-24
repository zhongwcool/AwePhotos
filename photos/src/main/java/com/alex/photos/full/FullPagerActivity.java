package com.alex.photos.full;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.alex.photos.BuildConfig;
import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.github.chrisbanes.photoview.OnPhotoTapListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FullPagerActivity extends AppCompatActivity implements OnPhotoTapListener,
        OnToggleListener {
    private static final String ARG_IS_LANDSCAPE = "is_landscape";
    /**
     * UI交互组件
     */
    private static final int UI_ANIMATION_DELAY = 0;
    private static final String photoListExtra = "photoListExtra";
    private static final String positionExtra = "positionExtra";

    public static void startActivity(Context context, int position, ArrayList<PhotoBean> list, boolean isLandscape) {
        Intent intent = new Intent(context, FullPagerActivity.class);
        intent.putParcelableArrayListExtra(photoListExtra, list);
        intent.putExtra(positionExtra, position);
        intent.putExtra(ARG_IS_LANDSCAPE, isLandscape);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private ActionBar mActionBar;
    private List<PhotoBean> mAllPhotoList;
    private View mContentView;
    private int lastViewPageItemPosition;
    private final Handler mHideHandler = new Handler();
    private boolean mVisible;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    };

    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mViewPager.setBackgroundColor(getResources().getColor(R.color.black));
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        mViewPager.setBackgroundColor(getResources().getColor(android.R.color.white));

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.white));
            //window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_pager);

        boolean isLand = getIntent().getBooleanExtra(ARG_IS_LANDSCAPE, true);
        if (isLand) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        mActionBar = getSupportActionBar();
        if (null != mActionBar) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;

        initView();

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_detail) {
            DetailBottomDialogFragment fragment = DetailBottomDialogFragment.newInstance(mAllPhotoList.get(lastViewPageItemPosition));
            fragment.show(getSupportFragmentManager(), "detail");

            return true;
        } else if (id == R.id.action_share) {
            if (mAllPhotoList.get(lastViewPageItemPosition).getMediaType() == 3) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, getUri(mAllPhotoList.get(lastViewPageItemPosition).getPath()));
                startActivity(Intent.createChooser(shareIntent, "分享视频到"));
            } else {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, getUri(mAllPhotoList.get(lastViewPageItemPosition).getPath()));
                startActivity(Intent.createChooser(shareIntent, "分享图片到"));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mContentView = findViewById(R.id.decor_content);
    }

    protected void initData() {
        Intent intent = getIntent();
        mAllPhotoList = intent.getParcelableArrayListExtra(photoListExtra);
        int position = intent.getIntExtra(positionExtra, 1);
        lastViewPageItemPosition = position;
        //mMaxSelectCount = intent.getIntExtra(maxSelectExtra, 1);

        mActionBar.setTitle((position + 1) + "/" + mAllPhotoList.size());
        //upDataDoneText();
        //mRbSelect.setChecked(isSelect(mAllPhotoList.get(position), mSelectList) >= 0);

        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mAllPhotoList.size(); i++) {
            fragmentList.add(FullPagerFragment.newInstance(mAllPhotoList.get(i)));
        }

        final MyFullPagerAdapter pagerAdapter = new MyFullPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int dataType = mAllPhotoList.get(position).getDataType();
                if (dataType == 0) {
                    if (position > lastViewPageItemPosition || position == 0) {
                        mViewPager.setCurrentItem(position + 1);
                    } else if (position < lastViewPageItemPosition || position == pagerAdapter.getCount() - 1) {
                        mViewPager.setCurrentItem(position - 1);
                    }
                    //mRbSelect.setChecked(false);
                } else {
                    mActionBar.setTitle((position + 1) + "/" + mAllPhotoList.size());
                    //mRbSelect.setChecked(isSelect(mAllPhotoList.get(position), mSelectList) >= 0);
                }
                lastViewPageItemPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        toggle();
    }

    private Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = TextUtils.concat(getHostAppId(), getString(R.string.app_authority_suffix)).toString();
            return FileProvider.getUriForFile(this, auth, new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    private String getHostAppId() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (null != info) {
                return info.packageName;
            } else {
                return BuildConfig.APPLICATION_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }

    @Override
    public void onToggle() {
        toggle();
    }
}
