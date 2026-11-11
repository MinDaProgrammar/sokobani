package com.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SokobaniApp extends Application {

    private GameMap map;
    private GridPane grid;
    private InputHandler input;

    private static final int TILE = 64;
    private static final String LEVEL = "level1.txt";

    @Override
    public void start(Stage stage) {
        // Load the game map and set up input handler
        map = new GameMap(LEVEL);
        input = new InputHandler(map);

        grid = new GridPane();
        grid.setHgap(0); grid.setVgap(0);

        var root = new BorderPane(grid);
        var scene = new Scene(root, map.getWidth()*TILE, map.getHeight()*TILE);

        render();

        map.addListener(e -> {
            switch (e.type()) {
                case MOVE, PUSH, RESTART -> render();
                case WIN -> {
                    render();
                    var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Victory");
                    alert.setHeaderText(null);
                    alert.setContentText("You win!");
                    alert.showAndWait();
                    grid.getScene().getRoot().requestFocus();
                }
                case INVALID_MOVE -> {
                    // Optional: flash/shake/sound for feedback
                }
            }
        });
        

        // UI → model (intent only)
        scene.setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            switch (c) {
                case W, UP    -> input.fireMoveUp();
                case A, LEFT  -> input.fireMoveLeft();
                case S, DOWN  -> input.fireMoveDown();
                case D, RIGHT -> input.fireMoveRight();
                case R        -> input.fireRestart();
                default       -> {}
            }
        });

        stage.setTitle("Sokobani");
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    // Draw floor first, then the top object
    private void render() {
        grid.getChildren().clear();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                grid.add(new Floor(new Position(x,y)).getImageView(), x, y);
                GameObject top = map.getObjectAt(x,y);
                if (top != null) grid.add(top.getImageView(), x, y);
            }
        }
    }

    public static void main(String[] args) { launch(); }
}
