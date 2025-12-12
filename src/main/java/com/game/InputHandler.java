package com.game;

public class InputHandler {

    static GameMap game;
    public InputHandler(GameMap map){
        InputHandler.game = map;
    }
    public  void fireMoveUp() {
        System.out.println("fireMoveUp() method called");
        game.movePlayer(0,-1);
    }

    public  void fireMoveDown() {
        System.out.println("fireMoveDown() method called");
        game.movePlayer(0,1);
    }

    public  void fireMoveLeft() {
        System.out.println("fireMoveLeft() method called");
        game.movePlayer(-1,0);
    }

    public  void fireMoveRight() {
        System.out.println("fireMoveRight() method called");
        game.movePlayer(1,0);
    }
    public void fireRestart() {
        //need to implement! not ready yet
    }
}

