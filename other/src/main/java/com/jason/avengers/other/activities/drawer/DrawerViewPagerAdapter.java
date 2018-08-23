package com.jason.avengers.other.activities.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.jason.avengers.common.widgets.viewpager.CustomFragmentPagerAdapter;

public class DrawerViewPagerAdapter extends CustomFragmentPagerAdapter {

    public DrawerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt("position", position);
        Fragment fragment = new DrawerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        Bundle arguments = new Bundle();
        arguments.putInt("position", position);
        ((Fragment) object).setArguments(arguments);
        return object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}