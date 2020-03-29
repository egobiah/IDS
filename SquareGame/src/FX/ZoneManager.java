package FX;
import Class.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class ZoneManager extends BorderPane {
    TableView<Zone> tableView;
    ArrayList<Zone> zones;
    ToolBar toolBar;
    EventHandler<ActionEvent> eventAjouter;
    EventHandler<ActionEvent> eventSupprimer;
    EventHandler<ActionEvent> eventColorPicker;
    Zone currentZone;
    Grid grid;

    ZoneManager(Grid grid) {
        super();
        this.grid = grid;
        tableView = new TableView<Zone>();
        tableView.setEditable(true);
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Selection de la table view");

                currentZone = zones.get(tableView.getFocusModel().getFocusedIndex());
                grid.setCurrentZone(currentZone);
                System.out.println(currentZone);
            }
        });

        TableColumn<Zone, String> zoneName = new TableColumn<Zone, String>("Zone");
        zoneName.setCellValueFactory(new PropertyValueFactory<>("nomZone"));

        zoneName.setCellFactory(TextFieldTableCell.<Zone>forTableColumn());
        zoneName.setOnEditCommit((TableColumn.CellEditEvent<Zone, String> event) -> {
            TablePosition<Zone, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            System.out.println("Modification en : " + newName);
            refreshCellOnGrid(null);

        });

        TableColumn<Zone, ColorPicker> cCol = new TableColumn<Zone, ColorPicker> ("Couleur");
        cCol.setCellValueFactory(new PropertyValueFactory<>("colorPicker"));



        tableView.getColumns().addAll(zoneName,cCol);

        eventColorPicker = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Nouvelle couleur : " + ((ColorPicker) e.getTarget()).getValue());
                refreshCellOnGrid(null);
            }
        };

        zones = new ArrayList<Zone>();
        currentZone = new Zone("ZoneA", Color.BLUE);
        currentZone.setEventColorPicker(eventColorPicker);
        zones.add(currentZone);
        zones.add(new Zone("ZoneB", Color.YELLOWGREEN));

        refreshTable();
        setCenter(tableView);

        Button ajouter = new Button("+");
        Button supprimer = new Button("-");
        toolBar = new ToolBar(ajouter,supprimer);

        eventAjouter = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                currentZone = new Zone("Nouvelle Zone", Color.color(Math.random(), Math.random(), Math.random()) );
                zones.add(currentZone);
                refreshTable();

            }
        };

        eventSupprimer= new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Zone aDelete = zones.get(tableView.getFocusModel().getFocusedIndex());
                zones.remove(aDelete);
                refreshTable();
                refreshCellOnGrid(aDelete);
            }
        };

        ajouter.setOnAction(eventAjouter);
        supprimer.setOnAction(eventSupprimer);
        setTop(toolBar);

    }


    public void refreshTable(){
        ObservableList<Zone> list = FXCollections.observableArrayList();
        for(int i = 0 ; i < zones.size(); i++){
            list.add(zones.get(i));
        }
        tableView.setItems(list);
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void refreshCellOnGrid(Zone delete){
        System.out.println("Je dois metre a jour toutes les cellules");
        if(delete != null){
            System.out.println("La zone " + delete + " a était supprimé");
        }
    }

}
