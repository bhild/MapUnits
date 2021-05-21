package com.mygdx.mapunits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import java.util.Random;

public class MapUnits extends ApplicationAdapter {
	final HashMap<Integer, float[]> lineSpots = new HashMap<Integer, float[]>();//stores the lines
	SpriteBatch batch;
	Texture[] textures;
	ShapeRenderer shapes;//this draws the envorment will be replaced when I have textures
	MapGen m;//this creates the map
	ArrayList<Worker> w;//the set of all workers
	ArrayList<Enemy> e;//the set of all enemies
	ArrayList<Defender> d;//the set of all defencers
	int[] res;//the amount of each resource the player has
	Stage stage;//this is used to display the dialogs and nothing else
	Dialog resMenu;//displays the resource count
	Dialog buildMenu;//allows the player to build stuff
	SelectBox<String> selectBox;//the dropdown to select what the player is building
	StretchViewport viewport;//used for resizing based on monitor size
	OrthographicCamera camera;//used for resizing based on monitor size
	Skin skin;//this is for the dialogs and dropdowns
	boolean buttonClicked = false;//used to see if a click will build something
	String[] items = {"type-1","type-2","defender","defenderGen"};//this is what the dropdown displays
	TextArea[] resT;//these are the textboxes used to display the resource counts
	ArrayList<Worker> toBeRemoved;//stores workers the need to be removed
	TextButton b;//this is the create button
	Vector3 touchPoint=new Vector3();//the location of the uses click
	ArrayList<int[]> locations;//this stores the locations of the nodes the user has created
	int[] price = new int[]{0,0};//the price in order of res1 res2. This is for the cheapest only
	long startT = System.currentTimeMillis();//the time the program starts
	ArrayList<Enemy> enemyClear;//same function as toBeRemoved but for enemies
	ArrayList<Defender> defClear;//same function as toBeRemoved but for defenders
	int difficulty = 0;//how fast the enemies spawn
	ArrayList<DefenderGenerator> generators;//the set of defender generators
	@Override
	public void create () {
		//initializing values
		textures = new Texture[]{new Texture(Gdx.files.internal("sprites/res1.png")),new Texture(Gdx.files.internal("sprites/res2.png")),
				new Texture(Gdx.files.internal("sprites/homeNode.png")),new Texture(Gdx.files.internal("sprites/defenderGen.png")),
				new Texture(Gdx.files.internal("sprites/worker1U.png")),new Texture(Gdx.files.internal("sprites/worker1F.png"))
				,new Texture(Gdx.files.internal("sprites/worker2U.png")),new Texture(Gdx.files.internal("sprites/worker2F.png")),
				new Texture(Gdx.files.internal("sprites/defender.png")),new Texture(Gdx.files.internal("sprites/enemy.png"))};
		batch = new SpriteBatch();
		generators = new ArrayList<>();
		enemyClear = new ArrayList<>();
		defClear = new ArrayList<>();
		locations = new ArrayList<>();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), camera);
		touchPoint= new Vector3();
		toBeRemoved = new ArrayList<>();
		skin = new Skin(Gdx.files.internal("data/glassy-ui.json"));
		b= new TextButton("create",skin,"small");
		resT = new TextArea[2];
		res = new int[2];
		res[0]=price[0];
		resT[0] = new TextArea("type1 - "+res[0],skin);
		resT[1] = new TextArea("type2 - "+res[1],skin);
		m = new MapGen();
		shapes = new ShapeRenderer();
		w = new ArrayList<Worker>();
		stage = new Stage(viewport);
		selectBox =new SelectBox<String>(skin);
		resMenu=new Dialog("resources",skin){		};
		buildMenu=new Dialog("build",skin){		};
		e = new ArrayList<>();
		d = new ArrayList<>();
		//end initializing values
		b.addListener(new InputListener(){//handels the button being pressed
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				buttonClicked=true;
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		//viewport = new StretchViewport(800,600, camera);
		selectBox.setItems(items);//fills the drop down with the options
		resMenu.setSize(150,150);//sets size of the resource counter
		resMenu.setPosition(0,Gdx.graphics.getHeight());//innital pos of the res counter
		resMenu.setScale(1);//not strictly needed but can be used to scale the field if need be
		resMenu.getContentTable().defaults().pad(0);//sets the padding of the menu
		resMenu.getContentTable().add(resT[0]);//textfeild 1
		resMenu.getContentTable().row();//spacer
		resMenu.getContentTable().add(resT[1]);//textfeild 2
		//mirror of the resMenu stuff
		buildMenu.setSize(150,150);
		buildMenu.setPosition(150,Gdx.graphics.getHeight());
		buildMenu.setScale(1);
		buildMenu.getContentTable().defaults().pad(0);
		buildMenu.getContentTable().add(selectBox);
		buildMenu.getContentTable().row();
		buildMenu.getContentTable().add(b);//adds button insead of textfeild
		Gdx.input.setInputProcessor(stage);//allows the items to be clicked on
		//order is important only one can be interacted with at a time
		stage.addActor(resMenu);//adds the actor for the res menu makes it untouchable while the build menu is shown
		stage.addActor(buildMenu);//adding this second sets it as the targeted dialogue making it intractable

	}

	@Override
	public void render () {
		ScreenUtils.clear(.5f,0.5f,0.5f, 1);//clears the old drawings

		//shapes.begin(ShapeRenderer.ShapeType.Filled);//this will be replaced when sprites are added
		//allows stuff to be drawn
		shapes.begin(ShapeRenderer.ShapeType.Line);
		//this will not be replaced as it draws the lines -- needs rework soonish
		shapes.setColor(Color.SKY);
		float[] temp = new float[0];
		for (int i = 0;i<lineSpots.size();i++){
			temp = lineSpots.get(i);
			shapes.line(temp[0]*m.size+m.size/2,temp[1]*m.size+m.size/2,temp[2]*m.size+m.size/2,temp[3]*m.size+m.size/2);
		}
		shapes.end();
		batch.begin();
		for(int i = 0; i<m.getVals().length;i++){
			for(int j = 0;j<m.getVals()[i].length;j++){
				/*shapes.setColor(m.getVals()[i][j].getColor());
				//shapes.rect(i*m.size,j*m.size, m.size, m.size);
				//m.size is the size of a square and will be used frequently to place objects on the grid*/
				if(m.getVals()[i][j].getType()==1||m.getVals()[i][j].getType()==2){
					batch.draw(textures[m.getVals()[i][j].getType()-1],i*m.size,j*m.size,m.size,m.size);
				}else if(m.getVals()[i][j].getType()==-2){
					batch.draw(textures[2],i*m.size,j*m.size,m.size,m.size);
				}else if(m.getVals()[i][j].getType()==-3){
					batch.draw(textures[4],i*m.size,j*m.size,m.size,m.size);
				}
			}
		}
		//shapes.end();
		for(Worker i : w){//for each worker in the set of workers
			if(!i.hasGoal()){//if the worker is not mining from a resorse
				getTarget(i);//this will be addressed later
			}
			if(System.currentTimeMillis()-i.getTime()>50){//this makes the worker move
				//each worker has an internal timer every 10ish milliseconds the worker moves
				//the timer starts when the worker is created
				i.move();//calls the workers internal move function
				i.setTime(System.currentTimeMillis());//resets the workers internal clock
				if(isClose(i.getPos(),i.getGoal())&&!i.hasRes&&m.getVals()[i.getGoal()[0]][i.getGoal()[1]].type!=0){
					//this if statment is for the worker having mined the resorse
					//this should always run at the end of the path after the worker AI upgrade
					i.invertHas();//gives the worker a resorse
					m.getVals()[i.getGoal()[0]][i.getGoal()[1]].reduce(i.carryCount);//deducts resorse from the node
					if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].getResorseCount()<=0){
						m.getVals()[i.getGoal()[0]][i.getGoal()[1]].setType(0);//destroys the node if it has nothing in it
					}
				}else if(isClose(i.getPos(),i.getGoal())&&!i.hasRes&&m.getVals()[i.getGoal()[0]][i.getGoal()[1]].resorseCount==0){
					//part of the AI upgrade
					//facilitates the worker changing targets if it comes back with nothing
					//this should never run but is here just in case
					i.emptyHanded=true;
				}
				if(i.getPos()[0]==i.start[0]&&i.getPos()[1]==i.start[1]&&i.hasRes){
					//when the worker is home and has resource it deposits it
					//uses if because the node might be next to the home
					i.invertHas();//inverts the workers carry state
					res[i.getRes()-1]+=i.carryCount;//adds the res to the total
					resT[i.res-1] = new TextArea("type1 - "+res[0],skin);//updated the textfeild
					if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].type==0){//sets the goal to a defult if the node is empty
						//this should run even after the AI update
						i.setGoal(new int[]{-1,-1});
					}
					else if(m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers*i.carryCount>m.getVals()[i.getGoal()[0]][i.getGoal()[1]].resorseCount){
						//part of the AI update
						//if the target node has enough workers this worker will reassign itself
						m.getVals()[i.getGoal()[0]][i.getGoal()[1]].workers--;//reduces worker count on target
						i.setGoal(new int[]{-1,-1});//sets the goal to a defult
					}
				}else if(i.getPos()[0]==i.start[0]&&i.getPos()[1]==i.start[1]&&i.emptyHanded){
					i.emptyHanded=false;//resets emptyhanded
					i.setGoal(new int[]{-1,-1});//sets the goal to a defult
				}

			}
			//this will be reworked when sprites are added
			//this displayes the workers color
			if(i.res==1 && i.hasRes){
				//shapes.setColor(Color.CYAN);
				batch.draw(textures[5],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);
			}else if(i.res==2 &&i.hasRes){
				//shapes.setColor(Color.LIME);
				batch.draw(textures[7],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);
			}else if(i.res==1){
				//shapes.setColor(Color.BROWN);
				batch.draw(textures[4],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);
			}else if(i.res==2){
				//shapes.setColor(Color.YELLOW);
				batch.draw(textures[6],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);
			}
			//and this its position
			//shapes.rect(i.getPos()[0]*m.size,i.getPos()[1]*m.size,m.size,m.size);
		}

		for(Defender i : d){//the previous loop but for defenders
			batch.draw(textures[8],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);//sets the location on the screen
			if(System.currentTimeMillis()-i.getTime()>50) {//runs the movement ai every n mills
				if(i.target==null&&e.size()>0){//the defenders can be targeted or not
					//this handels the no target wandering
					for(Enemy j : e){//tries to get a target
						if(!j.isTarget) {//wont target a targeted enemy
							i.setTarget(j);
							j.isTarget = true;
							break;	//ends the for each
									//i dont have a better way of doing this
						}
					}
				}
				if(e.contains(i.target)){//if the target still exists
					if(isClose(i.pos,i.target.pos)){//if the target is close to the defender
						e.remove(e.indexOf(i.target));//remove the target from the list of enemies
						defClear.add(i);//destory this defender
					}else{
						i.move();//get closer to the target
						i.setTime(System.currentTimeMillis());//reset internal clock
					}
				}else{
					i.target = null;//the target does not exist or there is no targer
					i.move(m);//wander movement
					i.setTime(System.currentTimeMillis());//reset internal clock
				}
			}
		}
		//System.out.println();
		shapes.setColor(Color.RED);
		for(Enemy i : e){
			batch.draw(textures[9],i.pos[0]*m.size,i.pos[1]*m.size,m.size,m.size);
			if(i.path==null){
				i.genPath();//if this enemy has no path make one
			}
			if(System.currentTimeMillis()-i.getTime()>100) {
				i.move();//move every n mills
			}
			if(w.contains(i.target)){//functions the same as the one for defenders
				if(isClose(i.pos,i.target.pos)){
					int[] pos = w.get(w.indexOf(i.target)).start;//this clears the home of the worker
					m.getVals()[pos[0]][pos[1]].setType(0);
					locations.remove(w.indexOf(i.target));
					w.remove(w.indexOf(i.target));
					enemyClear.add(i);
				}
			}else{
				i.target = w.get(new Random().nextInt(w.size()));//if the target is gone get a new one
			}
		}
		batch.end();
		for(DefenderGenerator i : generators){
			if(i.attemptGen(res[1],price[1])){//for each generator see if it can generate
				d.add(new Defender(i.location,newAstar(i.location),null));//if it can add a defender
				res[1]-=price[1];//subtract the price
			}
		}
		if(System.currentTimeMillis()-startT>10000-difficulty&&w.size()-e.size()>0&&w.size()!=0){//creates enemies
			//will not run if all workers have an enemy
			startT = System.currentTimeMillis();//resets start time
			Random r = new Random();//creates a random
			int[] pos = (r.nextInt(2)==0)?new int[]{r.nextInt(m.getVals().length)-1,(r.nextInt(2)==0)?m.getVals()[0].length-1:0}:
					new int[]{(r.nextInt(2)==0)?m.getVals().length-1:0,r.nextInt(m.getVals()[0].length)-1};
			//the above statment creates an enemy along one of the 4 sides of the envernment
			e.add(new Enemy(pos,newAstar(pos),w.get(new Random().nextInt(w.size()))));//creates a new enemy
			difficulty+=(difficulty==0)?10:difficulty%10+1;//increase difficulty
		}
		//clear old entities
		for(Enemy i : enemyClear){
			e.remove(i);
		}
		for(Defender i : defClear){
			d.remove(i);
		}
		for(Worker i:toBeRemoved){
			w.remove(i);
		}
		//clear the lists they are stored in
		enemyClear.clear();
		defClear.clear();
		toBeRemoved.clear();
		//end the drawing
		shapes.end();
		//show and update stage (needs to be done every iteration)
		stage.draw();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		//show or hide the menues
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			resMenu.setVisible(!resMenu.isVisible());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
			buildMenu.setVisible(!buildMenu.isVisible());
		}
		//hotkey for pressing the build button
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
			buttonClicked = true;
		}
		if(buttonClicked&&Gdx.input.justTouched()){
			//create stuff
			touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0);//sets the click pos
			int[] loc = closestBox(new float[]{touchPoint.x,touchPoint.y});//gets the closest grid spot to the click location
			boolean valid = true;//this will be used to determine if the constuction is valid
			if(selectBox.getSelectedIndex()==0||selectBox.getSelectedIndex()==1) {//creates a worker home and worker
				for (int i = 0; i < locations.size() && valid; i++) {
					if (locations.get(i)[0] == loc[0] && locations.get(i)[1] == loc[1]) {
						valid = false;//if something is in the spot you can't build there
					}
				}
				if (valid && !cost(selectBox.getSelectedIndex())) valid = false;//if you dont have the money you cant build
				if (valid) {
					locations.add(loc);//add the home to the set of home locations
					if (locations.size() >= 2) {//create a line in linespots
						int[] target = closeNode(locations.get(locations.size() - 1), -2);
						lineSpots.put(lineSpots.size(), new float[]{locations.get(locations.size() - 1)[0]
								, locations.get(locations.size() - 1)[1], target[0], target[1]});
					}
					m.getVals()[loc[0]][loc[1]].setType(-2);//denote that the spot is filled for unit pathing
					//number may change later
					w.add(new Worker(new int[]{loc[0], loc[1]}, newAstar(loc), selectBox.getSelectedIndex() + 1));
					//creates a new worker of the correct type
				}
			}else if(selectBox.getSelectedIndex()==2){//creates a defender
				for (int i = 0; i < locations.size() && valid; i++) {
					//all the same until...
					if (locations.get(i)[0] == loc[0] && locations.get(i)[1] == loc[1]) {
						valid = false;
					}
				}
				if (valid && !cost(selectBox.getSelectedIndex())) valid = false;
				//here the defender does not get a node and needs no line
				if (valid) {
					d.add(new Defender(loc,newAstar(loc),null));//adds a defender to the list
				}

			}else if(selectBox.getSelectedIndex()==3){//creates a defender generator
				//works the same
				for (int i = 0; i < locations.size() && valid; i++) {
					if (locations.get(i)[0] == loc[0] && locations.get(i)[1] == loc[1]) {
						valid = false;
					}
				}
				if (valid && !cost(selectBox.getSelectedIndex())) valid = false;
				//these do not get a line untill the line rework
				if (valid) {
					m.getVals()[loc[0]][loc[1]].setType(-3);//sets its location to be filled
					generators.add(new DefenderGenerator(1500,loc));//makes a new defender generator
				}
			}

			buttonClicked = false;//sets button clicked to false so you need to click it again to build something
			//even if you failed to build something
		}
		//this updates the resorse counts
		resMenu.getContentTable().reset();
		resT[0] = new TextArea("type1 - "+res[0],skin);
		resT[1] = new TextArea("type2 - "+res[1],skin);
		resMenu.getContentTable().add(resT[0]);
		resMenu.getContentTable().row();
		resMenu.getContentTable().add(resT[1]);
	}//end game loop

	@Override
	public void dispose () {//memory managment stuff, I think i am doing it correctly
		shapes.dispose();
		stage.dispose();

	}
	private boolean isClose(int[] pos,int[] posTarget){//used to determine if an element is close to another on the grid
		//meaning that the target is on a side or diagonal from the source
		int closeX = Math.abs(pos[0]-posTarget[0]);//distance on x
		int closeY = Math.abs(pos[1]-posTarget[1]);//distance on y
		if((closeX==1&&closeY==0)||(closeX==0&&closeY==1)||(closeX==1&&closeY==1)){//needs to be done this way because of diagonals
			return true;//the two objects are close
		}
		return false;//the two objects are not close
	}
	private Astar newAstar(final int[] pos){//creates a new Astar class
		//for some reason the person who built this class made it so you need to define a class in the class creator
		return new Astar(Gdx.graphics.getWidth()/m.size, Gdx.graphics.getHeight()/m.size) {
			protected boolean isValid (int x, int y) {
				return m.getVals()[x][y].getType()==0||(x==pos[0]&&y==pos[1]);//this is what makes all of the pathing work
				//do not touch this class
			}
		};
	}
	private int[] closestBox(float[] arr){//gets the closest grid box to a click location
		//i am poroud of this one
		//i might need to change this if I allow the user to resize the window
		int[] out = new int[2];
		out[0] = (int)arr[0]/m.size;
		out[1] = (int)(Gdx.graphics.getHeight()-arr[1])/m.size;
		return out;//returns the location
	}
	@Override
	public void resize(int width, int height) {//this is a part of libGDX
		viewport.update(width, height, true);
		shapes.setProjectionMatrix(camera.combined);

	}
	public boolean cost(int in){//takes in the resorse type
		//returns true if the price is correct
		//then subtracts the price from the total
		if(in==1&&res[0]>=price[0]*10){
			res[0]-=price[0]*10;
			return true;
		}else if(in == 0&&res[0]>=price[0]){
			res[0]-=price[0];
			return true;
		} else if (in == 2&&res[1]>=price[1]) {
			res[1]-=price[1];
			return true;
		}else if(in == 3&&res[0]*100>=price[0]){
			res[0]-=price[0]*100;
			return true;
		}
		return false;
	}
	private int[] closeNode(int[] loc,int type){
		//returns the closest node to the location of the target
		//only works for things that have map values
		double dist = Integer.MAX_VALUE;//distance from the orgin
		int[] closest = new int[0];
		for(int i = 0;i<m.getVals().length;i++){
			for(int j = 0; j<m.getVals()[i].length;j++){
				if(m.getVals()[i][j].getType()==type){//only looks at the distance of typed nodes
					double des = getDist(new int[]{loc[0],loc[1],i,j});//uses getDist to find the distance
					if(dist>des){//if it is closer get the pos of the closer node
						closest=new int[]{i,j};
						dist=des;
					}
				}
			}
		}
		return closest;//returns the closest node
	}
	private double getDist(int[] loc){
		return Math.sqrt(Math.pow(loc[0]-loc[2],2)+Math.pow(loc[1]-loc[3],2));//distance formula
	}
	private void getTarget(Worker i){
		/* old method
		try {
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
		//imporved ai method
		double dist = Integer.MAX_VALUE;
		int[] closest = new int[0];//this cant uses closest due to the ai changes
		for(int i1 = 0;i1<m.getVals().length;i1++){
			for(int j = 0; j<m.getVals()[i1].length;j++){
				if(m.getVals()[i1][j].getType()==i.getRes()){
					m.getVals()[i1][j].workers++;//adds a worker to the workers node
					double des = getDist(new int[]{i.getPos()[0],i.getPos()[1],i1,j});
					if(dist>des&&m.getVals()[i1][j].workers*i.carryCount<m.getVals()[i1][j].resorseCount){
						//this only runs if the node needs a new worker to pervent workers returning empty handed
						if(closest.length!=0){//remove the worker from a further node
							m.getVals()[closest[0]][closest[1]].workers--;
						}
						closest=new int[]{i1,j};
						dist=des;
					}else{
						m.getVals()[i1][j].workers--;//removes a worker from the node if the node is too far away
					}
				}
			}
		}
		if(closest.length!=0){
			//creates a path for the worker to follow here
			i.setGoal(closest);
			i.genPath();
		}else{
			//if the worker has no node to work it gets destroyed
			toBeRemoved.add(i);
		}
	}
}