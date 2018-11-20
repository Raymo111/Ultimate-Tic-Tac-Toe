/*
 * Author: Raymond Li
 * Date started: 2018-01-24
 * Description: Ultimate Tic Tac Toe program with GUI (PVP only) 
 */

//Imports java GUI classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

// Main class with JFrame and ActionListener enabled
public class Ultimate_Tic_Tac_Toe implements ActionListener {

	// Private class Variables
	// Main frame that contains everything
	public JFrame mainFrame = new JFrame();

	// Adds panels
	private static JPanel[][] panels = new JPanel[3][3];
	private static JPanel gamePanel = new JPanel();
	private static JPanel controlPanel = new JPanel();

	// Adds control button
	private static JButton newP = new JButton("New PVP");

	// Adds game button container
	private static Object[][] gameButtons = new Object[3][3];

	// Declares class variables
	private static int count = 0;
	private static boolean[][] checkBig = new boolean[3][3], checkClicked[][] = new boolean[3][3][3][3],
			checkArea[][] = new boolean[3][3][3][3], checkWin[][] = new boolean[3][3][3][3];
	private static char[] aMoves[][][] = new char[3][3][3][3], bMoves[] = new char[3][3];
	private static final JButton bigButtonX = new JButton("X"), bigButtonO = new JButton("O");

