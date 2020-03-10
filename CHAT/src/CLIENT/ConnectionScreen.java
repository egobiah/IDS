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
    TextField pseudo;
    TextField password;
    TextField url;
    Button button;
    ConnectionScreen(){
        root = new BorderPane();
        scene = new Scene(root,1000,40);

        Label ID = new Label("Pseudo:");
        pseudo = new TextField ();

        Label Lpassword = new Label("Mot de passe:");
        password = new TextField ();

        Label Lurl = new Label("Adresse du serveur:");
        url = new TextField ("127.0.0.1");


        button = new Button("Se connecter");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(ID, pseudo, Lpassword, password, Lurl, url, button);

        root.setCenter(hBox);




    }

    public String getUrl(){
        return url.getText();
    }

    protected Scene getScene() {
        return scene;
    }

    public String getPseudo(){
        return pseudo.getText();
    }

    public Button getButton(){
        return button;
    }
}
