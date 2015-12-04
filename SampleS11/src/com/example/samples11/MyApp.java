package com.example.samples11;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class MyApp {
	Context ctx;
	int lcdw,lcdh;
	Paint p = new Paint();
	boolean bInit;
	public MyApp(Context ctx){
		this.ctx = ctx;
	}	
	class MyTimer{
		long start, d;
		int num;		
		public MyTimer(int delay){
			d = delay;
			start=System.currentTimeMillis();
		}
		void run(long now){
			long t = now-start;
			if(t > d){
				num ++;
				if(num > chrF-1) num=0;
				start = now-(t-d);
			}
		}
	}
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);

		back=BitmapFactory.decodeResource(
				ctx.getResources(), R.drawable.board); 
		
		for(int i=0,j=R.drawable.user_move_2_0; i<chrF; i++,j++)
			chr[0][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_6_0; i<chrF; i++,j++)
			chr[1][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_0_0; i<chrF; i++,j++)
			chr[2][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_4_0; i<chrF; i++,j++)
			chr[3][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		
		x = back.getWidth()/2-cW/2;
		y = back.getHeight()/2-cH;
		bInit = true;
	}
	final int cW=64, cH=96, chrF=8, defw=480, defh=800;
	int x, y, spdx=4, dir;
	
	Bitmap back; 
	Bitmap[][] chr = new Bitmap[4][chrF];
	
	MyTimer myt = new MyTimer(40);
	Matrix mat = new Matrix();	
	float tx,ty, dnx,dny, mvx,mvy;
	
	void main(Canvas c){		
		long now = System.currentTimeMillis();
		myt.run(now);
		
		x += spdx;
		if(x<0 || x>back.getWidth()-cW){
			x -= spdx;
			dir=(dir==0)? 1:0;
			spdx = -spdx;
		}		
		c.scale((float)lcdw/defw, (float)lcdw/defw);
		c.translate(tx+mvx,ty+mvy);
		
		c.drawColor(Color.BLACK);
		c.drawBitmap(back, 0,0, null);
		c.drawBitmap(chr[dir][myt.num], x,y,null);

		c.setMatrix(mat);		
		c.drawText("Back Transform", 20,30, p);
	}
	void event(int state, float x, float y){	
		x *= (float)defw/lcdw;
		y *= (float)defh/lcdh;
		
		if(state == 1){
			dnx = x; 
			dny = y;
		}
		else if(state == 2){
			mvx = x-dnx;
			mvy = y-dnx;
		}
		else if(state == 0){
			tx+=mvx;
			ty+=mvy;
			dnx=dny=mvy=mvy=0;
		}
	}
	void event(float x, float y){		
	}	
	void event(int key){
	}
	void end(){
		back.recycle();
		for(int i=0; i<4; i++){
			for(int j=0;j<chrF;j++){
				chr[i][j].recycle();
			}
		}
	}
}
