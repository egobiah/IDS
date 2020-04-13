package FX;
import Class.Zone;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.awt.*;

public class Case extends Rectangle {
    Point p;
    Zone z;
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





    public Point getPoint(){
        return p;
    }

    public void changerCouleur(Color c){
        setFill(c);
    }

    public void setZone(Zone zone){
        this.z = zone;
        changerCouleur(z.getColor());
    }

    public void removeZone(){
        z = null;
        changerCouleur(Color.WHITE);
    }

    public Zone getZ() {
        return z;
    }

    public void setZ(Zone z) {
        this.z = z;
        changerCouleur(z.getColor());
    }
    @Override
    public String toString() {
        return "Position : ("+p.getX()+" ; " + p.getY() + " ).\n" + z;
    }
}
