package models;

import java.util.*;

public class PathFinder {
	
	//Uses A* search to test if there is a path between two points.
	
	Maze board;
	Coordinates finish;
	Coordinates[][] squares; //Uses a grid of coordinates so that we are working
							 //with the same set of objects
	Set<Coordinates> frontier;
	Set<Coordinates> explored;
	HashMap<Coordinates, Integer> gScores;
	HashMap<Coordinates, Integer> fScores;
	static char[] DIRECTIONS = {'u', 'r', 'd', 'l'};
	
	public PathFinder(Maze b, Coordinates s, Coordinates f){
		//Creates all possible nodes
		board = b;
		squares = new Coordinates[board.height][board.width];
		for(int row = 0; row < board.height; row++){
			for(int col = 0; col < board.width; col++){
				squares[row][col] = new Coordinates(col, row);
			}
		}
			
		Coordinates start = squares[s.y][s.x];
		finish = f;
		frontier = new HashSet<Coordinates>();
		explored = new HashSet<Coordinates>();
		gScores = new HashMap<Coordinates, Integer>();
		fScores = new HashMap<Coordinates, Integer>();
		
		//Add first node
		frontier.add(start);
		gScores.put(start, 0);
		fScores.put(start, start.manDistanceTo(finish));
	}
	
	public Coordinates getBestF (){
		double bestF = Double.POSITIVE_INFINITY;
		Coordinates best = null;
		for(Coordinates cur : frontier){
			int curF = fScores.get(cur); 
			if(curF < bestF){
				best = cur;
				bestF = curF;
			}
		}
		return best;
	}
	
	//Returns null if the proposed successor is invalid
	//Uses Manhattan distance for heuristic
	private Coordinates genSuccessor(Coordinates cur, char dir){
		if(board.moveIsValid(dir, cur)){
			switch(dir){
			case 'u': 
				return squares[cur.y + 1][cur.x];
			case 'r':
				return squares[cur.y][cur.x + 1];
			case 'd': 
				return squares[cur.y - 1][cur.x];
			case 'l':
				return squares[cur.y][cur.x - 1];
			}
		}
		return null;
	}
	
	public boolean findPath(){
		while(!frontier.isEmpty()){
			//If current node is finish, end
			Coordinates cur = getBestF();
			if(cur.equals(finish)) 
				return true;
			
			//Removes current node from frontier
			frontier.remove(cur);
			//Add current node to explored
			explored.add(cur);
			
			//For each possible move...
			for(char dir : DIRECTIONS){
				Coordinates succ = this.genSuccessor(cur, dir);
				//Check if the successor is legal and unexplored
				if(succ != null && !explored.contains(succ)){
					//Skip if the successor is already in the frontier
					//Otherwise put it in the frontier and add appropriate g and f scores
					boolean skip = false;
					int succG = gScores.get(cur) + 1;
					if(!frontier.contains(succ)){
						frontier.add(succ);
					} else if (succG >= gScores.get(succ)) {
						skip = true;
					}
					if(!skip){
						gScores.put(succ, succG);
						fScores.put(succ, succG + succ.manDistanceTo(finish));
					}
				}
			}
		}
	
		return false;
	}
	
	
}
