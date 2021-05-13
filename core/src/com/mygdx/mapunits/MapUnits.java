package com.mygdx.mapunits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

public class MapUnits extends ApplicationAdapter {
	final HashMap<Integer, float[]> lineSpots = new HashMap<Integer, float[]>();
	ShapeRenderer shapes;
	MapGen m;
	ArrayList<Worker> w;
	int[] res;
	Stage stage;
	Dialog resMenu;
	Dialog buildMenu;
	SelectBox<String> selectBox;
	StretchViewport viewport;
	OrthographicCamera camera;
	Skin skin;
	boolean buttonClicked = false;
	String[] items = {"type-1","type-2"};
	TextArea[] resT;
	ArrayList<Worker> toBeRemoved;
	TextButton b;
	Vector3 touchPoint=new Vector3();
	ArrayList<int[]> locations;
	ArrayList<int[]> targets;
	int[] price = new int[]{300};
	long startT = System.currentTimeMillis();
	Enemy e;
	@Override
	public void create () {
		locations = new ArrayList<>();
		targets = new ArrayList<>();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), camera);
		touchPoint= new Vector3();
		toBeRemoved = new ArrayList<>();
		skin = new Skin(Gdx.files.internal("data/glassy-ui.json"));
		b= new TextButton("create",skin,"small");
		b.addListener(new InputListener(){
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				buttonClicked=true;
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		resT = new TextArea[2];
		res = new int[2];
		res[0]=price[0];
		resT[0] = new TextArea("type1 - "+res[0],skin);
		resT[1] = new TextArea("type2 - "+res[1],skin);
		//viewport = new StretchViewport(800,600, camera);
		m = new MapGen();
		shapes = new ShapeRenderer();
		w = new ArrayList<Worker>();
		stage = new Stage(viewport);
		selectBox =new SelectBox<String>(skin);
		resMenu=new Dialog("resources",skin){		};
		buildMenu=new Dialog("build",skin){		};
		selectBox.setItems(items);
		resMenu.setSize(150,150);
		resMenu.setPosition(0,Gdx.graphics.getHeight());
		resMenu.setScale(1);
		resMenu.getContentTable().defaults().pad(0);
		resMenu.getContentTable().add(resT[0]);
		resMenu.getContentTable().row();
		resMenu.getContentTable().add(resT[1]);
		buildMenu.setSize(150,150);
		buildMenu.setPosition(150,Gdx.graphics.getHeight());
		buildMenu.setScale(1);
		buildMenu.getContentTable().defaults().pad(0);
		buildMenu.getContentTable().add(selectBox);
		buildMenu.getContentTable().row();
		buildMenu.getContentTable().add(b);
		Gdx.input.setInputProcessor(stage);
		stage.addActor(resMenu);
		stage.addActor(buildMenu);
		//w.add(new Worker(new int[]{32,32},newAstar(),1));
		//w.add(new Worker(new int[]{32,32},newAstar(),1));
		//w.add(new Worker(new int[]{32,32},newAstar(),2));
		//w.add(new Worker(new int[]{32,32},newAstar(),2));
		//w.add(new Worker(new int[]{32,32},newAstar(),2));
		//w.add(new Worker(new int[]{32,32},newAstar(),2));
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		shapes.begin(ShapeRenderer.ShapeType.Filled);

		for(int i = 0; i<m.getVals().length;i++){
			for(int j = 0;j<m.getVals()[i].length;j++){
				shapes.setColor(m.getVals()[i][j].getColor());
				shapes.rect(i*m.size,j*m.size, m.size, m.size);
			}
		}
		shapes.end();
		shapes.begin(ShapeRenderer.ShapeType.Line);
		shapes.setColor(Color.BLUE);
		float[] temp = new float[0];
		for (int i = 0;i<lineSpots.size();i++){
			temp = lineSpots.get(i);
			shapes.line(temp[0]*m.size+m.size/2,temp[1]*m.size+m.size/2,temp[2]*m.size+m.size/2,temp[3]*m.size+m.size/2);
		}
		shapes.end();
		shapes.begin(ShapeRenderer.ShapeType.Filled);
		for(Worker i : w){
			if(!i.hasGoal()){
				getTarget(i);
			}
			if(System.currentTimeMillis()-i.getTime()>10){
				i.move();
				i.setTime(System.currentTimeMillis());
				if(isClose(i.getPos(),i.getGoal())&&!i.hasRes&&m.getVals()[i.getGoal()[0]][i.getGoal()[1]].type!=0){
					i.invertHas();
					m.getVals()[i.getGoal()[0]][i.getGoal()[1]].reduce(i.carryCount);
					if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].getResorseCount()<=0){
						m.getVals()[i.getGoal()[0]][i.getGoal()[1]].setType(0);
					}
				}else if(isClose(i.getPos(),i.getGoal())&&!i.hasRes&&m.getVals()[i.getGoal()[0]][i.getGoal()[1]].resorseCount==0){
					i.emptyHanded=true;
				}
				if(i.getPos()[0]==i.start[0]&&i.getPos()[1]==i.start[1]&&i.hasRes){

					i.invertHas();
					res[i.getRes()-1]+=i.carryCount;
					resT[i.res-1] = new TextArea("type1 - "+res[0],skin);
					if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].type==0){
						i.setGoal(new int[]{-1,-1});
					}
					else if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers*i.carryCount>m.getVals()[i.getGoal()[0]][i.getGoal()[1]].resorseCount){
						m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers--;
						i.setGoal(new int[]{-1,-1});
					}
				}else if(i.getPos()[0]==i.start[0]&&i.getPos()[1]==i.start[1]&&i.emptyHanded){
					i.emptyHanded=false;
					i.setGoal(new int[]{-1,-1});
				}

			}
			if(i.res==1 && i.hasRes){
				shapes.setColor(Color.CYAN);
			}else if(i.res==2 &&i.hasRes){
				shapes.setColor(Color.LIME);
			}else if(i.res==1){
				shapes.setColor(Color.BROWN);
			}else if(i.res==2){
				shapes.setColor(Color.YELLOW);
			}
			shapes.rect(i.getPos()[0]*m.size,i.getPos()[1]*m.size,m.size,m.size);
		}
		/*if(e==null){
			try{
				e = new Enemy(new int[]{0,0},newAstar(new int[]{0,0}),w.get(0));
				e.genPath();
			}catch (Exception e){}
		}else{
			shapes.setColor(Color.RED);
			shapes.rect(e.getPos()[0]*m.size,e.getPos()[1]*m.size,m.size,m.size);
			if(System.currentTimeMillis()-startT>100){
				//e.genPath();
				startT=System.currentTimeMillis();
				e.move();
			}
		}*/
		for(Worker i:toBeRemoved){
			w.remove(i);
		}
		toBeRemoved.clear();
		shapes.setColor(Color.PURPLE);
		shapes.end();
		stage.draw();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			resMenu.setVisible(!resMenu.isVisible());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
			buildMenu.setVisible(!buildMenu.isVisible());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
			buttonClicked = true;
		}
		if(buttonClicked&&Gdx.input.justTouched()){
			touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0);
			int[] loc = closestBox(new float[]{touchPoint.x,touchPoint.y});
			boolean valid = true;
			for(int i = 0; i<locations.size()&&valid;i++){
				if(locations.get(i)[0]==loc[0]&&locations.get(i)[1]==loc[1]){
					valid = false;
				}
			}
			if(valid && !cost(selectBox.getSelectedIndex()))valid=false;
			if(valid){
				locations.add(loc);
				if(locations.size()>=2){
					int[] target = closeNode(locations.get(locations.size()-1),-2);
					lineSpots.put(lineSpots.size(),new float[]{locations.get(locations.size()-1)[0]
							,locations.get(locations.size()-1)[1],target[0],target[1]});
				}
				m.getVals()[loc[0]][loc[1]].setType(-2);
				w.add(new Worker(new int[]{loc[0],loc[1]},newAstar(loc),selectBox.getSelectedIndex()+1));
			}

			buttonClicked = false;
		}
		resMenu.getContentTable().reset();
		resT[0] = new TextArea("type1 - "+res[0],skin);
		resT[1] = new TextArea("type2 - "+res[1],skin);
		resMenu.getContentTable().add(resT[0]);
		resMenu.getContentTable().row();
		resMenu.getContentTable().add(resT[1]);
	}

	@Override
	public void dispose () {
		shapes.dispose();
		stage.dispose();

	}
	private boolean isClose(int[] pos,int[] posTarget){
		int closeX = Math.abs(pos[0]-posTarget[0]);
		int closeY = Math.abs(pos[1]-posTarget[1]);
		if((closeX==1&&closeY==0)||(closeX==0&&closeY==1)||(closeX==1&&closeY==1)){
			return true;
		}
		return false;
	}
	private Astar newAstar(final int[] pos){
		return new Astar(Gdx.graphics.getWidth()/m.size, Gdx.graphics.getHeight()/m.size) {
			protected boolean isValid (int x, int y) {
				return m.getVals()[x][y].getType()==0||(x==pos[0]&&y==pos[1]);
			}
		};
	}
	private int[] closestBox(float[] arr){
		int[] out = new int[2];
		out[0] = (int)arr[0]/m.size;
		out[1] = (int)(Gdx.graphics.getHeight()-arr[1])/m.size;
		return out;
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		shapes.setProjectionMatrix(camera.combined);

	}
	public boolean cost(int in){
		if(in==1&&res[0]>=price[0]*10){
			res[0]-=price[0]*10;
			return true;
		}else if(in == 0&&res[0]>=price[0]){
			res[0]-=price[0];

			return true;
		}
		return false;
	}
	private int[] closeNode(int[] loc,int type){
		double dist = Integer.MAX_VALUE;
		int[] closest = new int[0];
		for(int i = 0;i<m.getVals().length;i++){
			for(int j = 0; j<m.getVals()[i].length;j++){
				if(m.getVals()[i][j].getType()==type){
					double des = getDist(new int[]{loc[0],loc[1],i,j});
					if(dist>des){
						closest=new int[]{i,j};
						dist=des;
					}
				}
			}
		}
		return closest;
	}
	private double getDist(int[] loc){
		return Math.sqrt(Math.pow(loc[0]-loc[2],2)+Math.pow(loc[1]-loc[3],2));
	}
	private void getTarget(Worker i){
		/*try {
			ArrayList<Resource> tries = m.resPos(i);
			i.setGoal(tries.get(0).getPos());
			m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers++;
			tries.remove(0);
			while(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers*i.carryCount>m.getVals()[i.getGoal()[0]][i.getGoal()[1]].resorseCount&&tries.size()>=0){
				m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers--;
				i.setGoal(tries.get(0).getPos());
				m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers++;
				tries.remove(0);
			}
			if(tries.size()>=0){
				i.genPath();
			}else{
				toBeRemoved.add(i);
			}
		}catch (Exception e){
			toBeRemoved.add(i);
		}*/
		double dist = Integer.MAX_VALUE;
		int[] closest = new int[0];
		for(int i1 = 0;i1<m.getVals().length;i1++){
			for(int j = 0; j<m.getVals()[i1].length;j++){
				if(m.getVals()[i1][j].getType()==i.getRes()){
					m.getVals()[i1][j].workers++;
					double des = getDist(new int[]{i.getPos()[0],i.getPos()[1],i1,j});
					if(dist>des&&m.getVals()[i1][j].workers*i.carryCount<m.getVals()[i1][j].resorseCount){
						if(closest.length!=0){
							m.getVals()[closest[0]][closest[1]].workers--;
						}
						closest=new int[]{i1,j};
						dist=des;
					}else{
						m.getVals()[i1][j].workers--;
					}
				}
			}
		}
		if(closest.length!=0){
			i.setGoal(closest);
			i.genPath();
		}else{
			toBeRemoved.add(i);
		}
	}
}
