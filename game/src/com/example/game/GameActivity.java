package com.example.game;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.*;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.media.SoundPool;
import android.media.AudioManager;
import java.util.HashMap;
import java.util.ArrayList;
import android.media.MediaPlayer;


public class GameActivity extends Activity{
	SurfaceView gameView ;
	SurfaceHolder surfaceHolder ;
	int width;
	int height;
	int gameState=0;
	Ball ball ;
	ArrayList <Enemy> enemies ;
	Thread gameThread;
	boolean isGameThreadStop = true;
	GameObj backimg;
	GameObj hull;
	int enemyCount = 0;
	Canvas canvas = null;
	ArrayList<Enemy> deleteEnemy = new ArrayList<Enemy>();
	long time ;
	SoundPool soundPool;
	HashMap<Integer,Integer> soundPoolMap;
	MediaPlayer mediaPlayer;
	float volume;
	long touchTime;
	long endTouchTime;
	LifeBonus bonus;
	int lifeCount=0;
	int speed;
	int simulEnemy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
			              WindowManager.LayoutParams.FLAG_FULLSCREEN);
			//設置為橫式螢幕模式
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);
		width = display.widthPixels;
		height = display.heightPixels;
		initialSound();
		gameView = new SurfaceView(this);
		surfaceHolder = gameView.getHolder();
		surfaceHolder.addCallback( new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub	
				getReady();
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setContentView (gameView);
		
	 }
	
	public void initialSound(){
		AudioManager mgr = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		float current = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		volume = current/maxVolume;
		soundPool = new SoundPool (4,AudioManager.STREAM_MUSIC,100);
		soundPoolMap = new HashMap<Integer,Integer>();
		soundPoolMap.put(1,soundPool.load(this,R.raw.explode,5));
		soundPoolMap.put(2,soundPool.load(this,R.raw.dead,5 ));
		soundPoolMap.put(3,soundPool.load(this,R.raw.bulletsound1,5 ));
	}
	
	public void getReady(){
		
		
		Resources rs = getResources();
		
		time = 0;
		simulEnemy = 1;
		lifeCount = 0;
		enemyCount = 0;
		speed = 30;
		hull = new GameObj(rs.getDrawable(R.drawable.hull));
		hull.setRect(width/2,5,(width/2)+hull.drawable.getIntrinsicWidth(),5+hull.drawable.getIntrinsicHeight());
		backimg = new GameObj(rs.getDrawable(R.drawable.backimg));
		backimg.setRect(0,0,width,height);
		mediaPlayer =MediaPlayer.create(this, R.raw.back);
		ball = new Ball(rs.getDrawable(R.drawable.ball),width,height);
		enemies =  new ArrayList<Enemy>();
		if(!mediaPlayer.isPlaying())mediaPlayer.start();
		gameState = 1;	
		gameThreadStart();
		
		
	}
	
	@Override
	protected void onPause() {
		Log.d("log test","onPause()");
		pauseGame();
		super.onPause();
	};

	protected static final int MENU_Resume = Menu.FIRST;
	protected static final int MENU_Reset = Menu.FIRST + 1;
	protected static final int MENU_Quit = Menu.FIRST + 2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("log test","onCreate 1");
		menu.add(0, MENU_Resume, 0, "繼續");
		menu.add(0, MENU_Reset, 0, "重新開始");
		menu.add(0, MENU_Quit, 0, "離開");
		Log.d("log test","onCreate 2");
		return super.onCreateOptionsMenu(menu);
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_Resume:
			resumeGame();
			break;
		case MENU_Reset:
			getReady();
			break;
		case MENU_Quit:
			gameExit();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		Log.d("log test","menuOpen()");
		pauseGame();
		return super.onMenuOpened(featureId, menu);
	};
	
	public void pauseGame(){
		Log.d("log test","onPauseGame");
		if(mediaPlayer.isPlaying())mediaPlayer.pause();
		isGameThreadStop = true;
	}
	public void resumeGame(){
		Log.d("log test","onResume");
		if(!mediaPlayer.isPlaying()) mediaPlayer.start();
		isGameThreadStop = false;
		gameThreadStart();
	}
	public void gameExit(){
		isGameThreadStop = true;
		if (gameThread != null) {
			try {
				gameThread.join();
			} catch (InterruptedException e) {

			}
		}
		finish();
	}
	
	
	
	public void gameThreadStart(){
		
		isGameThreadStop = false;
		if (gameThread == null) {
			gameThread = new Thread(game);
			gameThread.start();
		} else if (!gameThread.isAlive()) {
			gameThread = new Thread(game);
			gameThread.start();
		}
	}
	
	Runnable game = new Runnable(){	
		public void run(){
			long delayTime = 25;
			while(!isGameThreadStop){			
				long startTime = System.currentTimeMillis();
				gameUpdate();
				draw();
				long endTime = System.currentTimeMillis();
				long waitTime = delayTime - (startTime - endTime);
				if (waitTime > 0) {
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
					}					
			}		
		}	
	}
	};

	
   void draw(){
		
		
		try {
			canvas = surfaceHolder.lockCanvas(null);
			synchronized (surfaceHolder) {
				if(gameState==1)
					drawCanvas(canvas);
				else if(gameState==2)
					drawOver(canvas);
			}
		} finally {
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
    void drawCanvas(Canvas canvas){
		
		int adjust;
		adjust = 0;
    	backimg.draw(canvas);
		ball.draw(canvas);
		Paint p = new Paint();
		p.setARGB(150, 255, 0,0 );
		p.setTextSize(100);
		canvas.drawText("分數 :"+ time,30 ,100, p);
		for(Enemy e:enemies) e.draw(canvas);
		if(bonus!=null) bonus.draw(canvas);
		
		for(int i=0;i<ball.life;i++){
			
			canvas.save();
			canvas.translate(adjust, 0);
			hull.draw(canvas);
			canvas.restore();
			adjust+=50;
			
		}
	}
	
    void drawOver(Canvas canvas) {
		// 執行緒停止
		isGameThreadStop = true;
		drawCanvas(canvas);
		if(mediaPlayer.isPlaying())mediaPlayer.pause();
		Paint pt = new Paint();
		pt.setARGB(30, 30, 30, 30);
		canvas.drawRect(backimg.getRect(), pt);
		pt.setTextAlign(Paint.Align.CENTER);
		pt.setARGB(100, 0, 0, 255);
		pt.setTextSize(250);
		canvas.drawText("-遊戲結束-", backimg.centerX(), backimg.centerY(), pt);
	}
	@Override
    public  boolean onTouchEvent(android.view.MotionEvent event){
		long interval;
		if(gameState ==1)
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
				touchTime = event.getDownTime();
			
				return true;
		case MotionEvent.ACTION_MOVE:
			return true;
		
		case MotionEvent.ACTION_UP:
			endTouchTime = event.getEventTime();
			interval = endTouchTime - touchTime;
			if (interval >=800 && ball.hisatsu>0){
				
				soundPool.play(soundPoolMap.get(2), volume, volume, 1, 0, 1f);
				enemies.clear();
				ball.hisatsu--;
			}
			else{
			ball.setTarget( (int)event.getX(), (int)event.getY() );
			}
			return true;
		
		}
		return true;
	}
	
	public void gameUpdate(){
		
		
		lifeCount++;
		time++;
		
		if(time%10==0) speed++;
		if(time%100 == 0) simulEnemy++;
		
		if( (enemyCount % 3) == 0){
			enemyCount =0;
			Resources rs=getResources();
			for(int i = 0 ;i<simulEnemy;i++)
			enemies.add(new Enemy(rs.getDrawable(R.drawable.enemy),width,height,speed));
		}
		if(lifeCount%200==0){
			lifeCount = 0;
			Resources rs=getResources();
			bonus=new LifeBonus(rs.getDrawable(R.drawable.life),width,height);
			Log.d("log test","lifeCreate");
		}
		
		
		ball.move();	
		
		for(Enemy e : enemies) e.move();
		if(bonus!=null)
		bonus.move();
		
		for(Enemy e: enemies) {
			
			if(Rect.intersects(e.getRect(), ball.getRect())){
				soundPool.play(soundPoolMap.get(1), volume, volume, 1, 0, 1f);
				deleteEnemy.add(e);
				ball.life--;
				if(ball.life<=0) gameState = 2 ;
			}
			else if((e.getRect().centerX()<0||e.getRect().centerX()>width)&&
					(e.getRect().centerY()<0||e.getRect().centerY()>height)
					){
				deleteEnemy.add(e);
			}
		
		}
		
		if(bonus!=null){
		if(Rect.intersects(bonus.getRect(), ball.getRect())){
			
			soundPool.play(soundPoolMap.get(3), volume, volume, 1, 0, 1f);
			if(ball.life<5)
			ball.life ++;
			
			bonus = null;
			Log.d("log test","lifedeleteByintersect");
		}else if((bonus.getRect().centerX()<0||bonus.getRect().centerX()>width)&&
				(bonus.getRect().centerY()<0||bonus.getRect().centerY()>height)
				){
			bonus = null;
			Log.d("log test","lifedeleteByOut");
		}
		}
				
		enemies.removeAll(deleteEnemy);
		enemyCount ++;
		
		
		
	}
	
	
	
}
