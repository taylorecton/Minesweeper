import javax.swing.*;
import java.awt.event.ActionListener;

/*
 * The menu bar for minesweeper.
 * Placed in a separate class to clean up Minesweeper class.
 * menuListener parameter is the Minesweeper class, allowing the
 * menu to control aspects of Minesweeper class.
 */
public class MineSweeperMenu extends JMenuBar {
    public MineSweeperMenu(ActionListener menuListener) {
        // Main Menus
        JMenu gameMenu = new JMenu("Game");
        JMenu helpMenu = new JMenu("Help");

        // Sub Menus
        JMenu settingsMenu = new JMenu("Settings");

        // "Game" menu items
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.setActionCommand("New Game");

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setActionCommand("Quit");

        // "Help" menu items
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setActionCommand("Help");

        // "Settings" menu items
        JMenuItem beginnerMenuItem = new JMenuItem("Beginner");
        beginnerMenuItem.setActionCommand("Beginner");

        JMenuItem intermediateMenuItem = new JMenuItem("Intermediate");
        intermediateMenuItem.setActionCommand("Intermediate");

        JMenuItem expertMenuItem = new JMenuItem("Expert");
        expertMenuItem.setActionCommand("Expert");

        JMenuItem customMenuItem = new JMenuItem("Custom...");
        customMenuItem.setActionCommand("Custom");

        // Set action listener on all menu items
        newGameMenuItem.addActionListener(menuListener);
        quitMenuItem.addActionListener(menuListener);
        helpMenuItem.addActionListener(menuListener);
        beginnerMenuItem.addActionListener(menuListener);
        intermediateMenuItem.addActionListener(menuListener);
        expertMenuItem.addActionListener(menuListener);
        customMenuItem.addActionListener(menuListener);

        // Populate "Settings" sub menu
        settingsMenu.add(beginnerMenuItem);
        settingsMenu.add(intermediateMenuItem);
        settingsMenu.add(expertMenuItem);
        settingsMenu.addSeparator();
        settingsMenu.add(customMenuItem);

        // Populate "Game" menu
        gameMenu.add(newGameMenuItem);
        gameMenu.add(settingsMenu);
        gameMenu.addSeparator();
        gameMenu.add(quitMenuItem);

        // Populate "Help" menu
        helpMenu.add(helpMenuItem);

        // Populate MinesweeperMenu
        this.add(gameMenu);
        this.add(helpMenu);
    }
}
