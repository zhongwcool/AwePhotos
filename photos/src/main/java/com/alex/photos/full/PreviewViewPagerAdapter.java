package com.alex.photos.full;



/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 17:04.
 *@描述         相册图片预览的adapter
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class PreviewViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragments;

    public PreviewViewPagerAdapter(FragmentManager fm, List<BaseFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}
