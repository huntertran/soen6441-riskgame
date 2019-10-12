package soen6441riskgame.models;
import soen6441riskgame.enums.Colors;
import java.util.ArrayList;

/**
 * 
 * 
 * This is a main class to read and store different elements of the country.
 *
 * @version 1.0.0
 */
public class Country {

	/** The country id. */
	private int countryId;

	/** The country name. */
	private String countryName;

	/** The continent ID. */
	private int continentID;

	/** The x coordinate. */
	private int xCoordinate;

	/** The y coordinate. */
	private int yCoordinate;

	/** The player id. */
	private int playerId;

	/** The army numbers. */
	private int armyNumbers;

	/** The country color. */
	private Colors countryColor;


	/**
	 * This method is used to get played id.
	 * @return playerId ID of the player
	 */
	public int getPlayerId() {
		return playerId;
	}


	/**
	 * This method is used to set played id.
	 * @param playerId ID of the player
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * This method is used to get color of country.
	 * @return countryColor, get color of country
	 */
	public Colors getCountryColor() {
		return countryColor;
	}

	/**
	 * This method is used to set color of country.
	 * @param countryColor the new country color
	 */
	public void setCountryColor(Colors countryColor) {
		this.countryColor = countryColor;
	}

	/** The neighbors string. */
	private ArrayList<String> neighboursString = new ArrayList<>();

	/** The neighbors of country. */
	private ArrayList<Country> neighboursOfCountry= new ArrayList<>();

	/**
	 * The Constructor is created to set the all parameters of the country Element.
	 * @param countryId ID of country
	 * @param countryName name of country
	 */
	public Country(int countryId, String countryName) {
		this.countryId = countryId;
		this.countryName = countryName;
	}
	/**
	 * The Constructor is created to set the all parameters of the country Element.
	 * @param countryId ID of country
	 * @param countryName name of country
	 * @param xCoordinate x coordinates  of country
	 * @param yCoordinate y coordinates  of country
	 */
	public Country (int countryId, String countryName, int xCoordinate, int yCoordinate){
		this.countryId = countryId;
		this.countryName = countryName;
		this.xCoordinate =xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	/**
	 * This method is used to get an integer value of the country which is set as Country ID.
	 * @return the country ID Integer
	 */
	public int getCountryId(){
		return countryId;
	}

	/**
	 * This method is used to set country id.
	 * @param countryId iD of country
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * This method is used to get the name of the Country.
	 * @return the country Name String
	 */
	public String getCountryName(){
		return countryName;
	}

	/**
	 * This method is used to set country name.
	 * @param countryName name of country
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * This method is used to get the X-coordinate of the corresponding Country.
	 * @return the integer value of x coordinate
	 */
	public int getxCoordinate(){
		return xCoordinate;
	}


	/**
	 * This method is used to set x coordinates.
	 * @param xCoordinate x coordinates  of country
	 */
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * This method is used to get the Y-coordinate of the corresponding Country.
	 * @return the integer value of y coordinate
	 */
	public int getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * This method is used to set y coordinates.
	 * @param yCoordinate y coordinates  of country
	 */
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * This method is used to get an integer value of the continent which is set as Country ID.
	 * @return the continent ID.
	 */
	public int getContinentID() {
		return continentID;
	}

	/**
	 * This method is used to sets an integer value of the continent which is set as Country ID.
	 * @param continentID the new continent ID
	 */
	public void setContinentID(int continentID) {
		this.continentID = continentID;
	}

	/**
	 * This method is used to get all the neighbours that a country has as an Array list.
	 * @return arrayList of the neighbouring country
	 */
	public ArrayList<Country> getNeighboursOfCountry() {
		return neighboursOfCountry;
	}

	/**
	 * From the country-neighbour HashMap adds the neighbours to the country individually.
	 * @param neighbour the neighbour
	 */
	public void addNeighboursToTheCountries(Country neighbour){
		neighboursOfCountry.add(neighbour);
	}

	/**
	 * This method is used to get the list of neighbour Strings.
	 * @return neighboursString
	 */
	public ArrayList<String> getNeighboursString() {
		return neighboursString;
	}

	/**
	 * This method is used to add name for neighbour string.
	 * @param newNeighbour the new neighbour
	 */
	public void addNeighborString(String newNeighbour) {
		if (!neighboursString.contains(newNeighbour)) {
			neighboursString.add(newNeighbour);
		}
	}
	

	/**
	 * This method is used to get number of armies.
	 * @return armyNumbers number of armies
	 */
	public int getnoOfArmies() {
		return armyNumbers;
	}
	

	/**
	 * This method is used to set number of armies.
	 * @param armies Number of armies
	 */
	public void setnoOfArmies(int armies) {
		 armyNumbers= armies;
	}

	/**
	 * This method is used to increment the count of army.
	 */
	public void increaseArmyCount() {
		armyNumbers++;
	}

	/**
	 * This method is used to increment the count of army and put the count in army numbers.
	 * @param count count of the army
	 */
	public void increaseArmyCount(int count) {
		armyNumbers += count;
	}

	/**
	 * This method is used to decrease the count of army.
	 */
	public void decreseArmyCount() {
		armyNumbers--;
	}

	/**
	 * This method is used to decrement the count of army and put the count in army numbers.
	 * @param count count of the army
	 */
	public void decreaseArmyCount(int count) {
		armyNumbers -= count;
	}

}
