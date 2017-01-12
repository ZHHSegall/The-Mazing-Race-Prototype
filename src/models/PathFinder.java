package models;

import java.util.*;

public class PathFinder {
	
	private class Node {
		Coordinates loc;
		int g, f;
		Node parent;
		
		public Node(Coordinates loc){
			this.loc = loc;
			g = Integer.MAX_VALUE;
			f = Integer.MAX_VALUE;
			parent = null;
		}
		
		public Node(Coordinates loc, int g, Node parent){
			this.loc = loc;
			this.g = g;
			this.f = g + loc.manDistanceTo(finish);
			this.parent = parent;
		}
		public boolean at(Coordinates other){
			return loc.equals(other);
		}
	}
	
	//Uses A* search to test if there is a path between two points.
	
	Maze board;
	Coordinates finish;
	Node[][] squares; //Uses a grid of coordinates so that we are working
							 //with the same set of objects
	Set<Node> frontier;
	Set<Node> explored;
	static char[] DIRECTIONS = {'u', 'r', 'd', 'l'};
	
	public PathFinder(Maze b, Coordinates s, Coordinates f){
		//Creates all possible nodes
		board = b;
		squares = new Node[board.height][board.width];
		for(int row = 0; row < board.height; row++){
			for(int col = 0; col < board.width; col++){
				squares[row][col] = new Node(new Coordinates(col, row));
			}
		}
		finish = f;
		frontier = new HashSet<Node>();
		explored = new HashSet<Node>();
		
		//Add first node
		Node start = squares[s.y][s.x];
		start.g = 0;
		start.f = start.loc.manDistanceTo(finish);
		frontier.add(start);
	}
	
	public Node getBestF (){
		int bestF = Integer.MAX_VALUE;
		Node best = null;
		for(Node cur : frontier){
			int curF = cur.f; 
			if(curF < bestF){
				best = cur;
				bestF = curF;
			}
		}
		return best;
	}
	
	//Returns null if the proposed successor is invalid
	//Uses Manhattan distance for heuristic
	private Node genSuccessor(Coordinates cur, char dir){
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
			Node cur = getBestF();
			if(cur.at(finish)) 
				return true;
			
			//Removes current node from frontier
			frontier.remove(cur);
			//Add current node to explored
			explored.add(cur);
			
			//For each possible move...
			for(char dir : DIRECTIONS){
				Node succ = this.genSuccessor(cur.loc, dir);
				//Check if the successor is legal and unexplored
				if(succ != null && !explored.contains(succ)){
					//Skip if the successor is already in the frontier
					//Otherwise put it in the frontier and add appropriate g and f scores
					boolean skip = false;
					int succG = cur.g + 1;
					if(!frontier.contains(succ)){
						frontier.add(succ);
					} else if (succG >= succ.g) {
						skip = true;
					} 
					if(!skip){
						succ.g = succG;
						succ.f = succG + succ.loc.manDistanceTo(finish);
						succ.parent = cur;
					}
				}
			}
		}
	
		return false;
	}
	
	public List<Coordinates> unrollPath (Node n){
		List<Coordinates> ret = new LinkedList<Coordinates>();
		while(n != null){
			ret.add(n.loc);
			n = n.parent;
		}
		return ret;
	}
}
