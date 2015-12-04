package com.example.samples2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;


@SuppressLint("NewApi")



public class MainActivity extends Activity implements SurfaceHolder.Callback2{
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        						WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().takeSurface(this);
        myt = new MyThread();
        myt.start();
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
	
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		lcdw = w;
		lcdh = h;
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
	
	
	MyThread myt;
	int lcdw, lcdh, num;
	float x,y, spdx=5, spdy=5;
	Paint p = new Paint();
	
	class MyThread extends Thread{
		SurfaceHolder mSurface;
		boolean bRun, bActive, bQuit;
		Canvas c;
		
		@Override
		public void run(){
			p.setColor(Color.RED);
			p.setStrokeWidth(10);
			
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
					
					if(num < 3){
						c.drawColor(Color.WHITE);
						
						num++;
					}
					x += spdx;
					y += spdy;
					if(x<0||x>lcdw) spdx = -spdx;
					if(y<0||y>lcdh) spdy = -spdy;
					c.drawPoint(x,y,p);
					mSurface.unlockCanvasAndPost(c);
				}
			}
		}
	}
	
}
