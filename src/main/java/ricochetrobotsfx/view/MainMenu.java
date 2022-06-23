package ricochetrobotsfx.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class MainMenu {
    private final BorderPane pane = new BorderPane();
    private final Scene scene = new Scene(pane, 640, 480);

    public MainMenu() {
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
        btn1.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        Button btn2 = new Button("LAN play");
        Button btn3 = new Button("Solve this for me");
        VBox vbox = new VBox(btn1, btn2, btn3);
        vbox.setAlignment(Pos.CENTER);
        pane.setCenter(vbox);

        scene.getStylesheets().add(getClass().getResource("/mainMenu.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
