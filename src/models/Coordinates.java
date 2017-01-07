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
	/*
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Coordinates))return false;
	    Coordinates o = (Coordinates) other;
		return x == o.x && y == o.y;
	}
	*/
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
	
	//Euclidean distance between two points
	public double eucDistanceTo(Coordinates other){
		return Math.sqrt(Math.pow((x - other.x), 2) + Math.pow((y - other.y), 2));
	}
		
	//Manhattan distance between two points
	public int manDistanceTo(Coordinates other){
		return Math.abs(x - other.x) + Math.abs(y - other.y);
	}
	
	
	
}
