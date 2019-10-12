package soen6441riskgame.views;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;


// TODO: Auto-generated Javadoc
/**
 *
 *This class is used to display the Congratulation message to the player who wins the game.
 *
 * 
 */
public class FinishView {

	/** The frame congratulation. */
	private static JFrame frame_congratulation = null;

	/** The panel congratulation. */
	private static JPanel panel_congratulation;

	/** The lab congratulation. */
	private static JLabel lab_congratulation;
	/**
	 * UI for printing the winner of the game
	 * @param playerName the player name
	 */
	public void Exchange(String playerName) {
		frame_congratulation = new JFrame("Congratulations");
		panel_congratulation = new JPanel();
		frame_congratulation.setSize(800, 300);
		frame_congratulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lab_congratulation = new JLabel("Congratulation! Player "+ playerName + " wins the game");
		
		
		Font font = new Font("Courier", Font.BOLD, 25);
		lab_congratulation.setFont(font);
		lab_congratulation.setBounds(100, 100, 220, 40);
		panel_congratulation.add(lab_congratulation);
		frame_congratulation.add(panel_congratulation);
		frame_congratulation.setVisible(true);

	}
}
