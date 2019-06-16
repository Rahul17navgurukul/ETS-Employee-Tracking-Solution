package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import io.realm.RealmObject;

public class locationModel extends RealmObject {

    String longitute;
    String latitude;

    public locationModel() {
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
