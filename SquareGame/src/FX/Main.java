package FX;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;

import java.nio.file.attribute.GroupPrincipal;

public class Main extends Application{

    public void start(Stage primaryStage) {

        Screen screen = Screen.getPrimary();
        Rectangle2D ecran = screen.getVisualBounds();
        Grid grid = new Grid(10,10,ecran.getHeight()/2,ecran.getWidth()/2);
        Scene s = new Scene(grid, ecran.getWidth()/2, ecran.getHeight()/2);
        s.setCursor(Cursor.CROSSHAIR);
        primaryStage.setTitle("SquaregGame - Julien ALAIMO - Olivier HUREAU");
        primaryStage.show();
        primaryStage.setScene(s);

       // primaryStage.setFullScreen(true);

    }


    public static void main(String[] args) {
        launch(args);
    }

 }