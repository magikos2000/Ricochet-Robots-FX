package ricochetrobotsfx.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneController {
    private final HashMap<String, Scene> sceneMap = new HashMap<>();
    private final Stage mainStage;

    public SceneController(Stage primaryStage) {
        mainStage = primaryStage;
    }

    public void addScene(String name, Scene scene) {
        sceneMap.put(name, scene);
    }

    public void switchTo(String name) {
        mainStage.setScene(sceneMap.get(name));
    }

    public void activate(String name) {
        Stage subStage = new Stage();
        subStage.setTitle(name);
        subStage.setScene(sceneMap.get(name));
        subStage.show();
        subStage.toFront();
        subStage.requestFocus();
    }
}
