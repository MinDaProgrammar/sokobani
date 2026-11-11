package com.game;

import javafx.scene.image.ImageView;

public abstract class GameObject {
    Position pos;

    public GameObject(Position pos) {
        this.pos = pos;
    }

    public Position getPosition(){
        return pos;
    }

    public abstract char getSymbol();
    

    public abstract ImageView getImageView();
    public abstract String getImageName();

}