	// Creates 3 different layouts
	private static final GridLayout grid1 = new GridLayout(3, 3), grid2 = new GridLayout(1, 1),
			grid3 = new GridLayout(3, 3);
	private static final FlowLayout flow1 = new FlowLayout();

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
	 * read a saved game from a file
	 *
	 * @param fileName The name the user has the games saved as.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void readFromFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			panels = (JPanel[][]) inStream.readObject();
			gamePanel = (JPanel) inStream.readObject();
			controlPanel = (JPanel) inStream.readObject();
			gameButtons = (Object[][]) inStream.readObject();
			count = Integer.parseInt((String) inStream.readObject());
			checkBig = (boolean[][]) inStream.readObject();
			checkClicked = (boolean[][][][]) inStream.readObject();
			checkArea = (boolean[][][][]) inStream.readObject();
			checkWin = (boolean[][][][]) inStream.readObject();
			aMoves = (char[][][][]) inStream.readObject();
			bMoves = (char[][]) inStream.readObject();
			inStream.close();
			fileIn.close();
			new Ultimate_Tic_Tac_Toe(true);
		} catch (FileNotFoundException e) {
		}
	}

	/**
	 * Saves a current game to a file
	 *
	 * @param array    2D array for objects that holds all of the information about
	 *                 the game board.
	 * @param fileName The name the user has the games saved as.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeToFile(String fileName) throws FileNotFoundException, IOException {
		try {
			FileOutputStream fileOut;
			if (fileName.contains(".mssg"))
				fileOut = new FileOutputStream(fileName);
			else
				fileOut = new FileOutputStream(fileName + ".mssg");
			ObjectOutput outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(panels);
			outStream.writeObject(gamePanel);
			outStream.writeObject(controlPanel);
			outStream.writeObject(gameButtons);
			outStream.writeObject(count);
			outStream.writeObject(checkBig);
			outStream.writeObject(checkClicked);
			outStream.writeObject(checkArea);
			outStream.writeObject(checkWin);
			outStream.writeObject(aMoves);
			outStream.writeObject(bMoves);
			outStream.close();
			fileOut.close();
		} catch (IOException e) {
		}
	}

	// Constructor
	public Ultimate_Tic_Tac_Toe(boolean isLoadGame) {

		// Adds Hgap and Vgap to grid1
		grid1.setHgap(10);
		grid1.setVgap(10);

		// Initializes and assigns layouts to each JPanel
		for (JPanel[] row : panels)
			Arrays.fill(row, new JPanel());
		for (int i = 0; i < panels.length; i++)
			for (int j = 0; j < panels[i].length; j++) {
				panels[i][j] = new JPanel();
				panels[i][j].setLayout(grid3);
				panels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, false));
			}

		// Sets layout of panels
		gamePanel.setLayout(grid1);
		controlPanel.setLayout(flow1);

		// Sets border and background of gamePanel
		gamePanel.setBackground(Color.BLACK);
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));

		// Adds game buttons to game button container
		for (int i = 0; i < gameButtons.length; i++) {
			for (int j = 0; j < gameButtons[i].length; j++) {
				gameButtons[i][j] = new JButton[3][3];
			}
		}

		// Initializes checkBig, check and checkArea
		for (boolean[] row : checkBig)
			Arrays.fill(row, true);
		for (int i = 0; i < checkClicked.length; i++)
			for (int j = 0; j < checkClicked[i].length; j++)
				for (boolean[] row : checkClicked[i][j])
					Arrays.fill(row, true);
		for (int i = 0; i < checkArea.length; i++)
			for (int j = 0; j < checkArea[i].length; j++)
				for (boolean[] row : checkArea[i][j])
					Arrays.fill(row, true);

		// Declares and adds ActionListeners and fonts to gamePanel buttons
		for (int i = 0; i < gameButtons.length; i++)
			for (int j = 0; j < gameButtons[i].length; j++)
				for (int k = 0; k < ((JButton[][]) gameButtons[i][j]).length; k++)
					for (int l = 0; l < ((JButton[][]) gameButtons[i][j])[k].length; l++) {
						((JButton[][]) gameButtons[i][j])[k][l] = new JButton();
						((JButton[][]) gameButtons[i][j])[k][l].addActionListener(this);
						((JButton[][]) gameButtons[i][j])[k][l].setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
						panels[i][j].add(((JButton[][]) gameButtons[i][j])[k][l]);
					}

		// Adds ActionListeners, tooltip texts and fonts to controlPanel button
		newP.addActionListener(this);
		newP.setToolTipText("New game against another player");
		newP.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		controlPanel.add(newP);

		// Adds panels to gamePanel
		for (int i = 0; i < panels.length; i++)
			for (int j = 0; j < panels[i].length; j++)
				gamePanel.add(panels[i][j]);

		// Adds gamePanel and controlPanel
		mainFrame.add(gamePanel);
		mainFrame.add(controlPanel);

		// Adds menuBar to frame
		mainFrame.setJMenuBar(createMenuBar());

		// Sets title, size, layout and location of GUI window
		mainFrame.setTitle("Ultimate Tic-Tac-Toe");
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setSize(640, 640);
		mainFrame.setLocationRelativeTo(null);

		// Makes the program terminate on press of close window
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Sets window to visible and disable resizing
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}

	// Main method
	public static void main(String[] args) {

		// Starts a new instance of Ultimate Tic Tac Toe
		new MenuGUI();
	}

	// Action performed method for action listener
	public void actionPerformed(ActionEvent event) {

		// Handles control clicks
		if (newP == event.getSource()) {

			// Close instance and create a new one
			dispose();
			new MenuGUI();
			count = 0;
		}

		// Handles game clicks
		for (int i = 0; i < gameButtons.length; i++)
			for (int j = 0; j < gameButtons[i].length; j++)
				if (!(gameButtons[i][j].equals(bigButtonX)) && !(gameButtons[i][j].equals(bigButtonO)))
					for (int k = 0; k < ((JButton[][]) gameButtons[i][j]).length; k++)
						for (int l = 0; l < ((JButton[][]) gameButtons[i][j])[k].length; l++)
							if ((JButton) ((JButton[][]) gameButtons[i][j])[k][l] == event.getSource()
									&& checkClicked[i][j][k][l] && checkArea[i][j][k][l]) {

								// If player x clicks
								if (count % 2 == 0) {

									// Sets text of clicked button to X
									((JButton[][]) gameButtons[i][j])[k][l].setText("X");

									// Sets move to x
									aMoves[i][j][k][l] = 'X';
								}

								else {

									// Sets text of clicked button to X
									((JButton[][]) gameButtons[i][j])[k][l].setText("O");

									// Sets move to x
									aMoves[i][j][k][l] = 'O';
								}

								// Disables reclicking button
								checkClicked[i][j][k][l] = false;

								// Checks if clicked area has been won
								if (checkBig[k][l]) {

									// Enables and selects in blue the corresponding area
									for (int m = 0; m < checkArea.length; m++)
										for (int n = 0; n < checkArea[m].length; n++) {
											panels[m][n].setBorder(null);
											for (boolean[] row : checkArea[m][n])
												Arrays.fill(row, false);
										}
									for (boolean[] row : checkArea[k][l])
										Arrays.fill(row, true);
									panels[k][l].setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, false));
								}

								else

									// Select in blue all valid areas
									for (int m = 0; m < checkArea.length; m++)
										for (int n = 0; n < checkArea[m].length; n++)
											if (checkBig[m][n]) {
												panels[m][n].setBorder(
														BorderFactory.createLineBorder(Color.BLUE, 5, false));
												for (boolean[] row : checkArea[m][n])
													Arrays.fill(row, true);
											}

								// Increments count for X/O order
								count++;
							}

		// Checks for wins
		for (int i = 0; i < checkWin.length; i++)
			for (int j = 0; j < checkWin[i].length; j++)
				if (checkBig[i][j])
					for (int k = 0; k < (checkWin[i][j]).length; k++)
						for (int l = 0; l < (checkWin[i][j])[k].length; l++)

							// Checks for whether player X wins
							if (

							// Horizontal checks
							(aMoves[i][j][0][0] == aMoves[i][j][0][1] && aMoves[i][j][0][0] == aMoves[i][j][0][2]
									&& aMoves[i][j][0][0] == 'X')
									|| (aMoves[i][j][1][0] == aMoves[i][j][1][1]
											&& aMoves[i][j][1][0] == aMoves[i][j][1][2] && aMoves[i][j][1][0] == 'X')
									|| (aMoves[i][j][2][0] == aMoves[i][j][2][1]
											&& aMoves[i][j][2][0] == aMoves[i][j][2][2] && aMoves[i][j][2][0] == 'X')
									||

									// Vertical checks
									(aMoves[i][j][0][0] == aMoves[i][j][1][0]
											&& aMoves[i][j][0][0] == aMoves[i][j][2][0] && aMoves[i][j][0][0] == 'X')
									|| (aMoves[i][j][0][1] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][1] == aMoves[i][j][2][1] && aMoves[i][j][0][1] == 'X')
									|| (aMoves[i][j][0][2] == aMoves[i][j][1][2]
											&& aMoves[i][j][0][2] == aMoves[i][j][2][2] && aMoves[i][j][0][2] == 'X')
									||

									// Diagonal checks
									(aMoves[i][j][0][0] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][0] == aMoves[i][j][2][2] && aMoves[i][j][0][0] == 'X')
									|| (aMoves[i][j][0][2] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][2] == aMoves[i][j][1][1] && aMoves[i][j][2][0] == 'X')) {

								// Changes JButton[3][3] array at gameButtons[i][j] to JButton
								gameButtons[i][j] = bigButtonX;
								((JButton) gameButtons[i][j]).setFont(new Font("Comic Sans MS", Font.PLAIN, 40));

								// Resets gamePanel
								panels[i][j].removeAll();
								panels[i][j].setLayout(grid2);
								panels[i][j].add((JButton) gameButtons[i][j]);

								// Sets bMoves and checkBig
								checkBig[i][j] = false;
								bMoves[i][j] = 'X';

								// Sets false to stop rechecking
								checkWin[i][j][k][l] = false;

								// Stops further clicks on game buttons
								for (boolean[] row : checkClicked[i][j])
									Arrays.fill(row, true);

							}

							// Checks for whether player Y wins
							else if (

							// Horizontal checks
							(aMoves[i][j][0][0] == aMoves[i][j][0][1] && aMoves[i][j][0][0] == aMoves[i][j][0][2]
									&& aMoves[i][j][0][0] == 'O')
									|| (aMoves[i][j][1][0] == aMoves[i][j][1][1]
											&& aMoves[i][j][1][0] == aMoves[i][j][1][2] && aMoves[i][j][1][0] == 'O')
									|| (aMoves[i][j][2][0] == aMoves[i][j][2][1]
											&& aMoves[i][j][2][0] == aMoves[i][j][2][2] && aMoves[i][j][2][0] == 'O')
									||

									// Vertical checks
									(aMoves[i][j][0][0] == aMoves[i][j][1][0]
											&& aMoves[i][j][0][0] == aMoves[i][j][2][0] && aMoves[i][j][0][0] == 'O')
									|| (aMoves[i][j][0][1] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][1] == aMoves[i][j][2][1] && aMoves[i][j][0][1] == 'O')
									|| (aMoves[i][j][0][2] == aMoves[i][j][1][2]
											&& aMoves[i][j][0][2] == aMoves[i][j][2][2] && aMoves[i][j][0][2] == 'O')
									||

									// Diagonal checks
									(aMoves[i][j][0][0] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][0] == aMoves[i][j][2][2] && aMoves[i][j][0][0] == 'O')
									|| (aMoves[i][j][0][2] == aMoves[i][j][1][1]
											&& aMoves[i][j][0][2] == aMoves[i][j][2][0] && aMoves[i][j][2][0] == 'O')) {

								// Changes JButton[3][3] array at gameButtons[i][j] to JButton
								gameButtons[i][j] = bigButtonO;
								((JButton) gameButtons[i][j]).setFont(new Font("Comic Sans MS", Font.PLAIN, 40));

								// Resets gamePanel
								panels[i][j].removeAll();
								panels[i][j].setLayout(grid2);
								panels[i][j].add((JButton) gameButtons[i][j]);

								// Sets bMoves and checkBig
								checkBig[i][j] = false;
								bMoves[i][j] = 'Y';

								// Sets false to stop rechecking
								checkWin[i][j][k][l] = false;

								// Stops further clicks on game buttons
								for (boolean[] row : checkClicked[i][j])
									Arrays.fill(row, true);
							}
	}

}