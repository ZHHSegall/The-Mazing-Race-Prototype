package models;

public class Maze {
	
	private class Player {
		int x; 
		int y;
		
		Player (int x, int y){
			this.x = x;
			this.y = y;
		}
		
		//Movement - named for clarity in coe
		void up (){ y++; }
		void down () { y--; }
		void left () { x--; }
		void right () { x++; }
		
		boolean at(int x, int y){ return this.x == x && this.y == y; } 
		
	}
	
	//Board properties
	int width;
	int height;
	
	//Board
	//Don't forget: 2D arrays are [row][col]
	boolean[][] hWalls; //horizontal walls
	boolean[][] vWalls; //vertical walls
	
	//Players
	Player p1;
	Player p2;
	
	public Maze (int h, int w){
		height = h;
		width = w;
		
		hWalls = new boolean[h + 1][w];
		vWalls = new boolean[h][w + 1];
		
		p1 = new Player(0, 0);
		p2 = new Player(0, w - 1);
		
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
	
	public void print (){
		//Print top coordinates
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
				int rowNum = (row -1)/2; 
				System.out.print("" + rowNum + " ");
				for(int col = 0; col < width + 1; col++){
					if(vWalls[rowNum][col]){
						System.out.print("|   ");
					} else {
						System.out.print("    ");
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
		if(p1.at(width - 1, height - 1)){ return 1; }
		else if(p2.at(0, height - 1)){ return -1; }
		else { return 0; }
	}
}
