package soen6441riskgame.models;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * This is a main class to read and store different elements of the continent. 
 * 
 * @version 1.0.0
 */
public class Continent {

	/** The continent ID. */
	private int continentID;
	
	/** The continent name. */
	private String continentName;
	
	/** The control value. */
	private int controlValue;
	
	/** The countries of the continent. */
	private ArrayList<Country> countriesOfTheContinent= new ArrayList<>();
	/**
	 * The Constructor is created to set the all parameters of the continent Element.
	 *
	 * @param continentID ID of continent
	 * @param continentName name of continent
	 * @param controlValue control value of continent
	 */
	public Continent(int continentID, String continentName, int controlValue){
		this.continentID = continentID;
		this.continentName = continentName;
		this.controlValue = controlValue;
	}

	/**
	 * This method is used to get an integer value of the continent which is set as Continent ID.
	 * @return continentID, ID of the continent.
	 */
	public int getContinentID() {
		return continentID;
	}

	/**
	 * This function sets the continent ID of the continent.	 
	 * @param continentID ,Id of the continent
	 */
	public void setContinentId(int continentID) {
		this.continentID = continentID;
	}
	
	/**
	 * This method is used to get the name of the Continent as a String.
	 * @return continentName, name of the continent.
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * This method is used to get the control Value of the Continent in the conquest Map.
	 * @return controlValue,  Control Value to the continent.
	 */
	public int getControlValue() {
		return controlValue;
	}

	/**
	 * This function sets the continent name.	
	 * @param continentName , Name of the continent
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * This function sets the control value of the continent object.	 
	 * @param controlValue the new control value
	 */
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**
	 * This method is used to takes all the countries as a parameter and list them in an array list under the Continent Name.
	 * @param country the country
	 */
	public void addCountriesToTheContinentList(Country country) {
		this.countriesOfTheContinent.add(country);

	}


	/**
	 * This function is used to return the country list.	 *
	 * @return ArrayList country object
	 */
	public ArrayList<Country> getCountryList() {
		return countriesOfTheContinent;
	}
}
