/*
 * Authors: Raymond Li, David Tuck
 * Date started: 2018-11-19
 * Date Finished: 
 * Description: Game Interface
 */

// Imports required packages
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

//Main class that extends JFrame and implements ActionListener
public class GameGUI implements ActionListener, MouseListener, Serializable {

	// Private class Variables
	// Main frame that contains everything
	public JFrame mainFrame = new JFrame();

	// 2D JButton array for the Ultimate Tic-Tac-Toe map
	public JButton[][] buttons;

	// Clock to display the amount of time elapsed for the user
	public JTextPane clockPane = new JTextPane();
	private Clock clock;

	// Check to see if click is the first
	public boolean firstClick = true;

	// MinesLeft to display the amount of mines left to be flagged
	private JTextPane minesLeft = new JTextPane();

	// GamePanel to hold all the buttons
	private JPanel gamePanel = new JPanel();

	// InfoPanel to hold clock and minesLeft
	private JPanel infoPanel = new JPanel();

	/*
	 * Colors for each number of mines: 1-Blue 2-Green 3-Red 4-Dark_Blue 5-Dark_Red
	 * 6-Turquoise 7-Black 8-Grey
	 */
	private final static Color[] mycolors = { new Color(0, 0, 255), new Color(0, 129, 0), new Color(255, 19, 0),
			new Color(0, 0, 131), new Color(129, 5, 0), new Color(42, 148, 148), new Color(0, 0, 0),
			new Color(128, 128, 128) };

	// MenuBar variables
	// The menuBar itself
	private JMenuBar menuBar = new JMenuBar();

	// The first menu
	private JMenu gameMenu = new JMenu("Game");

	// New game with F2 as keyboard shortcut - maintains current settings
	private JMenuItem newGame = new JMenuItem("New game", KeyEvent.VK_F2);

	// The submenu under gameMenu
	private JMenu newSubmenu = new JMenu("New");

	// Menu item to save game
	private JMenuItem saveGame = new JMenuItem("Save game");

	// Menu item to load game
	private JMenuItem loadGame = new JMenuItem("Load Game");

	// Help menu for general information
	private JMenu helpMenu = new JMenu("Help");

	// About button displays game's creators
	private JMenuItem about = new JMenuItem("About");

	// How to play button links to a webpage on how to play Ultimate Tic-Tac-Toe
	private JMenuItem howToPlay = new JMenuItem("How to Play");

	// Declares boolean value for whether a button was pressed
	private boolean pressed, finished = false;

	// Boolean and menuItem for sound or not
	public static boolean soundCheck = true;
	private JCheckBoxMenuItem sound = new JCheckBoxMenuItem("Sound", true);

