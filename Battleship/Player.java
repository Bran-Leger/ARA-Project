import java.util.Scanner;

/**
 * the Player class holds all methods the player requires to fire torpedoes, place ships, check game status, and more
 */
public class Player {
    // players grid to see hits and misses
    // ~ is a neutral ocean tile, X is a hit, 0 is a miss
    private char[][] playerShotGrid = new char[][]{
            {'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
            {'1', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'}
    };

    // player grid to see where there ships are
    private int[][] playerShipGrid = new int[11][11];

    // for taking user input
    private Scanner scan = new Scanner(System.in);

    // all ships
    private Ship[] shipArray = new Ship[5];


    /**
     * the placeAllShips algorithm instantiates all the player's ship and then places them from biggest to smallest
     */
    public void placeAllShips(){
        // instantiate ships
        Ship Carrier = new Ship(5, "Carrier");
        Ship Battleship = new Ship(4, "Battleship");
        Ship Destroyer = new Ship(3, "Destroyer");
        Ship Submarine = new Ship(3, "Submarine");
        Ship PatrolBoat = new Ship(2, "PatrolBoat");

        // put into array for iteration
        shipArray[0] = Carrier;
        shipArray[1] = Battleship;
        shipArray[2] = Destroyer;
        shipArray[3] = Submarine;
        shipArray[4] = PatrolBoat;

        // place all ships
        for (Ship ship : shipArray){
            placeShip(ship);
        }
    }

    /**
     * placeShip takes a ship object, asks the user for coordinates and direction, and tries to place the ship
     * @param ship Ship object to be placed
     */
    private void placeShip(Ship ship){
        while (true){
            displayShipGrid();
            System.out.println("Select starting coordinates to place the " + ship.getName() + " (length " + ship.getLength() + ")");

            // get user input and convert to numbers
            String s = scan.nextLine();
            int[] coordinates = new int[2];
            int direction;
            convertCoordinates(s, coordinates);

            while (true){
                // get the direction the ship is facing
                System.out.println("Choose a direction for the ship to face, enter 'right' for left-to-right or 'down' for top-to-bottom: ");
                s = scan.nextLine();

                if (s.equals("right")) {
                    direction = 0;
                    break;
                }
                else if (s.equals("down")) {
                    direction = 1;
                    break;
                }
                // if input is invalid, just loop again
            }

            // check if the ship can be placed there, if not just loop again
            if (placeShip(ship, coordinates[0], coordinates[1], direction)) {
                updateGrid(ship);
                break;
            }

            // otherwise the tell the user the ship couldn't fit in the specified orientation
            else System.out.println("ERROR: Cannot place ship in specified orientation, please try again");
        }
    }

    /**
     * the overloaded version of placeShip, this once checks to see if the coordinates are valid to place a ship
     * @param ship Ship obj
     * @param row row coordinates
     * @param col col coordinates
     * @param direction the direction the ship is facing, 0 for horizontal (left-to-right), 1 for vertical (top-to-bottom)
     * @return returns true if the ship can fit, o/w returns false
     */
    private boolean placeShip(Ship ship, int row, int col, int direction){
        int length = ship.getLength();
        int freeSpaces = 0;

        // iterate through the array using the coordinates as start locations,
        // count the number of free spaces until the ship can fit, if it can't or a ship is there return false
        // o/w return true
        if (direction == 1) {

            // if the coordinates allow for the full length of the ship to fit vertically
            if (row + length <= 10) {
                for (int i = 0; i < length; i++) {
                    if (playerShipGrid[row + i][col] == 0) freeSpaces++;
                }
                if (freeSpaces == length) {
                    // set the coordinates of the ship
                    ship.setCoordinates(row, col, direction);
                    return true;
                }
                else return false;
            }
            else return false;
        }
        else {

            // if the coordinates allow for the full length of the ship to fit horizontally
            if (col + length <= 10) {
                for (int i = 0; i < length; i++) {
                    if (playerShipGrid[row][col + i] == 0) freeSpaces++;
                }
                if (freeSpaces == length) {
                    // set the coordinates of the ship
                    ship.setCoordinates(row, col, direction);
                    return true;
                }
                else return false;
            }
            else return false;
        }
    }

    /**
     * convertCoordinates takes the players inputted coordinates and returns them back into integer format
     * @param s the input string submitted by the user
     * @param coordinates an int array of size 2 that is initially empty
     */
    private void convertCoordinates(String s, int[] coordinates){
        // check the length of the string to see if they put in a ten
        if (s.length() == 2) {
            // check if it's a capital or not
            if (Character.isUpperCase(s.charAt(0))) coordinates[0] = (s.charAt(0) - 64);
            else coordinates[0] = (s.charAt(0) - 96);

            // grab the next char
            coordinates[1] = Character.getNumericValue(s.charAt(1));
        }
        else {
            // check if it's a capital or not
            if (Character.isUpperCase(s.charAt(0))) coordinates[0] = (s.charAt(0) - 64);
            else coordinates[0] = (s.charAt(0) - 96);

            // assume its a 10
            coordinates[1] = 10;
        }
    }


    /**
     * checkHit checks if the player has succesfully hit the AI's ships or not, and updates the playerShotGrid and aiShipGrid accordingly
     * @param ai an AI object
     * @param row the row coordinate
     * @param col the col coordinate
     */
    private void checkHit(AI ai, int row, int col){

        // hit confirmed
        if (ai.getCoor(row, col) != 0){
            // update Player shot grid
            System.out.println(row + " " + col);
            playerShotGrid[row][col] = 'X';

            // update AI ship grid
            ai.setCoor(row, col);

            // see if any ship was just sunk or not
            System.out.println("\nPLAYER HAS HIT");
            ai.checkShips();
        }
        else {
            playerShotGrid[row][col] = '0';
            System.out.println("\nPLAYER HAS MISSED");
        }
    }

    /**
     * fireTorpedo asks the user for input and fires at the given coordinates, calls checkHit
     * @param ai an AI object
     */
    public void fireTorpedo(AI ai){
        // Display the hits and misses grid
        displayShotGrid();

        // Ask the user for input
        System.out.println("Enter coordinates to fire a torpedo: ");
        String s = scan.nextLine();

        int[] coordinates = new int[2];
        convertCoordinates(s, coordinates);

        checkHit(ai, coordinates[0], coordinates[1]);
    }




    //
    // ~ being ocean, X a hit, 0 being a miss

    /**
     * displays an integer grid to the user
     * @param grid an integer grid
     */
    private void displayGrid(int[][] grid){
        // prints the X coordinate numbers
        System.out.print("  ");
        for (int i = 1; i < 11; i++){
            System.out.print(i + " ");
        }
        System.out.println();

        char c = 'A';
        // prints the grid and the letters
        for (int i = 1; i < 11; i++){
            System.out.print((char)(c + i - 1) + " ");
            for (int j = 1; j < 11; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * overloaded char version of displayGrid
     * @param grid a char grid
     */
    private void displayGrid(char[][] grid){
        // prints the X coordinate numbers
        System.out.print("  ");
        for (int i = 1; i < 11; i++){
            System.out.print(i + " ");
        }
        System.out.println();

        char c = 'A';
        // prints the grid and the letters
        for (int i = 1; i < 11; i++){
            System.out.print((char)(c + i - 1) + " ");
            for (int j = 1; j < 11; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * updates the player's shp grid to reflect ships being placed
     * @param ship Ship obj
     */
    private void updateGrid(Ship ship){
        if (ship.getDirection() == 0) {
            for (int i = 0; i < ship.getLength(); i++){
                // place left to right
                playerShipGrid[ship.getBackRow()][ship.getBackCol() + i] = ship.getLength();
            }
        }
        else {
            for (int i = 0; i < ship.getLength(); i++){
                // place up to down
                playerShipGrid[ship.getBackRow() + i][ship.getBackCol()] = ship.getLength();
            }
        }
    }

    /**
     * checks to see if all the player's ships are sunk or not
     * @return if all ships sunk returns false, o/w return true
     */
    public boolean GameOver(){
        for (Ship ship: shipArray){
            if (ship.isFloating()) return false;
        }
        return true;
    }

    /**
     * checks the ship array to see if any ship has just been sunk
     * @return if a ship has just been sunk, return true
     */
    public boolean checkSunk(){
        checkShip:
        for (Ship ship: shipArray){
            // ignore any already sunk ships so if shipValue is 3 it doesn't return a false positive
            if (ship.isFloating()){
                // vertical ship
                if (ship.getDirection() == 1){

                    // iterate through the length of the ship to see if its been sunk
                    for (int i = 0; i < ship.getLength(); i++){
                        // ship has at least 1 health left so skip remaining checks
                        if (playerShipGrid[ship.getBackRow() + i][ship.getBackCol()] > 0) {
                            continue checkShip;
                        }
                    }
                    // otherwise the ship has no remaining health and is sunk
                    ship.setFloating();
                    return true;
                }
                // horizontal ship
                else {

                    // iterate through the length of the ship to see if its been sunk
                    for (int i = 0; i < ship.getLength(); i++){
                        // ship has at least 1 health left so skip remaining checks
                        if (playerShipGrid[ship.getBackRow()][ship.getBackCol() + i] > 0) {
                            continue checkShip;
                        }
                    }
                    // otherwise the ship has no remaining health and is sunk
                    ship.setFloating();
                    return true;
                }
            }
        }
        return false;
    }

    // displays the player grids
    public void displayShotGrid(){
        System.out.println("\n*****PLAYER SHOT GRID*******  LEGEND (~ OCEAN) (0 MISS) (X HIT)");
        displayGrid(playerShotGrid);
        System.out.println("*****PLAYER SHOT GRID*******");
    }
    public void displayShipGrid() {
        System.out.println("\n*****PLAYER SHIP GRID*******  LEGEND (5 Carrier) (4 Battleship) (3 Submarine) (3 Battleship) (2 Patrol Boat)");
        displayGrid(playerShipGrid);
        System.out.println("*****PLAYER SHIP GRID*******");
    }

    // getter and setter methods for the playerShipGrid
    public int getCoor(int row, int col) { return playerShipGrid[row][col];}
    // sets the coordinate to -1 to mark a successful hit
    public void setCoor(int row, int col) { playerShipGrid[row][col] = -1;}


}
