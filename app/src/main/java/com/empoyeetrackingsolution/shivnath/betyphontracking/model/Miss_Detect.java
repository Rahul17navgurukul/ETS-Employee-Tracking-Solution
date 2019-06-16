package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class Miss_Detect extends RealmObject {

    String missNum;
    String missDate;
    int missCount;

    public int getMissCount() {
        return missCount;
    }

    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }

    public Miss_Detect() {

    }

    public String getMissNum() {
        return missNum;
    }

    public void setMissNum(String missNum) {
        this.missNum = missNum;
    }

    public String getMissDate() {
        return missDate;
    }

    public void setMissDate(String missDate) {
        this.missDate = missDate;
    }
}
