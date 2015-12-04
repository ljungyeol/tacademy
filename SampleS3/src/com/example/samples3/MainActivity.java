package com.example.samples3;

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
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			break;
		default:
			p.setColor((int)(Math.random()*0xffffff)+0xff000000);
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
	int lcdw, lcdh, num;
	Move chr = new Move(); 
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
						chr.MoveCheck();
						c.drawPoint(chr.x,chr.y,p);
					mSurface.unlockCanvasAndPost(c);
				}
			}
		}
	}
	
	class Move{
		int x, y, ex, ey, dx, dy, p , direction;
		
		public Move(){
			MoveInit();
		}
		
		int abs(int n){
			return n=(n<0)? -n:n;
		}
		
		void MoveInit(){
			ex = (int)(Math.random()*lcdw);
			ey = (int)(Math.random()*lcdh);
			
			MoveInit(ex,ey);
		}
		
		void MoveInit(int tx, int ty){
			ex = tx; ey = ty;
			dx = ex - x; dy = ey - y;
			p = 0;
			
			if(dx>=0 && dy >=0){
				if(abs(dx)>abs(dy)) direction = 0;
				else direction = 1;
			}
			
			else if(dx <= 0 && dy <= 0){
				if(abs(dx)>abs(dy)) direction = 2;
				else direction = 3;
			}
			
			else if(dx >= 0 && dy <= 0){
				if(abs(dx)>abs(dy)) direction = 4;
				else direction = 5; 
			}
			
			else if(dx <= 0 && dy >= 0){
				if(abs(dx)>abs(dy)) direction = 6;
				else direction = 7; 
			}
			
		}
		
		void MoveCheck(){
			if(ex ==x && ey == y)MoveInit();
			else MoveChr();
		}
		
		void MoveChr(){
			switch(direction){
			
			case 0: x++; p+=dy;
				if(p>dx/2){y++; p-=dx;}
				break;
			
			case 1: y++; p+=dx;
				if(p>dy/2){x++; p-=dy;}
				break;
				
			case 2: x--; p-=dy;
				if(p>-dx/2){y--; p+=dx;}
				break;
			case 3: y--; p-=dx;
				if(p>-dy/2){x--; p+=dy;}
				break;
			case 4: x++; p-=dy;
				if(p>dx/2){y--; p-=dx;}
				break;
			case 5: y--; p+=dx;
				if(p>-dy/2){x++; p+=dy;}
				break;
			case 6: x--; p+=dy;
				if(p>-dx/2){y++; p+=dx;}
				break;
			case 7: y++; p-=dx;
				if(p>dy/2){x--; p-=dy;}
				break;
				
			}
		}
		
		
		
	}
	
	
	
}
