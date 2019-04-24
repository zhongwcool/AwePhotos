package com.alex.photos.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RvGridLayout extends RelativeLayout {
    public RvGridLayout(Context context) {
        super(context);
    }

    public RvGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RvGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
