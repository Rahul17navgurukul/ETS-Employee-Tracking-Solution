package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard {



        @SerializedName("user_call_data")
        @Expose
        private List<UserCallDatum> userCallData = null;
        @SerializedName("total_call_data")
        @Expose
        private TotalCallData totalCallData;

        public List<UserCallDatum> getUserCallData() {
            return userCallData;
        }

        public void setUserCallData(List<UserCallDatum> userCallData) {
            this.userCallData = userCallData;
        }

        public TotalCallData getTotalCallData() {
            return totalCallData;
        }

        public void setTotalCallData(TotalCallData totalCallData) {
            this.totalCallData = totalCallData;
        }



    public class TotalCallData {

        @SerializedName("total_followup_calls")
        @Expose
        private Integer totalFollowupCalls;
        @SerializedName("total_missed_calls")
        @Expose
        private Integer totalMissedCalls;
        @SerializedName("total_incoming_calls")
        @Expose
        private Integer totalIncomingCalls;
        @SerializedName("total_overdues_calls")
        @Expose
        private Integer totalOverduesCalls;
        @SerializedName("total_not_connected_calls")
        @Expose
        private Integer totalNotConnectedCalls;
        @SerializedName("total_calls")
        @Expose
        private Integer totalCalls;
        @SerializedName("total_outgoing_calls")
        @Expose
        private Integer totalOutgoingCalls;

        public Integer getTotalFollowupCalls() {
            return totalFollowupCalls;
        }

        public void setTotalFollowupCalls(Integer totalFollowupCalls) {
            this.totalFollowupCalls = totalFollowupCalls;
        }

        public Integer getTotalMissedCalls() {
            return totalMissedCalls;
        }

        public void setTotalMissedCalls(Integer totalMissedCalls) {
            this.totalMissedCalls = totalMissedCalls;
        }

        public Integer getTotalIncomingCalls() {
            return totalIncomingCalls;
        }

        public void setTotalIncomingCalls(Integer totalIncomingCalls) {
            this.totalIncomingCalls = totalIncomingCalls;
        }

        public Integer getTotalOverduesCalls() {
            return totalOverduesCalls;
        }

        public void setTotalOverduesCalls(Integer totalOverduesCalls) {
            this.totalOverduesCalls = totalOverduesCalls;
        }

        public Integer getTotalNotConnectedCalls() {
            return totalNotConnectedCalls;
        }

        public void setTotalNotConnectedCalls(Integer totalNotConnectedCalls) {
            this.totalNotConnectedCalls = totalNotConnectedCalls;
        }

        public Integer getTotalCalls() {
            return totalCalls;
        }

        public void setTotalCalls(Integer totalCalls) {
            this.totalCalls = totalCalls;
        }

        public Integer getTotalOutgoingCalls() {
            return totalOutgoingCalls;
        }

        public void setTotalOutgoingCalls(Integer totalOutgoingCalls) {
            this.totalOutgoingCalls = totalOutgoingCalls;
        }

    }


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
        @SerializedName("timestamp")
        @Expose
        private String timestamp;

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

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

    }
}
