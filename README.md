# The Mazing Race
A game by Zachary Segall

## The Game

#### Setting up
The game starts with a 10 by 10 square board with some walls randomly placed.
Player 1's path attempts to cross from the lower lefthand corner to the upper
righthand corner.
Player 2's path attempts to cross from the lower righthand corner to the upper
lefthand corner.

#### The Goal
Each player is trying to make their opponents path as long as possible while
minimizing the length of their path. 
The game ends when both players pass without placing a wall. At that point, the player
with the shorter path wins.

#### Turns
The player can either pass their turn or place a wall one an unoccupied border 
between two tiles. Walls cannot be placed so that they prevent either player's path 
from reaching the goal.
After the wall is placed, the paths automatically update so that they find the 
shortest route between the starting point and the goal.
