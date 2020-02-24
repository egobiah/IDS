package CLIENT;
import BD.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Chat {
    ChatRoom chatRoom;
    Person user;
    Scene scene;
    BorderPane root;
    ScrollPane scrollPane;
    Button toAccueil;
    Button send;


    public Chat(ChatRoom chatRoom, Person user){
        this.chatRoom = chatRoom;
        this.user = user;
        root = new BorderPane();
        VBox topBox = new VBox();
        toAccueil = new Button("Retour");
        Text roomDef = new Text("Salle : " + chatRoom.toString());
        Text userDef = new Text("Utilisateur : " + user.toString());

        topBox.getChildren().addAll(toAccueil, roomDef, userDef);

        scrollPane = new ScrollPane();

        BorderPane bottomBox = new BorderPane();
        TextField message = new TextField();
        send = new Button("Envoyer");
        bottomBox.setCenter(message);
        bottomBox.setRight(send);

        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);
        scene = new Scene(root, 400,400);

        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println(message.getText());
                message.clear();
            }
        });

    }

    public Button getToAccueil() {
        return toAccueil;
    }

    public Scene getScene() {
        return scene;
    }


}
