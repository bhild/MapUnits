package com.mygdx.mapunits;

public class DefenderGenerator {
    long clock;//internal timer
    int[] location;//location of this node
    long period;//time window to generate
    long delay;
    public DefenderGenerator(long period,int[] location,long delay){
        this.period = period;
        this.location = location;
        clock = System.currentTimeMillis();
        this.delay = delay;
    }
    public boolean attemptGen(int res,int cost){
        if(res>=cost&&System.currentTimeMillis()-clock>=period){
            //if the period has passed return true and rest clock
            clock = System.currentTimeMillis();
            period+=(Math.random()<0.5)?0:delay;
            return true;
        }
        return false;
    }

}
