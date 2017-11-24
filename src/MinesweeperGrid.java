import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;

/*
 * Class representing the Grid of tiles in Minesweeper
 */
public class MinesweeperGrid extends JPanel {
    // number of rows, columns, and bombs in the grid
    private int rows, columns, bombs;

    // MouseListener to have the grid respond to mouse events
    private MouseListener listener;

    // 2D-array of tiles
    private MinesweeperTile[][] tiles;

    // MinesweeperGrid constructor
    public MinesweeperGrid(int rows, int columns, int bombs, MouseListener listener) {
        // initialize number of rows, columns, and bombs
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;

        // set the MouseListener
        this.listener = listener;

        // instantiates all of the tiles
        tiles = createTiles();

        // specifies the layout for the grid
        this.setLayout(new GridLayout(rows, columns, 0, 0));

        // places bombs on the grid, sets numbers of adjacent bombs for tiles,
        // and adds the tiles to the grid
        setBombs();
        setNumAdjacentBombs();
        addTiles();
    }

    // resets the grid to an initial, playable configuration of the same size
    public void resetGrid() {
        // remove all the tiles and set all tiles back to initial
        // tile configuration
        this.removeAll();
        resetTiles();

        // places bombs, sets number of adjacent bombs for tiles, and
        // adds tiles to the grid, then redraws the grid
        setBombs();
        setNumAdjacentBombs();
        addTiles();
        this.repaint();
    }

