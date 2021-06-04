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
    private int FRAME_WIDTH = 550;
    private int FRAME_HEIGHT = 330;
    private JTextArea help = new JTextArea(0,0);
    private JScrollPane scroll = new JScrollPane(help);
    public Help(InputStream f) {
        setLayout(null);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        help.setEditable(false);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 0, FRAME_WIDTH-30, FRAME_HEIGHT-40);
        Scanner sc = new Scanner(f);
        String text= "";
        while(sc.hasNextLine()){
            text+=sc.nextLine();
        }
        pack();
        help.setText(text.replaceAll("_!","\n"));
        this.add(scroll);
        setVisible(true);
    }

}
