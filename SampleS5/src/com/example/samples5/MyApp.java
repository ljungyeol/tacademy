package com.example.samples5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

public class MyApp {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;
	
	int numf, numb;
	Bitmap[] back = new Bitmap[3];
	Bitmap[] front = new Bitmap[4];
	float x, y, spdx = 5, spdy =7;
	Rect dst;
	Rect[] src = new Rect[3];
	public MyApp(Context ctx){
		this.ctx = ctx;
	}
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
		
		for(int i = 0;i<3;i++)
			back[i] = BitmapFactory.decodeResource(
					ctx.getResources(),R.drawable.back01+i);
		for(int i=0; i<4; i++)
			front[i] = BitmapFactory.decodeResource(
					ctx.getResources(),R.drawable.front01+i);
		bInit = true;
		//아래값을 사용하면 최대값이 되지만 다른사진이 크기가 바뀔 수 있다.
		//src = new Rect(0,0,back[0].getWidth(),back[0].getHeight());
		back[0] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.back04);
		src[0] = new Rect(0,0,320,480);
		src[1] = new Rect(320,0,640,480);
		src[2] = new Rect(640,0,960,480);
		dst = new Rect(0,0,lcdw,lcdh);
	}
	void main(Canvas c){
		c.drawColor(Color.WHITE);
		//c.drawBitmap(back[numb],0,0,null);
		c.drawBitmap(back[0], src[numb], dst,null);
		
		x +=spdx;
		y +=spdy;
		if(x<0 || x>lcdw-front[0].getWidth()) spdx = -spdx;
		if(y<0 || y>lcdh-front[0].getHeight()) spdy = -spdy;
		c.drawBitmap(front[numf],x,y,null);
		
		c.drawText("Bitmap Animation", 20, 40, p);
	}
	void end(){
		for(int i=0; i<3; i++)
			back[i].recycle();
		for(int i=0; i<4; i++)
			front[i].recycle();
	}
	void event(int state, float x, float y){
		int w = lcdw / 2;
		int h = lcdh / 3;
		
		if (y<h){
			numf++;
			if(numf>3) numf=0;}
		else if (y>h*2){
			numf--;
			if(numf<0) numf=3;}
		else if(x<w){
			numb--;
			if(numb<0) numb=2;}
		else{
			numb++;
			if(numb>2) numb=0;}
	}
	void event(int state, int key){
		switch(key){
		case KeyEvent.KEYCODE_MENU:
			numf = numb = 0;
			break;
		}
	}
}
