package CLIENT;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ConnectionScreen {
    Scene scene;
    BorderPane root;
    TextField textField;
    Button button;
    ConnectionScreen(){
        root = new BorderPane();
        scene = new Scene(root,400,40);

        Label ID = new Label("Pseudo:");
        textField = new TextField ();
        button = new Button("Se connecter");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(ID, textField, button);

        root.setCenter(hBox);




    }

    protected Scene getScene() {
        return scene;
    }

    public String getPseudo(){
        return textField.getText();
    }

    public Button getButton(){
        return button;
    }
}
