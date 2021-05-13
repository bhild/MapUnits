package com.mygdx.mapunits;

import com.badlogic.gdx.utils.IntArray;

public class Enemy {
    int[] pos;
    Astar astar;
    Worker target;
    int pathIndex;
    long systime;
    IntArray path;
    boolean dir = true;
    public Enemy(int[] pos, Astar astar, Worker target){
        this.pos = pos;
        this.astar = astar;
        this.target = target;
        systime = System.currentTimeMillis();
        pathIndex=0;
    }
    public void genPath(){
        path = astar.getPath(target.getPos()[0], target.getPos()[1],pos[0], pos[1]);
    }
    public void move(){
        //genPath();
        int x = 0;
        int y =0;
        try{
            x = path.get(pathIndex);
            y = path.get(pathIndex + 1);
        }catch (Exception e){
            genPath();
        }
        pathIndex+=2;
        pos = new int[]{x,y};
    }

    public int[] getPos() {
        return pos;
    }
}
