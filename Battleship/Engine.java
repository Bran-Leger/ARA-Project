
// The Engine class drives the main events of the game Battleship
// it in effect, schedules the player and the AI's turn order
public class Engine {
    public static void main(String[] args) {
        // Starting message
        System.out.println("Welcome to Battleships! The goal is to sink the AI ships. Make sure to spread out your ships and avoid clumping.");

        // initializing AI
        AI ai = new AI();
        ai.placeAllShips();
        // uncomment to see where the AI has placed it's ships
        // ai.displayShipGrid();

        // initializing player
        Player p = new Player();
        p.placeAllShips();

        // loop game until someone wins
        while (true){
            p.displayShipGrid();
            p.fireTorpedo(ai);
            if (ai.GameOver()) break;
            ai.aiFireTorpedo(p);
            ai.displayShotGrid();
            if (p.GameOver()) break;
        }

        // Check who won
        if (ai.GameOver()) System.out.println("Well Done! You have won!");
        else {
            System.out.println("Sorry! You have lost!");
        }
    }
}
