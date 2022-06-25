package ricochetrobotsfx.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ricochetrobotsfx.controller.SceneController;

import java.util.Objects;

public class MainMenu {
    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane, 640, 480);

    public MainMenu(SceneController sceneController) {
        Menu menu1 = new Menu("Ricochet Robots FX");
        MenuItem menu1Item1 = new MenuItem("About Ricochet Robots FX");
        menu1.getItems().add(menu1Item1);
        Menu menu2 = new Menu("Sound");
        CustomMenuItem menu2Item1 = new CustomMenuItem(new Slider());
        menu2.getItems().add(menu2Item1);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2);
        pane.setTop(menuBar);

        Button btn1 = new Button("I'm feeling lucky!");
        btn1.setOnAction(event -> sceneController.switchTo("Feeling Lucky"));
        Button btn2 = new Button("LAN play");
        btn2.setOnAction(event -> sceneController.switchTo("LAN Play"));
        Button btn3 = new Button("Solve this for me");
        btn3.setOnAction(event -> sceneController.activate("Solver"));
        Button btn4 = new Button("Exit");
        btn4.setOnAction(event -> Platform.exit());

        VBox vbox = new VBox(btn1, btn2, btn3, btn4);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        pane.setCenter(vbox);

        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/mainMenuStyle.css")).toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
