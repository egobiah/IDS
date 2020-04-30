package FX;
import Class.*;

import Manager.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


import java.util.ArrayList;


public class ZoneManager extends BorderPane {
    private TableView<Zone> tableView;
    private ArrayList<Zone> zones;
    private EventHandler<ActionEvent> eventColorPicker;
    private Zone currentZone;
    private Grid grid;
    private Zone initialZone;
    private Button launch;

    private Manager manager;
    ZoneManager(Grid grid, Manager manager) {
        super();

        this.manager = manager;
        this.grid = grid;
        tableView = new TableView<Zone>();
        tableView.setEditable(true);

        tableView.setOnMousePressed(event -> {
            System.out.println("Selection de la table view");

            currentZone = zones.get(tableView.getFocusModel().getFocusedIndex());
            grid.setCurrentZone(currentZone);
            System.out.println(currentZone);
        });

        TableColumn<Zone, String> zoneName = new TableColumn<Zone, String>("Zone");
        zoneName.setCellValueFactory(new PropertyValueFactory<>("nomZone"));

        zoneName.setCellFactory(TextFieldTableCell.<Zone>forTableColumn());
        zoneName.setOnEditCommit((TableColumn.CellEditEvent<Zone, String> event) -> {
            TablePosition<Zone, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            System.out.println("Modification en : " + newName);
            tableView.getItems().get(event.getTablePosition().getRow()).setNomZone(newName);
            refreshCellOnGrid(null);
        });

        TableColumn<Zone, ColorPicker> cCol = new TableColumn<Zone, ColorPicker> ("Couleur");
        cCol.setCellValueFactory(new PropertyValueFactory<>("colorPicker"));
        cCol.setMaxWidth(80);

        TableColumn<Zone, String> zoneIP = new TableColumn<Zone, String>("Adresse IP");
        zoneIP.setCellValueFactory(new PropertyValueFactory<>("ip"));

        zoneIP.setCellFactory(TextFieldTableCell.<Zone>forTableColumn());
        zoneIP.setOnEditCommit((TableColumn.CellEditEvent<Zone, String> event) -> {
            //TablePosition<Zone, String> pos = event.getTablePosition();
            String newName = event.getNewValue();
            System.out.println("Modification en : " + newName);
            tableView.getItems().get(event.getTablePosition().getRow()).setIp(newName);
        });

        TableColumn<Zone, String> zonePort = new TableColumn<Zone, String>("port");
        zonePort.setCellValueFactory(new PropertyValueFactory<>("port"));

        zonePort.setCellFactory(TextFieldTableCell.<Zone>forTableColumn());
        zonePort.setOnEditCommit((TableColumn.CellEditEvent<Zone, String> event) -> {
            //TablePosition<Zone, String> pos = event.getTablePosition();

            String newName = event.getNewValue();
            System.out.println("Modification en : " + newName);
            tableView.getItems().get(event.getTablePosition().getRow()).setPort(newName);



        });



        tableView.getColumns().addAll(zoneName,cCol, zoneIP,zonePort);

        eventColorPicker = e -> {
            System.out.println("Nouvelle couleur : " + ((ColorPicker) e.getTarget()).getValue());
            refreshCellOnGrid(null);
        };
        initialZone = new Zone("Init", Color.WHITE);
        initialZone.setEventColorPicker(eventColorPicker);
        zones = new ArrayList<Zone>();
        currentZone = new Zone("ZoneA", Color.BLUE);
        currentZone.setEventColorPicker(eventColorPicker);
        zones.add(currentZone);
        currentZone = new Zone("ZoneB", Color.YELLOWGREEN);
        currentZone.setEventColorPicker(eventColorPicker);
        zones.add(currentZone);

        refreshTable();
        setCenter(tableView);

        Button ajouter = new Button("+");
        Button supprimer = new Button("-");
        Button print = new Button("Imprimer zone");
        launch = new Button("Initialisation");
        ToolBar toolBar = new ToolBar(ajouter, supprimer, print, launch);

        EventHandler<ActionEvent> eventAjouter = e -> {
            currentZone = new Zone("Nouvelle Zone", Color.color(Math.random(), Math.random(), Math.random()));
            currentZone.setEventColorPicker(eventColorPicker);
            zones.add(currentZone);

            refreshTable();

        };

        EventHandler<ActionEvent> eventSupprimer = e -> {
            Zone aDelete = zones.get(tableView.getFocusModel().getFocusedIndex());
            zones.remove(aDelete);
            try {
                currentZone = zones.get(0);
            } catch (IndexOutOfBoundsException i) {
                currentZone = initialZone;
            }
            grid.setCurrentZone(currentZone);
            refreshTable();
            refreshCellOnGrid(aDelete);

        };

        EventHandler<ActionEvent> eventImprimer = e -> {
            for (int i = 0; i < grid.getX(); i++) {
                for (int j = 0; j < grid.getX(); j++) {
                    System.out.println(" Case : " + grid.getCell(i, j));
                }
            }

        };

        ajouter.setOnAction(eventAjouter);
        supprimer.setOnAction(eventSupprimer);
        print.setOnAction(eventImprimer);

        setTop(toolBar);
        for(int i = 0; i < grid.getX(); i ++){
            for(int j = 0; j < grid.getX(); j ++){
                Case c = grid.getCell(i,j);
                c.setZ(initialZone);
            }
        }
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
        for(int i = 0; i < grid.getX(); i ++){
            for(int j = 0; j < grid.getX(); j ++){
                Case c = grid.getCell(i,j);
                if(c.getZ() == delete){
                    System.out.println("Il faut supprimer la zone pour " + c.getPoint());
                    c.setZ(initialZone);
                   // c.removeZone();
                } else {
                    for(Zone z : zones){
                        if(c.getZ().getId() == z.getId() ){
                            c.setZ(z);
                        }
                    }
                }
            }
        }
    }

    protected Button getLaunchButton(){
        return launch;
    }

    /**
     * Button start trigger
     * build the map and launch manager
     */
    void eventLaunch(){
        System.out.print("Build map");
        setTop(null);
        //toolBar = new ToolBar(print);
        //setTop(toolBar);
        grid.rmHandlerSelection();
        //zones.add(initialZone);


        ArrayList<Zone> finalZone = new ArrayList();
        Zone z = null;
        for(int i = 0; i < grid.getX(); i++){
            for(int j = 0; j < grid.getY(); j++){
                int index = finalZone.indexOf(grid.cases[i][j].getZ());
                if( index == -1){
                    z = grid.cases[i][j].getZ();
                    z.addCell(new PositionGrille(i, j));
                } else {
                    finalZone.add(z);
                    finalZone.get(index).addCell(new PositionGrille(i, j));
                }
            }
            System.out.print(".");
        }

        System.out.println("");

        this.zones = finalZone;
        this.manager.setMap(this.zones);

        for(Zone zone : finalZone){
            System.out.println(zone.toString());
        }

        try {
            this.manager.run();
        } catch (Exception e){
            System.out.println("Manager error while starting : " + e.toString());
            System.exit(-1);
        }
        refreshTable();
    }

}
