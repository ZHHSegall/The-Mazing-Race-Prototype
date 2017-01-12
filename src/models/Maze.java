package models;

import java.util.List;
import java.util.Random;

public class Maze {
	
	private class Player {
		int score;
		Coordinates start;
		Coordinates goal;
		
		protected Player (Coordinates start, Coordinates goal){
			score= 0;
			this.start = start;
			this.goal = goal;
		}
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
	int[][] paths;
	boolean p1Turn = true;
	
	public Maze (int h, int w){
		height = h;
		width = w;
		turn = 0;
		
		hWalls = new boolean[h + 1][w];
		vWalls = new boolean[h][w + 1];
		
		p1 = new Player(new Coordinates (0, 0), new Coordinates(w - 1, h - 1));
		p2 = new Player(new Coordinates (w - 1, 0), new Coordinates(0, h - 1));
		paths = new int[height][width];
		
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
	
	public void plotPaths(List<Coordinates> p1Path, List<Coordinates> p2Path){
		p1.score = p1Path.size();
		p2.score = p2Path.size();
		
		//Initialize all squares to zero in path
		for(int[] row : paths)
			for(int square : row)
				square = 0;
						
		//Plot player paths onto grid.
		for(Coordinates p1Squares : p1Path)
			paths[p1Squares.y][p1Squares.x]++;
		for(Coordinates p2Squares : p2Path)
			paths[p2Squares.y][p2Squares.x] += 2;			
	}
	
	//returns 0 for valid path
	//		  1 for squares not adjacent
	//		  2 for wall already present
	//        3 for wall blocks a path
	public int placeWall (Coordinates s1, Coordinates s2){
		//Pathfinders, paths, and max coordinates
		PathFinder p1PathFinder = new PathFinder(this, p1.start, p1.goal);
		PathFinder p2PathFinder = new PathFinder(this, p2.start, p2.goal);
		List<Coordinates> p1Path,  p2Path;
		int maxY = Math.max(s1.y, s2.y);
		int maxX = Math.max(s1.x, s2.x);
		
		if(!s1.adjacent(s2))
			return 1;
		if(wallBetween(s1, s2))
			return 2;
		
		//If adjacent and not horizontal, then vertical
		boolean horizontal = (s1.x == s2.x); 
		if(horizontal)
			hWalls[maxY][s1.x] = true;
		else 
			vWalls[s1.y][maxX] = true;

		p1Path = p1PathFinder.findPath();
		p2Path = p2PathFinder.findPath();
		boolean noPath = p1Path == null || p2Path == null;
		
		//undo bad wall placement and return failure
		if(noPath){
			if(horizontal)
				hWalls[maxY][s1.x] = false;
			else
				vWalls[s1.y][maxX] = false;
			return 3;
		}
		
		plotPaths(p1Path, p2Path);
		return 0;
	}
	
	//Seeds random walls on the board
	public void seed (double density){
		Random rng = new Random();
		boolean valid = false;
		List<Coordinates> p1Path = null, p2Path = null;
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
			PathFinder p1PathFinder = new PathFinder(this, p1.start, p1.goal);
			PathFinder p2PathFinder = new PathFinder(this, p2.start, p2.goal);
			p1Path = p1PathFinder.findPath();
			p2Path = p2PathFinder.findPath();
			valid = p1Path != null && p2Path != null;
		}
		plotPaths(p1Path, p2Path);
	}
	
	public void print (){
		//Print top coordinates
		int playerTurn = p1Turn ? 1 : 2;
		System.out.println("Turn: " + turn++ + " -- PLAYER " + playerTurn + " -- WALL");
		System.out.println("PLAYER 1 SCORE: " + p1.score + "\t\t" + "PLAYER 2 SCORE: " + p2.score);
		p1Turn = !p1Turn;
		
		System.out.println();
		System.out.print("  ");
		for(int i = 0; i < width; i++){
			System.out.print("  " + i +" ");
		}
		System.out.println();
		
		//Print board from top to bottom
		//TODO add characters
		for(int line = 2 * height; line >= 0; line--){
			if(line % 2 == 0){
			//If we are not printing tiles...
				int row = line/2;
				
				System.out.print("  ");
				for(int col = 0; col < width; col++){
					if(hWalls[row][col]){
						System.out.print("+ - ");
					} else {
						System.out.print("+   ");
					}
				}
				System.out.println("+");
			} else {
			// If we are printing tiles...
				int row = (line - 1)/2; 
				System.out.print("" + row + " ");
				for(int col = 0; col < width + 1; col++){
					if(vWalls[row][col]){
						System.out.print("|");
					} else {
						System.out.print(" ");
					}
					if(col != width){
						//prints player paths
						switch(paths[row][col]){
						case 3:
							System.out.print("X O");
							break;
						case 2:
							System.out.print(" O ");
							break;
						case 1:
							System.out.print(" X ");
							break;
						default:
							System.out.print("   ");
						}
					}
				}
				System.out.println(" " + row);
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
		return 0;
	}
	
}
