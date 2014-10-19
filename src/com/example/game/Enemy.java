package com.example.game;

import java.util.Random;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Random;

public class Enemy extends GameObj {

	private Rect limit;
	int startX, startY;
	int endX,endY;
	float degree;
	int spanX,spanY;
	int speed;
	public Enemy(Drawable d,int width,int height,int speed){
		
		
		super(d);
		this.speed= speed;
		int temp;
		Random r = new Random();
		temp = r.nextInt(4)+1;  //¤W ¤U ¥ª ¥k
		if(temp==1){
		   startX = r.nextInt(width) +1;
		   startY = 5;
		}
		else if(temp==2){
		  startX =r.nextInt(width)+1;
		  startY = height -5;
		}
		else if(temp==3){
		  startX = 5;
		  startY = r.nextInt(height)+1;
		}
		else{
		  startX = width -5;
		  startY = r.nextInt(height)+1;
			
		}
		
		this.endX =  (width/2)-startX;
		this.endY= (height/2) - startY;
		double distance = Math.sqrt( (endX*endX) + (endY*endY));
		spanX= (int)(endX * speed / distance);
		spanY= (int)(endY * speed / distance);
		this.setRect(startX,startY,startX+d.getIntrinsicWidth(),startY+d.getIntrinsicHeight());
		
	}
	
	
	public void move(){
		
		this.move(spanX,spanY);
	}
	
	
	
}
