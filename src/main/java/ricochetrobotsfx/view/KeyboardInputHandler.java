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
        KeyCode pressed = keyEvent.getCode();

        if (pressed.equals(KeyCode.UP) || pressed.equals(KeyCode.W)) {

        }

        if (pressed.equals(KeyCode.DOWN) || pressed.equals(KeyCode.S)) {

        }

        if (pressed.equals(KeyCode.LEFT) || pressed.equals(KeyCode.A)) {

        }

        if (pressed.equals(KeyCode.RIGHT) || pressed.equals(KeyCode.D)) {

        }
    }
}
