
package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalCallDatum {

    @SerializedName("total_duration")
    @Expose
    private Integer totalDuration;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("call_type")
    @Expose
    private String callType;
    @SerializedName("hour")
    @Expose
    private Integer hour;
    @SerializedName("total_calls")
    @Expose
    private Integer totalCalls;

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(Integer totalCalls) {
        this.totalCalls = totalCalls;
    }

}
