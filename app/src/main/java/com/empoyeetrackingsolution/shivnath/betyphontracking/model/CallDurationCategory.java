
package com.empoyeetrackingsolution.shivnath.betyphontracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallDurationCategory {

    @SerializedName("0")
    @Expose
    private Integer _0;
    @SerializedName("11-30")
    @Expose
    private Integer _1130;
    @SerializedName("0-10")
    @Expose
    private Integer _010;
    @SerializedName("61-180")
    @Expose
    private Integer _61180;
    @SerializedName("31-60")
    @Expose
    private Integer _3160;
    @SerializedName("180+")
    @Expose
    private Integer _180;

    public Integer get0() {
        return _0;
    }

    public void set0(Integer _0) {
        this._0 = _0;
    }

    public Integer get1130() {
        return _1130;
    }

    public void set1130(Integer _1130) {
        this._1130 = _1130;
    }

    public Integer get010() {
        return _010;
    }

    public void set010(Integer _010) {
        this._010 = _010;
    }

    public Integer get61180() {
        return _61180;
    }

    public void set61180(Integer _61180) {
        this._61180 = _61180;
    }

    public Integer get3160() {
        return _3160;
    }

    public void set3160(Integer _3160) {
        this._3160 = _3160;
    }

    public Integer get180() {
        return _180;
    }

    public void set180(Integer _180) {
        this._180 = _180;
    }

}
