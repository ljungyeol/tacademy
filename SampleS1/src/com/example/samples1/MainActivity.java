package com.example.samples1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        						WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new MyView(this));
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    
    class MyView extends View{
    	public MyView(Context context){
    		super(context);
    	}
    	
    	public void onDraw(Canvas canvas){
    		canvas.drawColor(Color.parseColor("#000000"));
    		
    		Bitmap bmp = BitmapFactory.decodeResource(
    				getResources(), R.drawable.ic_launcher);
    		canvas.drawBitmap(bmp, 50,50,null);
    		
    		Paint myPaint = new Paint();
    		myPaint.setAntiAlias(false);
    		
    		myPaint.setColor(Color.parseColor("#ffff00"));
    		canvas.drawLine(10, 10, 50, 250, myPaint);
    		
    		myPaint.setStrokeWidth(10);
    		canvas.drawPoint(50, 50, myPaint);
    		
    		myPaint.setARGB(128, 0, 255, 255);
    		canvas.drawCircle(200,300,100,myPaint);
    		
    		myPaint.setARGB(128,255,0,255);
    		canvas.drawCircle(100, 255, 100, myPaint);
    		
    		myPaint.setAntiAlias(true);
    		
    		myPaint.setARGB(255, 255, 255, 0);
    		canvas.drawRoundRect(new RectF(150,60,300,120),10,10,myPaint);
    		
    		Path myPath = new Path();
    		myPath.addCircle(200,300,100,Direction.CW);
    		
    		myPaint.setTextSize(20);
    		canvas.drawTextOnPath("DrawText on Path", myPath, 0, 20, myPaint);
    		
    		myPaint.setColor(Color.RED);
    		canvas.drawText("Android DrawText", 20,30 , myPaint);
    	}
    	
    }
    
}
