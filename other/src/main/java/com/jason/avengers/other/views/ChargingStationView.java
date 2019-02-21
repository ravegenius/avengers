package com.jason.avengers.other.views;

import com.jason.avengers.common.base.BaseView;

public interface ChargingStationView extends BaseView {

    void showMsg(String msg);

    void onChangedChargingStationStatus(String termName);
}
