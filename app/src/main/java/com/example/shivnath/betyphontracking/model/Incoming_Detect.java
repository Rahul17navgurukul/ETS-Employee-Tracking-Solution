package com.example.shivnath.betyphontracking.model;

import androidx.fragment.app.FragmentActivity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class Incoming_Detect extends RealmObject {



    private String num;
    private String incomingDate;
    private int count;

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
