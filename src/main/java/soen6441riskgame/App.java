package main.java.soen6441riskgame;

import java.util.Scanner;

import main.java.soen6441riskgame.controllers.GameController;
import main.java.soen6441riskgame.controllers.MapController;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("SOEN 6441 - Risk Domination game");

        if (args.length == 0) {
            runFromBegining();
        } else {
            jumpToCommand(args);
        }

    }

    public static void jumpToCommand(String[] args) {

    }

    public static void runFromBegining() {
        System.out.println("1: Use map editor");
        System.out.println("2: Play the game");

        System.out.print("Please choose what you want to do: ");

        Scanner scanner = new Scanner(System.in);
        int chosenOption = scanner.nextInt();

        switch (chosenOption) {
            case 1: {
                MapController mapController = new MapController();
                mapController.start();
                break;
            }
            case 2: {
                GameController gameController = new GameController();
                gameController.start();
            }
        }

        scanner.close();
    }
}