	/**
	 * Constructor
	 * 
	 * @param Ultimate Tic-Tac-Toe.mapSizeX
	 *            The horizontal size of the map
	 * @param Ultimate Tic-Tac-Toe.mapSizeY
	 *            The vertical size of the map
	 */
	public GameGUI(boolean isLoadGame) {

		// Initializes panels and buttons array
		gamePanel.setLayout(new GridLayout(Ultimate Tic-Tac-Toe.mapSizeX, Ultimate Tic-Tac-Toe.mapSizeY));
		infoPanel.setLayout(new FlowLayout());
		buttons = new JButton[Ultimate Tic-Tac-Toe.mapSizeX][Ultimate Tic-Tac-Toe.mapSizeY];

		// Sets minesLeft to mineCount
		Ultimate Tic-Tac-Toe.numOfMinesLeft = Ultimate Tic-Tac-Toe.mineCount;

		/*
		 * Initializes buttons with raised-bevel border, Consolas font and a fixed size
		 * of 30 pixels by 30 pixels
		 */
		for (int i = 0; i < Ultimate Tic-Tac-Toe.mapSizeX; i++)
			for (int j = 0; j < Ultimate Tic-Tac-Toe.mapSizeY; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				buttons[i][j].setFont(new Font("Consolas", Font.BOLD, 20));
				buttons[i][j].setPreferredSize(new Dimension(30, 30));
				buttons[i][j].setMaximumSize(new Dimension(30, 30));
				buttons[i][j].setMinimumSize(new Dimension(30, 30));

				// Adds mouse listener to each button
				buttons[i][j].addMouseListener(this);

				// Adds buttons to game panel
				gamePanel.add(buttons[i][j]);
			}

		// Initializes and adds textPanes to infoPanel
		clockPane.setEditable(false);
		minesLeft.setEditable(false);
		clockPane.setFont(new Font("Consolas", Font.BOLD, 20));
		minesLeft.setFont(new Font("Consolas", Font.BOLD, 20));
		clockPane.setForeground(Color.RED);
		minesLeft.setForeground(Color.RED);
		clockPane.setBackground(Color.BLACK);
		minesLeft.setBackground(Color.BLACK);
		clockPane.setText(Integer.toString(Ultimate Tic-Tac-Toe.clockSeconds));
		minesLeft.setText(Integer.toString(Ultimate Tic-Tac-Toe.numOfMinesLeft));
		infoPanel.add(clockPane);
		infoPanel.add(minesLeft);

		// Adds panels to frame
		mainFrame.getContentPane().add(infoPanel);
		mainFrame.getContentPane().add(gamePanel);

		// Adds menuBar to frame
		mainFrame.setJMenuBar(createMenuBar());

		// Sets title, size, layout and location of GUI window
		mainFrame.setTitle("Ultimate Tic-Tac-Toe");
		mainFrame.setSize(Ultimate Tic-Tac-Toe.mapSizeY * 30 + 30, Ultimate Tic-Tac-Toe.mapSizeX * 30 + 126);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setLocationRelativeTo(null);

		// Makes the program terminate on press of close window
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets window to visible and disable resizing
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);

		if (isLoadGame) {

			// Refreshes GUI according to save data
			for (int i = 0; i < Ultimate Tic-Tac-Toe.mapSizeX; i++)
				for (int j = 0; j < Ultimate Tic-Tac-Toe.mapSizeY; j++) {

					// Adds buttons to game panel
					if (Ultimate Tic-Tac-Toe.map[i][j].getMineType() == SquareTypes.FLAG)
						flagSquare(i, j);
					else if (Ultimate Tic-Tac-Toe.map[i][j].getMineType() == SquareTypes.EMPTY)
						showValue(i, j);
				}

			// Shows a popup telling the user that the saved game has been loaded
			JOptionPane.showMessageDialog(mainFrame.getContentPane(), new JLabel("Savegame loaded!", JLabel.CENTER),
					"FileLoader", JOptionPane.INFORMATION_MESSAGE);
		}

