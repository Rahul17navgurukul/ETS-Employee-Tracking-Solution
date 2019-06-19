
package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCallDatum {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("prospect_number")
    @Expose
    private String prospectNumber;
    @SerializedName("call_type")
    @Expose
    private String callType;
    @SerializedName("talk_time")
    @Expose
    private Integer talkTime;
    @SerializedName("start_datetime")
    @Expose
    private String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProspectNumber() {
        return prospectNumber;
    }

    public void setProspectNumber(String prospectNumber) {
        this.prospectNumber = prospectNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Integer getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(Integer talkTime) {
        this.talkTime = talkTime;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

}
