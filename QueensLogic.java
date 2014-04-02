/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import net.sf.javabdd.*;

public class QueensLogic {
    private int x = 0;
    private int y = 0;
    private int[][] board;

    //my vars:
    private BDDFactory factory;
    private BDD True;
    private BDD False;
    private BDD bdd;
    private int N = 8;

    public QueensLogic() {
       //constructor
    }

    public void initializeGame(int size) {
        this.x = size;
        this.y = size;
        this.board = new int[x][y];
        System.out.println("initialize Game");
        buildBDD();
    }

    public void buildBDD() {
        System.out.println("build BDD");

        //Factory
        this.factory = JFactory.init(2000000, 200000); // set buffer etc.

        //64 fields => 64 variables
        this.factory.setVarNum(this.N*this.N);

        //For clarity
        this.False = this.factory.zero();
        this.True = this.factory.one();

        //Initalize our bdd to true
        this.bdd = True;

        //Add the rules to the bdd
        createRules();

        //Run update invalid (Not really needed in this case, but good practice)
        updateInvalid();
    }

    private void createRules() {
      createColumnRules();
    }

    private void createRowRules() {
        for (int row = 0; row < N; row++) {
          BDD column_bdd = False;
          for (int column = 0; column < N; column++) {
            // The column must be true, the rest must be false:
            BDD column_solution_bdd = True;
            column_solution_bdd = column_solution_bdd.and(this.factory.ithVar(place(column,row)));
            for (int x = 0; x < N; x++) {
              if (x != column) {
                column_solution_bdd = column_solution_bdd.and(this.factory.nithVar(place(x,row)));
              }
            }
            //Add solution as a possibility to main bdd
            column_bdd = column_bdd.or(column_solution_bdd);
          }
          //Add column bdd to main bdd
          this.bdd = this.bdd.and(column_bdd);
        }
    }

    private void createColumnRules() {
        for (int row = 0; row < N; row++) {
          BDD column_bdd = False;
          for (int column = 0; column < N; column++) {
            // The column must be true, the rest must be false:
            BDD column_solution_bdd = True;
            column_solution_bdd = column_solution_bdd.and(this.factory.ithVar(place(column,row)));
            for (int x = 0; x < N; x++) {
              if (x != column) {
                column_solution_bdd = column_solution_bdd.and(this.factory.nithVar(place(x,row)));
              }
            }
            //Add solution as a possibility to main bdd
            column_bdd = column_bdd.or(column_solution_bdd);
          }
          //Add column bdd to main bdd
          this.bdd = this.bdd.and(column_bdd);
        }
    }

    private int place(int column,int row) {
      return row*this.N+column;
    }

    private boolean placeInvalid(int column,int row) {
      //add queen at x ,y
      BDD test_bdd = this.bdd.restrict(this.factory.ithVar(place(column,row))); 
      //check if unsatisfiable?
      return test_bdd.isZero();
    }
   
    public int[][] getGameBoard() {
        System.out.println("get GameBoard");
        return board;
    }

    private void updateInvalid() {
        //For each cell, check if placing a queen there makes it invalid
        //If so, make that places graphic be -1
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (placeInvalid(i,j)) {
                  board[i][j] = -1;
                }
            }
        }
    }

    public boolean insertQueen(int column, int row) {
        System.out.println("insertQueen");

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        //Set a queen in graphic
        board[column][row] = 1;

        //Set a queen in the bdd
        this.bdd = this.bdd.restrict(this.factory.ithVar(row*this.N+column)); 

        updateInvalid();

        return true;
    }
}
