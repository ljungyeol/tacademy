package com.example.samples4;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
	public void surfaceCreated(SurfaceHolder holder){
		synchronized(myt){
			myt.mSurface = holder;
			myt.notify();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		lcdw = chr.lcdw = w;
		lcdh = chr.lcdh = h;
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			break;
		default:
			iPCol = ((int)(Math.random()*0xffffff)+0xff000000);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			chr.MoveInit((int)event.getX(),(int)event.getY());
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE){
			chr.MoveInit((int)event.getX(),(int)event.getY());
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP){
		}
		return true;
	}
	
	
	
	MyThread myt;
	Move chr = new Move(); 
	int lcdw, lcdh, iBCol, iPCol, num;
	Paint p = new Paint();
	
	class MyTimer{
		long start, d;
		int color;
		
		public MyTimer(long delay){
			d=delay;
			color = (int)(Math.random()*0xffffff)+0xff000000;
			start = System.currentTimeMillis();
		}
		
		int run(long now){
			long t = now-start;
			if(t>d){
				start = now-(t-d);
				return color = (int)(Math.random()*0xffffff)+0xff000000;
			}
			return color;
		}
	}
	
	MyTimer myt1 = new MyTimer(3000);
	MyTimer myt2 = new MyTimer(1500);
	long s;
	
	class MyThread extends Thread{
		SurfaceHolder mSurface;
		boolean bRun, bActive, bQuit;
		Canvas c;
		
		@Override
		public void run(){
			p.setColor(Color.RED);
			p.setStrokeWidth(200);
			p.setTextSize(40);
			
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
					
					long now = System.currentTimeMillis();
					iBCol = myt1.run(now);
					iPCol = myt2.run(now);
					
					c.drawColor(iBCol);
					chr.MoveCheck();
					p.setColor(iPCol);
					c.drawPoint(chr.x,chr.y,p);
					
					long t = 1000/(now-s);
					s = now;
					c.drawText(Integer.toString((int)t), 40, 40, p);
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
