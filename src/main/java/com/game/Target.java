package com.game;

import javafx.scene.image.ImageView;

public class Target extends GameObject {

    public Target(Position pos) {
        super(pos);
    }

    @Override
    public char getSymbol() {
        return 'T';
    }
    
    @Override
    public String getImageName() { return "target.png"; }

    @Override
    public ImageView getImageView() {
        ImageView view = new ImageView(ImageLoader.load(getImageName()));
        view.setFitWidth(64);
        view.setFitHeight(64);
        return view;
    }
}
