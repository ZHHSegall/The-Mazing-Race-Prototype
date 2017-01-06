package models;

public class Coordinates {
	int x; 
	int y;
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Coordinates other){
		return x == other.x && y == other.y;
	}
	
	public boolean adjacent(Coordinates other){
		return (x == other.x && Math.abs(y - other.y) == 1) ||
			   (y == other.y && Math.abs(x - other.x) == 1);
	}
	
	public boolean within(Maze m){
		return (0 <= x && x < m.width) &&
			   (0 <= y && y < m.height);
	}
	
	

	
	
}