		// Plays new game sound if sound is enabled
		if (soundCheck) {
			AudioInputStream newGameSound;
			try {
				newGameSound = AudioSystem
						.getAudioInputStream(this.getClass().getClassLoader().getResource("NewGame.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(newGameSound);
				clip.start();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame.getContentPane(),
						new JLabel("You did not download the music file!", JLabel.CENTER), "Ultimate Tic-Tac-Toe Sound Player",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Creates the menuBar
	 * 
	 * @return The finished JMenuBar
	 */
	public JMenuBar createMenuBar() {

		// Adds gameMenu to menuBar
		menuBar.add(gameMenu);

		// Adds actionListeners to menuItems and adds menuItems to gameMenu
		newGame.addActionListener(this);
		gameMenu.add(newGame);
		saveGame.addActionListener(this);
		gameMenu.add(saveGame);
		loadGame.addActionListener(this);
		gameMenu.add(loadGame);

		// Adds newGame submenu
		gameMenu.addSeparator();

		// Adds actionListeners to submenuItems and adds menuItems to gameMenu
		beginner.addActionListener(this);
		newSubmenu.add(beginner);
		intermediate.addActionListener(this);
		newSubmenu.add(intermediate);
		expert.addActionListener(this);
		newSubmenu.add(expert);
		custom.addActionListener(this);
		newSubmenu.add(custom);
		gameMenu.add(newSubmenu);

		// Adds sound menuItem with actionListener to gameMenu
		gameMenu.addSeparator();
		sound.addActionListener(this);
		gameMenu.add(sound);

		// Adds help menu to menubar
		menuBar.add(helpMenu);
		about.addActionListener(this);
		helpMenu.add(about);
		howToPlay.addActionListener(this);
		helpMenu.add(howToPlay);

		// Returns the finished menuBar
		return menuBar;
	}

	/**
	 * MousePressed method sets the button as pressed
	 */
	public void mousePressed(MouseEvent event) {
		for (int i = 0; i < buttons.length; i++)
			for (int j = 0; j < buttons[i].length; j++)
				if (buttons[i][j] == event.getSource()) {
					buttons[i][j].getModel().setPressed(true);
					pressed = true;
				}
	}

	/**
	 * MouseReleased method changes left-clicked button to number of mines,
	 * left-clicked button with a mine to game over, and right-clicked button to a
	 * picture of a flag.
	 */
	public void mouseReleased(MouseEvent event) {

		for (int i = 0; i < buttons.length; i++)
			for (int j = 0; j < buttons[i].length; j++)
				if (buttons[i][j] == event.getSource()) {

					// Sets button as not pressed
					buttons[i][j].getModel().setPressed(false);

					// Checks if the same button was pressed
					if (pressed) {

						// Checks if the mouse click was a right-click
						if (event.getButton() == MouseEvent.BUTTON3
								&& (Ultimate Tic-Tac-Toe.map[i][j].getMineType() == SquareTypes.UNKNOWN
										|| Ultimate Tic-Tac-Toe.map[i][j].getMineType() == SquareTypes.FLAG)) {

							// Plays the flag sound if sound is enabled
							if (soundCheck) {
								AudioInputStream flagSound;
								try {
									flagSound = AudioSystem.getAudioInputStream(
											this.getClass().getClassLoader().getResource("Flag.wav"));
									Clip clip = AudioSystem.getClip();
									clip.open(flagSound);
									clip.start();
								} catch (Exception e) {
									JOptionPane.showMessageDialog(mainFrame.getContentPane(),
											new JLabel("You did not download the music file!", JLabel.CENTER),
											"Ultimate Tic-Tac-Toe Sound Player", JOptionPane.ERROR_MESSAGE);
								}
							}

							// If there is no flag at the square
							if (Ultimate Tic-Tac-Toe.map[i][j].getMineType() != SquareTypes.FLAG) {

								// Flags the square
								flagSquare(i, j);
							}

							// If there is a flag at the square
							else {

								// Reset button
								buttons[i][j].setIcon(null);
								Ultimate Tic-Tac-Toe.map[i][j].changeType(SquareTypes.UNKNOWN);
								Ultimate Tic-Tac-Toe.numOfMinesLeft++;
								minesLeft.setText(Integer.toString(Ultimate Tic-Tac-Toe.numOfMinesLeft));
							}
						}
						// If the mouse click was a left-click
						else if (event.getButton() == MouseEvent.BUTTON1
								&& Ultimate Tic-Tac-Toe.map[i][j].getMineType() == SquareTypes.UNKNOWN) {

							// Starts timer on the first click
							if (firstClick) {
								clock = new Clock(clockPane);
								firstClick = false;
							}

							// Checks if a mine exists at the clicked square
							if (!Ultimate Tic-Tac-Toe.checkForMine(i, j)
									&& Ultimate Tic-Tac-Toe.map[i][j].getMineType() != SquareTypes.FLAG) {
								if (Ultimate Tic-Tac-Toe.genNumOfMines(i, j) != 0) {
									showValue(i, j);

									// Plays click sound if sound is enabled
									if (soundCheck) {
										AudioInputStream clickSound;
										try {
											clickSound = AudioSystem.getAudioInputStream(
													this.getClass().getClassLoader().getResource("Click.wav"));
											Clip clip = AudioSystem.getClip();
											clip.open(clickSound);
											clip.start();
										} catch (Exception e) {
											JOptionPane.showMessageDialog(mainFrame.getContentPane(),
													new JLabel("You did not download the music file!", JLabel.CENTER),
													"Ultimate Tic-Tac-Toe Sound Player", JOptionPane.ERROR_MESSAGE);
										}
									}
								}

								/*
								 * Calls recursive function to auto-click all connecting blank squares and
								 * surrounding numbered squares
								 */
								else
									recursion(i, j);

								boolean won = true;

								// Checks if all empty squares were clicked
								for (int k = 0; k < Ultimate Tic-Tac-Toe.map.length; k++)
									for (int l = 0; l < Ultimate Tic-Tac-Toe.map[k].length; l++)

										// Exit loops and set won to false if any non-mine square has not been clicked
										if (!Ultimate Tic-Tac-Toe.map[k][l].checkMine()
												&& Ultimate Tic-Tac-Toe.map[k][l].getMineType() == SquareTypes.UNKNOWN) {
											won = false;
											break;
										}

								if (won) {

									// Plays the win sound if sound is enabled
									if (soundCheck) {
										AudioInputStream winSound;
										try {
											winSound = AudioSystem.getAudioInputStream(
													this.getClass().getClassLoader().getResource("Win.wav"));
											Clip clip = AudioSystem.getClip();
											clip.open(winSound);
											clip.start();
										} catch (Exception e) {
											JOptionPane.showMessageDialog(mainFrame.getContentPane(),
													new JLabel("You did not download the music file!", JLabel.CENTER),
													"Ultimate Tic-Tac-Toe Sound Player", JOptionPane.ERROR_MESSAGE);
										}
									}

									// For all squares on map, disables clicking
									for (int k = 0; k < Ultimate Tic-Tac-Toe.mapSizeX; k++)
										for (int l = 0; l < Ultimate Tic-Tac-Toe.mapSizeY; l++) {
											buttons[k][l].removeMouseListener(this);

											// If a square has a mine, autoflag it
											if (Ultimate Tic-Tac-Toe.map[k][l].getMineType() == SquareTypes.UNKNOWN) {
												Ultimate Tic-Tac-Toe.map[k][l].changeType(SquareTypes.FLAG);

												// Flags the square
												flagSquare(k, l);
											}
										}

									// Shows a win dialog and stops timer
									try {
										clock.cancel();
									} catch (Exception e) {
									}
									finished = true;
									JOptionPane.showMessageDialog(mainFrame.getContentPane(), new JLabel(
											"<html><div style='text-align: center;'>Congratulations!<br>You've won the game!<br>Game created by:<br>Raymond Li and David Tuck</div></html>"),
											"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
								}
							}

							// If user clicks on a mine
							else {

								// Plays the explosion sound if sound is enabled
								if (soundCheck) {
									AudioInputStream explosionSound;
									try {
										explosionSound = AudioSystem.getAudioInputStream(
												this.getClass().getClassLoader().getResource("Explosions.wav"));
										Clip clip = AudioSystem.getClip();
										clip.open(explosionSound);
										clip.start();
									} catch (Exception e) {
										JOptionPane.showMessageDialog(mainFrame.getContentPane(),
												new JLabel("You did not download the music file!", JLabel.CENTER),
												"Ultimate Tic-Tac-Toe Sound Player", JOptionPane.ERROR_MESSAGE);
									}
								}

								// Stops timer
								try {
									clock.cancel();
								} catch (Exception e) {
								}
								finished = true;

								for (int k = 0; k < Ultimate Tic-Tac-Toe.mapSizeX; k++)
									for (int l = 0; l < Ultimate Tic-Tac-Toe.mapSizeY; l++) {

										// For all squares on map, disables clicking
										buttons[k][l].removeMouseListener(this);

										// For squares with mine, shows mine image
										if (Ultimate Tic-Tac-Toe.checkForMine(k, l)) {

											if (!(Ultimate Tic-Tac-Toe.map[k][l].getMineType() == SquareTypes.FLAG))

												// Sets the buttons to an image (Mine.png)
												buttons[k][l].setIcon(new ImageIcon(
														this.getClass().getClassLoader().getResource("Mine.png")));
										}

										// Sets the buttons to Wrong.png if the flag was not a mine
										if (Ultimate Tic-Tac-Toe.map[k][l].getMineType() == SquareTypes.FLAG
												&& !Ultimate Tic-Tac-Toe.checkForMine(k, l))
											buttons[k][l].setIcon(new ImageIcon(
													this.getClass().getClassLoader().getResource("Wrong.png")));

									}

								// Sets the button to an image (Explode.png)
								buttons[i][j].setIcon(
										new ImageIcon(this.getClass().getClassLoader().getResource("Explode.png")));

								// Ends the game with a game over
								JOptionPane.showMessageDialog(mainFrame.getContentPane(), new JLabel(
										"<html><div style='text-align: center;'>Game Over!<br>Better luck next time!<br>Game created by:<br>Raymond Li and David Tuck</div></html>"),
										"Game Over!", JOptionPane.ERROR_MESSAGE);
							}
						}
					}

					// Resets pressed variable to false
					pressed = false;
				}
	}

	// Sets pressed to false if cursor leaves a button
	public void mouseExited(MouseEvent event) {
		pressed = false;
	}

	// Sets pressed to true if cursor enters a button that is not the help
	public void mouseEntered(MouseEvent event) {
		pressed = true;
	}

	/**
	 * Action performed method to control the menuBar
	 */
	public void actionPerformed(ActionEvent event) {

		/*
		 * If the beginner button is clicked, sets size of map to 9x9 and the number of
		 * mines to 10, and proceed to initialize the map with mines and empty squares,
		 * disposing of the menu window when done
		 */
		if (newGame == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}
			try {
				Ultimate Tic-Tac-Toe.menufinished();
			} catch (Exception e) {
			}
			mainFrame.dispose();
		}

		/*
		 * Sets up file chooser and loads game from file if the loadGame button is
		 * clicked
		 */
		else if (saveGame == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}

			if (!finished) {

				// Setup file saver
				JFileChooser saveFile = new JFileChooser();
				saveFile.setCurrentDirectory(new File("."));
				saveFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				saveFile.addChoosableFileFilter(new MSSGFilter());
				saveFile.setAcceptAllFileFilterUsed(false);

				// Processes the results of getting the user to load a game
				if (saveFile.showDialog(mainFrame, "Save Game") == JFileChooser.APPROVE_OPTION) {
					File game = saveFile.getSelectedFile();

					// Resets the file chooser for the next time it's shown
					saveFile.setSelectedFile(null);

					// Try-catch to handle exceptions
					try {

						// Saves game to file
						Ultimate Tic-Tac-Toe.writeToFile(game.getName());
					} catch (Exception e) {

						JOptionPane.showMessageDialog(mainFrame.getContentPane(),
								new JLabel("Oops, something happened and the game was not saved.", JLabel.CENTER),
								"FileSaver", JOptionPane.ERROR_MESSAGE);
					}

					// Shows a popup telling the user that the saved game has been loaded
					JOptionPane.showMessageDialog(mainFrame.getContentPane(), new JLabel("Game Saved!", JLabel.CENTER),
							"FileSaver", JOptionPane.INFORMATION_MESSAGE);

					// Starts clock
					clock = new Clock(clockPane);
				}
			} else
				JOptionPane.showMessageDialog(mainFrame.getContentPane(),
						new JLabel("You can not save a finished game.", JLabel.CENTER), "FileSaver",
						JOptionPane.ERROR_MESSAGE);
		}

		/*
		 * Sets up file chooser and loads game from file if the loadGame button is
		 * clicked
		 */
		else if (loadGame == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}

			JFileChooser loadFile = new JFileChooser();

			// Sets the default directory to wherever the Ultimate Tic-Tac-Toe game is
			loadFile.setCurrentDirectory(new File("."));

			// Adds a custom file filter and disables the default (Accept All) file filter
			loadFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			loadFile.addChoosableFileFilter(new MSSGFilter());
			loadFile.setAcceptAllFileFilterUsed(false);

			// Try-catch to handle exceptions
			try {

				// Processes the results of getting the user to load a game
				if (loadFile.showDialog(mainFrame, "Load Game") == JFileChooser.APPROVE_OPTION) {
					File game = loadFile.getSelectedFile();

					// Resets the file chooser for the next time it's shown
					loadFile.setSelectedFile(null);

					// Reads saved game from file
					Ultimate Tic-Tac-Toe.readFromFile(game.getName());
					mainFrame.dispose();
				}

				/*
				 * Shows a popup telling the user that the saved game has not been loaded
				 */
				else {
					JOptionPane.showMessageDialog(mainFrame.getContentPane(),
							new JLabel("Savegame not loaded. Bad File.", JLabel.CENTER), "FileLoader",
							JOptionPane.ERROR_MESSAGE);

					// Starts clock
					clock = new Clock(clockPane);
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame.getContentPane(),
						new JLabel("Savegame not loaded. Bad File.", JLabel.CENTER), "FileLoader",
						JOptionPane.ERROR_MESSAGE);
				if (!finished)

					// Starts clock
					clock = new Clock(clockPane);
			}
		}

		else if (beginner == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}
			Ultimate Tic-Tac-Toe.mapSizeX = 9;
			Ultimate Tic-Tac-Toe.mapSizeY = 9;
			Ultimate Tic-Tac-Toe.mineCount = 10;
			try {
				Ultimate Tic-Tac-Toe.menufinished();
			} catch (Exception e) {
			}
			mainFrame.dispose();
		}

		/*
		 * If the intermediate button is clicked, sets size of map to 16x16 and the
		 * number of mines to 40, and proceed to initialize the map with mines and empty
		 * squares, disposing of the Menu window when done
		 */
		else if (intermediate == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}
			Ultimate Tic-Tac-Toe.mapSizeX = 16;
			Ultimate Tic-Tac-Toe.mapSizeY = 16;
			Ultimate Tic-Tac-Toe.mineCount = 40;
			try {
				Ultimate Tic-Tac-Toe.menufinished();
			} catch (Exception e) {
			}
			mainFrame.dispose();
		}

		/*
		 * If the expect button is clicked, sets size of map to 16x30 and the number of
		 * mines to 99, and proceed to initialize the map with mines and empty squares,
		 * disposing of the Menu window when done
		 */
		else if (expert == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}
			Ultimate Tic-Tac-Toe.mapSizeX = 16;
			Ultimate Tic-Tac-Toe.mapSizeY = 30;
			Ultimate Tic-Tac-Toe.mineCount = 99;
			try {
				Ultimate Tic-Tac-Toe.menufinished();
			} catch (Exception e) {
			}
			mainFrame.dispose();
		}

