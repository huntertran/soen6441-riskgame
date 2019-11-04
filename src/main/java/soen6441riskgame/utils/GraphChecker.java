package soen6441riskgame.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import soen6441riskgame.models.Country;

/** The Graph Checker. */
public class GraphChecker {
    public static boolean isCountriesConnected(ArrayList<Country> countries) {
        if (countries.size() < 1) {
            return false;
        }

        // using depth first search algorithm
        // start node = first node in list
        Stack<Country> stack = new Stack<>();
        ArrayList<Country> visited = new ArrayList<>();

        stack.add(countries.get(0));
        visited.add(countries.get(0));

        while (!stack.isEmpty()) {
            Country element = stack.pop();
            if (!visited.contains(element)) {
                visited.add(element);
            }

            List<Country> neighbors = element.getNeighbors();
            List<Country> scopedNeighbors = new ArrayList<Country>();

            // remove any countries that not in the list of countries in param
            for (Country neighbor : neighbors) {
                if (countries.contains(neighbor)) {
                    scopedNeighbors.add(neighbor);
                }
            }

            for (int i = 0; i < scopedNeighbors.size(); i++) {
                Country neighbor = scopedNeighbors.get(i);
                if (neighbor != null && !visited.contains(neighbor)) {
                    stack.add(neighbor);
                }
            }
        }

        // compare visited and countries list
        for (Country country : countries) {
            if (!visited.contains(country)) {
                return false;
            }
        }

        return true;
    }
}
