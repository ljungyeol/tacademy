package com.example.samples14;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
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
	//MyApp myapp;
	//MyApp2 myapp;
	MyApp myapp;
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        						WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().takeSurface(this);
        myapp = new MyApp(getBaseContext());
        myt = new MyThread();
        myt.start();
    }
	public boolean onKeyDown(int keyCode, KeyEvent event){
		myapp.event(1, keyCode);
		return super.onKeyDown(keyCode, event);
	}	
	
	public boolean onKeyUp(int keyCode, KeyEvent event){
		//myapp.event(0, keyCode);
		return super.onKeyUp(keyCode, event);
	}
	

	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			myapp.event(1,event.getX(),event.getY());
		}
		//if(event.getAction() == MotionEvent.ACTION_MOVE){
		//	myapp.event(2,event.getX(),event.getY());
		//}
		//if(event.getAction() == MotionEvent.ACTION_UP){
		//	myapp.event(0,event.getX(),event.getY());
		//}
		return true;
	}
	
	
	
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		if(!myapp.bInit) myapp.init(w,h); 
	}
	
	@Override
	public void surfaceRedrawNeeded(SurfaceHolder holder){
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
			while(myt.bActive){
				try {
					myt.wait();
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	class MyThread extends Thread{
		SurfaceHolder mSurface;
		boolean bRun, bActive, bQuit;
		Canvas c;
		
		@Override
		public void run(){
			while(true){
				synchronized(this){
					while(mSurface == null || !bRun){
						if(bActive){
							bActive = false;
							notify();
						}
						if(bQuit) return;
						try{
							wait();
						} catch(InterruptedException e){
						}
					}
					if (!bActive){
						bActive = true;
						notify();
					}
					
					c = mSurface.lockCanvas();
					if(c==null) continue;
					
					if(myapp.bInit)myapp.main(c); 
					
					mSurface.unlockCanvasAndPost(c);
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
