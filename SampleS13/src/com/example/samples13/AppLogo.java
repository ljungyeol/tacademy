package com.example.samples13;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AppLogo implements AppState{
	AppManager app;
	Context ctx;
	
	Paint p;
	float x, spdx=2;
	
	public AppLogo(Context ctx, AppManager app){
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
		c.drawColor(Color.CYAN);
		
		x += spdx;
		if(x>app.lcdw || x<0) spdx = -spdx;
		c.drawText("This is LOGO Page ~", x, 30, p);
	}
	@Override
	public void end(){
		app.iStage=app.iNext;
	}
	@Override
	public void event(int s, float x, float y) {
		if(s == tchUp){
			if(x<app.lcdw/2)
				app.iNext = PLAY;
			else app.iNext = MENU;
			
			app.iState = END;
		}
	}

}
