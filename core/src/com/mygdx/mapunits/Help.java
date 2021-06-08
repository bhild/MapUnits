package com.mygdx.mapunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Help extends JFrame {
    private int FRAME_WIDTH = 950;
    private int FRAME_HEIGHT = 330;
    private JTextArea help = new JTextArea(0,0);
    private JTextArea leader = new JTextArea();
    private JScrollPane winners = new JScrollPane(leader);
    private JScrollPane scroll = new JScrollPane(help);
    public Help(InputStream f,InputStream l) {
        setLayout(null);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        help.setEditable(false);
        leader.setEditable(false);
        winners.setBounds(820,0,100,290);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 0, 800, FRAME_HEIGHT-40);
        Scanner sc = new Scanner(f);
        String text= "";
        while(sc.hasNextLine()){
            text+=sc.nextLine();
        }
        help.setText(text.replaceAll("_!","\n"));
        sc = new Scanner(l);
        text="";
        while(sc.hasNextLine()){
            text+=sc.nextLine();
        }
        leader.setText(text.replaceAll("_!","\n"));
        this.add(winners);
        this.add(scroll);
        setVisible(true);
        pack();
    }

}
