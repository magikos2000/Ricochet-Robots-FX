package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import ricochetrobotsfx.model.Model;

public class FeelingLucky {
    Model model;

    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane, 640, 480);

    public FeelingLucky() {
        
    }

    public void updateBoard() {

    }

    public Scene getScene() {
        return scene;
    }
}
