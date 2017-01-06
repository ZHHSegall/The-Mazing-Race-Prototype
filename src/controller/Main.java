package controller;
import models.*;

public class Main {
	
	static int STANDARD_BOARD_SIZE = 10; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maze gameboard = new Maze(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE);
		
		gameboard.print();
	}

}
