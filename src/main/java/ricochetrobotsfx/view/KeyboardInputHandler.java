package ricochetrobotsfx.view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ricochetrobotsfx.model.Model;

public class KeyboardInputHandler {
    private final Model model;

    public KeyboardInputHandler(Model model) {
        this.model = model;
    }

    void handlePressed(KeyEvent keyEvent) {
        FeelingLucky.handleKeyboardInput(keyEvent);
    }
}
