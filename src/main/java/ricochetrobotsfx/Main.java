package ricochetrobotsfx;

import javafx.application.Application;
import javafx.stage.Stage;
import ricochetrobotsfx.view.Menu;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Menu menu = new Menu();
        primaryStage.setScene(menu.getScene());
        primaryStage.setTitle("Ricochet Robots FX");
        primaryStage.show();
    }
}
