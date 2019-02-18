package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.presenters.ChargingStationPresenter;
import com.jason.avengers.other.views.ChargingStationView;

import java.text.SimpleDateFormat;
import java.util.Date;

@Route(path = RouterPath.OTHER_CHARGINGSTATION)
public class ChargingStationActivity extends BaseActivity<ChargingStationPresenter, ChargingStationView>
        implements ChargingStationView, View.OnClickListener {

    private SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");

    private StringBuilder mMsgSB = new StringBuilder();
    private TextView mMsgTextView;
    private ScrollView mMsgScrollView;
    private View mRobView, mImmediatelyRobView, mStopView;
    private Switch mSwitchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_chargingstation);
        initViews();
    }

    private void initViews() {
        mMsgTextView = findViewById(R.id.other_chargingstation_msg);
        mMsgScrollView = findViewById(R.id.other_chargingstation_scrollview);
        mRobView = findViewById(R.id.other_chargingstation_rob);
        mRobView.setOnClickListener(this);
        mImmediatelyRobView = findViewById(R.id.other_chargingstation_immediately_rob);
        mImmediatelyRobView.setOnClickListener(this);
        mStopView = findViewById(R.id.other_chargingstation_stop);
        mStopView.setOnClickListener(this);
        mSwitchView = findViewById(R.id.other_chargingstation_switch);

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
        boolean isSuper = mSwitchView.isChecked();
        if (id == R.id.other_chargingstation_rob) {
            getPresenter().robChargingStation(isSuper);
            toggleActionViews();
        } else if (id == R.id.other_chargingstation_immediately_rob) {
            getPresenter().immediatelyRobChargingStation(isSuper);
            toggleActionViews();
        } else if (id == R.id.other_chargingstation_stop) {
            getPresenter().stopRobChargingStation();
            toggleActionViews();
        }
    }

    private void toggleActionViews() {
        mRobView.setEnabled(false);
        mImmediatelyRobView.setEnabled(false);
        mStopView.setEnabled(false);
        mMsgTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRobView.setEnabled(true);
                mImmediatelyRobView.setEnabled(true);
                mStopView.setEnabled(true);
            }
        }, 2000);
    }

    @Override
    public void showMsg(String msg) {
        if (mMsgTextView != null) {
            mMsgSB.append(df.format(new Date())).append(" ").append(msg).append("\n");
            mMsgTextView.setText(mMsgSB);
        }
        if (mMsgScrollView != null) {
            mMsgScrollView.fullScroll(View.FOCUS_DOWN);
        }
    }

    @Override
    public void toggleChargingStationStatus(boolean isApply) {
    }
}
