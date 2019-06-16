package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class Outgoing_Detect extends RealmObject {

    String outNumer;
    String outDate;
    int outCount;

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public Outgoing_Detect() {

    }

    public String getOutNumer() {
        return outNumer;
    }

    public void setOutNumer(String outNumer) {
        this.outNumer = outNumer;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }
}
