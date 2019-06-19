
package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {

    @SerializedName("call_duration_category")
    @Expose
    private CallDurationCategory callDurationCategory;
    @SerializedName("user_call_data")
    @Expose
    private List<UserCallDatum> userCallData = null;
    @SerializedName("total_call_data")
    @Expose
    private List<TotalCallDatum> totalCallData = null;

    public CallDurationCategory getCallDurationCategory() {
        return callDurationCategory;
    }

    public void setCallDurationCategory(CallDurationCategory callDurationCategory) {
        this.callDurationCategory = callDurationCategory;
    }

    public List<UserCallDatum> getUserCallData() {
        return userCallData;
    }

    public void setUserCallData(List<UserCallDatum> userCallData) {
        this.userCallData = userCallData;
    }

    public List<TotalCallDatum> getTotalCallData() {
        return totalCallData;
    }

    public void setTotalCallData(List<TotalCallDatum> totalCallData) {
        this.totalCallData = totalCallData;
    }

}
