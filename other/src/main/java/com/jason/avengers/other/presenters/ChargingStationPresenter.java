package com.jason.avengers.other.presenters;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.other.services.ChargingStationService;
import com.jason.avengers.other.views.ChargingStationView;
import com.jason.core.network.NetworkBuilder;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ChargingStationPresenter extends BasePresenter<ChargingStationView> {

    private ChargingStationView mView;

    @Override
    protected void attach(ChargingStationView view) {
        mView = view;
    }

    @Override
    protected void detach() {
        mView = null;
    }

    public void doLogin() {
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
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                            }
                        });
    }

    public void doSearch() {
        String term_id = "1101081071";
        String custom_no = "1000031264";
        String timestamp = "1550215638339";
        String key = "0264140899E8FACA17BE2BCA6476E47E";
        // {"term_id":"1101081071","key":"0264140899E8FACA17BE2BCA6476E47E","custom_no":"1000031264","timestamp":1550215638339}
        Map<String, String> params = new HashMap<>(4);
        params.put("term_id", term_id);
        params.put("custom_no", custom_no);
        params.put("timestamp", timestamp);
        params.put("key", key);
        // {"code":0,"msg":"","data":{"open_time":"","station_pic":"","location":"北京市市辖区海淀区北京市海淀区软件园南街","parking_fee":[],"charge_collect":2,"charge_info":[{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"466","gname":"1","gstatus":2,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100453","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":1,"term_name":"网易5号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1294","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101280","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易9号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1295","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101281","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易10号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1296","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101282","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易11号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1297","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101283","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易12号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1298","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101284","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易13号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1299","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101285","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易14号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1300","gname":"1","gstatus":1,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101286","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易15号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"1301","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071101287","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易16号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"462","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100449","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易1号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"463","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100450","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易2号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"464","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100451","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易3号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"465","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100452","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":1,"term_name":"网易4号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"467","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100454","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易7号桩"},{"service_charge":[{"ssection":"00:00--23:59","sprice":"0.00"}],"location":"北京市市辖区海淀区海淀区软件园南街","gun_info":[{"gid":"468","gname":"1","gstatus":4,"ocnum":""}],"time_elec":[{"eprice":"0.00","esection":"00:00--23:59"}],"time_elec_count":1,"term_id":"1101081071100455","charge_order":1,"gun_count":1,"charge_order_remind":1,"service_charge_count":1,"term_type":2,"term_name":"网易8号桩"}],"dc_charge_count":2,"longitude":116.28211,"parking_fee_count":0,"latitude":40.04957,"ac_charge_count":13,"payment_method":"充电卡和在线支付","operator":"奇才智能","term_name":"BJ.0001网易充电桩"}}
        NetworkBuilder.build(ChargingStationService.class)
                .getChargingStations(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                            }
                        });
    }

    public void doApply() {
        // 网易4号桩 1101081071100452 465
        // 网易5号桩 1101081071100453 466
        // 网易7号桩 1101081071100454 467
        // 网易9号桩 1101081071101280 1294
        // 网易13号桩 1101081071101284 1298
        String custom_no = "1000031264";
        String term_id = "1101081071100452";
        String gun_id = "465";
        String timestamp = "1550216059114";
        String key = "A733E7792BA026D187CE152921E138C1";
        // {"term_id":"1101081071100453","key":"A733E7792BA026D187CE152921E138C1","custom_no":"1000031264","timestamp":1550216059114,"gun_id":"466"}
        Map<String, String> params = new HashMap<>(4);
        params.put("custom_no", custom_no);
        params.put("term_id", term_id);
        params.put("gun_id", gun_id);
        params.put("timestamp", timestamp);
        params.put("key", key);
        // {"code":1,"msg":"枪离线","data":null}
        NetworkBuilder.build(ChargingStationService.class)
                .applyChargingStations(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                            }
                        });
    }
}
