package Class;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Zone {
    public String nomZone;
    public ColorPicker colorPicker;

    public Zone(String nom, Color c){
        this.nomZone = nom;
        this.colorPicker = new ColorPicker();
        colorPicker.setValue(c);
    }

    public String getNomZone() {
        return nomZone;
    }

    public void setNomZone(String nomZone) {
        this.nomZone = nomZone;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    public Color getColor(){
        return colorPicker.getValue();
    }


    @Override
    public String toString() {
        return "Zone : "+nomZone;
    }
}
