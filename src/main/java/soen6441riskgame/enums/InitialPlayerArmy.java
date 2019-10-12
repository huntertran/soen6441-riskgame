package soen6441riskgame.enums;
// TODO: Auto-generated Javadoc

/**
 * This class sets the number of armies for every player in the game
 *
 *
 */
public class InitialPlayerArmy{	
	/**
	 * Gets the initial army count.
	 * @param playerCount the player count
	 * @return the initial army count
	 */
	public static int getInitialArmyCount(int playerCount) {
		switch (playerCount) {
		case 3:
			return 35;
		case 4:
			return 30;
		case 5:
			return 25;
		default:
			return 20;
		}
	}
}
