package com.game;

import javafx.scene.image.ImageView;

public class Player extends GameObject implements Moveable{
    private final int gridWidth, gridHeight;

    public Player(Position start,int gridWidth, int gridHeight) {
        super(start); // Explicitly call the constructor of GameObject
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }
    
    public char getSymbol(){
        return 'P';
    }
    
    @Override
    public String getImageName() { return "player.png"; }

    @Override
    public ImageView getImageView() {
        ImageView view = new ImageView(ImageLoader.load(getImageName()));
        view.setFitWidth(64);
        view.setFitHeight(64);
        return view;
    }

    @Override
    public void move(int dx, int dy){
        //check if player can move by checking grid and update position
        tryMove(pos.getX()+dx,pos.getY()+dy);
    }

    private void tryMove(int nx, int ny) {
        System.out.println("Width: "+gridWidth+" Height: "+gridHeight);
        System.out.println("Player x: "+nx+" player y: "+ny);
        if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight) {
            pos.setX(nx);
            pos.setY(ny);
        } else {
            System.out.println("You can't move outside the map!");
        }
    }
    // public void moveUp() {
    //     tryMove(pos.getX(), pos.getY() - 1);
    // }

    // public void moveDown() {
    //     tryMove(pos.getX(), pos.getY() + 1);
    // }

    // public void moveLeft() {
    //     tryMove(pos.getX() - 1, pos.getY());
    // }

    // public void moveRight() {
    //     tryMove(pos.getX() + 1, pos.getY());
    // }

    
}
