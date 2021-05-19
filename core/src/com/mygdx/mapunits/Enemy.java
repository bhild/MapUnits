package com.mygdx.mapunits;

import com.badlogic.gdx.utils.IntArray;

public class Enemy {
    int[] pos;
    Astar astar;
    Worker target;
    int pathIndex;
    IntArray path;
    boolean dir = true;
    boolean isTarget = false;
    long time;
    public Enemy(int[] pos, Astar astar, Worker target){
        this.pos = pos;
        this.time = System.currentTimeMillis();
        this.astar = astar;
        this.target = target;
        pathIndex=0;
    }
    public void genPath(){
        path = astar.getPath(target.getPos()[0], target.getPos()[1],pos[0], pos[1]);
    }
    public void move(){
        //makes a b-line to the target
        //works almost the same a workers
        time = System.currentTimeMillis();
        if(path.size/2<pathIndex){
            //at the half way point makes a new path and resets path index
            genPath();
            pathIndex=0;
        }
        int x = 0;
        int y =0;
        if(path.size>2){//error handeling + new pos
            x = path.get(pathIndex);
            y = path.get(pathIndex+1);
            pathIndex+=2;
            pos = new int[]{x,y};
        }
    }
    public long getTime(){
        return time;
    }
    public void setTime(long l){
        time = l;
    }

    public int[] getPos() {
        return pos;
    }
}