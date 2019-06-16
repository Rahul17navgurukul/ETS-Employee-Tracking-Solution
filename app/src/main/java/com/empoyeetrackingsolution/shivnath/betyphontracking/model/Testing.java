package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class Testing extends RealmObject {

    boolean demo;

    public Testing() {
    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }
}
