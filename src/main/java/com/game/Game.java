package com.game;
import java.util.Scanner;


public class Game {
    public static void main(String[] args) {
        String levelFile = "level1.txt";
        GameMap map = new GameMap(levelFile);
        //I changed the constructor for GameMap to receive two string 
        
        //GameMap map = new GameMap("/sokobani/src/main/resources/InvalidLevel.txt");
        //I checked with all cases for invalid file using InvalidLevel.txt
        //Not rectangular file
        //Other characters than T,B,P,#,' '
        //0 P or 2P

        map.draw();

        Scanner scanner = new Scanner(System.in);
        String player_input = "";
        //Right now I am testing and I don't know the game ending condition
        //so repeat the game loop until player type q for quit

        InputHandler input = new InputHandler(map);
        while(!player_input.toLowerCase().equals("q")){
            System.out.println();
            System.out.println("Which direction do you want to move?");
            System.out.println("Type w/a/s/d to move or q to quit");
            player_input = scanner.nextLine();
            System.out.println();

            switch(player_input.toLowerCase()){ //lower case just in case
                case "w": input.fireMoveUp(); //move up
                break;
                case "a": input.fireMoveLeft(); //move left
                break;
                case "s": input.fireMoveDown(); //move down
                break;
                case "d": input.fireMoveRight(); //move right
                break;
                case "q": System.out.println("Quitting the game");
                    System.exit(0);
                    break;
                default: System.out.println("Wrong input! Try it again!");
                    break;
                    
            }

        }
        scanner.close();
        
    }

}
