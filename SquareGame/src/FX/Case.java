package FX;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.awt.*;

public class Case extends Rectangle {
    Point p;
    Case(int x, int y, double hauteur, double largeur ){
        p = new Point(x,y);

        setX(x*largeur);
        setY(y*hauteur);
        setWidth(largeur);
        setHeight(hauteur);
        setFill(Color.WHITE);
        setStrokeType(StrokeType.CENTERED);
        setStroke(Color.BLACK);

    }


    @Override
    public String toString() {
        return "pos : ("+p.getX()+" ; " + p.getY() + " )";
    }

    public Point getPoint(){
        return p;
    }

    public void changerCouleur(Color c){
        setFill(c);
    }


}
