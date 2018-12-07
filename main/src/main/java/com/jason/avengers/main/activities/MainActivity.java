package com.jason.avengers.main.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.base.BaseView;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.main.R;
import com.jason.avengers.other.fragments.OtherFragment;
import com.jason.avengers.resume.fragments.ResumeFragment;
import com.jason.avengers.skill.fragments.SkillFragment;

@Route(path = RouterPath.MAIN_MAIN)
public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;

//    private UserPresenter mPresenter;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected BaseView initAttachView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.author_name);
        mToolbar.setSubtitle(R.string.author_desc);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mBottomNavigationView = findViewById(R.id.main_bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        mNavigationView = findViewById(R.id.main_nav);
        mNavigationView.setNavigationItemSelectedListener(this);

        mViewPager = findViewById(R.id.main_viewpager);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

//        mPresenter = new UserPresenter();
//        mPresenter.attach(this);
//        mPresenter.getUserData();
    }

    @Override
    protected void onDestroy() {
//        if (mPresenter != null) {
//            mPresenter.detach();
//        }
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.nav_resume) {
            mViewPager.setCurrentItem(0, false);
            return true;
        } else if (i == R.id.nav_skill) {
            mViewPager.setCurrentItem(1, false);
            return true;
        } else if (i == R.id.nav_other) {
            mViewPager.setCurrentItem(2, false);
            return true;
        } else {
            DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(findViewById(R.id.main_bottom_nav), "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.nav_resume);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.nav_skill);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.nav_other);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

//    @Override
//    public void getUserDataSuccess(UserBean bean) {
//        if (bean != null) {
//            if (mNavigationView != null) {
//                Glide.with(this)
//                        .load(bean.getAvatar()).apply(GlobalConfig.AvatarOptions)
//                        .into((ImageView) mNavigationView.findViewById(R.id.nav_header_avatar));
//                ((TextView) mNavigationView.findViewById(R.id.nav_header_name)).setText(bean.getUsername());
//                ((TextView) mNavigationView.findViewById(R.id.nav_header_email)).setText(bean.getEmail());
//            }
//            if (mToolbar != null) {
//                mToolbar.setTitle(bean.getUsername());
//                if (!TextUtils.isEmpty(bean.getMark())) {
//                    mToolbar.setSubtitle(bean.getMark());
//                }
//            }
//        }
//    }
//
//    @Override
//    public void getUserDataFailure(String errorMsg) {
//        // Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
//        // getUserDataSuccess(TestUtils.initUserTestData());
//    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragments = new Fragment[]
                {new ResumeFragment(), new SkillFragment(), new OtherFragment()};


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
