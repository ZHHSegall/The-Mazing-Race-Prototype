package models;

import java.util.Random;

public class Maze {
	
	private class Player {
		Coordinates location;
		
		Player (int x, int y){
			location = new Coordinates(x, y);
		}	
		//Movement - named for clarity in code
		void up (){ location.y++; }
		void down () { location.y--;  }
		void left () { location.x--;  }
		void right () { location.x++;  }
		
		boolean at(Coordinates square){ return location.equals(square); } 
		
		public int x() { return location.x; }
		public int y() { return location.y; }
	}
	
	//Board properties
	int width;
	int height;
	int turn;
	
	//Board
	//Don't forget: 2D arrays are [row][col]
	boolean[][] hWalls; //horizontal walls
	boolean[][] vWalls; //vertical walls
	
	//Players
	Player p1;
	Player p2;
	boolean p1Turn = true;
	Coordinates p1Goal;
	Coordinates p2Goal;
	
	public Maze (int h, int w){
		height = h;
		width = w;
		turn = 0;
		
		hWalls = new boolean[h + 1][w];
		vWalls = new boolean[h][w + 1];
		
		p1 = new Player(0, 0);
		p2 = new Player(w - 1, 0);
		p1Goal = new Coordinates(w - 1, h - 1);
		p2Goal = new Coordinates(0, h - 1);
		
		//Initialize the outer walls
		for(int i = 0; i < width; i++){
			hWalls[0][i] = true;
			hWalls[h][i] = true;
		}
		for(int i = 0; i < height; i++){
			vWalls[i][0] = true;
			vWalls[i][w] = true;
		}
	}
	
	
	//Tests if a move is valid without moving a player
	public boolean moveIsValid(char dir, Coordinates loc){
		if(!loc.within(this))
			return false;
		
		switch(dir){
		case 'u':
			if(hWalls[loc.y + 1][loc.x]){ return false; }
			break;
		case 'r':
			if(vWalls[loc.y][loc.x + 1]){ return false; }
			break;
		case 'd':
			if(hWalls[loc.y][loc.x]){ return false; }
			break;
		case 'l':
			if(vWalls[loc.y][loc.x]){ return false; }
			break;
		}
		
		return true;
	}
	
	//Returns true if valid; false otherwise
	public boolean move(char direction){
		Player cur;
		if(p1Turn){ cur = p1; }
		else {cur = p2; }
		
		switch(direction){
		case 'u':
			if(hWalls[cur.y() + 1][cur.x()]){ return false; }
			cur.up();
			break;
		case 'r':
			if(vWalls[cur.y()][cur.x() + 1]){ return false; }
			cur.right();
			break;
		case 'd':
			if(hWalls[cur.y()][cur.x()]){ return false; }
			cur.down();
			break;
		case 'l':
			if(vWalls[cur.y()][cur.x()]){ return false; }
			cur.left();
			break;
		}
		
		return true;
	}
	
	/*
	 * Tests if
	 * a) Squares are adjacent AND
	 * b) If squares are adjacent and have the same x-coordinate,
	 * 		is there a horizontal wall between them? OR
	 * c) If squares are adjacent and have the same y-coordinate,
	 * 		is there a vertical wall between them? 
	 */
	public boolean wallBetween(Coordinates s1, Coordinates s2){
		return s1.adjacent(s2) 
			&& ((s1.x == s2.x && hWalls[Math.max(s1.y, s2.y)][s1.x]) 
				|| (s1.y == s2.y && vWalls[s1.y][Math.max(s1.x, s2.x)]));
	}
	
	//returns 0 for valid path
	//		  1 for squares not adjacent
	//		  2 for wall already present
	//        3 for wall blocks a path
	public int placeWall (Coordinates s1, Coordinates s2){
		PathFinder p1Path = new PathFinder(this, p1.location, p1Goal);
		PathFinder p2Path = new PathFinder(this, p2.location, p2Goal);
		if(!s1.adjacent(s2))
			return 1;
		if(wallBetween(s1, s2))
			return 2;
		//Horizontal case
		if(s1.x == s2.x){
			int maxY = Math.max(s1.y, s2.y);
			hWalls[maxY][s1.x] = true;
			if(!p1Path.findPath() || !p2Path.findPath()){
				hWalls[maxY][s1.x] = false;
				return 3;
			}
		} 
		//Vertical case
		else if(s1.y == s2.y){
			int maxX = Math.max(s1.x, s2.x);
			vWalls[s1.y][maxX] = true;
			if(!p1Path.findPath() || !p2Path.findPath()){
				vWalls[s1.y][maxX] = false;
				return 3;
			}
		}
		return 0;
	}
	
	//Seeds random walls on the board
	public void seed (double density){
		Random rng = new Random();
		boolean valid = false;
		while(!valid){
			for(int row = 1; row < hWalls.length - 1; row++){
				for(int col = 0; col < hWalls[row].length; col++){
					hWalls[row][col] = rng.nextDouble() <= density;
				}
			}
			for(int row = 0; row < vWalls.length; row++){
				for(int col = 1; col < vWalls[row].length - 1; col++){
					vWalls[row][col] = rng.nextDouble() <= density;
				}
			}
			PathFinder p1Path = new PathFinder(this, p1.location, p1Goal);
			PathFinder p2Path = new PathFinder(this, p2.location, p2Goal);
			valid = p1Path.findPath() && p2Path.findPath();
		}
		
	}
	
	public void print (boolean move){
		//Print top coordinates
		int playerTurn = p1Turn ? 1 : 2;
		if(move){
			System.out.println("Turn: " + turn + " -- PLAYER " + playerTurn + " -- MOVE");
		} else {
			System.out.println("Turn: " + turn++ + " -- PLAYER " + playerTurn + " -- WALL");
			p1Turn = !p1Turn;
		}
		System.out.println();;
		System.out.print("  ");
		for(int i = 0; i < width; i++){
			System.out.print("  " + i +" ");
		}
		System.out.println();
		
		//Print board from top to bottom
		//TODO add characters
		for(int row = 2 * height; row >= 0; row--){
			if(row % 2 == 0){
			//If we are not printing tiles...
				int rowNum = row/2;
				
				System.out.print("  ");
				for(int col = 0; col < width; col++){
					if(hWalls[rowNum][col]){
						System.out.print("+ - ");
					} else {
						System.out.print("+   ");
					}
				}
				System.out.println("+");
			} else {
			// If we are printing tiles...
				int rowNum = (row - 1)/2; 
				System.out.print("" + rowNum + " ");
				for(int col = 0; col < width + 1; col++){
					if(vWalls[rowNum][col]){
						System.out.print("|");
					} else {
						System.out.print(" ");
					}
					if(col != width){
						//prints players
						boolean p1InSquare = p1.at(new Coordinates(col, rowNum));
						boolean p2InSquare = p2.at(new Coordinates(col, rowNum));
						if(p1InSquare && p2InSquare){
							System.out.print("X O");
						} else if(p1InSquare){
							System.out.print(" X ");
						} else if(p2InSquare){
							System.out.print(" O ");
						} else {
							System.out.print("   ");
						}
					}
				}
				System.out.println(" " + rowNum);
			}
		}
		
		//Print bottom coordinates
		System.out.print("  ");
		for(int i = 0; i < width; i++){
			System.out.print("  " + i + " ");
		}
		System.out.println();
	}
	
	//Returns 1 if player 1 has won, -1 is player 2 has, and 0 otherwise
	public int gameOver (){
		if(p1.at(p1Goal)){ return 1; }
		else if(p2.at(p2Goal)){ return -1; }
		else { return 0; }
	}
	
}
