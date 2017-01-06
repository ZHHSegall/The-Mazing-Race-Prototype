package models;

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
	
	public Maze (int h, int w){
		height = h;
		width = w;
		turn = 0;
		
		hWalls = new boolean[h + 1][w];
		vWalls = new boolean[h][w + 1];
		
		p1 = new Player(0, 0);
		p2 = new Player(w - 1, 0);
		
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
		
		p1Turn = !p1Turn;
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
	
	public boolean placeWall (Coordinates s1, Coordinates s2){
		if(!s1.adjacent(s2) || wallBetween(s1, s2)){ return false; }
		//Horizontal case
		if(s1.x == s2.x){
			int maxY = Math.max(s1.y, s2.y);
			hWalls[maxY][s1.x] = true; 
		} 
		//Vertical case
		else if(s1.y == s2.y){
			int maxX = Math.max(s1.x, s2.x);
			vWalls[s1.y][maxX] = true;
		}
		return true;
	}
	
	public void print (){
		//Print top coordinates
		int playerTurn = p1Turn ? 1 : 2;
		System.out.println("Turn: " + turn++ + "-- PLAYER " + playerTurn + "'S MOVE");
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
				System.out.println(rowNum);
			}
		}
		
		//Print top coordinates
		System.out.print("  ");
		for(int i = 0; i < width; i++){
			System.out.print("  " + i + " ");
		}
		System.out.println();
	}
	
	//Returns 1 if player 1 has won, -1 is player 2 has, and 0 otherwise
	public int gameOver (){
		if(p1.at(new Coordinates(width - 1, height - 1))){ return 1; }
		else if(p2.at(new Coordinates(0, height - 1))){ return -1; }
		else { return 0; }
	}
}
