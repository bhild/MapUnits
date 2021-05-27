package com.mygdx.mapunits.desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Launcher extends JFrame {
    private int FRAME_WIDTH = 200;
    private int FRAME_HEIGHT = 210;
    private JButton b;
    private JTextArea[] texts = new JTextArea[6];
    public Launcher(final ArrayList<Integer> r ) {
        setLayout(null);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        for(int i = 0;i<texts.length;i++){
            texts[i] = new JTextArea();
            texts[i].setSize(80,30);
            if(i%2==0){
                texts[i].setEditable(false);
                if(i==0){
                    texts[i].setText("Game Size");
                }
                else if(i==2){
                    texts[i].setText("Game Speed");
                }
                else if(i==4){
                    texts[i].setText("Cost Scaling");
                }
                texts[i].setLocation(5,i*20+5);
            }else{
                texts[i].setLocation(100,(i-1)*20+5);
            }
            this.add(texts[i]);
        }
        b = new JButton();
        b.setBounds(40,135,60,40);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                r.add(Integer.parseInt(texts[1].getText()));
                r.add(Integer.parseInt(texts[3].getText()));
                r.add(Integer.parseInt(texts[5].getText()));
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

                r.add(Integer.parseInt(texts[1].getText()));
                r.add(Integer.parseInt(texts[3].getText()));
                r.add(Integer.parseInt(texts[5].getText()));

            }
        });
    }

}