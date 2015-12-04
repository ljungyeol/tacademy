package com.example.samples13;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AppMenu implements AppState{
	
	Context ctx;
	AppManager app;
	Paint p;
	float x,y, spdx=2,spdy=3;

	public AppMenu(Context ctx, AppManager app) {
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
		c.drawColor(Color.GREEN);
		x+=spdx;
		y+=spdy;
		if(x<0 || x>app.lcdw) spdx=-spdx;
		if(y<0 || y>app.lcdh) spdy=-spdy;
		c.drawText("This is MENU Page ~", x, y, p);
	}

	@Override
	public void end(){
		app.iStage=app.iNext;
	}

	@Override
	public void event(int s, float x, float y) {
		if(s == tchUp){
			if(x<app.lcdw/2)
				app.iNext = LOGO;
			else app.iNext = PLAY;
			
			app.iState = END;
		}
	}

}
