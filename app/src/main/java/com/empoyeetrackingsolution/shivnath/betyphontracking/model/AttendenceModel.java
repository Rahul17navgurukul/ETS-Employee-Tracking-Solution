package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class AttendenceModel {

    String creation_datetime;
    String user_id;
    String logout_time;
    String full_name;
    String login_time;
    String status;

    String onDutyTime;
    String offDutyTime;
    String sartDate;
    String endDate;

    public AttendenceModel() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCreation_datetime() {
        return creation_datetime;
    }

    public void setCreation_datetime(String creation_datetime) {
        this.creation_datetime = creation_datetime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }
}
