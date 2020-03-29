package FX;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;

public class Grid extends Group {
    Case[][] cases;
    //int caseHauteur, int caseLargeur, int hauteurPX, int largeurPx;
    double largeurCase, hauteurCase;
    int x, y;

    Point debutSel = new Point(0,0);
    Point lastPos = new Point(0,0);

    Grid(int nbCaseHauteur, int nbCaseLargeur, double hauteurPX, double largeurPx) {
        this.x = nbCaseLargeur;
        this.y = nbCaseHauteur;
        this.largeurCase = largeurPx / nbCaseLargeur;
        this.hauteurCase = hauteurPX / nbCaseHauteur;
        this.cases = new Case[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                cases[i][j] = new Case(i, j, this.hauteurCase, this.largeurCase);
                cases[i][j].setOnMouseClicked(handlerCLICK);
                cases[i][j].setOnDragDetected(handlerDetect);
                cases[i][j].setOnMouseMoved(handlerEND);
              //  cases[i][j].setOnMouseDragOver(handlerMAJ);
              //  cases[i][j].setOnMouseDragReleased(handlerENDING);



            }
        }
        afficherCases();


    }

    public void afficherCases() {
        this.getChildren().removeAll();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.getChildren().add(cases[i][j]);
            }
        }
    }


    EventHandler<MouseEvent> handlerCLICK = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Cliqué sur la case : " + ((Case) event.getTarget()).getPoint());
           // setOnMousePressed(handlerBEGIN);

        }
    };

    EventHandler<MouseEvent> handlerDetect = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Drag la case : " + ((Case) event.getTarget()).getPoint());
           // setOnMousePressed(handlerBEGIN);

        }
    };


    EventHandler<MouseEvent> handlerEND = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            lastPos = ((Case) event.getTarget()).getPoint();
            System.out.println(lastPos);
        }
    };






}
