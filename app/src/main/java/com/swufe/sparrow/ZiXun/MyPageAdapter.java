package com.swufe.sparrow.ZiXun;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPageAdapter extends FragmentPagerAdapter {

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Frag1();
        } else if (position == 1) {
            return new Frag2();
        } else if (position == 2) {
            return new Frag3();
        } else {
            return new Frag4();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "好奇心日报";
        } else if (position == 1) {
            return "果壳：科学人";
        } else if (position == 2) {
            return "第一财经";
        } else {
            return "雪球：今日话题";
        }
    }
}
