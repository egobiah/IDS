package Class;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Zone {
    private static int compteur_id = 0;
    int id;
    public String nomZone;
    public ColorPicker colorPicker;
    EventHandler<ActionEvent> eventColorPicker;
    String ip = "auto";
    String port = "auto";

    public Zone(String nom, Color c){
        this.nomZone = nom;
        this.colorPicker = new ColorPicker();
        colorPicker.setValue(c);
        this.id = compteur_id;
        compteur_id++;


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
        return "Zone : "+nomZone+ " ip = " + ip + " port = " + port;
    }

    public void setEventColorPicker(EventHandler<ActionEvent> eventColorPicker) {
        colorPicker.setOnAction(eventColorPicker);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
