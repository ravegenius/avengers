package com.jason.avengers.user.beans;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.jason.avengers.R;
import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.common.configs.GlobalConfig;
import com.jason.avengers.common.configs.ItemViewTypeConfig;
import com.jason.avengers.resume.beans.ResumeBean;

import java.util.List;

/**
 * Created by jason on 2018/3/30.
 */

public class UserBean extends BaseItemBean {

    public static final int ItemViewType = ItemViewTypeConfig.USER;
    public static final String HeaderTitle = "个人信息";

    @SerializedName("id")
    private long userId;

    @SerializedName("name")
    private String username;

    @SerializedName("sex")
    private int sex;

    @SerializedName("age")
    private int age;

    @SerializedName("workAge")
    private int workAge;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("mark")
    private String mark;

    @SerializedName("resumes")
    private List<ResumeBean> resumes;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWorkAge() {
        return workAge;
    }

    public void setWorkAge(int workAge) {
        this.workAge = workAge;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public List<ResumeBean> getResumes() {
        return resumes;
    }

    public void setResumes(List<ResumeBean> resumes) {
        this.resumes = resumes;
    }

    @Override
    public int getItemViewType() {
        return ItemViewType;
    }

    @Override
    public void onBindViewHolder(final BaseItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView) holder.getView(R.id.user_info_name)).setText(getUsername());
        holder.getView(R.id.user_info_sex);
        ((TextView) holder.getView(R.id.user_info_age)).setText(holder.getContext().getString(R.string.user_age, getAge()));
        ((TextView) holder.getView(R.id.user_info_work_age)).setText(holder.getContext().getString(R.string.user_work_age, getWorkAge()));
        ((TextView) holder.getView(R.id.user_info_phone)).setText(getPhone());
        ((TextView) holder.getView(R.id.user_info_email)).setText(getEmail());
        Glide.with(holder.getContext())
                .load(getAvatar()).apply(GlobalConfig.AvatarOptions)
                .into((ImageView) holder.getView(R.id.user_info_avatar));

        holder.setViewAction(R.id.user_info_phone, this);
        holder.setViewAction(R.id.user_info_email, this);
    }

    public static BaseItemViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.layout_item_view_user_info);
    }

    public static void onBindHeaderViewHolder(BaseItemViewHolder holder) {
        ((TextView) holder.getView(R.id.header_info_title)).setText(HeaderTitle);
    }
}
