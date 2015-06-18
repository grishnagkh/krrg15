import javax.swing.*;

public class ShowGame{

    public static void main(String[] arg){   
        int cols=7;//7;
        int rows=6;//6;

        GameLogic player1 = new GameLogic();
        GameLogic player2 = new GameLogic();
        
        player1.initializeGame(cols,rows,1);
        player2.initializeGame(cols,rows,-1);

    
		player1.setSolver(new Negamax(player1.getPlayerID(), player1, 3, new MCEval(player1)));    
		player2.setSolver(new Negamax(player2.getPlayerID(), player2, 4, new PieceCounter(player2)));    

//        player1.setSolver(new Minimax(player1.getPlayerID(), player1.getOpponentID(), player1, 1));
  //      player2.setSolver(new Minimax(player2.getPlayerID(), player2.getOpponentID(), player2, 4));

        try{
            FourConnectGUI g = new FourConnectGUI(player1,player2,cols,rows);
            JFrame f = new JFrame();
            f.setSize(1000,800);
            f.setTitle("Connect 4");
            f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(g);    
            f.setVisible(true);
        }
        catch(Exception e){
                System.out.println("There has been an error in the implementation of your GameLogic");
        }
    }
}
