package at.connect4.game;

import at.connect4.solver.ISolver;

public class GameLogic{

        public int playerID;
        public int opponentID;
        public State board;
        public final static int tokensToWin = 4;
        //at.connect4.solver.Minimax minimax;
        ISolver solver = null;

        public void initializeGame(int cols, int rows, int playerID){
                this.playerID = playerID;
                opponentID = playerID == 1 ? 2 : 1;
                board = new State(cols, rows);
                //minimax = new at.connect4.solver.Minimax(playerID, opponentID, this);
        }
        
        public void setSolver(ISolver solver){
            this.solver = solver;
        }
        
        public int getPlayerID(){
            return this.playerID;
        }
    
        public int getOpponentID(){
            return this.opponentID;
        }

        // Called after every turn 
        // Implement efficient algorithm starting a search from
        // most recent disk entered
        public int gameFinished() {
                return TerminalTest(board);
        }

        public void insertCoin(int col, int playerID) {
                board.insertCoin(col, playerID);
        }

        // at.connect4.solver.Minimax + evaluation function
        public int decideNextMove() {
                //return minimax.getDecision(board);
            return this.solver.getDecision(board);
        }
        
        public int TerminalTest(State s){
                int currentRow = s.lastRow;
                int currentCol = s.lastCol;
                int count = 1;
                if (currentCol > 0){ //Checks left horizontally
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[--currentCol][currentRow]){
                                count++;
                                if (currentCol < 1) break;
                        }
                }
                currentCol = s.lastCol;
                if (currentCol < s.gameBoard.length-1){//Checks right horizontally
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[++currentCol][currentRow]){
                                count++;
                                if (currentCol > s.gameBoard.length-2) break;
                        }
                }
                if (count >= tokensToWin) return s.gameBoard[s.lastCol][s.lastRow];
                currentRow = s.lastRow;
                currentCol = s.lastCol;
                count = 1;
                if (currentRow > 0){ //Checks vertically
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[currentCol][--currentRow]){
                                count++;
                                if (currentRow < 1) break;
                        }
                }
                if (count >= tokensToWin) return s.gameBoard[s.lastCol][s.lastRow];
                currentRow = s.lastRow;
                currentCol = s.lastCol;
                count = 1;
                if (currentRow > 0 && currentCol > 0){ //Checks diagonally lower left to upper right, downwards
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[--currentCol][--currentRow]){
                                count++;
                                if (currentRow < 1 || currentCol < 1) break;
                        }
                }
                currentRow = s.lastRow;
                currentCol = s.lastCol;
                if (currentRow < s.gameBoard[currentRow].length-1 && currentCol < s.gameBoard.length-1){ //Checks diagonally lower left to upper right, upwards
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[++currentCol][++currentRow]){
                                count++;
                                if (currentRow > s.gameBoard[currentRow].length-2 || currentCol > s.gameBoard.length-2) break;
                        }
                }
                if (count >= tokensToWin) return s.gameBoard[s.lastCol][s.lastRow];
                currentRow = s.lastRow;
                currentCol = s.lastCol;
                count = 1;
                if (currentRow > 0 && currentCol < s.gameBoard.length-1){ //Checks diagonally upper left to lower right, downwards
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[++currentCol][--currentRow]){
                                count++;
                                if (currentRow < 1 || currentCol > s.gameBoard.length-2) break;
                        }
                }
                currentRow = s.lastRow;
                currentCol = s.lastCol;
                if (currentRow < s.gameBoard[currentRow].length-1 && currentCol > 0){ //Checks diagonally upper left to lower right, upwards
                        while (s.gameBoard[currentCol][currentRow] == s.gameBoard[--currentCol][++currentRow]){
                                count++;
                                if (currentRow > s.gameBoard[currentRow].length-2 || currentCol < 1) break;
                        }
                }
                if (count >= tokensToWin) return s.gameBoard[s.lastCol][s.lastRow];
                // Checks if there is a tie i.e. board if full
                if (s.openCols.isEmpty()) {
                        return 3;
                }
                return 0; //No winner
        }

}