		// If the custom button is clicked, opens a CustomMode dialog
		else if (custom == event.getSource()) {

			// Stops the clock
			try {
				clock.cancel();
			} catch (Exception e) {
			}

			// Calls a custom mode dialog
			CustomModeDialog customMode = new CustomModeDialog(mainFrame);

			// Packs the customMode dialog
			customMode.pack();

			// Sets the location of the customMode dialog
			customMode.setLocationRelativeTo(mainFrame.getContentPane());

			// Shows the customMode dialog
			customMode.setResizable(false);
			customMode.setVisible(true);
		}

		// If the about button is clicked, opens a dialog
		else if (about == event.getSource()) {

			// Shows a creators dialog
			JOptionPane.showMessageDialog(mainFrame.getContentPane(),
					new JLabel("Game created by Raymond Li and David Tuck.", JLabel.CENTER), "About",
					JOptionPane.INFORMATION_MESSAGE);
		}

		// If the how to play button is clicked, opens a webpage
		else if (howToPlay == event.getSource()) {

			// Opens Ultimate Tic-Tac-Toe.info
			try {
				Desktop.getDesktop().browse(new URL("http://Ultimate Tic-Tac-Toe.info/wiki/Windows_Minesweeper").toURI());
			} catch (Exception e) {
			}
		}

