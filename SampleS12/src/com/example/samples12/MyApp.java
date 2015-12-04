package com.example.samples12;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class MyApp {
	Context ctx;
	int lcdw,lcdh;
	Paint p = new Paint();
	boolean bInit;
	
	SoundPool mSound;
	MediaPlayer bgm=new MediaPlayer();
	int sndID, ding, walk;
	static final int None=0, Ding=1, Walk=2;
	
	@SuppressWarnings("deprecation")
	
	void soundInit(){
		mSound=new SoundPool(3,AudioManager.STREAM_MUSIC,0);
		ding = mSound.load(ctx,R.raw.ding,1);
		walk = mSound.load(ctx,R.raw.walk,1);
		
		bgm = MediaPlayer.create(ctx, R.raw.song);
		bgm.setLooping(true);
		bgm.start();
	}
	
	void soundPlay(int id, int vol, int loop){
		mSound.play(id, vol, vol, 1,loop,1);
	}
	
	void soundEnd(){
		mSound.release();
		bgm.stop();
		bgm.release();
	}
	
	void soundManager(){
		switch(sndID){
		case None: break;
		case Ding: soundPlay(ding,100,0); sndID=None; break;
		case Walk: soundPlay(walk,100,0); sndID=None; break;
		}
	}
	
	class MyTimer{
		long start, d;
		int num;		
		public MyTimer(int delay){
			d = delay;
			start=System.currentTimeMillis();
		}
		void run(long now){
			long t = now-start;
			if(t > d){
				num ++;
				if(num > chrF-1) num=0;
				start = now-(t-d);
			}
		}
	}
	public MyApp(Context ctx){
		this.ctx = ctx;
	}	
	
	void init(int w, int h){
		soundInit();
		lcdw = w; lcdh = h;
		p.setColor(0xffff0000);
		p.setTextSize(20);
		
		cntx = defw/tSize+1;
		cnty = defh/tSize+1;
		
		mTile[0]=BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tile01);
		mTile[1]=BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tile02);
		mTree[0]=BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tree01);
		mTree[1]=BitmapFactory.decodeResource(ctx.getResources(),R.drawable.post00);
		
		
		for(int i=0,j=R.drawable.user_move_2_0; i<chrF; i++,j++)
			chr[0][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_6_0; i<chrF; i++,j++)
			chr[1][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_0_0; i<chrF; i++,j++)
			chr[2][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i=0,j=R.drawable.user_move_4_0; i<chrF; i++,j++)
			chr[3][i]=BitmapFactory.decodeResource(ctx.getResources(),j);
		for(int i = 0; i<chrF; i++){
			chr[4][i] = chr[2][i];
			chr[5][i] = chr[0][i];
			chr[6][i] = chr[3][i];
			chr[7][i] = chr[1][i];
		}
		bInit = true;
	}
	final int chrF=8, tSize=64, cntTx=15, cntTy=25, defw=480, defh=800;
	int Mtx=2, Mty=3, Tx, Ty, Wx, Wy,Cx,Cy,cntx,cnty,offx,offy;
	int num, dir, spd=8;
	boolean going, bOrder;
	MyTimer t1 = new MyTimer(30);
	Bitmap[][] chr = new Bitmap[8][chrF];
	Bitmap mTile[] = new Bitmap[3];
	Bitmap mTree[] = new Bitmap[2];
	
	void main(Canvas c){		
		process();
		c.scale((float)lcdw/defw, (float)lcdw/defw);
		c.drawColor(Color.BLACK);
		
		for(int y=0; y<cnty+offy; y++){
			for(int x=0; x<cntx+offx; x++){
				c.drawBitmap(mTile[mbMap[Ty+y][Tx+x]],
						x*tSize+Cx, y*tSize+Cy,null);
			}
		}
		for(int y=0; y<cnty+offy; y++){
			for(int x=0; x<cntx+offx; x++){
				if(Mtx==Tx+x && Mty==Ty+y){
					c.drawBitmap(chr[dir][t1.num],128,164,null);
					
				}
				if(mbTree[Ty+y][Tx+x]>0){
					c.drawBitmap(mTree[mbTree[Ty+y][Tx+x]-1],
							x*tSize+Cx, y*tSize+Cy-32,null);
				}
			}
		}
		c.drawText("Back Scroll", 20,30, p);
		c.drawText(Integer.toString((int)fps), 20, 50, p);
	}
	
	
	long start, fps;
	
	void process(){
		soundManager();
		long now = System.currentTimeMillis();
		fps = 1000/(now-start);
		start = now;
		
		checkOrder();
		
		if(going){
			t1.run(now);
			
			switch(dir){
			case 0:
				Wx+=spd; Cx=-(Wx%tSize); Tx = Wx/tSize;
				if(Wx%tSize==0){going = false; Mtx++;}
				break;
			case 1:
				Wx-=spd; Cx=-(Wx%tSize); Tx = Wx/tSize;
				if(Wx%tSize==0){going=false; Mtx--;}
				break;
			case 2:
				Wy-=spd; Cy=-(Wy%tSize); Ty = Wy/tSize;
				if(Wy%tSize==0){going = false; Mty--;}
				break;
			case 3:
				Wy+=spd; Cy=-(Wy%tSize); Ty = Wy/tSize;
				if(Wy%tSize==0){going = false; Mty++;}
				break;
			case 4: 
				Wx += spd; Cx=-(Wx%tSize); Tx=Wx/tSize;
				Wy -= spd; Cy=-(Wy%tSize); Ty=Wy/tSize;
				if(Cx==-tSize/2){ Mtx++; Mty--; }
				if(Cx==0){ going=false; }
				break;
			case 5: 
				Wx += spd; Cx=-(Wx%tSize); Tx=Wx/tSize; 
				Wy += spd; Cy=-(Wy%tSize); Ty=Wy/tSize;
				if(Cx==-tSize/2){ Mtx++; Mty++;}
				if(Cx==0){ going=false; }
				break;
			case 6:
				Wx -= spd; Cx=-(Wx%tSize); Tx=Wx/tSize;
				Wy += spd; Cy=-(Wy%tSize); Ty=Wy/tSize;
				if(Cx==-tSize/2){ Mtx--; Mty++;}
				if(Wx%tSize==0){ going=false; }
				break;
			case 7: 
				Wx -= spd; Cx=-(Wx%tSize); Tx=Wx/tSize;
				Wy -= spd; Cy=-(Wy%tSize); Ty=Wy/tSize;
				if(Cx==-tSize/2){ Mtx--; Mty--;}
				if(Cx==0){ going=false; }
				break;
			}
		}
		offx = (Cx==0)? 0:1;
		offy = (Cy>-32)? 0:1;
	}
	
	
	void checkOrder(){
		if(bOrder && !going){
			switch(dir){
			case 1:
				if(mbTree[Mty][Mtx-1]!=0 || Tx==0)
					going= false;
				else going = true;
				break;
			case 0:
				if(mbTree[Mty][Mtx+1]!=0 || Tx+cntx>cntTx-1)
					going= false;
				else going = true;
				break;
			case 2:
				if(mbTree[Mty-1][Mtx]!=0 || Ty==0)
					going= false;
				else going = true;
				break;
			case 3:
				if(mbTree[Mty+1][Mtx]!=0 || Ty+cnty>cntTy-1)
					going= false;
				else going = true;
				break;
			case 4 : 
				if(mbTree[Mty-1][Mtx+1]!=0 || Tx+cntx>cntTx-1 || Ty==0)
					going=false;			
				else going=true;
				break;
			case 5: 
				if(mbTree[Mty+1][Mtx+1]!=0 || Tx+cntx>cntTx-1 || Ty+cnty>cntTy-1)
					going=false;				
				else going=true;
				break;
			case  6: 
				if(mbTree[Mty+1][Mtx-1]!=0 || Tx==0 || Ty+cnty>cntTy-1)
					going=false;				
				else going=true;
				break;
			case 7: 
				if(mbTree[Mty-1][Mtx-1]!=0 || Tx==0 || Ty==0)
					going=false;				
				else going=true;
				break;
			}
			if(going==false)sndID = Ding;
			else sndID=Walk;
		}
		bOrder=false;
	}
	
	void event(int state, float x, float y){	
		if(!going){
			int w = lcdw/3, h=lcdh/3;
			if(y<h && x<w){
				dir=7; bOrder = true;
			}else if(y<h && x<2*w){
				dir=2; bOrder = true;
			}else if(y<h && x<3*w){
				dir=4; bOrder = true;
			}else if(y>2*h && x<w){
				dir=6; bOrder = true;
			}else if(y>2*h && x<2*w){
				dir=3; bOrder = true;
			}else if(y>2*h && x<3*w){
				dir=5; bOrder = true;
			}else if(y>h && x<w){
				dir=1; bOrder = true;
			}else{
				dir=0; bOrder = true;
			}
		}
	}

	void event(int state, int key){
	}
	void end(){
		for(int i=0;i<2;i++){
			mTile[i].recycle();
			mTree[i].recycle();
		}
		for(int i=0;i<chrF;i++){
			chr[0][i].recycle();
			chr[1][i].recycle();
			chr[2][i].recycle();
			chr[3][i].recycle();
			chr[4][i].recycle();
			chr[5][i].recycle();
			chr[6][i].recycle();
			chr[7][i].recycle();
		}
	}
	public byte mbMap[][]={
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
	};
	public byte mbTree[][]={
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,1,1,1,1,1,1,1,1,0,0,0,0,0},
			{0,1,0,0,0,0,1,0,0,1,1,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,1,0,0,0,0},
			{0,1,0,0,2,0,0,1,0,0,1,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,1,0,0,0,0},
			{0,1,0,1,0,0,0,0,0,0,1,0,0,0,0},
			{0,1,0,0,1,0,0,2,0,0,1,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,1,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,1,0,0,0,0},
			{0,1,1,0,2,1,1,2,0,1,1,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,1,0,0,0,0,0,0,1,0,0,0},
			{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
			{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
			{0,0,0,0,1,0,0,0,0,0,0,1,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};	
	
	
	
	
}
