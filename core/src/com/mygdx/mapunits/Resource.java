package com.mygdx.mapunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class Resource {
    int type;
    Color color;
    int resorseCount;
    int workers = 0;
    public Resource(int type){
        this.type = type;
        if(type!=-1){//if type = -1 it is empty
            resorseCount = (new Random().nextInt(11)+5)*15;
            //the number of times a worker can mine this
            //they work in 3s so it goes from 15 to 45 times
        }
        setColor();
    }
    public void reduce(int amount){
        resorseCount-=amount;
    }//lowers the resorse count
    public int getResorseCount(){
        return resorseCount;
    }//returns the resorse count
    public void setType(int type){
        this.type = type;//sets the type of resorse
    }
    private void setColor(){//will be updated later
        //retruns the color the node should be on the map
        if(type==1){color=Color.GOLD;}
        else if(type==2){color = Color.BLUE;}
        else if(type==-2)color=Color.PURPLE;
        else if(type==-3)color=Color.PINK;
        else
            color=Color.GRAY;//default color
    }
    public int getType(){return type;}
    public Color getColor(){setColor();return color;}
    public boolean equals(Resource r){//not used, determines of one res type equals another
        if(this.type == r.getType()){
            return true;
        }
        return false;
    }
}
