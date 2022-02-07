// the ai class hosts all actions the computer is able to do
public class AI {

    // the ai's grid of ships
    public int[][] aiShipGrid = new int[11][11];

    // all the ai's ships
    private Ship[] shipArray = new Ship[5];

    // grid to keep track of hits and misses
    private char[][] aiShotGrid = new char[][]{
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



    // variables for determining and tracking the player's ships
    private boolean foundPlayerShip = false;
    private int playerShipRow;
    private int playerShipCol;
    private int playerShipDirection = -1; // -1 for undetermined, 0 for horizontal, 1 for vertical
    private boolean checkUp = false;
    private boolean checkDown = false;
    private boolean checkLeft = false;
    private boolean checkRight = false;

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


        shipArray[0] = Carrier;
        shipArray[1] = Battleship;
        shipArray[2] = Destroyer;
        shipArray[3] = Submarine;
        shipArray[4] = PatrolBoat;

        // place all ships
        for (Ship ship : shipArray){
            placeShip(ship);
            updateGrid(ship);
        }
    }

    /**
     * placeShip takes a ship object, randomly tries to place it
     * @param ship Ship object to be placed
     */
    public void placeShip(Ship ship){
        int length = ship.getLength();

        while (true){
            // range of 10, min 1
            int rowCoor = (int)(Math.random() * 10) + 1;
            int colCoor = (int)(Math.random() * 10) + 1;

            // determine a random direction, 1 is vertical, 0 is horizontal
            int direction = (int)(Math.random() * 2);

            // free spaces available to the ship
            int freeSpaces = 0;

            // iterate through the array using the random coordinates as start locations,
            // count the number of free spaces until the ship can fit, if it can't or a ship is there, try again
            // with different coordinates
            if (direction == 1) {

                // if the coordinates allow for the full length of the ship to fit vertically
                if (rowCoor + length <= 10) {
                    for (int i = 0; i < length; i++) {
                        if (aiShipGrid[rowCoor + i][colCoor] == 0) freeSpaces++;
                    }
                    if (freeSpaces == length) {
                        // set the coordinates of the ship
                        ship.setCoordinates(rowCoor, colCoor, direction);
                        return;
                    }
                }
            }
            else {

                // if the coordinates allow for the full length of the ship to fit horizontally
                if (colCoor + length <= 10) {
                    for (int i = 0; i < length; i++) {
                        if (aiShipGrid[rowCoor][colCoor + i] == 0) freeSpaces++;
                    }
                    if (freeSpaces == length) {
                        // set the coordinates of the ship
                        ship.setCoordinates(rowCoor, colCoor, direction);
                        return;
                    }
                }
            }
        }
    }

    /**
     * updateGrid updates changes to AI's ship grid
     * @param ship Ship obj
     */
    public void updateGrid(Ship ship){
        if (ship.getDirection() == 0) {
            for (int i = 0; i < ship.getLength(); i++){
                // place left to right
                aiShipGrid[ship.getBackRow()][ship.getBackCol() + i] = ship.getLength();
            }
        }
        else {
            for (int i = 0; i < ship.getLength(); i++){
                // place up to down
                aiShipGrid[ship.getBackRow() + i][ship.getBackCol()] = ship.getLength();
            }
        }
    }

