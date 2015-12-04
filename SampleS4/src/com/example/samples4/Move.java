package com.example.samples4;

public class Move{
	int x, y, ex, ey, dx, dy, p , direction;
	int lcdw, lcdh;
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