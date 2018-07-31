package com.jason.avengers.skill.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseActivity;
import com.jason.avengers.skill.beans.SkillInfoBean;
import com.jason.avengers.skill.fragments.SkillDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/4/8.
 */

public class SkillTabActivity extends BaseActivity {

    public static final String PARAM_SKILL = "param_skill";
    private SkillInfoBean mSkillInfoBean;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_skill_tab);

        if (getIntent() != null) {
            mSkillInfoBean = (SkillInfoBean) getIntent().getSerializableExtra(PARAM_SKILL);
            String point = mSkillInfoBean.getCurrentClickedPoint();
            ArrayList<String> points = (ArrayList<String>) mSkillInfoBean.getSkillPoints();

            mTabLayout = findViewById(R.id.skill_tablayout);
            mViewPager = findViewById(R.id.skill_viewpager);

            mViewPager.setAdapter(new SkillTabAdapter(this, getSupportFragmentManager(), points));
            mViewPager.setCurrentItem(points.indexOf(point));
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    class SkillTabAdapter extends FragmentStatePagerAdapter {

        private Context context;
        private List<String> data;

        SkillTabAdapter(Context context, FragmentManager fm, List<String> data) {
            super(fm);
            this.context = context;
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            String url;
            //url = String.format("file:///android_asset/%s/%s/index.html", mSkillInfoBean.getKind(), data.get(position));
            url = "https://m.weibo.cn/1677875323/4253108710122863";
            //url = "file:///android_asset/微博正文 - 微博HTML5版.html";
            //url = "https://m.weibo.cn/status/4253108710122863?";

            args.putString(SkillDetailFragment.PARAM_DATA, url);
            return SkillDetailFragment.instantiate(context, SkillDetailFragment.class.getName(), args);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position);
        }
    }
}
