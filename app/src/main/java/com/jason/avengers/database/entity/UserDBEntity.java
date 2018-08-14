package com.jason.avengers.database.entity;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;

@Entity
@NameInDb("USER")
public class UserDBEntity extends BaseDBEntity {

    @Index
    @NameInDb("USERID")
    private long userId;

    @Index
    @NameInDb("USERNAME")
    private String username;

    @NameInDb("SEX")
    private int sex;

    @NameInDb("AGE")
    private int age;

    @NameInDb("WORKAGE")
    private int workAge;

    @NameInDb("AVATAR")
    private String avatar;

    @Index
    @NameInDb("EMAIL")
    private String email;

    @Index
    @NameInDb("PHONE")
    private String phone;

    @Transient
    private String mark;

    @Backlink
    private ToMany<ResumeDBEntity> resumes;

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

    public ToMany<ResumeDBEntity> getResumes() {
        return resumes;
    }

    public void setResumes(ToMany<ResumeDBEntity> resumes) {
        this.resumes = resumes;
    }
}
