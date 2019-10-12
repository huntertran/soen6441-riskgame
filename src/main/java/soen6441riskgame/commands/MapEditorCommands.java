package soen6441riskgame.commands;

import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.views.MapView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * This Class is to read and Validate the created or existing Map file according to the requirement.
 *
 * @version 1.0.0
 */
public class MapEditorCommands {	
	/** The scanner. */
	Scanner scanner = new Scanner(System.in);


	/** The print. */
	//Making the objects of classes
	PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();
	
	/** The map view. */
	MapView mapView = new MapView();

	/** The continents list. */
	// Arraylist of countries, continents
	ArrayList<Continent> continentsList = new ArrayList<>();
	
	/** The visited list. */
	ArrayList<String> visitedList = new ArrayList<>();

	/** The visited list for continent. */
	//ContinentLevel disconnection Finding
	private ArrayList<String> visitedListForContinent = new ArrayList<>();
	
	/** The country list for one continent. */
	private ArrayList<Country> countryListForOneContinent = new ArrayList<>();


	/** The map name. */
	public String mapName;
	
	/** The map path. */
	public String mapPath;

	/**
	 * This method is used to import the existing file from the directory. It reads the map file and stores the
	 * corresponding values for countries and continents.
	 * @param mapPath the map path
	 * @return map
	 */
	
	public MapEditorCommands readMapFile(String mapPath) {
		MapEditorCommands map = null;
		if (mapPath.isEmpty())
			return map;
		map = new MapEditorCommands();
		try {
			boolean getContinents = false;
			boolean getTerritories = false;
			int continentID= 0;
			int territoryID = 0;

			HashMap<String, Country> countryNameMapCountryObj= new HashMap<String, Country>();
			HashMap<Country, String[]> neighboursOfCountry = new HashMap<Country, String[]>();

			BufferedReader readFileFromDir = new BufferedReader(new FileReader(mapPath));
			String lineStream;
			while((lineStream = readFileFromDir.readLine()) != null){
				if (lineStream.trim().length() == 0)
					continue;
				else if (lineStream.contains("[Continents]")) {
					getContinents = true;
					continue;
				} else if (lineStream.contains("[Territories]")){
					getContinents = false;
					getTerritories = true;
					continue;
				}

				// Get continents and store them in continentList
				if (getContinents){
					String[] continentElements= lineStream.split("=");
					Continent continent = new Continent (continentID++, continentElements[0],
							Integer.parseInt(continentElements[1]));
					continentsList.add(continent);
				}


				// Get Territories form the stream of sentences and store them country object with all of three values
				else if (getTerritories){
					String[] territoryElements = lineStream.split(",");
					String countryName = territoryElements[0];
					int xCoordinate = Integer.parseInt(territoryElements[1]);
					int yCoordinate = Integer.parseInt(territoryElements[2]);
					String belongsToContinent = territoryElements[3];

					Country country = new Country(territoryID++, countryName, xCoordinate, yCoordinate);
					countryNameMapCountryObj.put(countryName, country);

					String[] neighboursFromArray = Arrays.copyOfRange(territoryElements, 4, territoryElements.length);
					neighboursOfCountry.put(country, neighboursFromArray); // all the neighbours are put as an array

					//add the neighboring Countries only by one as String values in the country Object
					int k = 0;
					for (String neighbourCountry : neighboursFromArray) {
						country.addNeighborString(neighbourCountry);
						k++;
					}

					//To list the countries depending on their continent name
					//Eg: NorthAmerica: Alaska, Canada etc
					for (int i = 0; i < continentsList.size(); i++) {
						if (continentsList.get(i).getContinentName().equals(belongsToContinent)) {
							continentsList.get(i).addCountriesToTheContinentList(country);
							country.setContinentID(continentsList.get(i).getContinentID());
							break;
						}
					}
				}
			}


			//In the array the neighbor and the country is NOT interconnected
			//here, we are getting the country name as Country object as key, and each of the neighbors
			//Individually as string array. thus the country object is paired with neighboring countries array
			Country neighbours;
			for (HashMap.Entry<Country, String[]> countryNeighbourPair : neighboursOfCountry.entrySet()) {
				Country countryOfPair = countryNeighbourPair.getKey();
				String[] neighbourOfPair = countryNeighbourPair.getValue();
				for (int i = 0; i < neighbourOfPair.length; i++) {
					neighbours = countryNameMapCountryObj.get(neighbourOfPair[i]);
					countryOfPair.addNeighboursToTheCountries(neighbours);
				}
			}
			readFileFromDir.close();
		} catch(IOException exception){
			System.out.println(exception);
		}
		return map;

	}

