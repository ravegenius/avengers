package com.jason.avengers.resume.beans;


import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.resume.R;

import static com.jason.avengers.common.configs.ItemViewType.RESUME;

/**
 * Created by jason on 2018/3/21.
 */

public class ResumeBean extends BaseItemBean {

    public static final int ItemViewType = RESUME;
    public static final String HeaderTitle = "工作经历";

    @SerializedName("company")
    private String companyName;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("position")
    private String position;

    @SerializedName("jobContent")
    private String jobContent;

    @SerializedName("jobDescription")
    private String jobDescription;

    @SerializedName("serial")
    private int serial;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
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
        ((TextView) holder.getView(R.id.resume_info_serial)).setText(String.valueOf(getSerial()));
        ((TextView) holder.getView(R.id.resume_info_date)).setText(String.format("%s - %s", getStartDate(), getEndDate()));
        ((TextView) holder.getView(R.id.resume_info_company)).setText(getCompanyName());
        ((TextView) holder.getView(R.id.resume_info_position)).setText(getPosition());
        ((TextView) holder.getView(R.id.resume_info_job_content)).setText(getJobContent());
        ((TextView) holder.getView(R.id.resume_info_job_description)).setText(getJobDescription());
        holder.setViewAction(R.id.resume_info_layout, this);
    }

    public static BaseItemViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.resume_layout_item_view_resume_info);
    }

    public static void onBindHeaderViewHolder(BaseItemViewHolder holder) {
        ((TextView) holder.getView(R.id.header_info_title)).setText(HeaderTitle);
    }
}
