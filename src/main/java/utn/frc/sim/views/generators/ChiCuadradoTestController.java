package utn.frc.sim.views.generators;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.chicuadrado.IntervalsCreator;
import utn.frc.sim.generators.javanative.JavaGenerator;

public class ChiCuadradoTestController {


    private RandomGenerator generator;

    @FXML
    private ScrollPane scpTableScroll;

    @FXML
    private BarChart<?, ?> grpGraficoDeFrecuencias;

    @FXML
    private TextField txtCantDeNum;

    @FXML
    private TextField txtIntervalos;

    @FXML
    private Button btnGenerar;

    @FXML
    private ComboBox<?> cmbTipoDePrueba;

    @FXML
    private ComboBox<?> cmbGenerador;



    @FXML
    void btnGenerarClick(ActionEvent event) {
        createTableView();
    }

    private void createTableView(){

        generator = new JavaGenerator();

        TableView table = new TableView();
        table.setEditable(false);


        TableColumn from = new TableColumn();
        from.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn to = new TableColumn();
        from.setCellValueFactory(new PropertyValueFactory<>("to"));



        TableColumn fo = new TableColumn();
        fo.setCellValueFactory(new PropertyValueFactory<>("observedFrequency"));

        TableColumn fe = new TableColumn();
        fe.setCellValueFactory(new PropertyValueFactory<>("expectedFrequency"));


        table.getColumns().setAll(from, to, fo, fe);
        IntervalsCreator intervalsCreator = new IntervalsCreator(200,5, IntervalsCreator.GeneratorType.JAVA_NATIVE);
        table.getItems().addAll(intervalsCreator.getIntervals());
        scpTableScroll.setContent(table);

    }

}
