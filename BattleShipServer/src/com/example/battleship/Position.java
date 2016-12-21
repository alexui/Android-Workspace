package com.example.battleship;

public class Position {
	
	public int x;
	public int y;
	
	public Position(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	@Override
	public boolean equals(Object o) {
		Position p = (Position) o;
		
		return (p.x==this.x && p.y==this.y);
	}
	
	
	
}
