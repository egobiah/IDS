package FX;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.awt.*;

public class MenuSelectionZone extends Scene {
    BorderPane borderPane;
    Grid grid;
    ZoneManager zoneManager;
    MenuSelectionZone(double largeur, double hauteur){
        super(new BorderPane(),  largeur,  hauteur);
        borderPane = (BorderPane) this.getRoot();

        grid = new Grid(10,10,hauteur*0.95,largeur/2);
        borderPane.setCenter(grid);
        zoneManager = new ZoneManager(grid);
        borderPane.setRight(zoneManager);
       // grid.setCurrentZone(zoneManager.getCurrentZone());



    }


}
