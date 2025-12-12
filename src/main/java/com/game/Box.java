package com.game;

import javafx.scene.image.ImageView;

public class Box extends GameObject implements Moveable{

    public Box(Position pos) {
        super(pos);
    }

    @Override
    public void move(int dx, int dy){
        //change position by adding each dx and dy to the original position
        pos.setX(pos.getX()+dx);
        pos.setY(pos.getY()+dy);
    }

    @Override
    public char getSymbol() {
        return 'B';
    }
    
    @Override
    public String getImageName() { return "box.png"; }

    @Override
    public ImageView getImageView() {
        ImageView view = new ImageView(ImageLoader.load(getImageName()));
        view.setFitWidth(64);
        view.setFitHeight(64);
        return view;
    }
}
