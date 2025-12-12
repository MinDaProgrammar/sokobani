package com.game;

import javafx.scene.image.ImageView;

public class Wall extends GameObject {
    public Wall(Position pos) {
        super(pos);
    }

    @Override
    public char getSymbol() {
       return '#';
    }

    @Override
    public String getImageName() { return "wall.png"; }

    @Override
    public ImageView getImageView() {
        ImageView view = new ImageView(ImageLoader.load(getImageName()));
        view.setFitWidth(64);
        view.setFitHeight(64);
        return view;
    }

}
