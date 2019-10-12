package soen6441riskgame.views;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

import soen6441riskgame.enums.PrintConsoleAndUserInput;

// TODO: Auto-generated Javadoc
/**
 * This class is used to create a map. Also, this creates the window to show the map by using JFrame.
 * 
 * 
 * @version 1.0.0
 */
public class MapView {

	/** The print. */
	PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();
	
	/** The panel window. */
	JPanel panelWindow = new JPanel();
	
	/** The frame window. */
	JFrame frameWindow = new JFrame(getClass().getSimpleName());
	
	/** The text field name. */
	String textFieldName = "Enter file name you want to save?";
	
	/** The map name. */
	JTextField mapName = new JTextField(textFieldName);
	
	/** The save button. */
	public JButton saveButton = new JButton("Save Map File");
	
	/** The text parameters. */
	JTextArea textParameters = new JTextArea("[Map]\n\n\n[Continents]\n\n\n[Territories]");

	/**
	 * This method displays menu for map generator.
	 * @return  user input for map menu
	 */
	public int displayMapMenu() {
		print.consoleOut("\n PROCEED");
		print.consoleOut("\t MAP GENERATOR MENU\t");
		print.consoleOut("1. VERIFY MAP FILE");
		print.consoleOut("2. CREATE A NEW MAP FROM SCRATCH");
		print.consoleOut("3. EDIT THE MAP");
		print.consoleOut("4. Back to The Main Menu");
		print.consoleOut("*\n");
		return print.userIntInput();
	}
	/**
	 * This method displays the map menu while editing the map.
	 * @return userIntInput
	 */
	public int editMapMenu() {
		
		print.consoleOut("\n PROCEED");
		print.consoleOut("\t EDIT MAP MENU\t");
		print.consoleOut("1. ADD CONTINENT to the map?");
		print.consoleOut("2. ADD COUNTRY to the map?");
		print.consoleOut("3. DELETE CONTINENT from the map?");
		print.consoleOut("4. DELETE COUNTRY from the map?");
		print.consoleOut("5. Back to map menu?");
		print.consoleOut("*");
		print.consoleOut("Select number from above editing menu:");
		return print.userIntInput();
	}
	/**
	 * This method is used to return content of file(like continents, territories etc).
	 * @return String mapContent
	 */
	public String returnMapContent() {
		return textParameters.getText();
	}

	/**
	 * This method is used to return name of the map.
	 * @return String mapName
	 */
	public String returnMapName() {
		return mapName.getText();
	}

	/**
	 * This method is used to open a Jframe window to create a map.
	 * 
	 */
	public void createJframe() {
		// TODO Auto-generated method stub		
		panelWindow.setLayout(new FlowLayout());
		panelWindow.setPreferredSize(new Dimension(100, 100));
		panelWindow.setBackground(Color.lightGray);
		panelWindow.add(saveButton);
		panelWindow.add(mapName);
		panelWindow.add(Box.createHorizontalGlue());

		// mapname text parameters
		mapName.setPreferredSize(new Dimension(300, 40));
		mapName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (mapName.getText().equals(textFieldName)) {
					mapName.setText("");
					mapName.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (mapName.getText().isEmpty()) {
					mapName.setForeground(Color.GRAY);
					mapName.setText(textFieldName);
				}
			}
		});
		saveButton.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
		saveButton.setPreferredSize(new Dimension(180, 40));

		frameWindow.setTitle("Map Generator for Risk Board Game");
		frameWindow.setPreferredSize(new Dimension(800, 800));
		frameWindow.setLocationRelativeTo(null);
		frameWindow.add(textParameters);
		textParameters.setFont(new Font("Serif", Font.BOLD,24));
		frameWindow.add(panelWindow, BorderLayout.SOUTH);   // consists of save button and textfield
		frameWindow.pack();
		frameWindow.setVisible(true);
		frameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * This method is used to close the jFrame window.
	 */
	public void closeFrameWindow() {
		// TODO Auto-generated method stub
		frameWindow.dispose();
	}
}
