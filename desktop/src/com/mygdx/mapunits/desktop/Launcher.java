package com.mygdx.mapunits.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Launcher extends JFrame {
    private int FRAME_WIDTH = 200;
    private int FRAME_HEIGHT = 350;
    private JButton b;
    private JTextArea[] texts = new JTextArea[12];
    public Launcher(final ArrayList<String> r ) throws FileNotFoundException {
        setLayout(null);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        for(int i = 0;i<texts.length;i++){
            texts[i] = new JTextArea();
            texts[i].setSize(80,30);
            if(i%2==0) {
                texts[i].setEditable(false);
                texts[i].setLocation(5,i*20+5);
            }else{
                texts[i].setLocation(100,(i-1)*20+5);
            }
            if(i==0){
                texts[0].setText("Game Size");
            }else if(i==1){
                texts[1].setText("10");
            }else if(i==2){
                texts[2].setText("Game Speed");
            }else if(i==3){
                texts[3].setText("50");
            }else if(i==4){
                texts[4].setText("Cost Scaling");
            }else if(i==5){
                texts[5].setText("1");
            }else if(i==6){
                texts[6].setText("Defender AI type");
            }else if(i==7){
                texts[7].setText("0");
            }else if(i==8){
                texts[8].setText("Attacker Scale");
            }else if(i==9){
                texts[9].setText("1");
            }else if(i==10){
                texts[10].setText("Name");
            }else if(i==11){
                texts[11].setText("User");
            }
            this.add(texts[i]);
        }
        b = new JButton();
        b.setBounds(40,255,60,40);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                r.add(texts[1].getText());
                r.add(texts[3].getText());
                r.add(texts[5].getText());
                r.add(texts[7].getText());
                r.add(texts[9].getText());
                if(!texts[11].getText().matches("[a-zA-z]*[^a-zA-z]+[a-zA-z]*")){
                    r.add(texts[11].getText());
                }else{
                    r.add("user");
                }
                dispose();
            }
        });
        this.add(b);
        pack();
        setTitle("Launcher");
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                r.add(texts[1].getText());
                r.add(texts[3].getText());
                r.add(texts[5].getText());
                r.add(texts[7].getText());
                r.add(texts[9].getText());
                if(!texts[11].getText().matches("[a-zA-z]*[^a-zA-z]+[a-zA-z]*")){
                    r.add(texts[11].getText());
                }else{
                    r.add("user");
                }

            }
        });
    }

}