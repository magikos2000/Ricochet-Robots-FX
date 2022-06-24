package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Solver {
    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane, 640, 480);

    public Scene getScene() {
        return scene;
    }
}
