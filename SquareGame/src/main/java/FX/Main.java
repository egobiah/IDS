package FX;
import Server.Manager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;

import java.nio.file.attribute.GroupPrincipal;

public class Main extends Application{
    Server.Manager manager;
    public void start(Stage primaryStage) {
        manager = new Manager();
        Screen screen = Screen.getPrimary();
        Rectangle2D ecran = screen.getVisualBounds();

      //  Scene s = new Scene(grid, ecran.getWidth()/2, ecran.getHeight()/2);
        MenuSelectionZone menuSelectionZone = new MenuSelectionZone(ecran.getWidth()/4*3, ecran.getHeight()/4*3, manager);
        primaryStage.setTitle("SquaregGame - Julien ALAIMO - Olivier HUREAU");
        primaryStage.show();
        primaryStage.setScene(menuSelectionZone);

       // primaryStage.setFullScreen(true);

    }


    public static void main(String[] args) {
        launch(args);
    }

 }