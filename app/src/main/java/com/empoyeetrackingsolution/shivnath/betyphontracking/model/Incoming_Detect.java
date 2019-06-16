package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class Incoming_Detect extends RealmObject {



    private String num;
    private String incomingDate;
    private int count;
    boolean test;

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Incoming_Detect() {
    }

    public String getIncomingDate() {
        return incomingDate;
    }

    public void setIncomingDate(String incomingDate) {
        this.incomingDate = incomingDate;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
