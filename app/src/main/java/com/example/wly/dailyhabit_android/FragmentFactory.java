package com.example.wly.dailyhabit_android;

import android.app.Fragment;

public class FragmentFactory {
    public static Fragment getBottomTabInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.tab_today:
                fragment = new TodayFragment();
                break;
            case R.id.tab_all:
                fragment = new AllFragment();
                break;
            case R.id.tab_mine:
                fragment = new MineFragment();
                break;
        }
        return fragment;
    }
}
