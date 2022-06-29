package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import ricochetrobotsfx.model.Board;
import ricochetrobotsfx.model.Model;

public class FeelingLucky {
    Model model;

    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane, 640, 480);

    public FeelingLucky() {
        Board board = new Board();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(board.getBoard()[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(board.getSpec()[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void updateBoard() {

    }

    public Scene getScene() {
        return scene;
    }
}
