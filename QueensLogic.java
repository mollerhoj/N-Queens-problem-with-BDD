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
        buildBDD();
        //Run update invalid (Not really needed in this case, but good practice)
        updateInvalid();
    }

    public void buildBDD() {
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
        createEightRule();
    }

    //All rows should have a queen:
    private void createEightRule() {
      for (int y = 0; y < N; y++) {
        BDD sub_bdd = False;

        for (int x = 0; x < N; x++) {
          sub_bdd = sub_bdd.or(this.factory.ithVar(place(x,y)));
        }

        //sub_bdd must be true
        this.bdd = this.bdd.and(sub_bdd);
      }
    }

    private void createRules() {
      for (int x = 0; x < N; x++) {
          for (int y = 0; y < N; y++) {
              createCellRule(x,y);
          }
      }
    }

    private void createCellRule(int x,int y) {
      BDD sub_bdd = False;
      BDD rest_false_bdd = True;
      
      //All other y's must be false
      for (int yy = 0; yy < N; yy++) {
        if (y != yy) {
          rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(x,yy)));
        }
      }

      //All other x's must be false
      for (int xx = 0; xx < N; xx++) {
        if (x != xx) {
          rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y)));
        }
      }

      //All other y+xx-x must be false
      for (int xx = 0; xx < N; xx++) {
        if (x != xx) {
          if ((y+xx-x < 8) && (y+xx-x > 0)) {
            rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y+xx-x)));
          }
        }
      }

      //All other y-xx+xx must be false
      for (int xx = 0; xx < N; xx++) {
        if (x != xx) {
          if ((y-xx+x < 8) && (y-xx+x > 0)) {
            rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y-xx+x)));
          }
        }
      }

      //Either the x,y is false
      sub_bdd = sub_bdd.or(this.factory.nithVar(place(x,y)));
      //Or (if the x,y is true) the rest is false
      sub_bdd = sub_bdd.or(rest_false_bdd);

      //sub_bdd must be true
      this.bdd = this.bdd.and(sub_bdd);
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
