package com.example.samples5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

import com.example.samples5.MyApp2.MyTimer;

public class MyApp3 {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit,going;


	public MyApp3(Context ctx){
		this.ctx = ctx;
	}
	class MyTimer{
		long start, d;
		int num;
		public MyTimer(int delay){
			d = delay;
			start = System.currentTimeMillis();
		}
		void run(long now){
			long t = now - start;
			if(t>d){
				num++;
				if(num > chrF-1) num =0;
				start = now-(t-d);
			}
		}
	}
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
		
		for(int i =0, j=R.drawable.user_move_2_0; i<chrF;i++,j++)
			chr[0][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_6_0; i<chrF;i++,j++)
			chr[1][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_0_0; i<chrF;i++,j++)
			chr[2][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i =0, j=R.drawable.user_move_4_0; i<chrF;i++,j++)
			chr[3][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		x = lcdw/2 -cW/2;
		y = lcdh/2 -cH;
		mov = new Move(lcdw,lcdh);			
		mov.x = lcdw/2; 
		mov.y = lcdh/2;	
		going = true;
		bInit = true;
	}
	static final int cW= 64, cH=96, chrF=8;
	int x, y;
	int dir, spd=2;
	
	Bitmap[][] chr = new Bitmap[4][chrF];
	MyTimer myt = new MyTimer(40);
	Move mov;
	void main(Canvas c){
		long now = System.currentTimeMillis();
		
		if(mov.going){
			myt.run(now);
			mov.MoveCheck(spd);
		}
		/*	if(dir<2) x+=spdx;
			else y+= spdy;
			if (x<0 || x>lcdw-cW){
				dir=(x<0)? 0:1;
				x-=spdx;
				going = false;
			}
			if(y<0 || y>lcdh-cH){
				dir=(y<0)? 3:2;
				y-=spdy;
				going=false;
			}
		}
		c.drawColor(Color.BLACK);
		c.drawBitmap(chr[dir][myt.num],x,y,null);
		c.drawText("Character Animation", 20, 30, p);*/
		c.drawColor(Color.WHITE); 
		c.drawBitmap(chr[dir][myt.num],mov.x-cW/2,mov.y-cH,null);
			
		c.drawPoint(mov.x, mov.y, p);
		c.drawPoint(mov.ex, mov.ey, p);		
		c.drawText("Character Animation", 10,20, p);	
		
		
	}
	int tchs;
	float dnx,dny,mvx,mvy,tx,ty;
	
	void event(int state, float x , float y){
		//myt.start = System.currentTimeMillis();
		//going = true;
		mov.MoveInit((int)x,(int)y);
		
		if(mov.direction==0 || mov.direction==4) dir=0;
		else if(mov.direction==2 || mov.direction==6) dir=1;
		else if(mov.direction==3 || mov.direction==5) dir=2;
		else if(mov.direction==1 || mov.direction==7) dir=3;
		
		/*
		if(x<lcdw/3){
			dir =1;
			spdx=(spdx>0)?-spdx:spdx;
		}
		else if(x>lcdw/3*2){
			dir=0;
			spdx=(spdx<0)?-spdx:spdx;
		}
		else if(y<lcdh/3){
			dir =2;
			spdy=(spdy>0)?-spdy:spdy;
		}
		else if(y>lcdh/3*2){
			dir =3;
			spdy=(spdy<0)?-spdy:spdy;
		}
		else{
			going = false;
		}*/
		
		
		
		
		
		
	}
	
	void event(int state, int key){
		myt.start = System.currentTimeMillis();
		switch(key){
		case KeyEvent.KEYCODE_MENU:
			going = (!going)? true:false;
			break;
		}
		
	}
	
	void end(){
		for(int j =0; j<4; j++){
			for(int i = 0; i<chrF;i++){
				chr[j][i].recycle();
			}
		}
	}
}
