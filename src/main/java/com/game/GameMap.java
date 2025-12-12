package com.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    List<GameObject> staticObjects = new ArrayList<>();  //Walls, Target
    List<GameObject> dynamicObjects = new ArrayList<>(); //Player, Box (the ones with moveable)
    
    private final java.util.List<GameEventListener> listeners = new java.util.ArrayList<>();
    public void addListener(GameEventListener l) { listeners.add(l); }
    public void removeListener(GameEventListener l) { listeners.remove(l); }
    private void emit(GameEvent e) { for (var l : listeners) l.onEvent(e); }
    
    Player player;
    int width ; // row.length of the grid 
    int height; // column length of the grid
    String levelFile;
    public GameMap(String levelFile) {
        System.out.println("Levelfile: "+ levelFile);
        this.staticObjects = new ArrayList<>();
        this.dynamicObjects = new ArrayList<>();
        this.levelFile = levelFile;
        try {
            loadFromFile(levelFile);
        } catch (InvalidLevelFormatException e) {
            e.printStackTrace();
            System.out.println("Closing the game due to invalid file.");
            System.exit(1);
        } //setting up the map
        System.out.println("Width:"+width+" Height: "+height);
    }

    private void loadFromFile(String filename) throws InvalidLevelFormatException{
        System.out.println("loadFromFile method called!");
        System.out.println("File name: "+filename);
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);

        if (is == null) {
            throw new InvalidLevelFormatException("File \'" + filename + "\' not found!");
        }

        List<String> lines = new ArrayList<>();

        //changed scanner to bufferReader for try out
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.out.println("Error in reading file. Invalid File maybe!?");
            e.printStackTrace();
            return; // Stop processing if reading failed
        }

        
        height = lines.size();
        width = lines.get(0).length();

        for (int i = 0; i < lines.size(); i++) {
            int currentLength = lines.get(i).length();
            if (currentLength != this.width) {
                throw new InvalidLevelFormatException(
                    "The Map is not Rectangular!! Invalid File!"
                );
            }
        }
        
        int playerNum = 0;
        int y = 0;

        for (String currentLine : lines) {
            if (currentLine.length() > this.width) {
                this.width = currentLine.length();
            }

            for (int x = 0; x < currentLine.length(); x++) {
                char type = currentLine.charAt(x);
                if (type == '#') {
                    staticObjects.add(new Wall(new Position(x, y)));
                } else if (type == 'T') {
                    staticObjects.add(new Target(new Position(x, y)));
                } else if (type == 'P') {
                    this.player = new Player(new Position(x, y), width, height);
                    dynamicObjects.add(this.player);
                    playerNum++;
                } else if (type == 'B') {
                    dynamicObjects.add(new Box(new Position(x, y)));
                } else if(type == '+'){ //Target and Player on same position
                    this.player = new Player(new Position(x, y), width, height);
                    dynamicObjects.add(this.player);
                    playerNum++;
                    staticObjects.add(new Target(new Position(x, y)));
                } else if(type == '*'){ //Target and Box on same position
                    staticObjects.add(new Target(new Position(x, y)));
                    dynamicObjects.add(new Box(new Position(x, y)));
                }else if (type != ' ') { 
                    //System.out.println("Unknown Character: " + type);
                    throw new InvalidLevelFormatException("Invalid Character found in the file!");
                }
            }
            y++;
        }

        //if 0 player or more than 1 player created, then invalid
        if (playerNum == 0) {
            throw new InvalidLevelFormatException("File is missing a player!");
        } else if (playerNum > 1) {
            throw new InvalidLevelFormatException("File has more than one player! ");
        }

    }

    //previously printGrid method from Game.java
    public void draw() {
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(objectIsAt(x, y)); //dead code fixed!
            }
            System.out.println(row);
        }
    }

    public void update() {
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(objectIsAt(x, y));
                //fixed the row.append(true ? objectIsAt(x,y): ". ")
            }
            System.out.println(row);
        }

        //also needs to check if win condition is met
        if(checkWinCondition()){
            System.out.println("Congratulation! You Won!! YAY~~~");
        }
    }

    //Checking the wincon - all target covered by boxes
    public boolean checkWinCondition() {
        for (GameObject staticObj : staticObjects) {
            if (staticObj instanceof Target) {  //repeat for every target
                
                boolean boxOnTarget = false;   //default is that the game is not won
                
                for (GameObject dynamicObj : dynamicObjects) {
                    // Checking box and targets on same coordinates for every target
                    if (dynamicObj instanceof Box 
                        && dynamicObj.getPosition().getX() == staticObj.getPosition().getX() 
                        && dynamicObj.getPosition().getY() == staticObj.getPosition().getY()) {
                            boxOnTarget = true;
                        break; // escape when finding one box == for one target
                    }
                }

                if (!boxOnTarget) { //If a single target is not covered by a box, then return false
                    return false; 
                }
            }
        }

        // If we get here, it means we looped through ALL targets 
        // and every single one had a box on top.
        return true; 
    }

    //Returns object at a specific coordinate. 
    public GameObject getObjectAt(int x, int y) {
        // Check dynamic objects first like players and box
        //Because if box and target on the same coordinate, it should print B instead of T
        for (GameObject go : dynamicObjects) {
            if (go.getPosition().getX() == x && go.getPosition().getY() == y) {
                return go;
            }
        }
        // Then check static objects like walls and target
        for (GameObject go : staticObjects) {
            if (go.getPosition().getX() == x && go.getPosition().getY() == y) {
                return go;
            }
        }
        return null; // when no object
    }

    //check if object exist at point (x,y)
    public String objectIsAt(int x, int y){
        for(GameObject go: dynamicObjects){
            if(go.getPosition().getX()==x&&go.getPosition().getY()==y){
                return go.getSymbol()+" ";
            }
        }

        for (GameObject go : staticObjects) {
            if (go.getPosition().getX() == x && go.getPosition().getY() == y) {
                return go.getSymbol()+" ";
            }
        }

        return ". ";
    }

    //Check only if Target exists on position
    public boolean isTargetAt (int x, int y){
        for (GameObject go : staticObjects) {
            if (go.getPosition().getX() == x && go.getPosition().getY() == y) {
                return 'T'==go.getSymbol();
            }
        }
        return false;
    }

    public boolean movePlayer(int dx, int dy){
        Position from = player.getPosition();
        boolean moved  = false;
        boolean pushed = false;
    
        //first see which object is located at that move requested 
        GameObject nextObject = getObjectAt(player.getPosition().getX()+dx,player.getPosition().getY()+dy);

        // If it contains no object: move the player
        //Also can move when Target
        if(nextObject == null || nextObject instanceof Target){
            player.move(dx, dy);
            moved = true;
        }else if(nextObject instanceof Wall){
            // If it contains a wall: do nothing
            System.out.println("There is a wall, so you cannot move!");
        }else if(nextObject instanceof Box){ 
            // If it contains a box:
            // Calculate the space behind the box
            GameObject nextToBox = getObjectAt(nextObject.getPosition().getX()+dx, nextObject.getPosition().getY()+dy);
             // If that space is empty, move the box, then move the player
             if(nextToBox == null || nextToBox instanceof Target){ //if target is next to box, box is movable
                ((Box)nextObject).move(dx, dy);
                player.move(dx, dy);
                pushed = true;
                moved = true;
             }
            // Otherwise, do nothing
            else
                System.out.println("You cannot move the box!");
        
        }
        update();
        writeToFile(levelFile);

        if (!moved) {
            emit(new GameEvent(GameEventType.INVALID_MOVE, from, from, null));
            return false;
        }

        Position to = player.getPosition();
        emit(new GameEvent(pushed ? GameEventType.PUSH : GameEventType.MOVE, from, to, null));

        if (checkWinCondition()) emit(new GameEvent(GameEventType.WIN, null, null, "All boxes on targets"));
        return true;

    }

    public void writeToFile(String filename) {
        char[][] grid = new char[height][width];

        //Create the empty map first
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = ' ';
            }
        }

        //Fill it with static objects such as Wall and Box
        for (GameObject obj : staticObjects) {
            int x = obj.getPosition().getX();
            int y = obj.getPosition().getY();
            
            if (obj instanceof Wall) {
                grid[y][x] = '#';
            } else if (obj instanceof Target) {
                    grid[y][x] = 'T';
            }
        }

        //Fill with Box
        for (GameObject obj : dynamicObjects) {
            if (obj instanceof Box) {
                int x = obj.getPosition().getX();
                int y = obj.getPosition().getY();
                grid[y][x] = 'B';

                //When Target and Box on the same position
                if(isTargetAt(x, y))
                    grid[y][x] = '*';   
            }
        }

        //Check player exists first to use methods and fill with player
        if (player != null) {
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            grid[y][x] = 'P';

            //When Player and Target on same position
            if(isTargetAt(x, y))
                grid[y][x] = '+';
        }

        // String safeFilename = filename.startsWith("/") ? filename.substring(1) : filename;
        // String savePath = "save_" + safeFilename;

        //because I can only call level1.txt by '/level1./txt' using /, it gives autosave fail. 
        // so I had to check if the filename has / inside and remove / when writing.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < height; i++) {
                writer.write(grid[i]);
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Autosave failed: ...");
            e.printStackTrace();
        }

    }

    //Getters
    public List<GameObject> getStaticObjects() {
        return staticObjects;
    }

    public List<GameObject> getDynamicObjects() {
        return dynamicObjects;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLevelFile() {
        return levelFile;
    }
    
}
