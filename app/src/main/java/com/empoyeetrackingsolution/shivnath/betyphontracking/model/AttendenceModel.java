package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class AttendenceModel extends RealmObject {

    String onDutyTime;
    String offDutyTime;
    String sartDate;
    String endDate;

    public AttendenceModel() {

    }

    public String getSartDate() {
        return sartDate;
    }

    public void setSartDate(String sartDate) {
        this.sartDate = sartDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOffDutyTime() {
        return offDutyTime;
    }

    public void setOffDutyTime(String offDutyTime) {
        this.offDutyTime = offDutyTime;
    }

    public String getOnDutyTime() {
        return onDutyTime;
    }

    public void setOnDutyTime(String onDutyTime) {
        this.onDutyTime = onDutyTime;
    }
}
