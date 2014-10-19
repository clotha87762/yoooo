package com.example.game;

import java.util.Random;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class LifeBonus extends GameObj {
	
	private Rect limit;
	int startX, startY;
	int endX,endY;
	float degree;
	int spanX,spanY;
	public LifeBonus(Drawable d,int width,int height){
		
		super(d);
		int temp;
		Random r = new Random();
		temp = r.nextInt(4)+1;  //¤W ¤U ¥ª ¥k
		if(temp==1){
		   startX = r.nextInt(width) +1;
		   startY = 10;
		}
		else if(temp==2){
		  startX =r.nextInt(width)+1;
		  startY = height -10;
		}
		else if(temp==3){
		  startX = 10;
		  startY = r.nextInt(height)+1;
		}
		else{
		  startX = width -10;
		  startY = r.nextInt(height)+1;
			
		}
		
		this.endX =  (width/2)-startX;
		this.endY= (height/2) - startY;
		double distance = Math.sqrt( (endX*endX) + (endY*endY));
		spanX= (int)(endX * 70 / distance);
		spanY= (int)(endY * 70 / distance);
		this.setRect(startX,startY,startX+d.getIntrinsicWidth(),startY+d.getIntrinsicHeight());
		
	}
	
	
	public void move(){
		
		this.move(spanX,spanY);
	}
	
	
}
