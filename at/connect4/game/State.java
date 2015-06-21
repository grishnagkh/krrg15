package at.connect4.game;

import java.util.ArrayList;


public class State implements Cloneable{
//      public int playerID;
//      public int opponentID;
        public int[][] gameBoard;
        public int lastRow;
        public int lastCol;
        public ArrayList<Integer> openCols;
        
        public State(State s) {
//              this.playerID = s.playerID;
                this.lastCol = s.lastCol;
                this.lastRow = s.lastRow;
                
                this.gameBoard = new int[s.gameBoard.length][s.gameBoard[0].length];
                for (int col = 0; col < gameBoard.length; col++) {
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                this.gameBoard[col][row] = s.gameBoard[col][row];
                        }       
                }
                
                this.openCols = new ArrayList<Integer>(s.openCols.size());
                for (int i = 0; i < s.openCols.size(); i++) {
                        this.openCols.add(s.openCols.get(i));
                }
        }
        
        public State(int cols, int rows){
//              this.playerID = playerID;
                //this.opponentID = 
                this.gameBoard = new int[cols][rows];
                this.openCols = new ArrayList<Integer>(cols);
                for (int i = 0; i < cols; i++) {
                        this.openCols.add(i);
                }
        }
        
        public void insertCoin(int col, int playerID) {
                int row;
                for (row = 0; row < this.gameBoard[0].length; row++) {
                        if (this.gameBoard[col][row] == 0) {
                                this.gameBoard[col][row] = playerID;
                                this.lastRow = row;
                                this.lastCol = col;
                                break;
                        }
                }
                if (row + 1 == gameBoard[0].length) {
                        this.openCols.remove((Integer)col);
                }
        }
        
        void printAll() {
                for (int row = gameBoard[0].length - 1; row >= 0; row--) {
                        for (int col = 0; col < gameBoard.length; col++) {
                                System.out.print(gameBoard[col][row]);
                        }       
                        System.out.println();
                }
                System.out.println("lastCol: " + lastCol + ", lastRow: " + lastRow);
//              System.out.println("playerID: " + playerID);
        }
        
        private static boolean hashTrace = false;
        public int hashCode() {
                final int PRIME = 31;
                int totalResult = 0;
                int result1 = 1;

                for (int col = 0; col < gameBoard.length / 2; col++) {
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                result1 = result1 * PRIME + col;
                                result1 = result1 * PRIME + row;
                                result1 = result1 * PRIME + gameBoard[col][row];
                                if (hashTrace)System.out.println(col + ", " + row + "," + gameBoard[col][row]);
                        }       
                }
                if (hashTrace)System.out.println("\nhalf 1: " + result1 + "\n");
                int result2 = 1;
                int trans = gameBoard.length - 1;
                for (int col = gameBoard.length - 1; col >= (gameBoard.length + 1) / 2; col--) {
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                result2 = result2 * PRIME + col - trans;
                                result2 = result2 * PRIME + row;
                                result2 = result2 * PRIME + gameBoard[col][row];
                                if (hashTrace)System.out.println((col - trans) + ", " + row + "," + gameBoard[col][row]);
                        }
                        trans -= 2;
                }
                if (hashTrace)System.out.println("half 2: " + result2 + "\n");

                totalResult = result1 + result2;
                
                
                boolean oddNumberCols = gameBoard.length % 2 == 1 ? true : false;
                if (oddNumberCols) {
                        int midCol = gameBoard.length / 2;
                        if (hashTrace)System.out.println("mid COL: " + midCol + " ----------------");
                        int result3 = 1;
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                result3 = result3 * PRIME + midCol;
                                result3 = result3 * PRIME + row;
                                result3 = result3 * PRIME + gameBoard[midCol][row];
                                totalResult += result3;
                                if (hashTrace)System.out.println((midCol + ", " + row + "," + gameBoard[midCol][row]));
                        }
                        if (hashTrace)System.out.println("\nmiddle: " + result3 + "\n");
                }
                
                if (hashTrace)System.out.println("total: " + totalResult);
                return totalResult;
        }

        public boolean equals(Object o) {
                boolean equal = true;
                State other = (State) o;

                for (int col = 0; col < gameBoard.length; col++) {
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                equal = equal && this.gameBoard[col][row] == other.gameBoard[col][row];
                                if (!equal) {
                                        break;
                                }
                        }       
                        if (!equal) {
                                break;
                        }
                }
                if (equal) {
                        return true;
                }
                equal = true;
                int trans = gameBoard.length - 1;
                for (int col = 0; col < gameBoard.length; col++) {
                        for (int row = 0; row < gameBoard[0].length; row++) {
                                equal = equal && this.gameBoard[col][row] == other.gameBoard[col + trans][row];
                                if (!equal) {
                                        break;
                                }
                        }
                        if (!equal) {
                                break;
                        }
                        trans -= 2;
                }

                return equal;
        }
//      public Object clone(){
//              at.connect4.game.State result = this;
//              try {
//                      result = (at.connect4.game.State) super.clone();
//              } catch (CloneNotSupportedException e) {
//                      // TODO Auto-generated catch block
//                      e.printStackTrace();
//              }
//              result.gameBoard = this.gameBoard.clone();
//              result.lastRow = this.lastRow;
//              result.lastCol = this.lastCol;
//              result.openCols = (ArrayList<Integer>) this.openCols.clone();
//              return result;
//      }
}