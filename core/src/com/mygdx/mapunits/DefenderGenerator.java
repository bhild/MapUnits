package com.mygdx.mapunits;

public class DefenderGenerator {
    long clock;//internal timer
    int[] location;//location of this node
    long period;//time window to generate
    public DefenderGenerator(long period,int[] location){
        this.period = period;
        this.location = location;
        clock = System.currentTimeMillis();
    }
    public boolean attemptGen(int res,int cost){
        if(res>=cost&&System.currentTimeMillis()-clock>=period){
            //if the period has passed return true and rest clock
            clock = System.currentTimeMillis();
            return true;
        }
        return false;
    }

}