    /**
     * displayGrid displays any int grid
     * @param grid an int grid
     */
    public void displayGrid(int[][] grid){
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
     * overloaded char version of updateGrid
     * @param grid char grid
     */
    public void displayGrid(char[][] grid){
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
     * checkShips checks to see if a ship has just been sunk or not
     */
    public void checkShips(){
        for (Ship ship : shipArray) {
            // check the status of each ship, if sunk then ignore
            if (ship.isFloating()) {
                // check to see if a ship has just been sunk
                if (!stillFloating(ship)) {
                    // update the ships status and print the specific ship that's been sunk
                    ship.setFloating();
                    System.out.println(ship.getName() + " (" + ship.getLength() + ")" + " SUNK");
                }
            }
        }
    }

    /**
     * stillFloating checks to see whether a ship is still floating
     * @param ship Ship obj
     * @return false if sunk, true if o/w
     */
    public boolean stillFloating(Ship ship) {
        // check the ships orientation
        if (ship.getDirection() == 1){ // ship is vertical
            for (int i = 0; i < ship.getLength(); i++){
                if (aiShipGrid[ship.getBackRow() + i][ship.getBackCol()] == ship.getLength()) return true;
            }
            return false;
        }
        else { // ship is horizontal
            for (int i = 0; i < ship.getLength(); i++){
                if (aiShipGrid[ship.getBackRow()][ship.getBackCol() + i] == ship.getLength()) return true;
            }
            return false;
        }
    }

    /**
     * attempts to fire a torpedo at the player based off if a ship was hit previously, if not it will shoot randomly
     * this method is a little bizarre in it's behaviour so sometimes it won't successfully fire a torpedo when it's down to the last ship
     * @param p Player obj
     */
    public void aiFireTorpedo(Player p){
        // check if a ship has been hit and hasn't been sunk yet
        if (foundPlayerShip){
            // check if a direction has been established
            if (playerShipDirection != -1){

                // scan vertically for adjacent unchecked spaces
                if (playerShipDirection == 1){
                    for (int i = 1; i < 10; i++){

                        // if this is an unchecked tile
                        if (aiShotGrid[i][playerShipCol] == '~'){

                            // if there's a nearby confirmed hit above
                            if (i != 1 && aiShotGrid[i - 1][playerShipCol] == 'X'){
                                // fire
                                aiCheckHit(p, i, playerShipCol);
                            }
                            // if there's a nearby confirmed hit below
                            else if (aiShotGrid[i + 1][playerShipCol] == 'X'){
                                // fire
                                aiCheckHit(p, i, playerShipCol);
                            }
                        }
                    }
                }
                // scan horizontally
                else {
                    for (int i = 1; i < 10; i++){
                        // if this is an unchecked tile
                        if (aiShotGrid[playerShipRow][i] == '~'){

                            // if there's a nearby confirmed hit to the left
                            if (i != 1 && aiShotGrid[playerShipRow][i - 1] == 'X'){
                                // fire
                                aiCheckHit(p, playerShipRow, i);
                            }
                            // if there's a nearby confirmed hit to the right
                            else if (aiShotGrid[playerShipRow][i + 1] == 'X'){
                                // fire
                                aiCheckHit(p, playerShipRow, i);
                            }
                        }
                    }
                }

            }
            // otherwise try and establish a direction
            else {
                // check the immediate up, down, left, and right locations until a direction is established
                // Check up
                if (playerShipRow - 1 >= 1 && !checkUp && aiShotGrid[playerShipRow - 1][playerShipCol] == '~'){
                    checkUp = true;
                   if (aiCheckHit(p, playerShipRow - 1, playerShipCol)) playerShipDirection = 1;
                }
                // Check left
                else if (playerShipCol - 1 >= 1 && !checkLeft && aiShotGrid[playerShipRow][playerShipCol - 1] == '~'){
                    checkLeft = true;
                    if (aiCheckHit(p, playerShipRow, playerShipCol - 1)) playerShipDirection = 0;
                }
                // Check down
                else if (playerShipRow + 1 >= 1 && !checkDown && aiShotGrid[playerShipRow + 1][playerShipCol] == '~'){
                    checkDown = true;
                    if (aiCheckHit(p, playerShipRow + 1, playerShipCol)) playerShipDirection = 1;
                }
                // Check right
                else if (playerShipCol + 1 >= 1 && !checkRight && aiShotGrid[playerShipRow][playerShipCol + 1] == '~'){
                    checkRight = true;
                    if (aiCheckHit(p, playerShipRow, playerShipCol + 1)) playerShipDirection = 0;
                }
                // reset all locations and fire again
                else {
                    checkUp = false;
                    checkDown = false;
                    checkLeft = false;
                    checkRight = false;
                    aiFireTorpedo(p);
                }
            }
        }
        // otherwise shoot randomly at a location that hasn't already been selected
        else {
            while (true){

                // range of 10, min 1
                int row = (int)(Math.random() * 10) + 1;
                int col = (int)(Math.random() * 10) + 1;

                if (aiShotGrid[row][col] == '~'){
                    aiCheckHit(p, row, col);
                    break;
                }
                // else keep repeating till the shot is fired at a new location
            }
        }
    }

    /**
     * aiCheckHit checks to see if the AI has succesfully hit the player or not
     * @param p Player obj
     * @param row row coordinate
     * @param col col coordinate
     * @return true if the player ship has been hit, false if o/w
     */
    public boolean aiCheckHit(Player p, int row, int col){
        // a ship was hit
        if (p.getCoor(row, col) > 0){
            System.out.println("AI HAS HIT");
            // save the information for subsequent hits
            foundPlayerShip = true;
            playerShipRow = row;
            playerShipCol = col;
            aiShotGrid[row][col] = 'X'; // mark a hit on the shot grid

            // grab the value of the ship to check if it's been now sunk
            //int shipValue = p.getCoor(row, col);

            // mark a hit on the player's ship board
            p.setCoor(row, col);

            // check if the ship was sunk or not
            if (p.checkSunk()) {
                System.out.println("AI HAS SUNK A SHIP");
                // reset the values back so a new ship will be found
                foundPlayerShip = false;
                playerShipRow = 0;
                playerShipCol = 0;
                playerShipDirection = -1;
                checkUp = false;
                checkRight = false;
                checkLeft = false;
                checkDown = false;

            }
            return true;
        }
        // otherwise a ship wasn't hit
        else {
            aiShotGrid[row][col] = '0'; // mark a miss on the shot grid
            System.out.println("AI HAS MISSED");
            return false;
        }
    }


    public boolean GameOver(){
        for (Ship ship: shipArray){
            if (ship.isFloating()) return false;
        }
        return true;
    }

    // getter and setter methods
    public int getCoor(int xCoor, int yCoor){ return aiShipGrid[xCoor][yCoor];}
    public void setCoor(int xCoor, int yCoor){ aiShipGrid[xCoor][yCoor] = -1;}

    // displays various aiGrids
    public void displayShotGrid(){
        System.out.println("\n*****AI SHOT GRID*******");
        displayGrid(aiShotGrid);
        System.out.println("*****AI SHOT GRID*******");
    }
    public void displayShipGrid(){
        displayGrid(aiShipGrid);
        System.out.println();
    }
}
