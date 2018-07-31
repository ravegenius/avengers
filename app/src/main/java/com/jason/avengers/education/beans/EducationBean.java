package com.jason.avengers.education.beans;

import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.jason.avengers.R;
import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.common.configs.ItemViewTypeConfig;

/**
 * Created by jason on 2018/4/3.
 */

public class EducationBean extends BaseItemBean {

    public static final int ItemViewType = ItemViewTypeConfig.EDUCATION;
    public static final String HeaderTitle = "教育背景";

    @SerializedName("school")
    private String schoolName;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("degree")
    private String degree;

    @SerializedName("serial")
    private int serial;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    @Override
    public int getItemViewType() {
        return ItemViewType;
    }

    @Override
    public void onBindViewHolder(BaseItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView) holder.getView(R.id.education_info_serial)).setText(String.valueOf(getSerial()));
        ((TextView) holder.getView(R.id.education_info_date)).setText(String.format("%s - %s", getStartDate(), getEndDate()));
        ((TextView) holder.getView(R.id.education_info_school)).setText(getSchoolName());
        ((TextView) holder.getView(R.id.education_info_degree)).setText(getDegree());
    }

    public static BaseItemViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.layout_item_view_education_info);
    }

    public static void onBindHeaderViewHolder(BaseItemViewHolder holder) {
        ((TextView) holder.getView(R.id.header_info_title)).setText(HeaderTitle);
    }
}
