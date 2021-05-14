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
    public Defender(int[] pos, Astar astar, Enemy target){
        this.pos = pos;
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
    public void move(MapGen m){
        if(target==null&&path.size-1==pathIndex){
            wander(m);
        }else if(target==null){
            genPathPos();
        }else if(path.size/2<pathIndex){
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
        Resource[][] vals = m.getVals();
        Random r = new Random();
        int i = r.nextInt(vals.length);
        int j = r.nextInt(vals[i].length);
        while(!(i<vals.length/4||i>vals.length/4*3)||(j<vals[i].length/4||j>vals[i].length/4*3)){
            i = r.nextInt(vals.length);
            j = r.nextInt(vals[i].length);
        }
        targetPos = new int[]{i,j};
    }

    public int[] getPos() {
        return pos;
    }
}
