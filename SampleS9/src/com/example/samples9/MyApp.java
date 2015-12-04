package com.example.samples9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyApp {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;

	class MyTimer{
		long start, d;
		int num;
		public MyTimer(int delay){
			d = delay;
			start=System.currentTimeMillis();
		}
		void run(long now){
			long t = now-start;
			if(t>d){
				num++;
				if(num > chrF-1) num=0;
				start = now-(t-d);
			}
		}
	}
	
	public MyApp(Context ctx){
		this.ctx = ctx;
	}
	
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
				
		back = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.back1);
		
		for(int i =0, j=R.drawable.user_move_2_0; i<chrF;i++,j++)
			chr[0][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_6_0; i<chrF;i++,j++)
			chr[1][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_0_0; i<chrF;i++,j++)
			chr[2][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_4_0; i<chrF;i++,j++)
			chr[3][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		x = defw/2 -cW/2;
		y=defh/2;
		bInit = true;
	}
	final int cW =64, cH=96, chrF=8, defw=480,defh=800;
	int x, y , spdx=4, spdy=4, dir, bx, by;
	boolean going;
	
	Bitmap back;
	Bitmap[][] chr = new Bitmap[4][chrF];
	MyTimer myt = new MyTimer(40);
	
	void main(Canvas c){	
		long now = System.currentTimeMillis();
		
		if(going){
			myt.run(now);
			
			switch(dir){
			case 0:
				bx-=spdx; break;
			case 1:
				bx+=spdx; break;
			case 2:
				by+=spdy; break;
			case 3:
				by-=spdy; break;
			}
			if (bx>0) bx=-defw;
			else if(bx<-defw) bx=0;
			if (by>0) by=-defh;
			else if(by<-defh) by=0;
		}
		c.scale((float)lcdw/defw,(float)lcdh/defh);
		c.drawColor(Color.WHITE);
		
		c.drawBitmap(back, bx,by, null);
		c.drawBitmap(back, bx+defw,by, null);
		c.drawBitmap(back,bx,by+defh,null);
		c.drawBitmap(back,bx+defw,by+defh,null);
		
		c.drawBitmap(chr[dir][myt.num], x, y,null);	
		c.drawText("Back Scroll", 10,20, p);	
		
		
	}
	
	void event(int state, float x, float y){
		myt.start=System.currentTimeMillis();
		
		int w = lcdw/3;
		int h = lcdh/3;
		
		if(x<w){
			dir=1; going =true;
		}else if (x>w*2){
			dir=0; going=true;
		}else if (y <h){
			dir=2; going=true;
		}else if (y > h*2){
			dir=3; going=true;
		}else{
			going=false;
		}
		
	}
	
	void event(int state, int key){
	}
	
	void end(){
		back.recycle();
		for(int i=0;i <4; i++){
			for(int j=0; j<chrF;j++){
				chr[i][j].recycle();
			}
		}
	}
	

}
