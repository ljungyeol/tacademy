package com.example.samples14;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyApp {
	Context ctx;	
	int lcdw,lcdh;
	boolean bInit;	
	Paint p = new Paint();
	
	final int MAX = 500;
	Move[] mov = new Move[MAX];
	float tchx,tchy;
	
	
	public MyApp(Context ctx){
		this.ctx = ctx;
	}
	void init(int w, int h){
		for(int i=0;i<MAX;i++){
			mov[i] = new Move(w,h);
		}
		bInit = true;
	}	
	void main(Canvas c){		
		c.drawColor(Color.BLACK);
		    
		for(int i=0; i<MAX; i++){
			if(mov[i].going) 
				mov[i].MoveCheck(mov[i].spd);	
			p.setColor(mov[i].color);
			c.drawLine(tchx, tchy, mov[i].x, mov[i].y, p);
		}
	} 
	void event(int state, float x, float y){
		if(state==1){
			tchx = x;  tchy = y;
			
			for(int i=0; i<MAX; i++){
				mov[i].MoveInit(x, y);
			}
		}
	}
	void event(int state, int key) {		
	}
	void end(){

	}	
}