    // reveals all of the bombs on the grid; called when a user
    // clicks on a bomb tile
    public void revealBombs() {
        // iterates through all of the tiles
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (tiles[row][col].isFlag() && !tiles[row][col].isBomb()) {
                    // if the bomb is a flag that doesn't cover a bomb,
                    // reveal it as a "false" flag
                    tiles[row][col].revealFalseFlag();
                } else if (tiles[row][col].isBomb() && !tiles[row][col].isFlag()) {
                    // if it is a bomb and the user DID NOT mark it as a bomb,
                    // reveal it as a bomb
                    tiles[row][col].reveal();
                }
                // make sure these tiles no longer respond to input
                tiles[row][col].removeMouseListener(listener);
            }
        }
    }

    // fills in blank tiles surrounding a clicked tile
    // returns the number of tiles flipped by the function for bookkeeping
    // in Minesweeper class
    public int floodfill(MinesweeperTile clickedTile) {
        // coordinates of the current tile being checked
        int[] coords;
        int row;
        int col;

        // coordinates of surrounding tiles to look at
        int[] rowsToCheck;
        int[] colsToCheck;

        // the actual tile being checked
        MinesweeperTile toCheck;

        // tracks the number of revealed tiles
        int tilesFlipped = 0;

        // array denoting which tiles have been checked; all values initialized to false
        // function works without this, but it improves the speed of the function
        // in cases where a large number of tiles need to be revealed
        boolean[][] checked = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                checked[i][j] = false;
            }
        }

        // The LinkedList acts as a Queue of buttons to check, preventing
        // the need for recursive function calls; the Queue is initialized
        // with the tile that was clicked
        LinkedList<MinesweeperTile> tilesToCheck = new LinkedList<>();
        tilesToCheck.add(clickedTile);

        // while there are still tiles in the queue
        while (tilesToCheck.size() != 0) {
            // get the head of the queue to check, reveal it,
            // increment tilesFlipped and remove the MouseListener
            toCheck = tilesToCheck.poll();
            toCheck.reveal();
            tilesFlipped++;
            toCheck.removeMouseListener(listener);

            // get the tiles coordinates
            coords = toCheck.getCoords();
            row = coords[0];
            col = coords[1];

            // set the coordinates to check
            rowsToCheck = new int[] { row - 1, row, row + 1 };
            colsToCheck = new int[] { col - 1, col, col + 1 };

            // check all the tiles surrounding the current tile
            for (int r : rowsToCheck) {
                for (int c : colsToCheck) {
                    // ignore cases where r or c is outside of the grid, the index is already checked,
                    // or the tile is already revealed
                    if (r < 0 || c < 0 || r >= rows || c >= columns || checked[r][c] || tiles[r][c].isRevealed())
                        continue;

                    // case where the surrounding tile will have a number;
                    // these tiles do not need to be added to queue of tiles to check
                    if (tiles[r][c].getNumAdjacentBombs() > 0) {
                        // reveal the tile and increment number of tilesFlipped
                        tiles[r][c].reveal();
                        if (!(tiles[r][c].isFlag() && !tiles[r][c].isBomb()))
                            tilesFlipped++;
                        // remove the MouseListener
                        tiles[r][c].removeMouseListener(listener);
                    }

                    // add blank tiles to the queue
                    if (tiles[r][c].getNumAdjacentBombs() == 0)
                        tilesToCheck.add(tiles[r][c]);

                    // set checked of this index to true to speed up function
                    checked[r][c] = true;
                }
            }
        }

        // returns the number of tiles flipped in the function
        return tilesFlipped;
    }

    // the board is cleared: remove all MouseListeners
    // and any bomb tiles that have not been marked with a flag
    // will have a flag set
    public void allCleared() {
        // iterate through all the tiles to ensure all MouseListeners are removed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                tiles[row][col].removeMouseListener(listener);
                if (!tiles[row][col].isFlag() && tiles[row][col].isBomb()) {
                    // set a flag on unflagged bombs
                    tiles[row][col].setFlag();
                }
            }
        }
    }

    // Resets all tiles to initial configuration
    private void resetTiles() {
        // removes MouseListener for every tile and resets
        // variables for every tile
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                tiles[row][col].removeMouseListener(listener);
                tiles[row][col].resetVariables();
            }
        }
    }

    // initializes all the tiles for the grid
    private MinesweeperTile[][] createTiles() {
        // the Tiles for the grid
        MinesweeperTile[][] theTiles = new MinesweeperTile[rows][columns];

        // initialize all the tiles
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                theTiles[row][col] = new MinesweeperTile(row, col);
            }
        }

        // returns all the tiles for the grid
        return theTiles;
    }

    // places bombs on the grid
    private void setBombs() {
        // Random number generator for generating indices for bombs
        Random RNG = new Random(System.currentTimeMillis());

        // The random indices where the bomb will be planted
        int randRow = RNG.nextInt(rows);
        int randCol = RNG.nextInt(columns);

        // plant bombs number of bombs
        for (int i = 0; i < bombs; i++) {
            // get new random indices if there happens to be a bomb
            // already planted at this generated index pair
            while (tiles[randRow][randCol].isBomb()) {
                randRow = RNG.nextInt(rows);
                randCol = RNG.nextInt(columns);
            }

            // place a bomb on the generated random index
            tiles[randRow][randCol].placeBomb();
        }
    }

    // has the tiles check for adjacent bombs
    private void setNumAdjacentBombs() {
        // for every tile
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // if the tile is a bomb, move to the next tile
                if (tiles[row][col].isBomb()) continue;

                // otherwise set its number of adjacent bombs
                checkAdjTiles(row, col);
            }
        }
    }

    // set the number of adjacent bombs for non-bomb tiles
    private void checkAdjTiles(int row, int col) {
        // coordinates for all surrounding tiles
        int[] rowsToCheck = new int[] { row - 1, row, row + 1 };
        int[] colsToCheck = new int[] { col - 1, col, col + 1 };

        // for all surrounding tiles
        for (int r : rowsToCheck) {
            for (int c : colsToCheck) {
                // ignore cases where r or c are outside of the grid, and the case
                // where r and c correspond to the current tile
                if ((r == row && c == col) || r < 0 || c < 0 || r >= rows || c >= columns)
                    continue;

                // if the surrounding tile corresponding to r and c is a bomb, increment
                // the number of surrounding bombs for the tile corresponding to row and column
                if (tiles[r][c].isBomb())
                    tiles[row][col].increaseAdjacentBombs();
            }
        }
    }

    // add all of the tiles to the grid
    private void addTiles() {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                tiles[row][col].addMouseListener(listener);

                this.add(tiles[row][col]);
            }
        }
    }
}
