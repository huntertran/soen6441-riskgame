package soen6441riskgame.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.models.Continent;
import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.views.MapView;

// TODO: Auto-generated Javadoc
/**
 * This class is used to handle the operations to generate, edit the map.
 * 
 * @version 1.0.0
 */

public class MapController {

	/** The scanner. */
	Scanner scanner = new Scanner(System.in);

	/** The print. */
	PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();

	/** The map view. */
	MapView mapView = new MapView();

	/** The mapEditor Commands. */
	MapEditorCommands mapModel ;

	/** The main controller. */
	MainController maincontroller = new MainController();

	/** The continents list. */
	// It stores all the continents of map File in this list
	ArrayList<Continent> continentsList = new ArrayList<>();

	/**
	 *
	 * This method is used to select the map options(like import, design a new map, save a map).	 *
	 * @return userinput
	 */
	public boolean generateMap() {
		int selectMapMenuOption = 0;
		while (selectMapMenuOption != 3){
			selectMapMenuOption = mapView.displayMapMenu();
			switch (selectMapMenuOption) {

			case 1: // Importing map file
				print.listofMapsinDirectory();

				// Check if the entered map file name is exists in directory or not
				checkMapFileExistsOrNot();
				break;

			case 2:	// Create and save user Map
				createAndSaveUserMap();
				break;

			case 3: // Edit map
				editExistingMapFile();
				break;

			case 4: // Back to main menu
				maincontroller.displaymainMenu();
				break;
			}

			while (selectMapMenuOption < 1 || selectMapMenuOption > 6) {
				print.consoleErr("Error!!! Enter a valid choice (1-6).");
				selectMapMenuOption = mapView.displayMapMenu();
			}
		}
		return false;
	}

	/**
	 * This method is used to create the user map and save it in directory.
	 *
	 */
	public void createAndSaveUserMap() {
		mapModel = new MapEditorCommands();//refreshing
		mapView.createJframe();
		mapView.saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StringBuffer mapContent = new StringBuffer(mapView.returnMapContent());
				String mapName = mapView.returnMapName();
				boolean checkMapIsCreated;
				checkMapIsCreated = mapModel.createValidateAndSaveMap(mapContent, mapName);

				if (checkMapIsCreated) {
					print.consoleOut("The Map has been created successfully in "+print.getMapDir()+ " as " +mapName+".map");
				} else {
					print.consoleErr(" Error!!!! Map has not been created successfully! ");
				}
				mapView.closeFrameWindow();
			}
		});
	}

	/**
	 * This method is used to check if the entered map file name exists in directory or not.
	 */
	public void checkMapFileExistsOrNot() {
		mapModel = new MapEditorCommands(); 
		String mapPath = mapModel.getMapNameByUserInput();
		File tempFile = new File(mapPath);
		boolean exists = tempFile.exists();
		if (exists) {
			mapModel.readMapFile(mapPath);
			mapModel.printMapValidOrNot();
		} else {
			print.consoleErr("File not found!!!. Please enter the coreect name of map.");
		}
	}


	/**
	 * This method is used to edit the map.
	 *
	 * @version 1.0.0
	 */
	public void editExistingMapFile() {
		
		// Printing all the map files
		print.listofMapsinDirectory();

		// Select map name by user and check file exists or not
		print.consoleOut("Please enter the map name you want to edit from the list?");

		String mapDirectory = print.getMapDir();
		String mapNameByUserInput = scanner.nextLine().trim();
		String mapPathWithMapName = mapDirectory + mapNameByUserInput;
		String mapPath = mapPathWithMapName+".map";		

		File tempFile = new File(mapPath);
		boolean exists = tempFile.exists();
		mapModel = new MapEditorCommands(); //refresh
		if (exists) {
			mapModel.readMapFile(mapPath);
			mapModel.printMapValidOrNot();
			if (!mapModel.checkMapIsValid()){
				print.consoleErr(mapNameByUserInput+ ".map  is not valid");
			}else {
				print.consoleOut(mapNameByUserInput+ ".map  is valid");

				int inputForEditMap = -1;
				while (inputForEditMap != 5) {

					inputForEditMap = mapView.editMapMenu();

					switch (inputForEditMap) {
					case 1:  // 1. Add Continent to the map?
						mapModel.printingContinents();
						mapModel.addContinentNameToMapFile();

						if(mapModel.checkMapIsValid()){
							mapModel.saveEditedMap(mapNameByUserInput,mapPath);
							print.consoleOut("*Continent has been added successfully!*");
						}else{
							print.consoleErr("Invalid Map! Try again!!!");
						}
						break;

					case 2:  // 2. Add Country to the map?
						int continentID = 0;
						mapModel.printingContinents();
						print.consoleOut("Enter the Continent Name from the list in which you want to add new country?");
						String continentName = scanner.nextLine();
						mapModel.addCountryToContinentInMap(continentName,continentID);

						if(mapModel.checkMapIsValid()){
							mapModel.saveEditedMap(mapNameByUserInput,mapPath);
							print.consoleOut("Country has been added successfully!");
						}else{
							print.consoleErr("Invalid Map! Try again!!!");
						}
						break;

					case 3:  // 3. Delete Continent from map?

						mapModel.printingContinents();
						print.consoleOut("Deleting Continent from map");
						print.consoleOut("Enter name of the Continent you want to delete:");
						String deleteContinentEnteredByUser = scanner.nextLine();

						boolean checkContinentIsDeleted = mapModel.deleteContinentFromMap(deleteContinentEnteredByUser);

						if(checkContinentIsDeleted){				
							if (mapModel.checkMapIsValid()){
								//	mapModel.saveEditedMap();
								mapModel.saveEditedMap(mapNameByUserInput,mapPath);
								print.consoleOut("Continent "+deleteContinentEnteredByUser+" has been deleted successfuly!");
							}
							else{
								print.consoleErr("Map is invalid!");
							}					
						}
						else {
							print.consoleErr("Error!!! Continent can not be deleted");
						}
						break;

					case 4:  // 4. Delete Country from map?

						mapModel.printCountriesFromMap();
						print.consoleOut("Enter name of the Country you want to delete:");
						String deleteCountryNameByUser = scanner.nextLine();

						boolean checkCountryIsDeleted =  mapModel.deleteCountryFromMap(deleteCountryNameByUser);


						if(checkCountryIsDeleted){				
							if (mapModel.checkMapIsValid()){
								mapModel.saveEditedMap(mapNameByUserInput,mapPath);
								print.consoleOut(deleteCountryNameByUser+" has been deleted successfuly!");
							}
							else{
								print.consoleErr("Map is invalid!");
							}				
						}
						else {
							print.consoleErr("Error!!! The Country can not be deleted");
						}
						break;

					case 5:

						break;

					default:
						print.consoleErr("Option not Available. Select Again!!!");
						break;
					}
				}
			}
		} else {
			print.consoleErr("File not found!!!. Please enter the correct map name.");

		}
	}

	/**
	 * This method is used to get The ContinentList form the map file.
	 *
	 * @return the list of all map file
	 */
	public ArrayList<Continent> getContinentList() {
		return continentsList;
	}

}

