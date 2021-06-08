package com.mygdx.mapunits.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.mapunits.MapUnits;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DesktopLauncher {

	public static void main (String[] arg) throws InterruptedException {
		final ArrayList<String> run = new ArrayList<>();
		final Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Launcher l = new Launcher(run);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t = new Thread(){
			public void run(){
				r.run();
			}
		};
		t.start();
		while(run.size()==0){
			Thread.sleep(10);
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MapUnits(run), config);
	}
}
