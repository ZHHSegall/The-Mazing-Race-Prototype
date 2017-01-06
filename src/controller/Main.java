package controller;
import java.io.InputStream;
import java.util.Scanner;

import models.*;

public class Main {
	
	static int STANDARD_BOARD_SIZE = 10; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maze gameboard = new Maze(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE);
		Scanner in = new Scanner(System.in);
		
		while(gameboard.gameOver() == 0){
			gameboard.print();
			System.out.println();
			while(!gameboard.move(getMove(in))){
				System.out.println("You seem to be running into a wall. Please try another move.");
			}
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
}
