package com.alex.photos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.photos.R;
import com.alex.photos.bean.PhotoBean;
import com.alex.photos.piece.MyGalleryAdapter;
import com.alex.photos.utils.DateUtils;

import java.util.List;

public class FlowHeadItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mTitleHeight;
    private MyGalleryAdapter mAdapter;

    public FlowHeadItemDecoration(Context context, MyGalleryAdapter adapter) {
        super();
        this.mContext = context;
        this.mAdapter = adapter;
        mTitleHeight = (int) mContext.getResources().getDimension(R.dimen.item_head_height);
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        try {
            //获取到视图中第一个可见的item的position
            List<PhotoBean> allData = mAdapter.getAllData();
            if (allData.size() == 0) {
                return;
            }

            int firstVisiblePosition = ((GridLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();

            int currentTimeItemCount = mAdapter.getCurrentTimeItemCount(firstVisiblePosition) - 1;
            if (currentTimeItemCount >= 0) {
                int remainder = currentTimeItemCount % ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                if (remainder == 0) {
                    firstVisiblePosition = firstVisiblePosition + ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() - 1;
                } else if (remainder != 1) {
                    firstVisiblePosition = firstVisiblePosition + ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() - remainder;
                }
            }

            String time = DateUtils.getSdfTime(allData.get(firstVisiblePosition).getTime(), DateUtils.YMDHMS2);

            View child = parent.findViewHolderForLayoutPosition(firstVisiblePosition).itemView;

            boolean flag = false;
            if ((firstVisiblePosition + 1) < allData.size()) {
                String lastTime = DateUtils.getSdfTime(allData.get(firstVisiblePosition + 1).getTime(), DateUtils.YMDHMS2);
                if (!(time + "").equals(lastTime)) {
                    if (child.getHeight() + child.getTop() < mTitleHeight) {
                        canvas.save();
                        flag = true;
                        canvas.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
                    }
                }
            }

            View topTitleView = mLayoutInflater.inflate(R.layout.item_rv_head, null, false);
            TextView textView = topTitleView.findViewById(R.id.tv_title);
            textView.setText(DateUtils.getSdfTime(allData.get(firstVisiblePosition).getTime(), DateUtils.YMDHMS2));
            int toDrawWidthSpec;//用于测量的widthMeasureSpec
            int toDrawHeightSpec;//用于测量的heightMeasureSpec
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) topTitleView.getLayoutParams();
            if (lp == null) {
                lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
                topTitleView.setLayoutParams(lp);
            }
            topTitleView.setLayoutParams(lp);
            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
            } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
            } else {
                //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
            }
            //高度同理
            if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
            } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
            } else {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(mTitleHeight, View.MeasureSpec.EXACTLY);
            }
            //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上。
            topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec);
            topTitleView.layout(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getPaddingLeft() + topTitleView.getMeasuredWidth(), parent.getPaddingTop() + topTitleView.getMeasuredHeight());
            topTitleView.draw(canvas);//Canvas默认在视图顶部，无需平移，直接绘制
            if (flag) {
                canvas.restore();//恢复画布到之前保存的状态}
            }
        } catch (Exception e) {
            Log.d("", "onDrawOver:  " + e);
        }
    }
}
