package com.mygdx.mapunits;

import com.badlogic.gdx.utils.IntArray;

public class Defender {
    int[] pos;
    Astar astar;
    Worker target;
    int pathIndex;
    IntArray path;
    boolean dir = true;
    public Defender(int[] pos, Astar astar, Worker target){
        this.pos = pos;
        this.astar = astar;
        this.target = target;
        pathIndex=0;
    }
    public void genPath(){
        path = astar.getPath(target.getPos()[0], target.getPos()[1],pos[0], pos[1]);
    }
    public void move(){
        if(path.size/2<pathIndex){
            genPath();
            pathIndex=0;
        }
        int x = 0;
        int y =0;
        if(path.size>2){
            x = path.get(pathIndex);
            y = path.get(pathIndex+1);
            pathIndex+=2;
            pos = new int[]{x,y};
        }
    }
    public void wander(MapGen m){
    }

    public int[] getPos() {
        return pos;
    }
}
