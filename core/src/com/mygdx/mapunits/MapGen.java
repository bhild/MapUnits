package com.mygdx.mapunits;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MapGen {
    private Resource[][] vals;
    ArrayList<ArrayList<Resource>> res;
    int size  = 3;
    public MapGen(){
        res = new ArrayList<ArrayList<Resource>>();
        res.add(new ArrayList<Resource>());
        res.add(new ArrayList<Resource>());
        res.add(new ArrayList<Resource>());
        vals = new Resource[Gdx.graphics.getWidth()/size][Gdx.graphics.getHeight()/size];
        for(int i = 0; i< Gdx.graphics.getWidth()/size; i++){
            for(int j = 0;j< Gdx.graphics.getHeight()/size;j++){
                double rand = Math.random();
                int type = 0;
                if((i<vals.length/4||i>vals.length/4*3)||(j<vals[i].length/4||j>vals[i].length/4*3)){
                    if(rand<0.05){
                        type=1;
                    }else if(rand<0.1){
                        type = 2;
                    }else{
                        type = 0;
                    }
                }
                vals[i][j] = new Resource(type,new int[]{i,j});
                res.get(type).add(vals[i][j]);
            }
        }
    }
    public Resource[][] getVals(){
        return vals;
    }

    /*public int[] resPos(Worker w){
        //System.out.println(res.get(w.res).size());
        Random rand = new Random();
        int r = rand.nextInt(res.get(w.res).size());
        while(res.get(w.res).get(r).getResorseCount()==0){
            res.get(w.res).remove(r);
            r = rand.nextInt(res.get(w.res).size());
        }
        int[] spot = res.get(w.res).get(r).getPos();
        return spot;
    }*/
    public ArrayList<Resource> resPos(Worker w){
        Collections.shuffle(res.get(w.res));
        return res.get(w.res);
    }
    public void setVal(int[] pos, int type){
        vals[pos[0]][pos[1]].setType(type);
    }
}
