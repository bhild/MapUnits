package com.mygdx.mapunits;

import com.badlogic.gdx.utils.IntArray;

import java.util.Random;

public class Defender {
    int[] pos;
    Astar astar;
    Enemy target;
    int pathIndex;
    IntArray path;
    int[] targetPos;
    boolean dir = true;
    long time;
    public Defender(int[] pos, Astar astar, Enemy target){
        this.pos = pos;
        this.time = System.currentTimeMillis();
        this.astar = astar;
        this.target = target;
        pathIndex=0;
        targetPos = null;
    }
    public void genPath(){
        path = astar.getPath(target.getPos()[0], target.getPos()[1],pos[0], pos[1]);
    }
    public void genPathPos(){
        path = astar.getPath(targetPos[0], targetPos[1],pos[0], pos[1]);
    }
    public void setTarget(Enemy target){
        this.target=target;
    }
    public void move(){
        //if the defender has a target
        if(path==null){
            genPath();//makes a path if there is none
        }
        else if(path.size/2<pathIndex){
            //if the defender has passed the 1/2 way point make new path and reset path index
            genPath();
            pathIndex=0;
        }
        //will never need to reach the end of a path
        //so there is no backtracking like the workers
        int x = 0;
        int y =0;
        if(path.size>2){
            x = path.get(pathIndex);
            y = path.get(pathIndex+1);
            pathIndex+=2;
            pos = new int[]{x,y};
        }
        setTime(System.currentTimeMillis());
    }
    public void move(MapGen m){
        //no target
        if(targetPos==null){
            //calls wander to select a pos then makes a path
            //runs if the target pos is null
            wander(m);
            genPathPos();
        }else if(path.size-2==pathIndex){
            //if the path length is reached makes a new path as describe above
            //then resets path index
            wander(m);
            pathIndex=0;
            genPathPos();
        }
        int x = 0;
        int y =0;
        if(path.size>2){
            x = path.get(pathIndex);
            y = path.get(pathIndex+1);
            pathIndex+=2;
            pos = new int[]{x,y};
        }
        //resets internal clock here
        setTime(System.currentTimeMillis());
    }
    public void wander(MapGen m){
        //selects a random spot
        //if it is in the middle and not obstructed return that location
        Resource[][] vals = m.getVals();
        Random r = new Random();
        int i = r.nextInt(vals.length);
        int j = r.nextInt(vals[i].length);
        while((i<vals.length/4||i>vals.length/4*3)||(j<vals[i].length/4||j>vals[i].length/4*3)){
            i = r.nextInt(vals.length);
            j = r.nextInt(vals[i].length);
        }
        targetPos = new int[]{i,j};
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