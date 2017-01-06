package controller;
import java.io.InputStream;
import java.util.Scanner;

import models.*;

public class Main {
	
	static int STANDARD_BOARD_SIZE = 10; 
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Maze gameboard = new Maze(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE);
		Scanner in = new Scanner(System.in);
		
		while(gameboard.gameOver() == 0){
			gameboard.print();
			System.out.println();
			//TODO: Move before placing wall?
			while(!gameboard.move(getMove(in))){
				System.out.println("You seem to be running into a wall. Please try another move.");
			}
			placeWall(in, gameboard);
			System.out.println();
		}
		if(gameboard.gameOver() == 1){
			System.out.println("PLAYER 1 WINS!" );
		} else {
			System.out.println("PLAYER 2 WINS!" );
		}
		in.close();
	}
	
	
	public static char getMove(Scanner in){
		boolean valid = false;
		char direction = 'c';
		while(!valid){
			System.out.println("Please enter a direction to move: (l)eft, (r)ight, (u)p, (d)own");
			//TODO Improve character verification
			String next = in.nextLine();
			if(next.length() > 0) { direction = next.toLowerCase().charAt(0); }
			valid = (direction == 'u' || direction == 'd' || direction == 'l' || direction == 'r');
			if(!valid){
				System.out.println("Not a valid direction. Please try again");
			}
		}
		return direction;
	}
	
	public static void placeWall(Scanner in, Maze board) throws Exception{
		Coordinates s1 = new Coordinates (0, 0);
		Coordinates s2 = new Coordinates (0, 0);
		boolean acceptable = false;
		boolean needS1 = true;
		boolean needS2 = true;
		while(!acceptable){
			
			System.out.println("Please enter the coordinates of two squares you want to place a wall between");
			//TODO fix input reading
			while(needS1){
				System.out.print("Square 1 Coordinates: ");
				s1 = new Coordinates(in.nextInt(), in.nextInt());
				if(!s1.within(board)){ System.out.println("Those coordinates don't fall within the board. Try again."); }
				needS1 = !s1.within(board);
			}
			while(needS2){
				System.out.print("Square 2 Coordinates: ");
				s2 = new Coordinates(in.nextInt(), in.nextInt());
				if(!s2.within(board)){ System.out.println("Those coordinates don't fall within the board. Try again."); }
				needS2 = !s2.within(board);
			}
			if(!s1.adjacent(s2)){
				System.out.println("Those coordinates don't seem to be adjacent. Try again.");
			}
			if(board.wallBetween(s1, s2)){
				System.out.println("There is already a wall between those coordinates. Try again.");
			}
			acceptable = s1.adjacent(s2) && !board.wallBetween(s1, s2);
			if(!acceptable){
				needS1 = true;
				needS2 = true;
			}
		}
		board.placeWall(s1, s2);
	}
}
