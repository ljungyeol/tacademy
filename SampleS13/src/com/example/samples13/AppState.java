package com.example.samples13;


import android.graphics.Canvas;

public interface AppState {
	
	static final int defw=720, defh=1280;	
	
	static final int QUIT=-1, LOGO=0, MENU=1, PLAY=2; // Stage List Create	
	static final int INIT=0, MAIN=1, END=2;
	static final int tchNo=0, tchDn=1, tchMv=2, tchUp=3; 
	
	public void init();
	public void main(Canvas c);
	public void end();
	public void event(int s, float x,float y);
}
