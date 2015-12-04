package com.example.samples7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import java.lang.*;

public class MyApp {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;


	public MyApp(Context ctx){
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
		bInit = true;
	}
	
	Bitmap arrow;
	Move mov;
	
	Matrix mat = new Matrix();
	int spd = 5;
	float angle;
	
	void main(Canvas c){	
		if(mov.going){
			mov.MoveCheck(spd);
		}
		mat.reset();
		mat.preTranslate(mov.x, mov.y);
		mat.preRotate(angle);
		mat.preTranslate(-arrow.getWidth()/2, -arrow.getHeight()/2);
		
		
		
		
		c.drawColor(Color.WHITE); 
		c.drawBitmap(arrow, mat, null);
			
		c.drawPoint(mov.x, mov.y, p);
		c.drawPoint(mov.ex, mov.ey, p);		
		c.drawText("Character Animation", 10,20, p);	
		
		
	}
	
	void event(int state, float x, float y){
		mov.MoveInit((int)x,(int)y);
		angle =(float)(Math.atan((mov.y-y)/(mov.x-x))*(180/Math.PI));
		angle = ((mov.x-x)<0)? angle+180 : angle;
		//Math.atan2(mov.dy,mov.dx) 그냥하면되는것
	}
	
	void event(int state, int key){
	}
	
	void end(){
		arrow.recycle();
	}
	

}
