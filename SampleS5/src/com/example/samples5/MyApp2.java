package com.example.samples5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

public class MyApp2 {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;

	public MyApp2(Context ctx){
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
		
		x = lcdw/2 -cW/2;
		y = lcdh/2 -cH;
		bInit = true;
	}
	static final int cW= 64, cH=96, chrF=8, JSPD=32, JMAX=192;
	int x, y, spd=2,dir,iJump;
	float jumpy, jspd, ymin, ymax;
	
	Bitmap[][] chr = new Bitmap[2][chrF];
	MyTimer myt = new MyTimer(40);
	
	void initJump(int mode){		
		switch(mode){
		case 0:
			if(iJump==0){
				iJump=1;
				jumpy=this.y;
				jspd=JSPD;
				myt.num=7;
			}
			break;
		case 1:
			if(iJump==0)
				ymin = this.y;
			ymax = this.y-JMAX;
			iJump = 1;
			jspd = JSPD;
			myt.num = 7;
			break;
		}
	}
	void checkJump(int mode){
		
		switch(mode){ 	
		case 0:		// single jump
			if(iJump==1){
			   y-=(jspd*=0.9f);
			   if(y<jumpy-JMAX){
				   y=(int) (jumpy-JMAX);
				   iJump=2;
			   }
			}else if(iJump==2){
			   y+=(jspd*=1.12f);
			   if(y>jumpy){
				   y=(int) jumpy;
				   iJump=0;
			   }
			}
			break;
		case 1:		// multi jump
			if(iJump==1){
				y-=(jspd*=0.9f);
				if(y<ymax){
					y=(int)ymax;
					iJump=2;
				}				
			}else if(iJump==2){
				y+=(jspd*=1.12f);
				if(y>ymin){
					y=(int)ymin;
					iJump=0;
					myt.num=0;
				}
			}
			break;
		}		
	}
	
	
	
	void main(Canvas c){
		long now = System.currentTimeMillis();
		
		x+=spd;
		if(x<-cW || x>lcdw){
			spd = -spd;
			dir=(x<0)? 0:1;
		}
		if(iJump==0){
			myt.run(now);
		}else{
			checkJump(Mode);
		}
		
		/*else if (iJump==1){
			y-=(jspd*=0.9f);
			if(y<jumpy-JMAX){
				y=(int)(jumpy-JMAX);
				iJump=2;
			}
		}else if(iJump==2){
			y+=(jspd*=1.12f);
			if(y>jumpy){
				y=(int)jumpy;
				iJump=0;
			}
		}*/
		c.drawColor(Color.BLACK);
		c.drawBitmap(chr[dir][myt.num],x,y,null);
		c.drawText("Character Animation", 20, 20, p);
	}
	int tchs;
	float dnx,dny,mvx,mvy,tx,ty;
	int Mode;
	void event(int state, float x , float y){
		//single jump
		/*if(iJump==0){
			iJump=1;
			jumpy=this.y;
			jspd=JSPD;
			myt.num=7;
		}*/
		initJump(Mode);
		
		
	}
	
	
	void event(int state, int key){
		if(key == KeyEvent.KEYCODE_MENU){
			dir = (dir==0)? 1:0;
			spd = -spd;
		}
	}
	
	void end(){
		for(int i = 0; i<chrF;i++){
			chr[0][i].recycle();
			chr[1][i].recycle();
		}
	}
}
