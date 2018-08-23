package com.jason.avengers.common.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.TargetIdProperty;
import io.objectbox.relation.ToOne;

/**
 * Created by jason on 2018/7/24.
 */
@Entity
@NameInDb("RESUME")
public class ResumeDBEntity extends BaseDBEntity {

    @Index
    @NameInDb("COMPANY")
    private String companyName;

    @NameInDb("STARTDATE")
    private String startDate;

    @NameInDb("ENDDATE")
    private String endDate;

    @NameInDb("POSITION")
    private String position;

    @NameInDb("JOBCONTENT")
    private String jobContent;

    @NameInDb("JOBDESCRIPTION")
    private String jobDescription;

    @NameInDb("SERIAL")
    private int serial;

    @TargetIdProperty("USERID")
    private ToOne<UserDBEntity> user;

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

    public ToOne<UserDBEntity> getUser() {
        return user;
    }

    public void setUser(ToOne<UserDBEntity> user) {
        this.user = user;
    }
}
