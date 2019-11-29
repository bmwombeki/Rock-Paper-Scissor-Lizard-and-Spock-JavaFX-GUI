import java.util.ArrayList;

public class GameLogic {

    //make a method that takes in the player choices and returns who won.

    public String whoWon(GameInfo player1, GameInfo player2){
            String whoWonMessage = "";
            String round = "";

            int p1Choice, p2Choice;


            System.out.println("Player1 played: " + player1.choice);
            System.out.println("Player2 played: " + player2.choice);

        //Player 1 win conditions
        if((player1.choice == "Spock" && player2.choice == "Scissor") || (player1.choice == "Spock" && player2.choice == "Rock")){
            player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1.choice);
        }
        if((player1.choice == "Scissor" && player2.choice == "Lizard") || (player1.choice == "Scissor" && player2.choice == "Paper")){
            player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1.choice);
        }
        if((player1.choice == "Paper" && player2.choice == "Rock") || (player1.choice == "Paper" && player2.choice == "Spock")){
            player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1.choice);
        }
        if((player1.choice == "Rock" && player2.choice == "Lizard") || (player1.choice == "Rock" && player2.choice == "Scissor")){
            player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1.choice);
        }
        if((player1.choice == "Lizard" && player2.choice == "Spock") || (player1.choice == "Lizard" && player2.choice == "Paper")){
            player1.p1Points++;
            whoWonMessage = "Player1";
            System.out.println("Player 1 won with " + player1.choice);
        }

        //player 2 win conditions
        if((player2.choice == "Spock" && player1.choice == "Scissor") || (player2.choice == "Spock" && player1.choice == "Rock")){
            player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2.choice);
        }
        if((player2.choice == "Scissor" && player1.choice == "Lizard") || (player2.choice == "Scissor" && player1.choice == "Paper")){
            player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2.choice);
        }
        if((player2.choice == "Paper" && player1.choice == "Rock") || (player2.choice == "Paper" && player1.choice == "Spock")){
            player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2.choice);
        }
        if((player2.choice == "Rock" && player1.choice == "Lizard") || (player2.choice == "Rock" && player1.choice == "Scissor")){
            player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2.choice);
        }
        if((player2.choice == "Lizard" && player1.choice == "Spock") || (player2.choice == "Lizard" && player1.choice == "Paper")){
            player2.p2Points++;
            whoWonMessage = "Player2";
            System.out.println("Player 2 won with " + player2.choice);
        }


    return whoWonMessage;

    }



}
