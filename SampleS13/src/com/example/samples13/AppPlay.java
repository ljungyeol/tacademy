package com.example.samples13;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AppPlay implements AppState {

	Context ctx;
	AppManager app;
	Paint p; 
	float y, spdy = 2;
	
	public AppPlay(Context ctx, AppManager app) {
		this.ctx=ctx;
		this.app=app;
	}

	@Override
	public void init() {
		p = new Paint(Color.RED);
		p.setTextSize(30);
	}

	@Override
	public void main(Canvas c) {
		c.drawColor(Color.YELLOW);
		
		y+=spdy;
		if(y<0 || y>app.lcdh) spdy=-spdy;
		c.drawText("This is GAME Page ~", 20, y, p);
	}

	@Override
	public void end() {
		app.iStage=app.iNext;
	}

	@Override
	public void event(int s, float x, float y) {
		if(s == tchUp){
			if(x<app.lcdw/2)
				app.iNext = MENU;
			else app.iNext = LOGO;
			app.iState = END;
		}
	}

}
