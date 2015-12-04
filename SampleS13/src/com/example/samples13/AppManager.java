package com.example.samples13;

import android.content.Context;
import android.graphics.Canvas;

public class AppManager implements AppState{
	Context ctx;
	boolean bInit;
	
	int iState, iStage, iNext;
	int lcdw, lcdh, ts;
	float tx, ty;	
	
	AppLogo appLogo; 
	AppMenu appMenu;
	AppPlay appPlay;	

	public AppManager(Context ctx) {
		this.ctx = ctx;		
	}
	@Override
	public void init() {		
		appLogo = new AppLogo(ctx,this); 
		appMenu = new AppMenu(ctx,this);
		appPlay = new AppPlay(ctx,this);
	}
	@Override
	public void event(int s, float x, float y) {
			tx=x; 
			ty=y; 
			ts=s;
	}
	@Override
	public void main(Canvas c) {
		
		switch(iStage){ 
		case LOGO:
			switch(iState){
			case INIT: appLogo.init(); iState=MAIN; break;
			case MAIN: appLogo.main(c); appLogo.event(ts, tx,ty); ts=tchNo; break;
			case END: appLogo.end(); iState=INIT; break;	
			}
			break;
		case MENU:
			switch(iState){
			case INIT: appMenu.init(); iState=MAIN; break;
			case MAIN: appMenu.main(c); appMenu.event(ts, tx,ty); ts=tchNo; break;
			case END: appMenu.end(); iState=INIT; break;	
			}
			break;
		case PLAY:
			switch(iState){
			case INIT: appPlay.init(); iState=MAIN; break;
			case MAIN: appPlay.main(c); appPlay.event(ts, tx,ty); ts=tchNo; break;
			case END: appPlay.end(); iState=INIT; break;	
			}
			break;		
		}
	}
	@Override
	public void end() {		
	}
}
