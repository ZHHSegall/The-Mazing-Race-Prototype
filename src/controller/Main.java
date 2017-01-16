package controller;
import java.io.InputStream;
import java.util.Scanner;

import models.*;

public class Main {
	
	static int STANDARD_BOARD_SIZE = 10; 
	static double STARTING_WALL_DENSITY = .2; 
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Maze gameboard = new Maze(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE);
		gameboard.seed(STARTING_WALL_DENSITY);
		Scanner in = new Scanner(System.in);
		boolean p1Pass = false, p2Pass = false;
		boolean playerTurn;
		
		while(p1Pass == false || p2Pass == false){
			//Printing Board
			gameboard.print(); //wall 
			System.out.println(); 
			boolean pass = !makeWall(in);
			//Wall placing
			if(!pass){
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
			} 
			if(gameboard.isP1Turn())
				p1Pass = pass;
			else 
				p2Pass = pass;
			
			in.nextLine(); //Clears in buffer after placing a wall
			
			System.out.println();
		}
		
		switch(gameboard.winner()){
		case 1:
			System.out.println("PLAYER X WINS!" );
			break;
		case 2: 
			System.out.println("PLAYER O WINS!" );
			break;
		default:
			System.out.println("TIE!" );	
		}
		in.close();
	}
	
	
	public static boolean makeWall(Scanner in){
		boolean valid = false;
		char move = 'c';
		while(!valid){
			System.out.println("Please choose to (w)all or (p)ass");
			//TODO Improve character verification
			//TODO (Bug) You need to press enter twice for the game to understand a (p)ass
			String next = in.nextLine();
			if(next.length() > 0) { move = next.toLowerCase().charAt(0); }
			valid = (move == 'w' || move == 'p');
			if(!valid){
				System.out.println("Not a option. Please try again");
			}
		}
		return move == 'w';
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
