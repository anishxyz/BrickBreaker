// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunBrickBreaker implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        final JFrame frame = new JFrame("Brick Breaker");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Top Panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });

        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.saveState();
            }
        });


        //info string
        String infoString = "Welcome to Brick Breaker! \n\n" +
                "Your goal is to take out all the bricks with ONE ball.\n" +
                "Use the paddle to stay in control by moving " +
                "left and right to keep the ball bouncing\n" +
                "and prevent the ball from falling through. " +
                "To win you must break ALL the bricks on the\n" +
                "board. Some bricks have special powers.\n\n" +
                "Controls:\n" +
                "Move the paddle with LEFT and RIGHT arrow keys.\n" +
                "Press N to reset the game board\n\n" +
                "Brick Types:\n" +
                "BLUE: Basic Brick, one touch and destroyed\n" +
                "YELLOW: Speed Brick, increases ball speed\n" +
                "FROSTY: Slow Brick, decreases ball speed\n" +
                "ORANGE/PEACH: Double Brick, takes two hits to destroy\n" +
                "*Note: All bricks are worth one point and must be COMPLETELY destroyed.\n\n";

        String userPrompt = "New player? Enter a new username below:\n" +
                "Returning player? Enter your username below:";

        String design = "\n\n\n\nProject built by Anish Agrawal, Freshman @ UPenn";

        String loadPrompt = "Place file in the src folder\n" +
                "Enter the filename below (without filetype):";

        //load state button
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ins = new JFrame();
                String u = JOptionPane.showInputDialog(
                        ins, loadPrompt, "Load", JOptionPane.INFORMATION_MESSAGE);
                court.loadState("src//" + u + ".txt");
            }
        });

        //Force username on intro
        JFrame ins = new JFrame();
        String intro = infoString + userPrompt + design;
        String user = JOptionPane.showInputDialog(
                ins, intro, "Info", JOptionPane.INFORMATION_MESSAGE);
        court.reset(user);

        //Change player/user
        final JButton newPlayer = new JButton("Change Player");
        newPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ins = new JFrame();
                String u = JOptionPane.showInputDialog(
                        ins, userPrompt, "Change Player", JOptionPane.INFORMATION_MESSAGE);
                court.reset(u);
            }
        });

        //define instructions and info panel
        final JButton info = new JButton("Info");
        info.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ins = new JFrame();
                String i = infoString + design;
                JOptionPane.showMessageDialog(
                        ins, i, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //leaderboard display
        final JButton leaderboard = new JButton("Leaderboard");
        leaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ins = new JFrame();
                String i = "Welcome to the leaderboard of the " +
                        "best Brick Breaker players!\n\n" +
                         GameCourt.getLeaderboard();
                JOptionPane.showMessageDialog(
                        ins, i, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //add buttons to control panel
        control_panel.add(info);
        control_panel.add(reset);
        control_panel.add(newPlayer);
        control_panel.add(leaderboard);
        control_panel.add(save);
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}