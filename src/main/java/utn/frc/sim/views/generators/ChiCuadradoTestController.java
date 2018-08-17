package utn.frc.sim.views.generators;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.chicuadrado.Interval;
import utn.frc.sim.generators.chicuadrado.IntervalsCreator;
public class ChiCuadradoTestController {


    private RandomGenerator generator;

    private TableView<Interval> tblIntervalTable;

    @FXML
    private TitledPane paneTablePanel;


    @FXML
    private BarChart<String, Number> grpGraficoDeFrecuencias;

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

    public ChiCuadradoTestController(){
        initializeGraph();
    }

    @FXML
    void btnGenerarClick(ActionEvent event) {
        generateTableView();
    }



    private void generateTableView(){

        ObservableList<Interval> list = getIntervals();

        getTableView().getItems().clear();
        getTableView().getItems().addAll(list);

        XYChart.Series<String,Number> expected = new XYChart.Series<>();
        expected.setName("Esperada");
        XYChart.Series<String,Number> observed = new XYChart.Series<>();
        observed.setName("Observada");


        for(Interval interval: list){
            expected.getData().add(new XYChart.Data<>(interval.getPlottableInterval(), interval.getExpectedFrequency()));
            observed.getData().add(new XYChart.Data<>(interval.getPlottableInterval(), interval.getObservedFrequency()));
        }
        grpGraficoDeFrecuencias.getData().clear();
        grpGraficoDeFrecuencias.getData().addAll(expected,observed);



    }

    private TableView<Interval> getTableView(){
        if (tblIntervalTable == null){
            tblIntervalTable = new TableView<>();

            TableColumn<Interval,String> colIntervalo = new TableColumn<>("Intervalo");
            colIntervalo.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().displayName()));

            TableColumn<Interval, Integer> colFE = new TableColumn<>("FE");
            colFE.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getExpectedFrequency()).asObject());

            TableColumn<Interval, Integer> colFO = new TableColumn<>("FO");
            colFO.setCellValueFactory(e-> new SimpleIntegerProperty(e.getValue().getObservedFrequency()).asObject());

            TableColumn<Interval, String> colResultado = new TableColumn<>("(FE-FO)^2/2");

            colResultado.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().getDisplayableResult()));

            tblIntervalTable.getColumns().addAll(colIntervalo,colFE,colFO,colResultado);

            paneTablePanel.setContent(tblIntervalTable);
        }
        return tblIntervalTable;
    }

    private ObservableList<Interval> getIntervals(){
        IntervalsCreator intervalsCreator = new IntervalsCreator(200,20, IntervalsCreator.GeneratorType.JAVA_NATIVE);
        return FXCollections.observableArrayList(intervalsCreator.getIntervals());
    }

    private void initializeGraph(){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Intervalos");
        yAxis.setLabel("Frecuencia relativa");
        grpGraficoDeFrecuencias = new BarChart<>(xAxis,yAxis);


    }

}
