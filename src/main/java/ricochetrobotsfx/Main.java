package ricochetrobotsfx;

import javafx.application.Application;
import javafx.stage.Stage;
import ricochetrobotsfx.controller.SceneController;
import ricochetrobotsfx.view.FeelingLucky;
import ricochetrobotsfx.view.LANPlay;
import ricochetrobotsfx.view.MainMenu;
import ricochetrobotsfx.view.Solver;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneController sceneController = new SceneController(primaryStage);
        MainMenu menu = new MainMenu(sceneController);
        FeelingLucky feelingLucky = new FeelingLucky();
        LANPlay lanPlay = new LANPlay();
        Solver solver = new Solver();

        sceneController.addScene("Main Menu", menu.getScene());
        sceneController.addScene("Feeling Lucky", feelingLucky.getScene());
        sceneController.addScene("LAN Play", lanPlay.getScene());
        sceneController.addScene("Solver", solver.getScene());


        sceneController.switchTo("Main Menu");
        primaryStage.setTitle("Ricochet Robots FX");
        primaryStage.show();
    }
}
