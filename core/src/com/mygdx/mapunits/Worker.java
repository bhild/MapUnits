package com.mygdx.mapunits;

import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;

public class Worker {
    IntArray path;
    int[] start;
    int[] goal;
    Astar astar;//the pathing algorithm
    int[] pos;//location of worker
    int pathIndex;//where the worker is on its path
    int carryCount = 3;//amount the worker can carry
    boolean hasRes;
    int res;
    long systime;//internal clock
    boolean dir = true;
    boolean emptyHanded = false;//see MapUnits for explation
    public Worker(int[] start,Astar astar,int res){//constructor and initializer
        this.start = start;
        this.goal=new int[]{-1,-1};
        this.astar = astar;
        this.pos = start;
        pathIndex=0;
        this.res = res;
        systime = System.currentTimeMillis();
    }
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
        //uses a try catch because that is eaier and handles expetions well
        //if dir is postive it move twards the goal
        //otherwise it moves twards home
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
    public boolean hasGoal(){//retuns if there is a goal
        return goal[0]!=-1&&goal[1]!=-1;
    }
    public int getRes(){
        return res;//returns resorse type
    }
}
