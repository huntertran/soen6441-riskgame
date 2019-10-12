package soen6441riskgame.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


import soen6441riskgame.commands.Game;
import soen6441riskgame.enums.Card;
import soen6441riskgame.enums.PrintConsoleAndUserInput;

// TODO: Auto-generated Javadoc
/**
 *
 * This class is used to chose the cards by the player during the Reinforcement phase of the game to obtain new armies.
 * 
 * 
 * @version 1.0.0
 */
public  class CardView implements Observer{
	
	/** The frame card exchange. */
	public static JFrame frame_cardExchange = null;
	
	/** The panel card exchange. */
	private static JPanel panel_cardExchange;
	
	/** The lab card exchange. */
	private static JLabel lab_cardExchange;
	
	/** The lab for player turn. */
	private static JLabel lab_forPlayerTurn;
	
	/** The list cards owned by the player. */
	public static JList<String> list_cardsOwnedByThePlayer;
	
	/** The lab total new armies. */
	private static JLabel lab_totalNewArmies;
	
	/** The button card exchange. */
	private static JButton button_cardExchange = new JButton("Exchange Cards");
	
	/** The button exit. */
	private static JButton button_exit = new JButton("Skip Exchange");
	
	/** The game. */
	//Instantiate game object
	 Game game;
	/**
	 * Instantiates a new card view.
	 *
	 * @param gameTemp the game temp
	 */
	public CardView(Game gameTemp){
		game = gameTemp;
	}
	
	/**
	 * ui for the card exchange.
	 */
	public  void Exchange() {
		frame_cardExchange = new JFrame("Card Exchange View");
		panel_cardExchange = new JPanel(null);
		frame_cardExchange.setSize(800, 600);

		frame_cardExchange.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lab_cardExchange = new JLabel();
		lab_cardExchange
		.setBorder(BorderFactory.createTitledBorder(null, "Exchange Card", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 11), Color.BLACK));
		lab_cardExchange.setBounds(100, 100, 600, 400);
		lab_forPlayerTurn = new JLabel(game.getCurrentPlayer().getPlayerName());
		
		
		Font font = new Font("Courier", Font.BOLD, 14);
		lab_forPlayerTurn.setFont(font);
		lab_forPlayerTurn.setForeground(PrintConsoleAndUserInput.getColor(game.getCurrentPlayer().getColor()));
		lab_forPlayerTurn.setBorder(new TitledBorder("Active Player"));
		lab_forPlayerTurn.setBounds(30, 45, 250, 150);
		lab_forPlayerTurn.setHorizontalAlignment(lab_forPlayerTurn.CENTER);
		lab_forPlayerTurn.setVerticalAlignment(lab_forPlayerTurn.CENTER);
		
		
		//getting the cards a player owns
		ArrayList<Card> typeOfCards = game.getCurrentPlayer().getCards();
		String cards[] = new String[typeOfCards.size()];
		//assigning the cards the player has in a string array
		for (int i = 0; i < typeOfCards.size(); i++) {
			cards[i] = typeOfCards.get(i).toString();
		}
		//putting it in a JList
		list_cardsOwnedByThePlayer = new JList<>(cards);
		list_cardsOwnedByThePlayer.setBorder(new TitledBorder("Cards Owned"));
		list_cardsOwnedByThePlayer.setBounds(310, 45, 250, 150);
		lab_totalNewArmies = new JLabel("" + game.getCurrentPlayer().getNumberOfReinforcedArmies());
		lab_totalNewArmies.setBorder(new TitledBorder("Reinforced Number Army"));
		lab_totalNewArmies.setBounds(180, 200, 250, 70);
		button_cardExchange.setBounds(120, 280, 160, 40);
		button_exit.setBounds(310, 280, 160, 40);
		lab_cardExchange.add(lab_totalNewArmies);
		lab_cardExchange.add(list_cardsOwnedByThePlayer);
		lab_cardExchange.add(lab_forPlayerTurn);
		lab_cardExchange.add(button_cardExchange);
		lab_cardExchange.add(button_exit);
		panel_cardExchange.add(lab_cardExchange);
		frame_cardExchange.add(panel_cardExchange);
		frame_cardExchange.setVisible(true);
		//default close button to not work
		frame_cardExchange.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
	}

	/**
	 * Exchange action listener.
	 *
	 * @param listener the listener
	 */
	public static void exchange_actionListener(ActionListener listener) {
		button_cardExchange.addActionListener(listener);
	}
	
	/**
	 * Exit action listener.
	 * @param listener the listener
	 */
	public static void exit_actionListener(ActionListener listener) {
		button_exit.addActionListener(listener);
	}
	
	/**
	 * Close the window.
	 */
	public static void closeTheWindow() {
		frame_cardExchange.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Game game = ((Game)o);
	}
	
	public void updateCardView(Game game) {

		if (game.getCurrentPlayer() != null && lab_totalNewArmies != null) {
			lab_totalNewArmies.setText("" + game.getCurrentPlayer().getNumberOfReinforcedArmies());
			ArrayList<Card> typeOfCards = game.getCurrentPlayer().getCards();
			String cards[] = new String[typeOfCards.size()];
			for (int i = 0; i < typeOfCards.size(); i++) {
				cards[i] = typeOfCards.get(i).toString();
			}
			lab_cardExchange.remove(list_cardsOwnedByThePlayer);
			list_cardsOwnedByThePlayer = null;
			list_cardsOwnedByThePlayer = new JList<>(cards);
			list_cardsOwnedByThePlayer.setBorder(new TitledBorder("Cards Owned"));
			list_cardsOwnedByThePlayer.setBounds(310, 45, 250, 70);
			lab_cardExchange.add(list_cardsOwnedByThePlayer);	
			frame_cardExchange.revalidate();
			frame_cardExchange.repaint();

		
		}

	}
	
}
