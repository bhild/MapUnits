package com.mygdx.mapunits;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MapGen {
    private Resource[][] vals;//this is the map arary
    int size  = 20;
    public MapGen(int s){
        size = s;
        vals = new Resource[Gdx.graphics.getWidth()/size][Gdx.graphics.getHeight()/size];
        for(int i = 0; i< Gdx.graphics.getWidth()/size; i++){
            for(int j = 0;j< Gdx.graphics.getHeight()/size;j++){
                int type = 0;
                if((i<vals.length/4||i>vals.length/4*3)||(j<vals[i].length/4||j>vals[i].length/4*3)){//used to find outside the middle
                    double rand = Math.random();//creates the random number used for resource gen
                    //both res types have the same odds about 5%
                    if(rand<0.05){
                        type=1;
                    }else if(rand<0.1){
                        type = 2;
                    }else{
                        type = 0;
                    }
                }
                vals[i][j] = new Resource(type);//adds the res type to the array
            }
        }
    }
    public Resource[][] getVals(){
        return vals;
    }//returns vals

    public void setVal(int[] pos, int type){
        vals[pos[0]][pos[1]].setType(type);
    }//sets the value of a node
    //as of now it is unused as a directly access the vals array
}
