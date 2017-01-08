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
			//Printing Board
			gameboard.print();
			System.out.println();
			
			//Movement
			//TODO: Display move before placing wall?
			while(!gameboard.move(getMove(in))){
				System.out.println("You seem to be running into a wall. Please try another move.");
			}
			
			//Wall placing
			int validWall = -1;
			Coordinates s1, s2; //Squares between which the wall will be placed
			while(validWall != 0){
				System.out.println("Enter two squares to place a wall between.");
				s1 = getCoordinates(in, gameboard, "Square 1");
				s2 = getCoordinates(in, gameboard, "Square 2");
				validWall = gameboard.placeWall(s1, s2);
				switch(validWall){
				case 1: 
					System.out.println("Those coordinates don't seem to be adjacent. Try again.");
					break;
				case 2:
					System.out.println("There is already a wall between those coordinates. Try again.");
					break;
				case 3: 
					System.out.println("That wall prevents a player from reaching their goal. Try again.");
					break;
				}
				
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
	
	public static Coordinates getCoordinates(Scanner in, Maze board){
		Coordinates square = new Coordinates(-1, -1);
		while(!square.within(board)){
			System.out.print("Enter Coordinates: ");
			square.set(in.nextInt(), in.nextInt());
			if(!square.within(board)){ System.out.println("Those coordinates don't fall within the board. Try again."); }
		}
		return square;
	}
	
	public static Coordinates getCoordinates(Scanner in, Maze board, String name){
		Coordinates square = new Coordinates(-1, -1);
		while(!square.within(board)){
			System.out.print("Enter " + name  + " Coordinates: ");
			square.set(getInt(in), getInt(in));
			if(!square.within(board)){ System.out.println("Those coordinates don't fall within the board. Try again."); }
		}
		return square;
	}
	
	public static int getInt(Scanner in){
		while(!in.hasNextInt()){
			in.next();
			System.out.println("Please enter integers");
		}
		return in.nextInt();
	}
}
