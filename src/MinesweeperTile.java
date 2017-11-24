import javax.swing.*;
import java.awt.*;

/*
 * Class representing a single tile on a MinesweeperGrid
 */
public class MinesweeperTile extends JButton {
    // boolean values representing whether the button is a
    // bomb or flag and whether it is revealed
    private boolean isBomb, isFlag, isRevealed;

    // integer representing the number of bombs adjacent to this tile
    private int numAdjacentBombs;

    // coordinates for this tile; this class tracks them
    // so they are retrievable from the main class
    private int[] coords;

    // class loader used for loading images
    private ClassLoader loader = getClass().getClassLoader();

    private Icon concaveIcon;

    // constructor for class
    public MinesweeperTile(int row, int col) {
        // initialize coordinates to provided row and column
        coords = new int[] { row, col };

        // set all booleans to false initially
        isBomb = false;
        isFlag = false;
        isRevealed = false;

        // initialize adjacent number of bombs to 0
        numAdjacentBombs = 0;

        concaveIcon = new ImageIcon(loader.getResource("resources/concave.jpg"));

        // set background color for tile
        // this will be visible in reveal function
        super.setBackground(Color.LIGHT_GRAY);
        super.setOpaque(false);
    }

    // public accessor to check if tile is a bomb
    public boolean isBomb() { return isBomb; }

    // public accessor to check if the tile is revealed
    public boolean isRevealed() { return isRevealed; }

    // public accessor to check if the tile is a flag
    public boolean isFlag() { return isFlag; }

    // public accessor to get the number of bombs adjacent to this tile
    public int getNumAdjacentBombs() { return numAdjacentBombs; }

    // public accessor to get the coordinates of this bomb on the MinesweeperGrid
    public int[] getCoords() { return coords; }

    // increments the number of adjacent bombs to this tile
    public void increaseAdjacentBombs() { numAdjacentBombs++; }

    // turns this tile into a bomb
    public void placeBomb() {
        isBomb = true;
        numAdjacentBombs = -1;
    }

    // reveals this tile, either when directly clicked or in reaction
    // to another tile being clicked
    public void reveal() {
        // The image that will be set for the revealed tile
        Icon image;

        // Update isRevealed and make the background color of the
        // tile visible
        isRevealed = true;
        super.setOpaque(true);

        // Handle different types of tile
        if (isBomb && !isFlag) {
            // sets the image to the standard bomb icon
            // ignores this for tiles that are flagged
            image = new ImageIcon(loader.getResource("resources/bomb.jpeg"));
            super.setIcon(image);
        } else if (isFlag && !isBomb) {
            // essentially ignores being revealed if the user placed
            // a flag on this tile
            isRevealed = false;
            super.setOpaque(false);
        } else if (numAdjacentBombs == 0) {
            // sets the image to the concave image for a blank tile
            // image = new ImageIcon(loader.getResource("resources/concave.jpg"));
            image = concaveIcon;
            super.setIcon(image);
        } else {
            // sets the tile to show the number of adjacent bombs
            // the text color is based on the number of adjacent bombs
            // as in the original Minesweeper
            String text = "";
            Color numberColor = getNumberColor();
            text += numAdjacentBombs;
            super.setForeground(numberColor);
            super.setText(text);
        }
    }

    // this is called when the user clicks on a bomb tile
    // essentially it sets the image on tiles that were flagged by the user as a bomb
    // that were not actually a bomb
    public void revealFalseFlag() {
        Icon image = new ImageIcon(loader.getResource("resources/falseFlag.jpeg"));

        isRevealed = true;

        super.setIcon(image);
    }

    // Allows the user to place or remove a flag on a tile
    public void setFlag() {
        // the image of the flag
        Icon image;


        if (isFlag) {
            // removes flag
            super.setIcon(null);
            isFlag = false;
        } else {
            // sets flag image
            isFlag = true;
            image = new ImageIcon(loader.getResource("resources/flag.jpg"));
            super.setIcon(image);
        }
    }

    // resets all the data for this tile, except for its coordinates
    public void resetVariables() {
        super.setIcon(null);
        super.setText(null);
        super.setOpaque(false);
        isBomb = false;
        isFlag = false;
        isRevealed = false;
        numAdjacentBombs = 0;
    }

    // used to set the color of the text on revealed tiles
    // that show a number of adjacent bombs
    private Color getNumberColor() {
        Color c;

        // set the color based on the number of adjacent bombs
        switch (numAdjacentBombs) {
            case 1:
                c = Color.BLUE;
                break;
            case 2:
                c = Color.GREEN;
                break;
            case 3:
                c = Color.RED;
                break;
            case 4:
                c = Color.CYAN;
                break;
            case 5:
                c = Color.ORANGE;
                break;
            case 6:
                c = Color.PINK;
                break;
            case 7:
                c = Color.MAGENTA;
                break;
            default:
                // max possible is 8 (but is uncommon)
                c = Color.YELLOW;
                break;
        }

        return c;
    }
}
