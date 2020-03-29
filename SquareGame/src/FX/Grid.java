package FX;
import Class.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.awt.Point;
import java.util.ArrayList;

public class Grid extends Group {
    Case[][] cases;
    Group zoneSelection= new Group();
    Group grille = new Group();
    Rectangle zone = new Rectangle();
    //int caseHauteur, int caseLargeur, int hauteurPX, int largeurPx;
    double largeurCase, hauteurCase;
    int x, y;

    Point caseDebut = new Point(0,0);
    Point caseFin = new Point(0,0);
    Point debutSelection = new Point(0,0);
    Point finSelection = new Point(0,0);
    Zone currentZone;



    Grid(int nbCaseHauteur, int nbCaseLargeur, double hauteurPX, double largeurPx) {
        this.x = nbCaseLargeur;
        this.y = nbCaseHauteur;
        this.largeurCase = largeurPx / nbCaseLargeur;
        //this.largeurCase = this.compute / nbCaseHauteur
        this.hauteurCase = hauteurPX / nbCaseHauteur;
       // this.hauteurCase = maxHeight() / nbCaseHauteur;
        this.cases = new Case[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                cases[i][j] = new Case(i, j, this.hauteurCase, this.largeurCase);
                cases[i][j].setOnMouseClicked(handlerCLICK);
                cases[i][j].setOnDragDetected(handlerDetect);
                cases[i][j].setOnMouseDragOver(handlerDRAGING);
                cases[i][j].setOnMouseDragReleased(handlerEND);
                cases[i][j].setOnMousePressed(handlerPRESS);

              //  cases[i][j].setOnMouseDragOver(handlerMAJ);
              //  cases[i][j].setOnMouseDragReleased(handlerENDING);



            }
        }

        this.getChildren().addAll(zoneSelection,grille);
        afficherCases();
        grille.setOpacity(0.5);
        zoneSelection.getChildren().add(zone);




    }

    public void afficherCases() {
        grille.getChildren().removeAll();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grille.getChildren().add(cases[i][j]);
            }
        }
    }


    EventHandler<MouseEvent> handlerCLICK = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            //System.out.println("Cliqué sur la case : " + ((Case) event.getTarget()).getPoint());
            colorerZone( ((Case) event.getTarget()).getPoint() ,((Case) event.getTarget()).getPoint());

        }
    };

    EventHandler<MouseEvent> handlerPRESS = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            debutSelection = new Point((int)event.getX(), (int) event.getY());
            caseDebut = ((Case) event.getTarget()).getPoint();
            //System.out.println("Pressed");
        }
    };

    EventHandler<MouseEvent> handlerDetect = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            System.out.println("Drag la case : " + caseDebut);

            ((Case) event.getTarget()).startFullDrag();
            //debutSelection = new Point((int)event.getX(), (int) event.getY());
            //caseDebut = ((Case) event.getTarget()).getPoint();
        }
    };


    EventHandler<MouseEvent> handlerDRAGING = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
           finSelection = new Point((int)event.getX(), (int) event.getY());
           dessinerZoneSelection(debutSelection, finSelection);
        }
    };

    EventHandler<MouseEvent> handlerEND = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            //System.out.println("Finis");
            caseFin = ((Case) event.getTarget()).getPoint();
            System.out.println(caseFin);
            zone.setStroke(Color.TRANSPARENT);
            zone.setX(0);
            zone.setY(0);
            zone.setWidth(0);
            zone.setHeight(0);
            colorerZone(caseDebut,caseFin);


        }
    };

   public void dessinerZoneSelection(Point p1, Point p2){
     //  System.out.println("Je dessine");
       int minX = Math.min((int)p1.getX(), (int) p2.getX());
       int maxX = Math.max((int)p1.getX(), (int) p2.getX());
       int minY = Math.min((int)p1.getY(), (int) p2.getY());
       int maxY = Math.max((int)p1.getY(), (int) p2.getY());
     //  zoneSelection.getChildren().removeAll();

      // Rectangle zone = new Rectangle();
       zone.setX(minX);
       zone.setY(minY);
       zone.setWidth(maxX-minX);
       zone.setHeight(maxY-minY);
       zone.setFill(Color.TRANSPARENT);
       zone.setStrokeType(StrokeType.CENTERED);
       zone.setStroke(Color.BLACK);
    }

    public void colorerZone(Point p1, Point p2){
        int minX = Math.min((int)p1.getX(), (int) p2.getX());
        int maxX = Math.max((int)p1.getX(), (int) p2.getX());

        int minY = Math.min((int)p1.getY(), (int) p2.getY());
        int maxY = Math.max((int)p1.getY(), (int) p2.getY());


        for(int i = minX; i <= maxX; i++){
            for(int j = minY; j <= maxY; j++){
                //cases[i][j].changerCouleur(currentZone.getColor());
                cases[i][j].setZone(currentZone);
            }
        }
        //System.out.print("Coloriage de la zone ");
    }

    public void setCurrentZone(Zone zone) {
        this.currentZone = zone;
    }
}
