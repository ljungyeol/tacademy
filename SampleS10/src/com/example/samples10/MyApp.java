package com.example.samples10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyApp {
	Context ctx;
	int lcdw, lcdh;
	Paint p = new Paint();
	boolean bInit;

	class Star{
		float x,y,r,spd,scale;
		Paint p = new Paint();
		//bitmap을 a로 지정할 필요 없이 임의의 num으로 지정할 수 있다.
		// int num = myrandom(0,6);
		// 이후 drawpic의 bmp를 stars[num]으로 부르면 된다. 더 쉬움!;
		Bitmap a;
		Matrix matrix = new Matrix();
		public Star(int r){
			x = myRandom(0,defw);
			y = myRandom(0,defh);
			this.r = (float)r;
			switch(r){
			case 5: spd=2.f;scale=0.1f; break;
			case 20: spd=4.f;scale=0.4f; break;
			case 50: spd=12.f;scale=1.f; break;
			}
			this.a=stars[(int)(Math.random()*7)];

			p.setColor(myRandom(0xff000000,0xffffffff));
		}
	
	
		void draw(Canvas c){
			check();
			c.drawCircle(x,y,r,p);
		}
		
		void drawpic(Canvas c,Bitmap bmp){
			check();
			matrix.reset();
			matrix.preTranslate(x-r,y-r);
			matrix.preScale(scale, scale); // 사이즈 줄이기
			c.drawBitmap(bmp,matrix,null);
		}
		void check(){
			x-=spd;
			if(x+r<0){
				x= myRandom((int)(defw+r),(int)(defw+200+r));
				y = myRandom(0,defh);
			}
		}
	}
	
	int myRandom(int min, int max){
		return (int)(Math.random()*(max-min))+min;
	}
	Star[] star = new Star[100];
	Bitmap rocket;
	int defw=480, defh=800, order;
	float x,y, spd=5; // 로켓 위치, 스피드
	Bitmap[] stars = new Bitmap[7];
	
	public MyApp(Context ctx){
		this.ctx = ctx;
	}
	
	void init(int w, int h){
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
				
		rocket = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.rocket);
		for(int i=0; i<7; i++){
			stars[i] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.star01+i);
		}
		for(int i=0; i<70; i++) star[i]=new Star(5);
		for(int i=70; i<95; i++) star[i]=new Star(20);
		for(int i=95; i<100; i++) star[i]=new Star(50);
		
		x = defw/2-rocket.getWidth()/2;
		y = defw/2-rocket.getHeight()/2;
		bInit = true;
	}


	void main(Canvas c){	
		if(order >0){
			switch(order){
			case 1: x-=spd;
				if(x<0) x+=spd;
				break;
			case 2: x +=spd;
				if(x>defw-rocket.getWidth()) x-=spd;
				break;
			case 3: y -=spd;
				if(y<0) y+=spd;
				break;
			case 4: y +=spd;
				if(y>defh-rocket.getHeight()) y -=spd;
				break;
			}
		}
		c.scale((float)lcdw/defw,(float)lcdh/defh);
		c.drawColor(Color.BLACK);
		for(int i=0; i<100; i++){
			star[i].drawpic(c,star[i].a);
		}
		c.drawBitmap(rocket, x,y,null);
		
		c.drawText("Star Scroll", 10,20, p);	
	}
	
	void event(int state, float x, float y){
		float w = lcdw/3;
		float h = lcdh/3;
		
		if(x<w) order =1;
		else if (x>w*2) order =2;
		else if( y<h) order =3;
		else if (y>h*2) order =4;
		else order =5;
		
	}
	
	void event(int state, int key){
	}
	
	void end(){
		rocket.recycle();
	}
	

}