	/**
	 * This method is used to read the content of map file while creating a map from scratch and saved in the directory.
	 *
	 * @param mapContent the map content
	 * @param nameOfMap the name of map
	 * @return true, if successful
	 */
	private boolean readMapContentSaveInDirectory(StringBuffer mapContent, String nameOfMap) {
		String mapDir = getMapDir();
		Path path = Paths.get( nameOfMap + ".map");
		BufferedWriter bw = null;
		try {
			// Delete temporary file from the directory
			Path tempFilePath = Paths.get(mapDir+"temp" + ".map");
			Files.deleteIfExists(tempFilePath);

			bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
			bw.write(new String(mapContent));
			bw.close();
			return true;
		} catch (Exception e) {
			print.printException(e);
			return false;
		}
	}

	/**
	 * This method is used to create and validate map.
	 *
	 * @param mapContent the map content
	 * @param mapName the map name
	 * @return true if the map is valid and false if it is not valid
	 */
	public boolean createValidateAndSaveMap(StringBuffer mapContent, String mapName) {
		String mapDir = getMapDir();
		if (readMapContentSaveInDirectory(mapContent, mapDir+"temp")) {
			try {
				readMapFile(mapDir+"temp.map");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (checkMapIsValid()) {
				readMapContentSaveInDirectory(mapContent, mapDir+ mapName);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * This method is used to check if the map is valid or not
	 * checks connectivity, the coordinates, one country does not belong to two Continents, there is at least one
	 * country in each Continent.
	 *
	 * @return false
	 */
	public boolean checkMapIsValid() {
		try {
			boolean oneCountryNotInDiffContinent = true ;
			boolean atLeastOneCountryInOneContinent = true;
			boolean aCountryIsNotOwnNeighbor = true;
			boolean aCountryNotLeftUndefined = true;

			ArrayList<String> growingCountryList = new ArrayList<>();
			ArrayList<String> countriesForSorting = countryListString(getCountryList());
			ArrayList<String> neighbourString = new ArrayList<>();


			for(Continent continent : this.continentsList){
				if(continent.getCountryList().isEmpty()){
					atLeastOneCountryInOneContinent = false;
					print.consoleErr("\n Each continent should have at least one country.\n **"+
							continent.getContinentName() + "** is not assigned with any country.\n");
				}
				for(Country country : continent.getCountryList()){
					if(growingCountryList.contains(country.getCountryName())){
						oneCountryNotInDiffContinent = false;

						print.consoleErr("\nA Country cannot belong to different continents.\n **" + country.getCountryName() +
								"** is assigned in multiple Continents.\n");
					}else {
						growingCountryList.add(country.getCountryName());
					}
				}
			}


			for (Country countriesInformation : getCountryList()) {
				String countryName = countriesInformation.getCountryName();
				for (int i = 0; i < countriesInformation.getNeighboursString().size(); i++) {
					String sameAsNeighbor = countriesInformation.getNeighboursString().get(i);
					if (!(neighbourString.contains(sameAsNeighbor))){
						neighbourString.add(sameAsNeighbor); //get the neighbor List to determine if Any country left Unassigned
					}
					if(countryName.equals(sameAsNeighbor)) {
						print.consoleErr("\nCountry *** " + sameAsNeighbor + " *** Can not be a neighbor of its own\n");
						aCountryIsNotOwnNeighbor = false;
					}

				}
			}

			//If the visitedList is same as the allCountryList then is is conclusive that the Map is connected
			//otherwise the two lists would never be the same because visitedList adds elements only if can visit in DFS
			Collections.sort(countriesForSorting);

			Collections.sort(neighbourString);
			if(!(neighbourString.equals(countriesForSorting))){
				for(int i = 0; i<neighbourString.size(); i++){
					if(!countriesForSorting.contains(neighbourString.get(i))){
						print.consoleErr("\n Country Name *** " +neighbourString.get(i) + " *** is Not Defined\n");
					}
				}
				aCountryNotLeftUndefined = false;
			}


			// Set the very first Country from the first Continent of the arrayList and set them as an starting
			// For checking if a Map is a connected one.
			Country startingVertex = ((this.continentsList.get(0)).getCountryList()).get(0);
			visitedList.clear();

			depthFirstSearch(startingVertex);
			Collections.sort(visitedList);

			if(!atLeastOneCountryInOneContinent || !oneCountryNotInDiffContinent ||
					!aCountryIsNotOwnNeighbor || !aCountryNotLeftUndefined ) {
				return false;
			}

			if (visitedAndAllCountryListCheck(visitedList, countriesForSorting)) {
				if (continentSubGraphConnected()) {
					return true;
				} else {
					print.consoleErr("\n*** The Continent subGraph is not Connected. ***\n");
					return false;
				}
			} else {
				print.consoleErr("\n *** This Map is NOT Connected. ***\n");
				return false;
			}

		}catch (Exception e){
			print.printException(e);
			return false;
		}
	}

    /**
 * this function is to check if the graph is connected the subGraph(Continents) are connected or not
 * returns true individual continents are a connected subGraph.
 *
 * @return true individual continents are a connected subGraph
 */
    public boolean continentSubGraphConnected(){
		ArrayList<String> countryListForThisContinent = new ArrayList<>();
		for (Continent forEachContinent: this.continentsList) {
			countryListForThisContinent.clear();
			countryListForOneContinent.clear();
			for (Country country : forEachContinent.getCountryList()) {
				countryListForThisContinent.add(country.getCountryName());
				countryListForOneContinent.add(country);
			}
			Collections.sort(countryListForThisContinent);

			Country startingVertex = forEachContinent.getCountryList().get(0);
			visitedListForContinent.clear();
			depthFirstSearchForContinent(startingVertex);
			Collections.sort(visitedListForContinent);

			if(!visitedAndAllCountryListCheck(visitedListForContinent,countryListForThisContinent)){
			    return false;
            }//return false if the visitedList and the countryList for the continent are not same
		}
		return true;
	}

	/**
	 * This method is used to print that map is valid or not.
	 */
	public void printMapValidOrNot(){
		if(checkMapIsValid()){
			print.consoleOut("\n  This Map is VALID \n ");
		}	else {
			print.consoleErr("\n  Failed, Map is NOT VALID, Try Again!!! \n");
		}
	}

	/**
	 * This is a recursive function which implements DFS algorithm to visit ALL the vertices(countries) one
	 * by one starting from the first one = this is only possible if all the vertices are inter-connected
	 * store all the visited vertices(countries) in an arrayList 'visitedList'.
	 *
	 * @param startingVertex check from the starting point
	 */
	public void depthFirstSearch(Country startingVertex){
		visitedList.add(startingVertex.getCountryName());
		for (String neighbourVertex: startingVertex.getNeighboursString()) {
			if(!(visitedList.contains(neighbourVertex))){	//if the vertex is not visited then visit it
				Country newVertex = null;
				for (Continent continent : this.continentsList) {
					//					System.out.println(continent.getCountryList().size());
					for (Country country : continent.getCountryList()) {
						if (country.getCountryName().equals(neighbourVertex)) {
							newVertex = country;
						}
					}
				}
				if (newVertex != null) {
					depthFirstSearch(newVertex);
				}
			}
		}
	}

    /**
     * This is a recursive function which implements DFS algorithm to visit ALL the vertices(countries) one
     * by one starting from the first one in One continent.
     * store all the visited vertices(countries) in an arrayList 'visitedListForContinent'
     * @param startingVertex check from the starting point
     */
	public void depthFirstSearchForContinent(Country startingVertex){
		visitedListForContinent.add(startingVertex.getCountryName());
		for (String neighbourVertex: startingVertex.getNeighboursString()) {
			Country newVertex = null;
			if(!(visitedListForContinent.contains(neighbourVertex))){	//if the vertex is not visited then visit it
				for (Country country : countryListForOneContinent) {
					if (country.getCountryName().equals(neighbourVertex)) {
						newVertex = country;
					}
				}
			}
			if (newVertex != null) {
				depthFirstSearchForContinent(newVertex);
			}
		}
	}



	/**
	 * checking the visitedList and allCountryList if they are same. If returns True, that proves DFS traversal
	 * has visited all the nodes, thus connected. If returns False, then the graph is not connected. so the visitedList
	 * and allCountryList are not same.
	 * @param visitedList visited list
	 * @param allCountryList list of all countries
	 * @return true if visited otherwise false
	 */
	public boolean visitedAndAllCountryListCheck(ArrayList<String> visitedList, ArrayList<String> allCountryList){
		if (visitedList == null && allCountryList == null)
			return true;
		if ((visitedList == null && allCountryList != null) || (visitedList != null && allCountryList == null))
			return false;

		if (visitedList.size() != allCountryList.size())
			return false;
		for (String visitedListElements : visitedList) {
			if (!allCountryList.contains(visitedListElements))
				return false;
		}
		return true;
	}


	/**
	 * This function is used to return the list of countries.
	 *
	 * @return countriesList , list of countries
	 */
	public ArrayList<Country> getCountryList() {
		ArrayList<Country> countriesList = new ArrayList<>();
		for (Iterator<Continent> continents = continentsList.iterator(); continents.hasNext();) {
			countriesList.addAll(continents.next().getCountryList());
		}
		return countriesList;
	}

	/**
	 * Get country object from country name.
	 *
	 * @param countryName the country name
	 * @return Country
	 */
	public Country getCountryFromName(String countryName) {
		Country country = getCountryList().stream().filter(x -> x.getCountryName().equals(countryName)).findAny()
				.orElse(null);

		return country;
	}
	
	/**
	 * This function is used to return the list of country names as ArrayList string type.
	 *
	 * @param countriesList list of countries
	 * @return countriesListString , list of countries
	 */
	public ArrayList<String> countryListString(ArrayList<Country> countriesList) {
		//		ArrayList<String> countriesListString = new ArrayList<>(countriesList.size());
		ArrayList<String> countriesListString = new ArrayList<>();
		for(Country countryForAdding : countriesList){
			//			countriesListString.add(Objects.toString(countryForAdding.getCountryName(), null));
			countriesListString.add(countryForAdding.getCountryName());
		}
		return countriesListString;
	}

	//                starting here the add/edit/delete map functions                //

	/**
	 * This method is used to add the continent to the continent list.
	 *
	 * @param continent the continent
	 */
	public void addContinent(Continent continent) {
		continentsList.add(continent);
	}

	/**
	 * This method is used to add new continent into the map when editing map operation is performed.
	 */
	public void addContinentNameToMapFile() {
		// Asks the number of CONTINENTS a user wants to add.
		print.consoleOut("How many continents you want to add in a Map?");
		int numberOfContinentsToAdd = print.userIntInput();


		// It increments the number of continents a user wants to add.
		// Plus it asks to enter the continent names for each number a user has selected.
		for (int k = 0; k < numberOfContinentsToAdd; k++) {
			int incrementOfContinentNumber = k+1;
			print.consoleOut("Input the CONTINENT NAME for number = (" +incrementOfContinentNumber+ ") continent and its caontrol value.");
			String continentNameByUser = scanner.nextLine();
			int controlValueByUser = print.userIntInput();

			// add to the continent list
			Continent addedcontinent = new Continent(k, continentNameByUser, controlValueByUser);

			// Asks the number of COUNTRIES a user wants to add in a given continent. And eneter the x and y coordinates
			print.consoleOut("How many countries do you want to create in ("+continentNameByUser+") continent:\n");
			int numberOfCountriesByUser = print.userIntInput();
			for (int i = 0; i < numberOfCountriesByUser; i++) {

				int numberOfCountriesCounter = (i + 1);
				// Enter country name
				print.consoleOut("Input the country name for country number " + numberOfCountriesCounter);
				String countryNameByUser = scanner.nextLine();

				// Enter x coordinates of country name
				print.consoleOut("Input x coordinates for");
				int xCoordinateOfCountry = print.userIntInput();

				// Enter y coordinates of country name
				print.consoleOut("Input y coordinates for");
				int yCoordinateOfCountry = print.userIntInput();

				// put in a continent and set continent id
				Country country = new Country(i, countryNameByUser);
				country.setxCoordinate(xCoordinateOfCountry);
				country.setyCoordinate(yCoordinateOfCountry);
				country.setContinentID(i);

				// Enter the number of neighbor countries
				print.consoleOut("\nInput  the number of neighbor countries you want to add:\n");
				int numberOfNeighbourCountriesByUser = print.userIntInput();

				for (int j = 0; j < numberOfNeighbourCountriesByUser; j++) {
					int neighborCountriesCounter = (j + 1);
					// Enter the names of neighbor countries
					print.consoleOut("Input the country name for neighbor country number " + neighborCountriesCounter + "\n");
					String neighborCountryName = scanner.nextLine();

					// put the neighbor countries into the country
					country.addNeighborString(neighborCountryName);

					// get the country list as new list by ignoring the camel case letter.
					// Then add the neighbor countries
					for (Country newList: getCountryList()) {
						if (newList.getCountryName().equalsIgnoreCase(neighborCountryName)){
							newList.addNeighborString(countryNameByUser);
						}
					}
				}

				// It add a COUNTRY to the continent
				addedcontinent.addCountriesToTheContinentList(country);
			}

			//It adds the continent which is entered by user
			addContinent(addedcontinent);
		}
	}

	/**
	 * This method is used to add country name to the continent.
	 *
	 * @param continentName name of the continent
	 * @param continentID ID of the continent
	 * @return true if country is added
	 */
	public boolean addCountryToContinentInMap(String continentName, int continentID) {
		Continent listOfCurrentContinents  = continentsList.stream()
				.filter(x-> x.getContinentName().equalsIgnoreCase(continentName))
				.findAny()
				.orElse(null);

		if(listOfCurrentContinents == null){
			print.consoleErr("Error!!! Country name does not Exist.");
			return false;
		}

		print.consoleOut("Input the number of Countries you want to create in this continent:\n");
		int numberOfCountriesToAddInContinent = scanner.nextInt();
		Scanner userinput = new Scanner(System.in);
		for (int i = 0; i < numberOfCountriesToAddInContinent; i++) {
			//	int incrementCountryCounter = (i + 1);
			print.consoleOut("Input the Country Name for country no: => "+(i + 1));
			String countryName = userinput.nextLine();


			print.consoleOut("Input x coordinate:");
			int xCoordinate = scanner.nextInt();

			print.consoleOut("Input y coordinate:");
			int yCoordinate = scanner.nextInt();

			Country country = new Country(i, countryName, xCoordinate, yCoordinate);
			country.setContinentID(continentID);

			print.consoleOut("Input the number of neighbor countries you want to create?\n");
			int neighborCountries = scanner.nextInt();

			for (int k = 0; k < neighborCountries; k++) {
				int incrementCounterForNeighborCountries =  (k + 1);
				print.consoleOut("Input the country name for adjacency country number: " + incrementCounterForNeighborCountries);
				String neighbourName = userinput.nextLine();
				//System.out.println(neighbourName+"-----");
				country.addNeighborString(neighbourName);
				for (Country countryList: getCountryList()) {
					if (countryList.getCountryName().equalsIgnoreCase(neighbourName)){
						countryList.addNeighborString(countryName);
					}
				}
			}
			listOfCurrentContinents.addCountriesToTheContinentList(country);
		}
		return true;
	}

	/**
	 * Delete continent from map.
	 *
	 * 
	 * @version 1.0.0
	 * This method is used to delete the continent.
	 * @param deleteContinentEnteredByUser the delete continent entered by user
	 * @return true
	 */
	public boolean deleteContinentFromMap(String deleteContinentEnteredByUser) {
		Continent elementInCurrentContinents = continentsList.stream()
				.filter(x-> x.getContinentName().equalsIgnoreCase(deleteContinentEnteredByUser))
				.findAny().orElse(null);

		if(elementInCurrentContinents==null){
			print.consoleErr("Error!!! Continent " +deleteContinentEnteredByUser+ " does not exist");
			return false;
		}

		ArrayList<Country> countriesListOfCurrentContinent = elementInCurrentContinents.getCountryList();
		for ( Continent currentContinentList: continentsList){
			for (Country countryListToRemoveContinent : currentContinentList.getCountryList()) {
				for (int i = 0; i < countryListToRemoveContinent.getNeighboursString().size() ; i++) {
					String countryNameToDelete = countryListToRemoveContinent.getNeighboursString().get(i);

					Country deleteElement = countriesListOfCurrentContinent.stream()
							.filter(x -> x.getCountryName().equalsIgnoreCase(countryNameToDelete))
							.findAny().orElse(null);
					if (deleteElement!=null){
						countryListToRemoveContinent.getNeighboursString().remove(i);
					}
				}
			}
		}
		continentsList.remove(elementInCurrentContinents);
		return true;
	}


	/**
	 * This method is used to delete country from map file.
	 *
	 * @param deleteCountryByUser the delete country by user
	 * @return true if the country is deleted
	 */
	public boolean deleteCountryFromMap(String deleteCountryByUser) {
		ArrayList<Country> countriesList = getCountryList();
		Country elementInCurrentCountry = countriesList.stream()
				.filter(x-> x.getCountryName().equalsIgnoreCase(deleteCountryByUser))
				.findAny().orElse(null);

		if(elementInCurrentCountry == null){
			print.consoleErr("Error!!! Country " +deleteCountryByUser+ " does not exist");
			return false;
		}

		for (Country countryListToRemoveCountry : countriesList) {
			print.consoleOut(countryListToRemoveCountry.getCountryName());
			for (int i = 0; i < countryListToRemoveCountry.getNeighboursString().size() ; i++) {
				if (countryListToRemoveCountry.getNeighboursString().get(i).equalsIgnoreCase(deleteCountryByUser)){
					countryListToRemoveCountry.getNeighboursString().remove(i);
				}
			}
		}
		for (Continent continent : continentsList) {
			continent.getCountryList().remove(elementInCurrentCountry);
		}
		return true;
	}



	/**
	 * This method is used to save the user map into mapFiles folder.
	 *
	 * @param mapContent the map content
	 * @param mapName the map name
	 * @return true if file is created
	 */
	public boolean saveUserMapIntoDirectory(StringBuffer mapContent, String mapName) {
		BufferedWriter bw = null;
		try {

			File filePath = new File(print.getMapDir() + mapName + ".map");
			if (!filePath.exists()) {
				filePath.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(filePath);
			bw = new BufferedWriter(fileWriter);
			bw.write(System.getProperty( "line.separator" ));
			bw.write(new String(mapContent));
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		finally{
			try{
				if(bw!=null)
					bw.close();
			}catch(Exception ex){
				print.consoleErr("Error in closing the BufferedWriter"+ex);
			}
		}
	}

	/**
	 * This method is used to take the user input of map file with full directory path.
	 * @return mapPath
	 */
	public String getMapNameByUserInput() {
		String mapDirectory = print.getMapDir();
		String mapNameByUserInput = scanner.nextLine().trim();
		String mapPathWithMapName = mapDirectory + mapNameByUserInput;
		String mapPath = mapPathWithMapName+".map";
		mapName = mapNameByUserInput;
		return mapPath;
	}

	/**
	 * This method is used to take the user input of map name.
	 *
	 * @return mapNameByUserInput, map name entered by the user
	 */
	public String getMapNameFromUser() {
		String mapNameByUserInput = scanner.nextLine().trim();
		return mapNameByUserInput;
	}

	/**
	 * This method is used to take the user input of map name.
	 *
	 * @return mapName, map name entered by the user
	 */
	public String getMapName() {
		return mapName;
	}
	
	/**
	 * Gets The ContinentList form the map file.
	 *
	 * @return the list of all map file
	 */
	public ArrayList<Continent> getContinentList() {
		return continentsList;
	}

	/**
	 * This method is used to save the map into the directory when a user adds, delete
	 * countries or continents from the map.
	 * @param mapNameByUserInput  name of map by user
	 * @param mapPath directory of map files
	 */
	public void saveEditedMap(String mapNameByUserInput, String mapPath) {
		StringBuffer textContentInFile = new StringBuffer();

		print.consoleOut("Updated data is saved in the map file like this");

		// Add the [Continents] parameter in the text file
		textContentInFile.append("[Continents]\r\n");

		// Appending continent name with its control value
		for (Continent continentInformation : continentsList) {
			String continentName = continentInformation.getContinentName();
			int controlValue = continentInformation.getControlValue();
			textContentInFile.append(continentName+"="+ controlValue + "\r\n");
		}

		// Appending Country name and its coordinates with its neighbor countries
		textContentInFile.append("\n[Territories]\r\n");

		for (Continent continentInformation : continentsList) {
			for (Country countriesInformation : continentInformation.getCountryList()) {

				// fetching and putting the data into variables
				String countryName = countriesInformation.getCountryName();
				int xCoordinates = countriesInformation.getxCoordinate();
				int yCoordinates = countriesInformation.getyCoordinate();
				String continentName = continentInformation.getContinentName();

				// Append territories parameters
				textContentInFile.append(countryName + "," + xCoordinates+ "," + yCoordinates + "," + continentName);
				for (String neighborCountries : countriesInformation.getNeighboursString()) {
					textContentInFile.append("," + neighborCountries);
				}
				textContentInFile.append("\n");
			}
		}
		System.out.println(textContentInFile);

		// Getting the path of directory to save the created map file in directory
		Path path = Paths.get(print.getMapDir() + mapNameByUserInput+".map" );
		BufferedWriter bw = null;
		try {
			// first it deletes the temporary file and then write content into file.
			Path tempFilePath = Paths.get(mapPath + "temp" + ".map");
			Files.deleteIfExists(tempFilePath);
			bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
			bw.write(new String(textContentInFile));
			bw.close();
		} catch (Exception e) {
			print.printException(e);
		}
	}


	/**
	 * This method is printing continents in the list.
	 */
	public void printingContinents() {
		print.consoleOut("List of Continents\n");
		int i=1;
		for (Continent continentInformation : continentsList) {
			int continentID = continentInformation.getContinentID();
			String continentName = continentInformation.getContinentName();
			int controlValue = continentInformation.getControlValue();

			print.consoleOut(i+"."+continentName+"="+ controlValue+"\n");
			i++;
		}
	}

	/**
	 * This method is printing territories and its neighbors.
	 */
	public void printingTerritoriesAndNeighborCountries() {
		// Printing the territories
		print.consoleOut("[Territories]");
		for (Continent continentInformation : continentsList) {
			for (Country countriesInformation : continentInformation.getCountryList()) {
				// fetching and putting the data into variables
				String countryName = countriesInformation.getCountryName();
				int xCoordinates = countriesInformation.getxCoordinate();
				int yCoordinates = countriesInformation.getyCoordinate();
				String continentName = continentInformation.getContinentName();

				// Append territories parameters
				print.consoleOut(countryName + "," + xCoordinates+ "," + yCoordinates + "," + continentName);
			}
		}
	}


	/**
	 * This method prints the Neighboring country names when user puts the name of the country.
	 */
	public void printNeighboursGivenContry() {
		print.consoleOut("\n Write down the Country Name for the Neighbour: ");
		String countryNameForNeighbours = scanner.nextLine().trim();
		//		for (Continent continentInformation : continentsList) {
		for (Country countriesInformation : getCountryList()) {
			String countryName = countriesInformation.getCountryName().toLowerCase();
			if (countryNameForNeighbours.equals(countryName)) {
				print.consoleOut("List of the Neighbours for the Country: '" + countryName + "' is given below");
				for (int i = 0; i < countriesInformation.getNeighboursString().size(); i++) {
					print.consoleOut("Neighbours List ->" +
							countriesInformation.getNeighboursString().get(i));
				}
			}
		}
		//		}
	}

	/**
	 * This method is used to print Countries of the map file.
	 */
	public void printCountriesFromMap() {
		ArrayList<Country> countryList = getCountryList();
		int i =1;
		print.consoleOut("LIST OF COUNTRIES\n");
		for (Country nameOfCountry: countryList ) {
			print.consoleOut(i+"." +nameOfCountry.getCountryName());
			i++;
		}
	}

	/**
	 * This method is used to get the directory of map.
	 * @return path of the map directory
	 */
	public String getMapDir() {
		return print.getMapDir();
	}


	/**
	 * This function sets the map name.
	 *
	 * @param mapName the new map name
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}


	/**
	 * This function sets the map directory path.
	 * @param mapPath path of map directory
	 */
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
}


