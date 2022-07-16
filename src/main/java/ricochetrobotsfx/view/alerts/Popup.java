package ricochetrobotsfx.view.alerts;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Popup {
    Stage window = new Stage();

    public Popup(String title, String text) {
        window.setTitle(title);

        TextArea label = new TextArea(text);
        label.setEditable(false);
        label.setWrapText(true);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
    }

    public void show() {
        window.show();
    }
}
