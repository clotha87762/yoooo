package com.example.game;

import java.util.Random;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Ball extends GameObj {

	private Rect limit;
    int targetX ;
	int targetY;
	public int life ;
	int hisatsu;
	public void setTarget(int x,int y){
		targetX = x;
		targetY = y;
		
	}
	
	public Ball(Drawable d,int width,int height){
		super(d);
		life = 5;
		hisatsu = 2;
		this.setRect((width/2)-(d.getIntrinsicWidth()/2),(height/2)-(d.getIntrinsicWidth()/2),(width/2)+(d.getIntrinsicWidth()/2),(height/2)+(d.getIntrinsicWidth()/2));
		targetX = d.getBounds().centerX();
		targetY = d.getBounds().centerY();
	}
	
	public void move(){
		
		int spanX = targetX -this.centerX();
		int spanY = targetY -this.centerY();
		double distance = Math.sqrt((spanX*spanX) + (spanY*spanY)) ;
		
		if(distance >=50){
		
		
		if(distance !=0){
			spanX =(int) (spanX*50/distance);
			spanY =(int) (spanY*50/distance);
			}
		
		}
		
		this.move(spanX,spanY);
	}
	
}	
