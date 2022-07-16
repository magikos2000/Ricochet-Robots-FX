package ricochetrobotsfx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ricochetrobotsfx.controller.Controller;
import ricochetrobotsfx.controller.SceneController;
import ricochetrobotsfx.model.Board;
import ricochetrobotsfx.view.FeelingLucky;
import ricochetrobotsfx.view.LANPlay;
import ricochetrobotsfx.view.MainMenu;
import ricochetrobotsfx.view.Solver;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Board board = new Board();
        Controller controller = new Controller(board);
        SceneController sceneController = new SceneController(primaryStage);

        MainMenu menu = new MainMenu(sceneController);
        FeelingLucky feelingLucky = new FeelingLucky(board, controller);
        LANPlay lanPlay = new LANPlay();

        sceneController.addScene("Main Menu", menu.getScene());
        sceneController.addScene("Feeling Lucky", feelingLucky.getScene());
        sceneController.addScene("LAN Play", lanPlay.getScene());
        sceneController.switchTo("Main Menu");

        primaryStage.setTitle("Ricochet Robots FX");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("icon.png"))
        ));
        primaryStage.show();
    }
}
