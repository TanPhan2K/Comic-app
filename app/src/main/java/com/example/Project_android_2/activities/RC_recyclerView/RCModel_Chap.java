package com.example.Project_android_2.activities.RC_recyclerView;

import com.example.Project_android_2.models.chapter;

public class RCModel_Chap implements Comparable<RCModel_Chap>{
    private chapter Chap;

    public RCModel_Chap(chapter chap) {
        Chap = chap;
    }

    public chapter getChap() {
        return Chap;
    }

    public void setChap(chapter chap) {
        Chap = chap;
    }

    @Override
    public int compareTo(RCModel_Chap other) {
        long thisIndex = Long.parseLong(this.Chap.getIndex());
        long otherIndex = Long.parseLong(other.getChap().getIndex());
        return Long.compare(thisIndex, otherIndex);
    }
}