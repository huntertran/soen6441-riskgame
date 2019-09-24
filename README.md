SOEN 6441 - RISK GAME GROUP PROJECT
---

<!-- TOC -->

- [1. Members](#1-members)
- [2. General description](#2-general-description)
- [3. Problem statement](#3-problem-statement)
    - [3.1. Map](#31-map)
    - [3.2. Game](#32-game)
        - [3.2.1. Reinformcements Phase](#321-reinformcements-phase)
        - [3.2.2. Attack Phase](#322-attack-phase)
        - [3.2.3. Fortification Phase](#323-fortification-phase)
    - [3.3. Cards](#33-cards)
- [4. Implementations](#4-implementations)
    - [4.1. Design Pattern](#41-design-pattern)
    - [4.2. JDK version and tools](#42-jdk-version-and-tools)

<!-- /TOC -->

# 1. Members
<a id="markdown-members" name="members"></a>
- 40124288 - Van Tuan Tran
- 40080998 - Benjamin Osei Asante
- 40114377 - Tejinder Singh
- 40089008 - Bharti Saini

# 2. General description
<a id="markdown-general-description" name="general-description"></a>
The project is to be undertaken teams of 5 members and consists of the building of a challengingly large Java program.

The completion of the project is divided into three separate components:

1. the First and Second Intermediate Project Delivery are intermediate operational build of the software, effectively demonstrating the full implementation of some important software features;

2. the Final Project Delivery is the demonstration of the finalized version of your software. During the final project delivery, you also have to demonstrate that your code includes many of the Java features presented in the lectures, and that you effectively use the tools presented in the lectures in your project.

All project deliveries are to be undertaken in the laboratory where the team presents the implemented features to the instructor following a pre-circulated grading sheet. The individual assignments will also be related to the project, but graded individually and separately from the project.

It is important to realize that the project description is purposely incomplete, and that it is one of your duties in this project to:

1. elicit and formulate all the missing details before you start the implementation,
2. limit the scope of the project according to the time that is available,
3. determine what design decisions will be made, as well as
4. what tools will be used for the implementation. These activities require some investigations and discussions that are important aspects of software development and this project.

# 3. Problem statement
<a id="markdown-problem-statement" name="problem-statement"></a>

The specific project for this semester consists in building a simple “Risk” computer game. The developed program will have to be compatible with the rules and map files and the command-line play of the **`Domination`** version of Risk, which can be downloaded at: <https://sourceforge.net/projects/domination/>.

A Risk game consists of a connected graph map representing a world map, where each node is a country and each edge represents adjacency between countries. Two or more players can play by placing armies on countries they own, from which they can attack adjacent countries to conquer them. The objective of the game is to conquer all countries on the map.

## 3.1. Map
<a id="markdown-map" name="map"></a>
The game map is a connected graph where each node represents a country owned by one of the players. Edges between the nodes represent adjacency between countries. The map is divided into subgraphs that represent continents. A continent is a connected subgraph of the map graph, and every country belongs to one and only one continent. Each continent is given a control value that determines the number of armies per turn that is given to a player that controls all of it. During game play, every country belongs to one and only one player and contains one or more armies that belong to the player owning the country. In your implementation, it will be expected that the game can be played on any connected subgraph that is defined by the user before play, saved as a text file representation, and loaded by the game during play.

## 3.2. Game
<a id="markdown-game" name="game"></a>
The game starts by the startup phase, where the number of players is determined, then all the countries are randomly assigned to the players. Then the turn-based main play phase begins, in which all players are given a turn in a round-robin fashion.

Each player’s turn is itself divided into three phases:

1. Reinforcement phase
2. Attack phase
3. Fortifications phase

Once a player is finished with these three phases, the next player’s turn starts.

### 3.2.1. Reinformcements Phase
<a id="markdown-reinformcements-phase" name="reinformcements-phase"></a>
In the reinforcements phase, the player is given a number of armies that depends on the number of countries he owns (# of countries owned divided by 3, rounded down). If the player owns all the countries of an entire continent, the player is given an amount of armies corresponding to the continent’s control value. 

Finally, if the player owns three cards of different sorts or the same sorts, he can exchange them for armies. The number of armies a player will get for cards is first 5, then increases by 5 every time any player does so (i.e. 5, 10, 15, …).

In any case, the minimal number of reinforcement armies is 3. Once the total number of reinforcements is determined for the player’s turn, the player may place the armies on any country he owns, divided as he wants. Once all the reinforcement armies have been placed by the player, the attacks phase begins.

### 3.2.2. Attack Phase
<a id="markdown-attack-phase" name="attack-phase"></a>
In the attack phase, the player may choose one of the countries he owns that contains two or more armies, and declare an attack on an adjacent country that is owned by another player. A battle is then simulated by the attacker rolling at most 3 dice (which should not be more than the number of armies contained in the attacking country) and the defender rolling at most 2 dice (which should not be more than the number of armies contained in the attacking country).

The outcome of the attack is determined by comparing the defenders best dice roll with the attackers best dice roll. If the defender rolls greater or equal to the attacker then the attacker loses an army otherwise the defender loses an army. If the defender rolled two dice then his other dice roll is compared to the attacker's second best dice roll and a second army is lost by the attacker or defender in the same way.

The attacker can choose to continue attacking until either all his armies or all the defending armies have been eliminated. If all the defender's armies are eliminated the attacker captures the territory. The attacking player must then place a number of armies in the conquered country which is greater or equal than the number of dice that was used in the attack that resulted in conquering the country.

A player may do as any attacks as he wants during his turn. Once he declares that he will not attack anymore (or cannot attack because none of his countries that have an adjacent country controlled by another player is containing more than one army), the fortification phase begins.

### 3.2.3. Fortification Phase
<a id="markdown-fortification-phase" name="fortification-phase"></a>
In the fortification phase, the player may move any number of armies from one of his owed countries to the other, provided that there is a path between these two countries that is composed of countries that he owns. Only one such move is allowed per fortification phase. Once the move is made or the player forfeits his fortification phase, the player’s turn ends and it is now the next player’s turn. Any player than does not control at least one country is removed from the game.

The game ends at any time one of the players owns all the countries in the map.

## 3.3. Cards
<a id="markdown-cards" name="cards"></a>
A player receives a card at the end of his turn if he successfully conquered at least one country during his turn. Each card is either an infantry, cavalry, or artillery card.

During a player’s reinforcement phase, a player can exchange a set of three cards of the same kind, or a set of three cards of all different kinds for a number of armies that increases every time any player does so.

The number of armies a player will get for cards is first 5, then increases by 5 every time any player does so (i.e. 5, 10, 15, …).

A player that conquers the last country owned by another player receives all the cards held by that player.

If a player holds five cards during his reinforcement phase, he must exchange three of them for armies.

# 4. Implementations
<a id="markdown-implementations" name="implementations"></a>

This part of the document briefly introduce the implementations of the project. For more detailed implementation, check out `wiki` section.

## 4.1. Design Pattern
<a id="markdown-design-pattern" name="design-pattern"></a>

The project using MVC Pattern, which is stand for Model-View-Controller

More about applying MVC in Java applications can be found here: [MVC article on Codeproject](https://www.codeproject.com/Articles/879896/Programming-in-Java-using-the-MVC-architecture)

## 4.2. JDK version and tools
<a id="markdown-jdk-version-and-tools" name="jdk-version-and-tools"></a>

The project use features of Java 8, as well as a mix of Eclipse IDE and Visual Studio Code with Java extension by RedHat.
