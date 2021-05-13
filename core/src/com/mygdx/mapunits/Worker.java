package com.mygdx.mapunits;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;

public class Worker {
    IntArray path;
    int[] start;
    int[] goal;
    Astar astar;
    int[] pos;
    int pathIndex;
    int carryCount = 3;
    boolean hasRes;
    int res;
    long systime;
    boolean dir = true;
    boolean emptyHanded = false;
    public Worker(int[] start,int[] goal,Astar astar,int res){
        this.start = start;
        this.goal=goal;
        this.astar = astar;
        this.pos = goal;
        pathIndex=0;
        this.res = res;
        systime = System.currentTimeMillis();
    }public Worker(int[] start,Astar astar,int res){
        this.start = start;
        this.goal=new int[]{-1,-1};
        this.astar = astar;
        this.pos = start;
        pathIndex=0;
        this.res = res;
        systime = System.currentTimeMillis();
    }
    /*public boolean isDiagonal(){
        boolean returnVal = false;
        if (pathIndex+3<path.size&&dir){
            returnVal = Math.abs(path.get(pathIndex+2)-path.get(pathIndex))==1&&Math.abs(path.get(pathIndex+3)-path.get(pathIndex+1))==1;
        }
        else if(pathIndex-3>0&&!dir){
            returnVal = Math.abs(path.get(pathIndex-2)-path.get(pathIndex))==1&&Math.abs(path.get(pathIndex-1)-path.get(pathIndex+1))==1;
        }
        return returnVal;
    }*/
    public long getTime(){
        return systime;
    }
    public void setTime(long time){
        systime = time;
    }
    public boolean isHasRes(){
        return hasRes;
    }
    public void invertHas(){
        hasRes = !hasRes;
    }
    public IntArray getPath(){
        return path;
    }
    public void setGoal(int[] goal) {
        this.goal = goal;
    }
    public int[] getGoal(){
        return goal;
    }
    public int[] getPos(){
        return pos;
    }
    public void genPath(){
        path = astar.getPath(goal[0], goal[1],start[0], start[1]);
    }
    public void updateAStar(Astar a){
        astar = a;
    }
    public void move(){
        int x = 0;
        int y =0;
        try{
            x = path.get(pathIndex);
            y = path.get(pathIndex + 1);
        }catch (Exception e){
            dir = !dir;
            pathIndex+=(dir)?2:-2;
            x = path.get(pathIndex);
            y = path.get(pathIndex + 1);
            pathIndex+=(dir)?-2:2;
        }
        pathIndex+=(dir)?2:-2;
        pos = new int[]{x,y};
    }
    public boolean hasGoal(){
        return goal[0]!=-1&&goal[1]!=-1;
    }
    public int getRes(){
        return res;
    }
}
