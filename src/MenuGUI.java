/*
 * Author: Raymond Li
 * Date: 19/11/2018
 * Description: User first starts with a welcome message and options to start a new game
 * 				and	to load a game. New game brings out 3 choices - Beginner, Intermediate
 * 				and Expert. A game based on the selected difficulty is then created. Load
 * 				game brings out a file explorer to load a game previously saved to a .mssg
 * 				(Ultimate Tic-Tac-Toe Save Game) file.
 */

// Imports required packages
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// MenuGUI class extends JFrame and implements ActionListener
public class MenuGUI implements ActionListener {

	// Private class variables
	// Main frame that contains everything
	public JFrame mainFrame = new JFrame();

	// Message to display on start of game
	private JLabel message = new JLabel(
			"<html><div style='text-align: center;'>Welcome to Ultimate Tic-Tac-Toe!<br>Created by: Raymond Li</div></html>",
			JLabel.CENTER);

	// Button for user to start a new game of Ultimate Tic-Tac-Toe
	private JButton newGame = new JButton("New Game");

	// Button for user to load an existing game
	private JButton loadGame = new JButton("Load Game");

	// Back button for user to go back to initial view
	private JButton back = new JButton("Back");

	// Panels to hold message and buttons
	private JPanel messagePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();

	/** Constructor */
	public MenuGUI() {

		// Sets font of message and adds it to messagePanel
		message.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		messagePanel.add(message);

		// Adds action listeners and fonts to buttons
		newGame.addActionListener(this);
		newGame.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		loadGame.addActionListener(this);
		loadGame.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		back.addActionListener(this);
		back.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

		// Sets title, size, layout and location of GUI window
		mainFrame.setTitle("Start Game");
		mainFrame.setSize(640, 300);
		mainFrame.setLayout(new GridLayout(2, 1));
		mainFrame.setLocationRelativeTo(null);

		// Sets layout of and adds buttons to buttonPanel
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(newGame);
		buttonPanel.add(loadGame);
		mainFrame.getContentPane().add(messagePanel);
		mainFrame.getContentPane().add(buttonPanel);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/** Action performed method handles button clicks */
	public void actionPerformed(ActionEvent event) {

		/*
		 * If the newGame button is clicked, removes all buttons and adds difficulty
		 * level buttons and back button, and refreshes the panel and screen
		 */
		if (newGame == event.getSource()) {
			try {
				Ultimate_Tic_Tac_Toe.initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mainFrame.dispose();
		}

		/*
		 * Sets up file chooser and loads game from file if the loadGame button is
		 * clicked
		 */
		else if (loadGame == event.getSource()) {

			JFileChooser loadFile = new JFileChooser();

			// Sets the default directory to wherever the Ultimate Tic-Tac-Toe game is
			loadFile.setCurrentDirectory(new File("."));

			// Adds a custom file filter and disables the default (Accept All) file filter
			loadFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			loadFile.addChoosableFileFilter(new UTTTSGFilter());
			loadFile.setAcceptAllFileFilterUsed(false);

			// Processes the results of getting the user to load a game
			if (loadFile.showDialog(mainFrame, "Load Game") == JFileChooser.APPROVE_OPTION) {
				File game = loadFile.getSelectedFile();

				// Resets the file chooser for the next time it's shown
				loadFile.setSelectedFile(null);

				// Try-catch to handle exceptions
				try {

					// Reads saved game from file
					Ultimate_Tic_Tac_Toe.readFromFile(game.getName());
					mainFrame.dispose();

				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame.getContentPane(),
							new JLabel("Savegame not loaded. Bad File.", JLabel.CENTER), "FileLoader",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			/*
			 * Shows a popup telling the user that the saved game has not been loaded and
			 * restarts the MenuGUI window by disposing and recreating it
			 */
			else {
				JOptionPane.showMessageDialog(mainFrame.getContentPane(),
						new JLabel("Savegame not loaded. Bad File.", JLabel.CENTER), "FileLoader",
						JOptionPane.ERROR_MESSAGE);
				mainFrame.dispose();
				new MenuGUI();
			}
		}

		// If the back button is clicked, restart the MenuGUI
		else if (back == event.getSource()) {
			mainFrame.dispose();
			new MenuGUI();
		}
	}
}