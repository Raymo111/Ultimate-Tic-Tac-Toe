/*
 * Author: Raymond Li
 * Date started: 2018-01-24
 * Description: Ultimate Tic Tac Toe program with GUI (PVP only) 
 */

//Imports java GUI classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

// Main class with JFrame and ActionListener enabled
public class Ultimate_Tic_Tac_Toe extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1567707511932220992L;

	// Adds panels
	JPanel[][] panels = new JPanel[3][3];
	JPanel gamePanel = new JPanel();
	JPanel controlPanel = new JPanel();

	// Adds control button
	JButton newP = new JButton("New PVP");

	// Adds game button container
	Object[][] gameButtons = new Object[3][3];

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

	// Constructor
	public Ultimate_Tic_Tac_Toe() {

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
		getContentPane().add(gamePanel);
		getContentPane().add(controlPanel);

		// Sets title, size, layout Box on y-axis, and location of GUI window
		setTitle("Ultimate Tic Tac Toe");
		setSize(733, 1080);
		setLayout(new GridLayout(2, 1));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// Main method
	public static void main(String[] args) {

		// Starts a new instance of Ultimate Tic Tac Toe
		new Ultimate_Tic_Tac_Toe();
	}

	// Action performed method for action listener
	public void actionPerformed(ActionEvent event) {

		// Handles control clicks
		if (newP == event.getSource()) {

			// Close instance and create a new one
			dispose();
			new Ultimate_Tic_Tac_Toe();
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