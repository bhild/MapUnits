package com.mygdx.mapunits;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tests {
    @Test
    public void isCloseTest0(){
        int[][] testVals = {{0,0},{1,1}};
        assertEquals(true,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest1(){//case is impossible in actual run
        int[][] testVals = {{1,1},{1,1}};
        assertEquals(false,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest2(){
        int[][] testVals = {{1,0},{1,1}};
        assertEquals(true,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest3(){
        int[][] testVals = {{0,1},{1,1}};
        assertEquals(true,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest4(){
        int[][] testVals = {{0,0},{2,2}};
        assertEquals(false,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest5(){
        int[][] testVals = {{3,3},{1,1}};
        assertEquals(false,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void isCloseTest6(){
        int[][] testVals = {{2,0},{0,1}};
        assertEquals(false,isClose(testVals[0],testVals[1]));
    }
    @Test
    public void nameIsValid0(){
        String name = "User";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid1(){
        String name = "Bran";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid2(){
        String name = "UngaBunga";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid3(){
        String name = "Hello";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid4(){
        String name = "FUNNI";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid5(){
        String name = "NaMe";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsValid6(){
        String name = "MEEEEEEEEEEEEEEEEEEEEEE";
        assertEquals(true,nameValid(name));
    }
    @Test
    public void nameIsNotValid0(){
        String name = "User 1";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid1(){
        String name = "User0";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid2(){
        String name = "Usern't";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid3(){
        String name = "User-9";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid4(){
        String name = "User-b";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid5(){
        String name = "User eee";
        assertEquals(false,nameValid(name));
    }
    @Test
    public void nameIsNotValid6(){
        String name = "User,e";
        assertEquals(false,nameValid(name));
    }
    //copu of isClose for testing
    //is close can not be static so this is a copy
    private boolean isClose(int[] pos, int[] posTarget){//used to determine if an element is close to another on the grid
        //meaning that the target is on a side or diagonal from the source
        int closeX = Math.abs(pos[0]-posTarget[0]);//distance on x
        int closeY = Math.abs(pos[1]-posTarget[1]);//distance on y
        if((closeX==1&&closeY==0)||(closeX==0&&closeY==1)||(closeX==1&&closeY==1)){//needs to be done this way because of diagonals
            return true;//the two objects are close
        }
        return false;//the two objects are not close
    }
    //copy of name validation regex
    //this is in another package and cant be inported
    private boolean nameValid(String name){
        return !name.matches("[a-zA-z]*[^a-zA-z]+[a-zA-z]*");
    }
}
