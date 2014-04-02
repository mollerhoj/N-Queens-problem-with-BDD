N-Queens problem with BDD
=========================
by Jens Dahl Mollerhoj

This java program uses JavaBDD to solve the nqueens problem. A GUI lets the
user see how her decisions affects the remaining solutions. A so called "Interactive configurator"

This project was build for an AI class at the IT-university:
"Intelligent Systems Programming 2013"

The following is a 'Project report'. (I have to make one for my course, if you
want to keep your sanity, I would stop reading now.)

Project report
==============

### Exercise goals

Learning to use a BDD library in order to solve a specific problem. I was
forced to do this in java. Luckily, my proffessors provided nice GUI
implementation. I spend a lot of time finding out how to compile the jar,
and how to mark a cell on the game board as invalid (set the cells int value to -1).
The JavaBDD library turned out to be surpricingly straightforward to use, after the usual java library problems.

I have not provided any tests, I believe it is the idiomatic java style. Also, my methods are long, and I use a lot of nested for loops, so this is definately idiomatic java.

### Methods
A description of my methods
#### buildBDD() 
Set up the BDD, Factory and all the other library initalization code. Also apply the rules and update the board accordingly.
#### createEightRule() 
Make sure that there is a queen in every row.
#### createRules() 
Make sure that no queen can take another one.
#### createCellRule(int x,int y) 
If there is a queen in this cell, make sure that she cannot be taken.
#### place(int column,int row) 
Given a column and a row, give the variable number in the BDD (0-63)
#### placeInvalid(int column,int row) 
Check if the BDD is solveable after a queen has been placed here. 
#### updateInvalid() 
Update the gui to show what cells that you can no longer place a queen in.

### Implemenation idea
My solutions consist of two basic rules. First, the rules makes sure that there is a queen in every row of the board.
Second, for every cell in the board, the rules make sure that either there is no queen in that cell, or (if there is a queen) that
there is no queen in any of the cells that the queen would be able to take.

The first rule is defined in the createEightRule() method. For every row, the following must hold:
There must be one of the cells in that row that contains a queen.

The second rule is defined in the createRules() method. For every cell, the following must hold:
If there is no queen in the cell, skip the rest.
If there is a queen, there can not be any queens in the cells in the same row.
If there is a queen, there can not be any queens in the cells in the same column.
If there is a queen, there can not be any queens in the cells in the same diagonals.

### How to compile and run the code:
You need the javabdd-1.0b2.jar from JavaBDDs website to be in the same folder as the project. Then run the following command:
````
javac -cp "javabdd-1.0b2.jar:." *.java && java -cp "javabdd-1.0b2.jar:." ShowBoard
````

### Some thoughts
The BDD turns out to be a very easi way to allow for interactive restrictions. However, it is seldom clear WHY a given cell is removed from the space of solutions. It would be interesting to see a tool that allowed the user to see WHY the a given cell can no longer contain a queen.
A nice part of using a BDD library is that a lot of optimisations have already been provided. The interface is as responsive as you could expect from a swing program.

### Conclusion
It has been fun to work with the BDD library. Eventhough java is so ugly, I actually had a lot of fun building this.

### License
DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE: www.wtfpl.net
