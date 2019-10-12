package soen6441riskgame.enums;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


// TODO: Auto-generated Javadoc
/**
 * This class is used to take the input from from the console.
 * 
 * @version 1.0.0
 */
public class PrintConsoleAndUserInput {

	/** The input. */
	static Scanner input = new Scanner(System.in);
	
	/** The os. */
	// Try with these directory path if code is not running
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	/** The map directory. */
	private static String  mapDir = "src/main/java/soen6441riskgame/mapFiles/";
	//private String mapDir = "./src/main/java/soen6441riskgamemapFiles/";
	//private static String mapDir = "./src/main/java/soen6441riskgamemapFiles/";


	/**
	 *  This method is used to print in console.
	 * @param s , to print in console
	 */
	public void consoleOut(String s){
		System.out.println(s);
	}

	/**
	 *  This method is used to show the error in console.
	 * @param error the error
	 */
	public void consoleErr(String error){
		System.err.println(error);
	}

	/**
	 * This method is used to get the string input.
	 * @return string nextLine
	 */
	public static String userStrInput(){
		return input.nextLine();
	}

	/**
	 * This method is used to get the integer input.
	 * @return Integer nextInt
	 */
	public static Integer userIntInput(){
		return input.nextInt();
	}

	/**
	 * This method is used to get path of map directory.
	 * @return String mapDir
	 */
	public static String getMapDir() {
		if(OS.indexOf("win") >= 0) {
			mapDir = ".\\src\\main\\java\\soen6441riskgame\\mapFiles\\";
		}
		return mapDir;
	}

	/**
	 * This method is used to print and handle Exceptions.
	 * @param exception the exception
	 */
	public static void printException(Exception exception) {
		System.out.println("Exception: " + exception.getMessage());
		System.out.println(exception.getCause());
		exception.printStackTrace();
	}


	/**
	 * Method to convert the enum color into color object of swings.
	 * @param selectedColor the selected color
	 * @return Color based on the objects
	 */
	public static Color getColor(Colors selectedColor) {
		if (selectedColor.equals(Colors.RED)) {
			return Color.RED;
		}
		if (selectedColor.equals(Colors.BLACK)) {
			return Color.BLACK;
		}
		if (selectedColor.equals(Colors.MAGENTA)) {
			return Color.MAGENTA;
		}
		if (selectedColor.equals(Colors.ORANGE)) {
			return Color.ORANGE;
		}
		if (selectedColor.equals(Colors.GREEN)) {
			return Color.GREEN;
		}

		if (selectedColor.equals(Colors.BLUE)) {
			return Color.BLUE;
		}
		return null;

	}

	/**
	 * List of maps in directory.
	 * @return the array list
	 */
	public ArrayList<String> listofMapsinDirectory(){
		ArrayList<String> mapFileList = new ArrayList<String>();
		File folder = new File(getMapDir());
		File[] listOfFiles = folder.listFiles();
		int i = 0, j = 1;
		for(File file : listOfFiles){
			if(file.isFile()){
				if (file.getName().toLowerCase().contains(".map")){
					mapFileList.add(listOfFiles[i].getName());
				}
			}
			i++;
		}
		consoleOut("\n"+ "The List of Maps is Given Below:-"+ "\n");
		for (String s : mapFileList) {
			consoleOut(j + "."+s);
			j++;
		}
		return mapFileList;
	}
}


