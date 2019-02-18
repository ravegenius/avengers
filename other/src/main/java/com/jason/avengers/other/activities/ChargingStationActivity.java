package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.presenters.ChargingStationPresenter;
import com.jason.avengers.other.views.ChargingStationView;

@Route(path = RouterPath.OTHER_CHARGINGSTATION)
public class ChargingStationActivity extends BaseActivity<ChargingStationPresenter, ChargingStationView>
        implements ChargingStationView, View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_chargingstation);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.other_chargingstation_login).setOnClickListener(this);
        findViewById(R.id.other_chargingstation_search).setOnClickListener(this);
        findViewById(R.id.other_chargingstation_apply).setOnClickListener(this);
    }

    @Override
    protected ChargingStationPresenter initPresenter() {
        return new ChargingStationPresenter();
    }

    @Override
    protected ChargingStationView initAttachView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.other_chargingstation_login) {
            getPresenter().doLogin();
        } else if (id == R.id.other_chargingstation_search) {
            getPresenter().doSearch();
        } else if (id == R.id.other_chargingstation_apply) {
            getPresenter().doApply();
        }
    }
}
