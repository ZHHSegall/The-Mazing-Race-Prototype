package models;

import java.util.*;

public class PathFinder {
	private class Node {
		Node parent;
		Coordinates loc;
		float f, g, h;
		
		protected Node (Node parent, Coordinates loc, float h){
			this.parent = parent;
			this.loc = loc;
			this.h = h;
			this.g = (parent == null) ? 0 : parent.g + 1;
			this.f = this.h + this.g;
		}
		
		protected boolean at(Coordinates other){
			return loc.equals(other);
		}
		
		protected boolean at(Node other){
			return loc.equals(other.loc);
		}
	}
	
	//Uses A* search to test if there is a path between two points.
	
	Maze board;
	Coordinates start;
	Coordinates finish;
	List<Node> frontier; //Open list - ordered wrt smallest f
	HashMap<Coordinates, Node> frontierMap;
	HashMap<Coordinates, Node> explored; //Closed list - ordered wrt smallest f
	static char[] DIRECTIONS = {'u', 'r', 'd', 'l'};
	
	public PathFinder(Maze b, Coordinates s, Coordinates f){
		board = b;
		start = s;
		finish = f;
		frontier = new ArrayList<Node>();
		frontierMap = new HashMap<Coordinates, Node>();
		explored = new HashMap<Coordinates, Node>();
		frontier.add(new Node(null, s, 0));
		frontierMap.put(s, frontier.get(0));
	}
	

	public static void insertNode(List<Node> lst, Node n){
		if(lst.size() == 0){
			lst.add(n);
		} else {
			//Binary insertion search
			int low = 0, high = lst.size();
			int mid = (high - low)/2 + low;
			while(high > low){
				if(n.f > lst.get(mid).f){
					low = mid + 1;
				} else if (n.f <= lst.get(mid).f){
					high = mid - 1;
				}
				mid = (high - low)/2 + low;
			}
			lst.add(low, n);
		}
	}
	
	//Returns null if the proposed successor is invalid
	//Uses Manhattan distance for heuristic
	private Node genSuccessor(Node cur, char dir){
		Node succ = null;
		Coordinates succLoc = new Coordinates(0,0); 
		if(board.moveIsValid(dir, cur.loc)){
			switch(dir){
			case 'u': 
				succLoc = new Coordinates(cur.loc.x, cur.loc.y + 1);
				break;
			case 'r':
				succLoc = new Coordinates(cur.loc.x + 1, cur.loc.y);
				break;
			case 'd': 
				succLoc = new Coordinates(cur.loc.x, cur.loc.y - 1);
				break;
			case 'l':
				succLoc = new Coordinates(cur.loc.x - 1, cur.loc.y);
				break;
			}
			succ = new Node(cur, succLoc, succLoc.manDistanceTo(finish));
		}
		return succ;
	}
	
	public boolean findPath(){
		while(frontier.size() > 0){
			//Removes current node from frontier
			Node cur = frontier.remove(0);
			frontierMap.remove(cur.loc);
			//Add current node to explored list
			explored.put(cur.loc, cur);
			
			System.out.println("Cur Coordinates: (" + cur.loc.x + ", " + cur.loc.y + "), f = " + cur.f 
					+ " g = " + cur.g + " h = " + cur.h);
			for(char dir : DIRECTIONS){
				Node succ = this.genSuccessor(cur, dir);
				if(succ != null){
					if(succ.at(finish)) 
						return true;
					
					//If a node with the same location is in the frontier or explored list, skip
					if(!(frontierMap.containsKey(succ.loc) || explored.containsKey(succ.loc))){ 
						insertNode(frontier, succ); 
						frontierMap.put(succ.loc, succ);
					}
				}
			}
			
		}
	
		return false;
	}
	
	
}
