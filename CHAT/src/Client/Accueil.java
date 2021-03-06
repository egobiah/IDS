package Client;

import BD.*;


import RmiPackage.TalkWithClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Accueil {
    Scene scene;
    BorderPane root;
    ArrayList<ChatRoom> chatRooms;
    Person user;
    TableView<ChatRoom> tableView;
    Button toConnectionScreen;
    Button toChatRoom;
    Button actualiser;
    TalkWithClient t;

    public Accueil(Person user, TalkWithClient t){
        this.t = t;
        toConnectionScreen = new Button("Retour");
        toChatRoom = new Button("Connection");
        actualiser = new Button("Actualiser");

        actualiser.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Actualistion de la liste");
                refreshTable();
            }
        });



        this.user = user;
        root = new BorderPane();
        scene = new Scene(root, 400,400);



        tableView = new TableView<ChatRoom>();
        tableView.setEditable(false);

        TableColumn<ChatRoom, String> chatName = new TableColumn<ChatRoom,String> ("Nom de la salle");
        chatName.setCellValueFactory(new PropertyValueFactory<>("name"));
        chatName.setCellFactory(TextFieldTableCell.<ChatRoom> forTableColumn());

        chatName.setOnEditCommit((TableColumn.CellEditEvent<ChatRoom, String> event) -> {

            TablePosition<ChatRoom, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            System.out.println("new name");

            try{

            }  catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du changement de nom");
                String msg = "Cannot modify";
                alert.setContentText(msg);
                alert.showAndWait();
               // refreshTable();
            }



        });

        TableColumn<ChatRoom, String> chatDescription = new TableColumn<ChatRoom,String> ("Description");
        chatDescription.setCellValueFactory(new PropertyValueFactory<>("description"));


        refreshTable();
        Text text = new Text("Connecté en tant que : " + user.toString() + " ");
        tableView.getColumns().addAll(chatName,chatDescription);
        root.setCenter(tableView);
        HBox returnBox = new HBox();
        returnBox.getChildren().addAll(text, toConnectionScreen, actualiser);

        root.setTop(returnBox);

        BorderPane connectPane = new BorderPane();
        connectPane.setRight(toChatRoom);
        root.setBottom(connectPane);


    }

    protected Scene getScene(){
        return scene;
    }



    public void refreshTable(){
        try {
            chatRooms = t.getChatList(user);
        } catch (RemoteException r){
            r.printStackTrace();;
        }
        ObservableList<ChatRoom> list = FXCollections.observableArrayList();
            for(int i = 0 ; i < chatRooms.size(); i++){
                list.add(chatRooms.get(i));
            }
        tableView.setItems(list);
    }

    public Button getToConnectionScreen(){
        return toConnectionScreen;
    }

    public Button getToChatRoom(){
        return toChatRoom;
    }

    public TableView<ChatRoom> getTableView() {
        return tableView;
    }
}
