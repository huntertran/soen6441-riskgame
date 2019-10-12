package soen6441riskgame.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import soen6441riskgame.enums.Colors;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.enums.JTableRowNameDominationView;
import soen6441riskgame.commands.Game;
import soen6441riskgame.models.Player;
// TODO: Auto-generated Javadoc
/**
 *	This class shows the updated view of the Players world domination which includes
 * 1. The percentage of the map controlled by every player 
 * 2. The continents controlled by every player
 * 3. The total number of armies owned by every player.
 * 
 * 
 * @version 1.0.0
 */
public class WorldDominationView implements Observer{
	/** The panel window for world domination view. */
	public static JPanel panelWindowForWorldDominationView;

	/** The frame window for world domination view. */
	public static JFrame frameWindowForWorldDominationView;
	public static JTable table;
	public static JScrollPane scroll;
	public static JList rowHeader;
	private static Game gameGlobal;

	/**
	 * Creates the jFrame for world domination view.	 
	 * @param rowData the row data
	 * @param playerNamesInTableColumns the player names in table columns
	 */
	public static void createJframeForWorldDominationView(String[][] rowData, String[] playerNamesInTableColumns) {

		// TODO Auto-generated method stub		
		panelWindowForWorldDominationView = new JPanel(new BorderLayout());
		frameWindowForWorldDominationView = new JFrame("THE PLAYERS WORLD DOMINATION VIEW");
		panelWindowForWorldDominationView.setLayout(new FlowLayout());
		panelWindowForWorldDominationView.setPreferredSize(new Dimension(1000, 200));

		// Putting the data in a table
		ListModel lm = new AbstractListModel() {
			String headers[] = {"Percentage Country", "Continents Owned", "Armies Owned"};
			public int getSize() { return headers.length; }
			public Object getElementAt(int index) {
				return headers[index];
			}
		};

		table = new JTable(rowData, playerNamesInTableColumns);
		table.setEnabled(false);
		table.getTableHeader().setBackground(Color.orange);
		rowHeader = new JList(lm);
		rowHeader.setFixedCellWidth(150);
		rowHeader.setFixedCellHeight(table.getRowHeight() + table.getRowMargin());
		rowHeader.setCellRenderer(new JTableRowNameDominationView(table));
		scroll = new JScrollPane( table );
		scroll.setRowHeaderView(rowHeader);
		frameWindowForWorldDominationView.getContentPane().add(scroll, BorderLayout.CENTER);
		frameWindowForWorldDominationView.setSize(1000, 200);
		frameWindowForWorldDominationView.setLocationRelativeTo(null);
		frameWindowForWorldDominationView.setVisible(true);
		frameWindowForWorldDominationView.add(panelWindowForWorldDominationView);
		frameWindowForWorldDominationView.pack();
		frameWindowForWorldDominationView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameWindowForWorldDominationView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameGlobal.dominationViewOn =false;
				frameWindowForWorldDominationView.dispose();
			}
		});
	}


	/**
	 * This method is used to update the values of the column cells
	 * @param rowData data displayed in columns
	 * @param playerNamesInTableColumns Player names in columns
	 */
	public static void updateWindow(String[][] rowData, String[] playerNamesInTableColumns) {
		frameWindowForWorldDominationView.getContentPane().removeAll();
		frameWindowForWorldDominationView.repaint();
		panelWindowForWorldDominationView = new JPanel(new BorderLayout());
		panelWindowForWorldDominationView.setLayout(new FlowLayout());
		panelWindowForWorldDominationView.setPreferredSize(new Dimension(1000, 200));

		// Putting the data in a table
		ListModel lm = new AbstractListModel() {
			String headers[] = {"Percentage Country", "Continents Owned", "Armies Owned"};
			public int getSize() { return headers.length; }
			public Object getElementAt(int index) {
				return headers[index];
			}
		};

		table = new JTable(rowData, playerNamesInTableColumns);
		table.setEnabled(false);
		table.getTableHeader().setBackground(Color.orange);

		rowHeader = new JList(lm);
		rowHeader.setFixedCellWidth(150);
		rowHeader.setFixedCellHeight(table.getRowHeight()
				+ table.getRowMargin());

		rowHeader.setCellRenderer(new JTableRowNameDominationView(table));

		scroll = new JScrollPane( table );
		scroll.setRowHeaderView(rowHeader);
		frameWindowForWorldDominationView.getContentPane().add(scroll, BorderLayout.CENTER);
		frameWindowForWorldDominationView.setSize(1000, 200);
		frameWindowForWorldDominationView.setLocationRelativeTo(null);
		frameWindowForWorldDominationView.setVisible(true);
		frameWindowForWorldDominationView.add(panelWindowForWorldDominationView);
		frameWindowForWorldDominationView.pack();
		frameWindowForWorldDominationView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameWindowForWorldDominationView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameGlobal.dominationViewOn =false;
				frameWindowForWorldDominationView.dispose();
			}
		});

	}

	

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		Game game = ((Game)o);
		gameGlobal = game;
		DecimalFormat countryPercentFormat = new DecimalFormat(".####");
		ArrayList<Player> playerList = game.getAllPlayers();

		// Get players from the above arraylist and add in the other arraylist.
		int x = 0;
		ArrayList<String> newPlayerNameList = new ArrayList<>();
		for (Player playerData : playerList) {
			String name = playerData.getPlayerName();
			newPlayerNameList.add(name);
			x++;
		}

		// print Player name in tabular columns(Ist row heading)
		String[] playerNamesInTableColumns = new String[newPlayerNameList.size()];
		int y=0;
		for ( String nameOfPlayer : newPlayerNameList ) {

			playerNamesInTableColumns[y] = "Player name : "+nameOfPlayer;
			y++;
		}
		int size = newPlayerNameList.size();

		// Get the Percentage of the map controlled by every player
		Float[] mapPercentage = new Float[size];
		HashMap<Integer,Float> findPercentageOfMap =  game.getPercentageOfMapControlledByEveryPlayer();
		int z=0;
		for (Map.Entry<Integer, Float> entry : findPercentageOfMap.entrySet()) {
			float value = entry.getValue();
			mapPercentage[z] = value;
			z++;
		}

		//Get the continents controlled by every player
		String[] continentsAcquired = new String[size];
		HashMap<Integer,String> findContinentsAcquired =  game.getContinentsControlledByEachPlayer();
		int l=0;
		for (Map.Entry<Integer, String> entry : findContinentsAcquired.entrySet()) {
			String value = entry.getValue();
			continentsAcquired[l] = value;
			l++;
		}

		int[] numberOfArmies = new int[size];
		HashMap<Integer, Integer> armiesMap = game.getNumberOfArmiesForEachPlayer();
		int i=0;
		for (Map.Entry<Integer, Integer> entry : armiesMap.entrySet()) {
			int value = entry.getValue();
			numberOfArmies[i] = value;
			i++;
		}

		// To print data in a table
		String[][] dataInTableRows = new String[3][size];
		for (int percentColumn = 0; percentColumn < dataInTableRows[0].length; percentColumn++) {
			String formattedPercent = countryPercentFormat.format(mapPercentage[percentColumn]);
			dataInTableRows[0][percentColumn] = formattedPercent + " %";
		}
		for (int continentColumn = 0; continentColumn < dataInTableRows[0].length; continentColumn++) {
			dataInTableRows[1][continentColumn] = continentsAcquired[continentColumn];
		}
		for (int armyColumn = 0; armyColumn < dataInTableRows[0].length ; armyColumn++) {
			dataInTableRows[2][armyColumn] = Integer.toString(numberOfArmies[armyColumn]);
		}
		if(game.dominationViewOn) {
			updateWindow(dataInTableRows,playerNamesInTableColumns);
		}

	}
}

