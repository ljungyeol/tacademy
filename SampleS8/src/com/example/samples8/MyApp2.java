package com.example.samples8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class MyApp2 {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;


	public MyApp2(Context ctx){
		this.ctx = ctx;
	}
	
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
		p.setStrokeWidth(10);
		
		arrow = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.arrow);
		
		mov = new Move(lcdw,lcdh);
		mov.x = lcdw/2; 
		mov.y = lcdh/2;	
		radius = lcdw/5;
		bInit = true;
	}
	
	Move mov;
	Bitmap arrow;
	Matrix mat = new Matrix();
	float x, y, angle, spin, radius, add=5;
	int spd = 5;
	
	void main(Canvas c){
		if(mov.going){
			mov.MoveCheck(spd);
		}
		spin +=add;
		double t = spin * (Math.PI/180);
		
		x= mov.x + (float)Math.cos(t) * radius;
		y= mov.y + (float)Math.sin(t) * radius;
		angle = (add>0)? spin+90:spin-90;
		
		mat.reset();
		mat.preTranslate(x, y);
		mat.preRotate(angle);
		mat.preTranslate(-arrow.getWidth()/2, -arrow.getHeight()/2);
		
		c.drawColor(Color.WHITE); 
		c.drawBitmap(arrow, mat, null);
		c.drawPoint(mov.x, mov.y, p);
		c.drawText("Character Animation", 10,20, p);	
		}
	
	void event(int state, float x, float y){
		mov.MoveInit((int)x,(int)y);
		if (state==1){
			add = -add;
		}
	}
	
	void event(int state, int key){
	}
	
	void end(){
		arrow.recycle();
	}
	

}
