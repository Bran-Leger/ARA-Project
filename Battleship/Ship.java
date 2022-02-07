
// The Ship class holds all relevant information about a ship
public class Ship {

    private int length; // the ship's length
    private int rowBack; // the x location of the ship's bow
    private int colBack; // the y location of the ship's bow
    private int rowFront;
    private int colFront;
    private String name; // the name or type of the ship
    private int direction; // direction the ship is facing
    private boolean floating = true; // status of the ship, true if at least 1 hit remains, false if 0

    // constructor, setting variables
    public Ship (int length, String name){
        this.length = length;
        this.name = name;
    }

    // sets the coordinates of the ship
    // takes the ships back x and y coordinates and direction
    // then calculates the front x and y coordinates
    public void setCoordinates(int x, int y, int direction){
        rowBack = x;
        colBack = y;
        this.direction = direction;

        // if the ship is horizontal
        if (direction == 0){
            rowFront = x;
            colFront = y + length;
        }
        // o/w the ship is vertical
        else {
            rowFront = x + length;
            colFront = y;
        }
    }


    // getter and setter methods
    public int getBackRow() { return rowBack;}
    public int getBackCol() { return colBack;}
    public int getFrontRow() { return rowFront;}
    public int getFrontCol() { return colFront;}
    public int getLength() { return length;}
    public int getDirection() { return direction;}
    public String getName() { return name;}
    public boolean isFloating() { return floating;}
    public void setFloating() { floating = false;}

}
