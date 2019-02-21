package com.jason.avengers.other.presenters;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.other.beans.ChargingStationBean;
import com.jason.avengers.other.services.ChargingStationService;
import com.jason.avengers.other.views.ChargingStationView;
import com.jason.core.network.NetworkBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ChargingStationPresenter extends BasePresenter<ChargingStationView> {

    private static Map<String, Map<String, String>> sApplyParams = new HashMap<>();
    private static Map<String, Map<String, String>> sCancelParams = new HashMap<>();
    private static final String sCustomNo = "1000031264";

    static {
        Map<String, String> params;

        /**
         * 网易7号桩
         *  预约 {"term_id":"1101081071100454","key":"8A3D10547423419837DBC06CADDB7F35","custom_no":"1000031264","timestamp":1550491580770,"gun_id":"467"}
         *  取消 {"term_id":"1101081071100454","key":"E222BE2E517DC562D636E7A4DDE024E7","custom_no":"1000031264","timestamp":1550491836651,"gun_id":"467","cancel_reason":""}
         */
        params = new HashMap<>();
        params.put("term_id", "1101081071100454");
        params.put("gun_id", "467");
        params.put("timestamp", "1550491580770");
        params.put("key", "8A3D10547423419837DBC06CADDB7F35");
        sApplyParams.put("网易7号桩", params);
        params = new HashMap<>();
        params.put("term_id", "1101081071100454");
        params.put("gun_id", "467");
        params.put("cancel_reason", "");
        params.put("timestamp", "1550491836651");
        params.put("key", "E222BE2E517DC562D636E7A4DDE024E7");
        sCancelParams.put("网易7号桩", params);

        /**
         * 网易9号桩
         *  预约 {"term_id":"1101081071101280","key":"B04134A8557B8F7327A5064333DE947E","custom_no":"1000031264","timestamp":1550566947306,"gun_id":"1294"}
         *  取消 {"term_id":"1101081071101280","key":"503759518FE35761A76E8F4A03E2D194","custom_no":"1000031264","timestamp":1550567082076,"gun_id":"1294","cancel_reason":""}
         */
        params = new HashMap<>();
        params.put("term_id", "1101081071101280");
        params.put("gun_id", "1294");
        params.put("timestamp", "1550566947306");
        params.put("key", "B04134A8557B8F7327A5064333DE947E");
        sApplyParams.put("网易9号桩", params);
        params = new HashMap<>();
        params.put("term_id", "1101081071101280");
        params.put("gun_id", "1294");
        params.put("cancel_reason", "");
        params.put("timestamp", "1550567082076");
        params.put("key", "503759518FE35761A76E8F4A03E2D194");
        sCancelParams.put("网易9号桩", params);

        /**
         * 网易13号桩
         *  预约 {"term_id":"1101081071101284","key":"19CA7C5E68947A37E693E9A18851EB8D","custom_no":"1000031264","timestamp":1550491471932,"gun_id":"1298"}
         *  取消 {"term_id":"1101081071101284","key":"52C8B5851389F18826C4D02660CE0BC6","custom_no":"1000031264","timestamp":1550491875924,"gun_id":"1298","cancel_reason":""}
         */
        params = new HashMap<>();
        params.put("term_id", "1101081071101284");
        params.put("gun_id", "1298");
        params.put("timestamp", "1550491471932");
        params.put("key", "19CA7C5E68947A37E693E9A18851EB8D");
        sApplyParams.put("网易13号桩", params);
        params = new HashMap<>();
        params.put("term_id", "1101081071101284");
        params.put("gun_id", "1298");
        params.put("cancel_reason", "");
        params.put("timestamp", "1550491875924");
        params.put("key", "52C8B5851389F18826C4D02660CE0BC6");
        sCancelParams.put("网易13号桩", params);

        /**
         * 网易4号桩
         *  预约 {"term_id":"1101081071100452","key":"2D3420983BEFEEB194B020B68B43B4F3","custom_no":"1000031264","timestamp":1550491688271,"gun_id":"465"}
         *  取消 {"term_id":"1101081071100452","key":"A2EB0990E00E805075BA282F666A0945","custom_no":"1000031264","timestamp":1550491714313,"gun_id":"465","cancel_reason":""}
         */
        params = new HashMap<>();
        params.put("term_id", "1101081071100452");
        params.put("gun_id", "465");
        params.put("timestamp", "1550491688271");
        params.put("key", "2D3420983BEFEEB194B020B68B43B4F3");
        sApplyParams.put("网易4号桩", params);
        params = new HashMap<>();
        params.put("term_id", "1101081071100452");
        params.put("gun_id", "465");
        params.put("cancel_reason", "");
        params.put("timestamp", "1550491714313");
        params.put("key", "A2EB0990E00E805075BA282F666A0945");
        sCancelParams.put("网易4号桩", params);

        /**
         * 网易5号桩
         *  预约
         *  取消
         */
        params = new HashMap<>();
        params.put("term_id", "1101081071100453");
        params.put("gun_id", "466");
        params.put("timestamp", "1550216059114");
        params.put("key", "A733E7792BA026D187CE152921E138C1");
        sApplyParams.put("网易5号桩", params);
        params = new HashMap<>();
        params.put("term_id", "1101081071100453");
        params.put("gun_id", "466");
        params.put("cancel_reason", "");
        params.put("timestamp", "1550491714313");
        params.put("key", "A2EB0990E00E805075BA282F666A0945");
        sCancelParams.put("网易5号桩", params);
    }

    private ChargingStationView mView;
    private Handler mHandler;
    private int mStep = 5;
    private long mIntervalTime = 5000L;
    private int mCount;
    private String mCustomNo;
    private Map<String, Map<String, String>> mApplyAvailableParams;
    private Map<String, Map<String, String>> mCancelAvailableParams;

    private boolean mIsSuper;
    private boolean mIsImmediatelyRob;
    private boolean mIsStop;


    @Override
    protected void attach(ChargingStationView view) {
        mView = view;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what % mStep == 0 && !mIsImmediatelyRob) {
                    doSearch();
                } else {
                    doApply();
                }
            }
        };
    }

    @Override
    protected void detach() {
        mView = null;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }

    public void robChargingStation(boolean isSuper) {
        showMsg("开抢========");

        mIsSuper = isSuper;
        mIsImmediatelyRob = false;
        mIsStop = false;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (TextUtils.isEmpty(mCustomNo)) {
            doLogin();
        } else {
            doSearch();
        }
    }

    public void immediatelyRobChargingStation(boolean isSuper) {
        showMsg("立刻开抢========");

        mIsSuper = isSuper;
        mIsImmediatelyRob = true;
        mIsStop = false;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mApplyAvailableParams == null) {
            mApplyAvailableParams = new HashMap<>(5);
        } else {
            mApplyAvailableParams.clear();
        }
        mApplyAvailableParams.putAll(sApplyParams);
        doApply();
    }

    public void stopRobChargingStation() {
        showMsg("停止========");

        mIsStop = true;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void cancelChargingStation() {
        showMsg("取消预约========");

        mIsStop = false;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mCancelAvailableParams == null) {
            mCancelAvailableParams = new HashMap<>(5);
        } else {
            mCancelAvailableParams.clear();
        }
        mCancelAvailableParams.putAll(sCancelParams);
        doCancel();
    }

    private void doLogin() {
        if (mIsStop) {
            return;
        }
        showMsg("开始登录========");

        String ac_name = "13401073452";
        String password = "dea92f8d0849ed355f5f1ba5028b0720";
        String timestamp = "1550215361016";
        String key = "DB54F26919DF1956237428C44BFB19C8";
        // {"key":"DB54F26919DF1956237428C44BFB19C8","timestamp":"1550215361016","password":"dea92f8d0849ed355f5f1ba5028b0720","ac_name":"13401073452"}
        Map<String, String> params = new HashMap<>(4);
        params.put("ac_name", ac_name);
        params.put("password", password);
        params.put("timestamp", timestamp);
        params.put("key", key);
        // {"code":0,"msg":"用户登录成功！","data":{"custom_type":1,"custom_no":"1000031264","pay_schema":1}}
        NetworkBuilder.build(ChargingStationService.class)
                .loginChargingStations(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ChargingStationBean<ChargingStationBean.ChargingStationUser>>() {
                            @Override
                            public void accept(ChargingStationBean<ChargingStationBean.ChargingStationUser> result) {
                                if (mIsStop) {
                                    return;
                                }
                                showMsg("结束登录========");
                                if (ChargingStationBean.CODE_SUCCESS == result.getCode()) {
                                    mCustomNo = result.getData().getCustom_no();
                                    doSearch();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                                if (mIsStop) {
                                    return;
                                }
                                showMsg("登录出错========");
                            }
                        });
    }

    private void doSearch() {
        if (mIsStop) {
            return;
        }
        showMsg("开始查找========");

        String term_id = "1101081071";
        String timestamp = "1550215638339";
        String key = "0264140899E8FACA17BE2BCA6476E47E";
        // {"term_id":"1101081071","key":"0264140899E8FACA17BE2BCA6476E47E","custom_no":"1000031264","timestamp":1550215638339}
        Map<String, String> params = new HashMap<>(4);
        params.put("custom_no", mCustomNo);
        params.put("term_id", term_id);
        params.put("timestamp", timestamp);
        params.put("key", key);
        // {"code":0,"msg":"","data":{"open_time":"","station_pic":"","location":"北京市市辖区海淀区北京市海淀区软件园南街","parking_fee":[],"charge_collect":2,"charge_info":[{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"466","gname":"1","gstatus":2,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100453","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":1,"term_name":"网易5号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1294","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101280","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易9号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1295","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101281","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易10号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1296","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101282","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易11号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1297","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101283","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易12号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1298","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101284","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易13号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1299","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101285","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易14号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1300","gname":"1","gstatus":1,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101286","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易15号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1301","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101287","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易16号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"462","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100449","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易1号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"463","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100450","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易2号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"464","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100451","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易3号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"465","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100452","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":1,"term_name":"网易4号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"467","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100454","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易7号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"468","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100455","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易8号桩"}],"dc_charge_count":2,"longitude":116.28211,"parking_fee_count":0,"latitude":40.04957,"ac_charge_count":13,"payment_method":"充电卡和在线支付","operator":"奇才智能","term_name":"BJ.0001网易充电桩"}}
        NetworkBuilder.build(ChargingStationService.class)
                .getChargingStations(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ChargingStationBean<ChargingStationBean.ChargingStationCollect>>() {
                            @Override
                            public void accept(ChargingStationBean<ChargingStationBean.ChargingStationCollect> result) {
                                if (mIsStop) {
                                    return;
                                }
                                showMsg("结束查找========");
                                if (ChargingStationBean.CODE_SUCCESS == result.getCode()) {
                                    ChargingStationBean.ChargingStationCollect collect = result.getData();
                                    List<ChargingStationBean.ChargingStationInfo> infos = collect.getCharge_info();
                                    if (null != infos && infos.size() > 0) {
                                        if (mApplyAvailableParams == null) {
                                            mApplyAvailableParams = new HashMap<>(5);
                                        } else {
                                            mApplyAvailableParams.clear();
                                        }
                                        mApplyAvailableParams.putAll(sApplyParams);
                                        for (ChargingStationBean.ChargingStationInfo info : infos) {
                                            List<ChargingStationBean.ChargingStationGunInfo> gunInfos = info.getGun_info();
                                            if (null != gunInfos && gunInfos.size() > 0) {
                                                for (ChargingStationBean.ChargingStationGunInfo gunInfo : gunInfos) {
                                                    String gstatus;
                                                    switch (gunInfo.getGstatus()) {
                                                        case 1:
                                                            gstatus = "以被约";
                                                            break;
                                                        case 4:
                                                            gstatus = "忙碌中";
                                                            break;
                                                        default:
                                                            gstatus = "其他";
                                                            break;
                                                    }
                                                    showMsg(info.getTerm_name() + "==(" + gunInfo.getGname() + "_" + gstatus + ")========");
                                                    if (ChargingStationBean.GSTATUS_AVAILABLE != gunInfo.getGstatus()) {
                                                        mApplyAvailableParams.remove(info.getTerm_name());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    doApply();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                                if (mIsStop) {
                                    return;
                                }
                                showMsg("查找出错========");
                            }
                        });
    }

    private void doApply() {
        if (mIsStop) {
            return;
        }
        showMsg("开始申请========");
        if (null == mApplyAvailableParams || mApplyAvailableParams.size() <= 0) {
            showMsg("结束申请========");
        }
        for (final String term_name : mApplyAvailableParams.keySet()) {
            if ("网易4号桩".equals(term_name) || "网易5号桩".equals(term_name)) {
                if (!mIsSuper) {
                    continue;
                }
            }
            Map<String, String> params = mApplyAvailableParams.get(term_name);
            showMsg(term_name + "==开始申请========");
            // {"term_id":"1101081071100453","key":"A733E7792BA026D187CE152921E138C1","custom_no":"1000031264","timestamp":1550216059114,"gun_id":"466"}
            params.put("custom_no", TextUtils.isEmpty(mCustomNo) ? sCustomNo : mCustomNo);
            // {"code":1,"msg":"枪离线","data":null}
            NetworkBuilder.build(ChargingStationService.class)
                    .applyChargingStations(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<ChargingStationBean>() {
                                @Override
                                public void accept(ChargingStationBean result) {
                                    if (mIsStop) {
                                        return;
                                    }
                                    showMsg(term_name + "==结束申请========");
                                    if (ChargingStationBean.CODE_SUCCESS == result.getCode()) {
                                        showMsg(term_name + "==申请成功========");
                                        if (null != mHandler) {
                                            mHandler.removeCallbacksAndMessages(null);
                                        }
                                        if (null != mView) {
                                            mView.onChangedChargingStationStatus(term_name);
                                        }
                                    } else {
                                        showMsg(term_name + "==申请失败(" + result.getMsg() + ")========");
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    if (mIsStop) {
                                        return;
                                    }
                                    showMsg(term_name + "==申请出错========");
                                }
                            });
        }
        if (null != mHandler) {
            mCount++;
            mHandler.sendEmptyMessageDelayed(mCount, mIntervalTime);
        }
    }

    private void doCancel() {
        if (mIsStop) {
            return;
        }
        showMsg("开始取消========");
        if (null == mCancelAvailableParams || mCancelAvailableParams.size() <= 0) {
            showMsg("结束取消========");
        }
        for (final String term_name : mCancelAvailableParams.keySet()) {
            Map<String, String> params = mCancelAvailableParams.get(term_name);
            showMsg(term_name + "==开始取消========");
            params.put("custom_no", TextUtils.isEmpty(mCustomNo) ? sCustomNo : mCustomNo);
            NetworkBuilder.build(ChargingStationService.class)
                    .cancelChargingStations(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<ChargingStationBean>() {
                                @Override
                                public void accept(ChargingStationBean result) {
                                    if (mIsStop) {
                                        return;
                                    }
                                    showMsg(term_name + "==结束取消========");
                                    if (ChargingStationBean.CODE_SUCCESS == result.getCode()) {
                                        showMsg(term_name + "==取消成功========");
                                        if (null != mHandler) {
                                            mHandler.removeCallbacksAndMessages(null);
                                        }
                                        if (null != mView) {
                                            mView.onChangedChargingStationStatus(null);
                                        }
                                    } else {
                                        showMsg(term_name + "==取消失败(" + result.getMsg() + ")========");
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    if (mIsStop) {
                                        return;
                                    }
                                    showMsg(term_name + "==取消出错========");
                                }
                            });
        }
    }

    private void showMsg(String msg) {
        if (mIsStop) {
            return;
        }
        if (null != mView) {
            mView.showMsg(msg);
        }
    }
}
