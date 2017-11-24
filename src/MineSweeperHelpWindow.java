import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/*
 * Class representing the separate help window that can be
 * spawned by clicking help in the menu bar.
 *
 * Separated from Minesweeper class to improve modularity and readability.
 */
public class MineSweeperHelpWindow extends JFrame {
    public MineSweeperHelpWindow() {
        // sets the title for the window
        super("Help");

        // the text contained in the help window
        JTextArea helpTextArea = new JTextArea(
                "Welcome to Minesweeper Redux!\n" +
                "    (designed by: Taylor Ecton)\n\n" +
                "Rules:\n" +
                "1. Don't click any bombs!\n" +
                "2. Revealed tiles show how many bombs are adjacent " +
                "to that tile!\n" +
                "3. You can mark tiles you think are bombs with a flag " +
                "so that you don't accidentally click them!\n" +
                "4. Reveal all the tiles that are NOT bombs to win!\n\n" +
                "Controls:\n" +
                "Left click:\tReveal a tile\n" +
                "Right click:\tMark tile with a flag\n\n" +
                "Additional info:\n" +
                "You can use the smiley button to start a new game in " +
                "addition to using the Game menu!\n"
        );

        // creates a border around the text, has the text wrap so it isn't one long
        // line, and makes the area un-editable
        helpTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpTextArea.setEditable(false);

        // a scroll pane to contain the text
        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 300));

        // add the scroll pane to the window and make the window visible
        this.add(scrollPane, BorderLayout.CENTER);
        this.setSize(300, 350);
        this.setVisible(true);
    }
}
