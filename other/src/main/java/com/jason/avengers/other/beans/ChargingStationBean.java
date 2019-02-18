package com.jason.avengers.other.beans;

import com.jason.avengers.common.base.BaseBean;

import java.util.List;

public class ChargingStationBean<T extends BaseBean> extends BaseBean {

    public static int CODE_SUCCESS = 0;
    public static int GSTATUS_AVAILABLE = 2;

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public class ChargingStationUser extends BaseBean {

        private int custom_type;
        private String custom_no;
        private int pay_schema;

        public int getCustom_type() {
            return custom_type;
        }

        public void setCustom_type(int custom_type) {
            this.custom_type = custom_type;
        }

        public String getCustom_no() {
            return custom_no;
        }

        public void setCustom_no(String custom_no) {
            this.custom_no = custom_no;
        }

        public int getPay_schema() {
            return pay_schema;
        }

        public void setPay_schema(int pay_schema) {
            this.pay_schema = pay_schema;
        }
    }

    public class ChargingStationCollect extends BaseBean {

        private int charge_collect;
        private List<ChargingStationInfo> charge_info;

        public int getCharge_collect() {
            return charge_collect;
        }

        public void setCharge_collect(int charge_collect) {
            this.charge_collect = charge_collect;
        }

        public List<ChargingStationInfo> getCharge_info() {
            return charge_info;
        }

        public void setCharge_info(List<ChargingStationInfo> charge_info) {
            this.charge_info = charge_info;
        }
    }

    public class ChargingStationInfo extends BaseBean {

        private String term_id;
        private String term_name;
        private int term_type;
        private List<ChargingStationGunInfo> gun_info;

        public String getTerm_id() {
            return term_id;
        }

        public void setTerm_id(String term_id) {
            this.term_id = term_id;
        }

        public String getTerm_name() {
            return term_name;
        }

        public void setTerm_name(String term_name) {
            this.term_name = term_name;
        }

        public int getTerm_type() {
            return term_type;
        }

        public void setTerm_type(int term_type) {
            this.term_type = term_type;
        }

        public List<ChargingStationGunInfo> getGun_info() {
            return gun_info;
        }

        public void setGun_info(List<ChargingStationGunInfo> gun_info) {
            this.gun_info = gun_info;
        }
    }

    public class ChargingStationGunInfo extends BaseBean {

        private String gid;
        private String gname;
        private int gstatus;
        private String ocnum;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getGstatus() {
            return gstatus;
        }

        public void setGstatus(int gstatus) {
            this.gstatus = gstatus;
        }

        public String getOcnum() {
            return ocnum;
        }

        public void setOcnum(String ocnum) {
            this.ocnum = ocnum;
        }
    }
}
