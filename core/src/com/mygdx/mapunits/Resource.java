package com.mygdx.mapunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class Resource {
    int type;
    Color color;
    int resorseCount;
    int workers = 0;
    int[] pos = new int[]{};

    public Resource(int type){
        this.type = type;
        if(type!=-1){
            resorseCount = new Random().nextInt(100)+70;
        }
        setColor();
    }
    public Resource(int type,int[] pos){
        this.type = type;
        this.pos = pos;
        if(type!=-1){
            resorseCount = (new Random().nextInt(11)+5)*15;
        }
        setColor();
    }
    public void reduce(int amount){
        resorseCount-=amount;
    }
    public int getResorseCount(){
        return resorseCount;
    }
    public int[] getPos(){
        return pos;
    }
    public void setType(int type){
        this.type = type;
    }
    private void setColor(){
        if(type==1){color=Color.GOLD;}
        else if(type==2){color = Color.BLUE;}
        else if(type==-2)color=Color.PURPLE;
        else
            color=Color.GRAY;
    }
    public int getType(){return type;}
    public Color getColor(){setColor();return color;}
    public boolean equals(Resource r){
        if(this.type == r.getType()){
            return true;
        }
        return false;
    }
}
