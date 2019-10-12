package soen6441riskgame.models;

import java.util.ArrayList;
import soen6441riskgame.enums.Colors;

/**
 * 
 * 
 * An object model for country with payers information and number of armies belongs to one country 
 * for creating a view.
 * 
 * 
 * @version 1.0.0
 *
 */

public class CountryViewModel {
	
	/** The country id. */
	private int countryId;
	
	/** The country name. */
	private String countryName;
	
	/** The x coordinate. */
	private int xCoordinate;
	
	/** The y coordinate. */
	private int yCoordinate;
	
	/** The number of armies. */
	private int numberOfArmies;
	
	/** The color of country. */
	private Colors colorOfCountry;
	
	/** The player ID. */
	private int playerID;
	
	/** The neighbours. */
	private ArrayList<String> neighbours = new ArrayList<>();

	/**
	 * This method is used to get an integer value of the country which is set as Country ID.
	 * @return the country ID Integer
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * This method is going to set the country id.
	 * @param countryId id of country
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * This method is used to get the name of the Country.
	 * @return the country Name String
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * This method will set the name of the country.
	 * @param countryName name of the country
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * This method will return the x coordinate of the view .
	 * @return xCoordinate as an integer value
	 */
	public int getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * this method will set xCoordinate as an integer value.
	 * @param xCoordinate x coordinates of country
	 */
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	
	/**
	 * This method will return yCoordinate as an integer value.
	 * @return yCoordinate as an integer value
	 */
	public int getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * This method is setting yCoordinate value.
	 * @param yCoordinate y coordinates of country
	 */
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * This method will return the numbers of armies that the country have right now.
	 * @return numberOfArmies as an integer value
	 */
	public int getNumberOfArmies() {
		return numberOfArmies;
	}

	/**
	 * This method is setting the numbers of armies.
	 * @param numberOfArmies , the numbers of armies
	 */
	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}

	/**
	 * This method will the return the country color according to the player color.
	 * @return colors , color of country
	 */
	public Colors getColorOfCountry() {
		return colorOfCountry;
	}

	/**
	 * This method is storing the color of the player who currently acquired the country.
	 * @param colorOfCountry color of country
	 */
	public void setColorOfCountry(Colors colorOfCountry) {
		this.colorOfCountry = colorOfCountry;
	}

	/**
	 * This function is returning the data of player id who is currently acquired the country.
	 * @return player id as integer value
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * This method is setting player id who is currently acquired the country.
	 * @param playerID ID of player
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * This function is returning the array list of the neighbours of the country.
	 * @return neighbours country neighbours
	 */
	public ArrayList<String> getNeighbours() {
		return neighbours;
	}

	/**
	 * The function is going to set the neighbours of the specific country.
	 * @param neighbours country neighbours
	 */
	public void setNeighbours(ArrayList<String> neighbours) {
		this.neighbours = neighbours;


	}

}
