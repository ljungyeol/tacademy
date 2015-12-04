package com.example.samples13;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements SurfaceHolder.Callback2{
	
	MyThread myt;
	AppManager myapp;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().takeSurface(this);		
		myapp = new AppManager(this);
		myt = new MyThread();
		myt.start();		
	}
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:		
			myapp.event(1, event.getX(), event.getY()); break;		
		case MotionEvent.ACTION_MOVE:
			myapp.event(2, event.getX(), event.getY()); break;
		case MotionEvent.ACTION_UP:
			myapp.event(3, event.getX(), event.getY()); break;
		}		
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return super.onKeyDown(keyCode, event);
	}
	
	class MyThread extends Thread{
		SurfaceHolder mSurface;
		boolean bRun, bActive, bQuit;        
		Canvas c;
		
		@Override
		public void run(){            
			while(true){
				synchronized(this){
					while (mSurface == null || !bRun){
						if(bActive){
							bActive = false;
							notify();
						}
						if(bQuit){ return; }                        
						try{
							wait();
						}catch(InterruptedException e){
						}
					}
					if(!bActive){
						bActive = true;
						notify();
					}                    
					c = mSurface.lockCanvas();
					if(c == null) continue; 
					
					if(myapp.bInit) myapp.main(c);
						
					mSurface.unlockCanvasAndPost(c);
				}
			}
		}
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
		}        
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder,int format,int w,int h){
		if(!myapp.bInit){
			myapp.init();
			myapp.lcdw=w; myapp.lcdh=h;
			myapp.bInit=true;
		}
	}
	@Override
	public void surfaceRedrawNeeded(SurfaceHolder holder){		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
			while (myt.bActive){
				try {
					myt.wait();
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	protected void onPause(){
		super.onPause();
		synchronized(myt){
			myt.bRun = false;
			myt.notify();
		}
	}
	@Override
	protected void onResume(){
		super.onResume();
		synchronized(myt){
			myt.bRun = true;
			myt.notify();
		}
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		synchronized(myt){
			myt.bQuit = true;
			myt.notify();
		}
	}
}
