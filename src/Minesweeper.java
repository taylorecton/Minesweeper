import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/*
 * Main controlling class for the Minesweeper game.
 *
 * Contains the game logic. Main function at bottom of class.
 */
public class Minesweeper extends JFrame implements ActionListener, MouseListener {
    private Container container;

    // the game menu
    private MineSweeperMenu menu;

    // panel above the grid with timer, number of bombs, and smileyButton
    private JPanel labelPanel;
    private JLabel timerLabel, bombLabel;
    private JButton smileyButton;
    // icons for the smiley button; boomIcon for initial bomb in a game over
    private Icon smileyIcon, worriedIcon, gameOverIcon, victoryIcon, boomIcon;

    // game timer and an int tracking elapsed seconds
    private Timer gameTimer;
    private int gameTime;

    // the grid of tiles
    private MinesweeperGrid grid;

    // String representing difficulty and boolean for checking if
    // difficulty was changed
    private String difficulty;
    private boolean difficultyChanged;

    // integers representing dimensions of window, dimensions of bomb grid,
    // and the number of bombs on the grid
    private int width, height, rows, columns, bombs;

    // bombCount is for the bomb label, tiles to clear is for
    // checking victory conditions
    private int bombCount, tilesToClear;

    // constructor for Minesweeper
    public Minesweeper() {
        // sets window name
        super("Minesweeper Redux");

        // load in all of the icons this class will use
        ClassLoader loader = getClass().getClassLoader();
        smileyIcon = new ImageIcon(loader.getResource("resources/smiley.jpeg"));
        worriedIcon = new ImageIcon(loader.getResource("resources/worried.jpg"));
        gameOverIcon = new ImageIcon(loader.getResource("resources/frowny.jpeg"));
        victoryIcon = new ImageIcon(loader.getResource("resources/victory.jpeg"));
        boomIcon = new ImageIcon(loader.getResource("resources/boom.jpg"));

        container = getContentPane();

        // set initial game settings
        rows = 5;
        columns = 5;
        bombs = 5;
        difficulty = "Beginner";
        difficultyChanged = false;

        // initializes the bombCount for the bomb label
        // and the number of tiles that need to be cleared to win
        bombCount = bombs;
        setTilesToClear();

        // sets the width and height of the game window based on rows & columns
        setWidthAndHeight();

        // initialize game timer
        gameTime = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String timeText = "";
                gameTime++;
                timeText += gameTime;
                while (timeText.length() < 3) {
                    timeText = "0"+timeText;
                }
                // gameTimer stays at 999 if 999 seconds is exceeded
                if (timeText.length() > 3)
                    timeText = "999";

                timerLabel.setText(timeText);
            }
        });

        // sets grid with initial settings
        grid = new MinesweeperGrid(rows, columns, bombs, this);

        // sets up label panel with bomb count, smiley icon, and game time
        labelPanel = setupLabelPanel();

        // adds the labelPanel and grid to the window
        container.add(labelPanel, BorderLayout.NORTH);
        container.add(grid, BorderLayout.CENTER);

        // sets the menu for the window and makes window visible
        menu = new MineSweeperMenu(this);
        this.setJMenuBar(menu);
        this.setSize(width, height);
        this.setResizable(false);
        this.setVisible(true);
    }

    // sets up the label panel for the game window
    private JPanel setupLabelPanel() {
        JPanel panel = new JPanel();

        // creates a clickable button with smiley icon
        smileyButton = new JButton();
        smileyButton.setIcon(smileyIcon);
        smileyButton.setPreferredSize(new Dimension(40, 40));
        smileyButton.addMouseListener(this);

        // sets up timer for labelPanel
        timerLabel = new JLabel("000");
        timerLabel.setFont(new Font("Courier", Font.PLAIN, 24));
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setOpaque(true);
        timerLabel.setForeground(Color.RED);

        // sets up bomb label for labelPanel
        bombLabel = new JLabel();
        bombLabel.setBackground(Color.BLACK);
        setBombText();
        bombLabel.setFont(new Font("Courier", Font.PLAIN, 24));
        bombLabel.setBackground(Color.BLACK);
        bombLabel.setOpaque(true);
        bombLabel.setForeground(Color.RED);

        // add the components to the panel
        panel.add(bombLabel, BorderLayout.EAST);
        panel.add(Box.createHorizontalStrut(width/6));
        panel.add(smileyButton, BorderLayout.CENTER);
        panel.add(Box.createHorizontalStrut(width/6));
        panel.add(timerLabel, BorderLayout.WEST);

        return panel;
    }

    /* ************************************************************************
     *            MENU CONTROLLING FUNCTIONS
     * ************************************************************************
     */
    // calls functions based on which input is chosen from the menu
    private void processAction(String command) {
        // each menu item has an associated action command that serves as
        // a parameter for a switch, which determines which function to call
        switch (command) {
            case "New Game":
                difficultyChanged = false;
                newGame();
                break;
            case "Quit":
                quitGame();
                break;
            case "Help":
                displayHelp();
                break;
            case "Beginner":
                setBeginnerMode();
                break;
            case "Intermediate":
                setIntermediateMode();
                break;
            case "Expert":
                setExpertMode();
                break;
            case "Custom":
                setCustomMode();
                break;
            default:
                System.err.println("Invalid action command: " + command);
                System.exit(1);
                break;
        }
    }

    // starts a new game
    private void newGame() {
        // resets variables and labels
        gameTime = 0;
        timerLabel.setForeground(Color.RED);
        timerLabel.setText("000");
        bombCount = bombs;
        setBombText();
        setTilesToClear();
        smileyButton.setIcon(smileyIcon);
        gameTimer.stop();

        // creates a new grid if the difficulty (and therefore grid size)
        // has changed, otherwise resets the current grid
        if (difficultyChanged) {
            this.remove(grid);
            setWidthAndHeight();
            this.setSize(width, height);
            grid = new MinesweeperGrid(rows, columns, bombs, this);
            this.add(grid, BorderLayout.CENTER);
        } else {
            grid.resetGrid();
        }
        container.add(grid, BorderLayout.CENTER);
    }

    // terminates the program
    private void quitGame() {
        System.exit(0);
    }

    // displays a help window
    private void displayHelp() {
        new MineSweeperHelpWindow();
    }

    // sets settings for beginner mode
    private void setBeginnerMode() {
        // predefined values for beginner mode
        rows = 5;
        columns = 5;
        bombs = 5;

        // sets difficulty and difficulty changed so new game function
        // knows if it needs to create a new grid
        if (!difficulty.equals("Beginner")) {
            difficulty = "Beginner";
            difficultyChanged = true;
        } else
            difficultyChanged = false;

        // create a new game with specified settings
        newGame();
    }

    // sets settings for intermediate mode
    private void setIntermediateMode() {
        // settings for intermediate mode
        rows = 8;
        columns = 8;
        bombs = 15;

        // sets difficulty and difficulty changed so new game function
        // knows if it needs to create a new grid
        if (!difficulty.equals("Intermediate")) {
            difficulty = "Intermediate";
            difficultyChanged = true;
        } else
            difficultyChanged = false;

        // creates a new game with specified settings
        newGame();
    }

    // sets settings for expert mode
    private void setExpertMode() {
        // predefined settings for expert mode
        rows = 10;
        columns = 10;
        bombs = 30;

        // sets difficulty and difficulty changed so new game function
        // knows if it needs to create a new grid
        if (!difficulty.equals("Expert")) {
            difficulty = "Expert";
            difficultyChanged = true;
        } else
            difficultyChanged = false;

        // creates a new game with these settings
        newGame();
    }

    // allows custom settings
    private void setCustomMode() {
        // create a window for user to specify settings in
        JFrame customSettingsFrame = new JFrame("Settings");

        // get current values of these parameters so they can be reset if
        // user cancels custom settings
        int currRows = rows;
        int currCols = columns;
        int currBombs = bombs;

        // sliders for setting values
        // maximums for rowSlider and columnSlider set to 10 to meet project specs
        JSlider rowSlider = new JSlider(JSlider.HORIZONTAL, 5, 18, currRows);
        JSlider columnSlider = new JSlider(JSlider.HORIZONTAL, 5, 30, currCols);
        JSlider bombSlider = new JSlider(JSlider.HORIZONTAL, 1, (rows*columns-1), currBombs);

        // buttons for confirming or canceling settings
        JButton okayButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // panels for the sliders and for the buttons
        JPanel buttonPanel;
        JPanel sliderPanel;

        // labels for each slider
        JLabel rowLabel = new JLabel("Rows: " + rows);
        JLabel colLabel = new JLabel("Columns: " + columns);
        JLabel bombLabel = new JLabel("Bombs: " + bombs);

        // handle the slider events
        ChangeListener sliderListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    if (source == rowSlider) {
                        rows = source.getValue();
                        rowLabel.setText("Rows: " + rows);
                        bombSlider.setMaximum(rows*columns-1);
                    } else if (source == columnSlider) {
                        columns = source.getValue();
                        colLabel.setText("Columns: " + columns);
                        bombSlider.setMaximum(rows*columns-1);
                    } else {
                        bombs = source.getValue();
                        bombLabel.setText("Bombs: " + bombs);
                    }
                }
            }
        };

        // handle button events
        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                if (source.getText().equals("OK")) {
                    difficulty = "Custom";
                    difficultyChanged = true;
                    newGame();
                    customSettingsFrame.dispose();
                } else {
                    rows = currRows;
                    columns = currCols;
                    bombs = currBombs;
                    customSettingsFrame.dispose();
                }
            }
        };

        // difficultyChanged set to false until "OK" is pressed
        // prevents settings from being changed unless OK is pressed
        difficultyChanged = false;

        // set up the rowSlider
        rowSlider.addChangeListener(sliderListener);
        rowSlider.setMajorTickSpacing(5);
        rowSlider.setMinorTickSpacing(1);
        rowSlider.setPaintTicks(true);
        rowSlider.setSnapToTicks(true);

        // set up the columnSlider
        columnSlider.addChangeListener(sliderListener);
        columnSlider.setMajorTickSpacing(5);
        columnSlider.setMinorTickSpacing(1);
        columnSlider.setPaintTicks(true);
        columnSlider.setSnapToTicks(true);

        // set up the bombSlider
        bombSlider.addChangeListener(sliderListener);
        bombSlider.setMajorTickSpacing(10);
        bombSlider.setMinorTickSpacing(1);
        bombSlider.setPaintTicks(true);
        bombSlider.setSnapToTicks(true);

        // add action listener to buttons
        okayButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);

        // add the sliders to the slider panel
        sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(6, 1, 0, 5));
        sliderPanel.add(rowLabel);
        sliderPanel.add(rowSlider);
        sliderPanel.add(colLabel);
        sliderPanel.add(columnSlider);
        sliderPanel.add(bombLabel);
        sliderPanel.add(bombSlider);

        // and the buttons to the button panel
        buttonPanel = new JPanel();
        buttonPanel.add(okayButton, BorderLayout.EAST);
        buttonPanel.add(cancelButton, BorderLayout.WEST);

        // add the panels to the window
        customSettingsFrame.add(sliderPanel, BorderLayout.CENTER);
        customSettingsFrame.add(buttonPanel, BorderLayout.SOUTH);

        // handle a window closing event
        customSettingsFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (difficultyChanged) {
                    newGame();
                } else {
                    rows = currRows;
                    columns = currCols;
                    bombs = currBombs;
                }
            }
        });

        // set the size of the window and make it visible
        customSettingsFrame.setSize(400, 400);
        customSettingsFrame.setVisible(true);
        customSettingsFrame.setResizable(false);
    }

    /* ********************************************************************
     *                   HELPER FUNCTIONS
     * ********************************************************************
     */
    // sets the height and width of window based on number of rows and columns
    private void setWidthAndHeight() {
        width = columns * 50;
        height = rows * 50 + 80;
    }

    // sets the text on the bomb label
    private void setBombText() {
        String bombText = "";
        bombText += bombCount;
        while (bombText.length() < 3)
            bombText = "0"+bombText;
        bombLabel.setText(bombText);
    }

    // sets the number of tiles that need to be cleared to win the game
    private void setTilesToClear() {
        tilesToClear = rows*columns - bombs;
    }

    // called when tilesToClear reaches zero, i.e. the player wins
    private void victory() {
        grid.allCleared();
        smileyButton.setIcon(victoryIcon);
        gameTimer.stop();
        timerLabel.setForeground(Color.GREEN);
        bombCount = 0;
        setBombText();
    }

    /* *************************************************************************
     *                  ACTION HANDLER FUNCTIONS
     * *************************************************************************
     */
    // process a menu action
    public void actionPerformed(ActionEvent e) {
        processAction(e.getActionCommand());
    }

    // mouse clicked event
    public void mouseClicked(MouseEvent e) {
        // start the game timer
        gameTimer.start();

        if (e.getSource() instanceof MinesweeperTile) {
            MinesweeperTile pressedButton = (MinesweeperTile) e.getSource();

            // plant a flag if the user right clicks and update bomb label text
            if (SwingUtilities.isRightMouseButton(e)) {
                pressedButton.setFlag();
                if (pressedButton.isFlag()) {
                    bombCount--;
                    if (bombCount < 0) {
                        pressedButton.setFlag();
                        bombCount++;
                    }
                    setBombText();
                } else {
                    bombCount++;
                    setBombText();
                }
            } else {
                // process results of clicking on given tile
                if (!pressedButton.isFlag())
                    handleClick(pressedButton);
            }
        } else {
            // calls new game when smileyButton is pressed
            difficultyChanged = false;
            newGame();
        }
    }

    // sets smiley icon to worried icon while mouse is pressed
    public void mousePressed(MouseEvent e) {
        if (e.getSource() != smileyButton && !SwingUtilities.isRightMouseButton(e)) {
            MinesweeperTile pressedButton = (MinesweeperTile) e.getSource();
            if (!pressedButton.isFlag())
                smileyButton.setIcon(worriedIcon);
        }
    }

    // returns smiley icon to normal after mouse is released
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() != smileyButton && !SwingUtilities.isRightMouseButton(e))
            smileyButton.setIcon(smileyIcon);
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // calls function based on what the tile clicked is
    private void handleClick(MinesweeperTile button) {
        if (button.isBomb()) {
            // the user lost in this case
            grid.revealBombs();
            smileyButton.setIcon(gameOverIcon);
            button.setIcon(boomIcon);
            gameTimer.stop();
        } else if (button.getNumAdjacentBombs() == 0) {
            // this is a blank space, clear all the blank
            // spaces surrounding
            int tilesFlipped = grid.floodfill(button);
            tilesToClear -= tilesFlipped;
        } else {
            // this tile has a number on it
            button.reveal();
            button.removeMouseListener(this);
            tilesToClear--;
        }

        if (tilesToClear == 0) {
            // the player won!
            victory();
        }
    }


    /* *************************************************************************
     *                         MAIN METHOD
     * *************************************************************************
     */
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