		// If the sound checkbox is clicked, assigns soundCheck to the value of sound
		else if (sound == event.getSource())
			soundCheck = sound.getState();
	}

	/**
	 * Allows for empty squares around a mouse click to be "pressed".
	 * 
	 * @param m
	 *            x coordinate of user mouse click
	 * @param n
	 *            y coordinate of user mouse click
	 */
	public void recursion(int m, int n) {

		if (Ultimate Tic-Tac-Toe.genNumOfMines(m, n) == 0 && Ultimate Tic-Tac-Toe.map[m][n].getMineType() == SquareTypes.UNKNOWN) {

			Ultimate Tic-Tac-Toe.map[m][n].changeType(SquareTypes.EMPTY);
			buttons[m][n].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			// Plays click sound if sound is enabled
			if (soundCheck) {
				AudioInputStream clickSound;
				try {
					clickSound = AudioSystem
							.getAudioInputStream(this.getClass().getClassLoader().getResource("Click.wav"));
					Clip clip = AudioSystem.getClip();
					clip.open(clickSound);
					clip.start();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame.getContentPane(),
							new JLabel("You did not download the music file!", JLabel.CENTER),
							"Ultimate Tic-Tac-Toe Sound Player", JOptionPane.ERROR_MESSAGE);
				}
			}

			// Disables clicks on square
			buttons[m][n].removeMouseListener(this);

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					try {
						recursion(i + m, j + n);
					} catch (Exception e) {
					}
				}
			}
		} else {
			showValue(m, n);
		}
	}

	/**
	 * Generates the number of mines around position i,j. sets the color and bevel.
	 * 
	 * @param i
	 *            x coordinate for square that is to be shown
	 * @param j
	 *            y coordinate for square that is to be shown
	 */
	public void showValue(int i, int j) {
		int mineCount = Ultimate Tic-Tac-Toe.genNumOfMines(i, j);
		if (Ultimate Tic-Tac-Toe.map[i][j].getMineType() != SquareTypes.FLAG) {

			if (mineCount != 0) {
				buttons[i][j].setText(Integer.toString(mineCount));
				buttons[i][j].setForeground(mycolors[mineCount]);
			}
			Ultimate Tic-Tac-Toe.map[i][j].changeType(SquareTypes.EMPTY);
			buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			// Disables clicks on square
			buttons[i][j].removeMouseListener(this);
		}
	}

	/**
	 * Flags the selected square
	 * 
	 * @param i
	 *            x coordinate for square that is to be flagged
	 * @param j
	 *            y coordinate for square that is to be flagged
	 */
	public void flagSquare(int i, int j) {
		// Sets the button to an image (Flag.png)
		buttons[i][j].setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("Flag.png")));
		Ultimate Tic-Tac-Toe.map[i][j].changeType(SquareTypes.FLAG);
		Ultimate Tic-Tac-Toe.numOfMinesLeft--;
		minesLeft.setText(Integer.toString(Ultimate Tic-Tac-Toe.numOfMinesLeft));
	}

	/**
	 * Calls mouseReleased method
	 * 
	 * @param event What kind of mouse is clicked, or what mouse button is clicked
	 */
	public void mouseClicked(MouseEvent event) {
		mouseReleased(event);
	}
}