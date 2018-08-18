package utn.frc.sim.views.generators;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import utn.frc.sim.generators.chicuadrado.Interval;
import utn.frc.sim.generators.chicuadrado.IntervalsCreator;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import utn.frc.sim.generators.chicuadrado.exceptions.IntervalNotDivisibleException;
import utn.frc.sim.util.MathUtils;


import java.util.List;

public class ChiCuadradoTestController {

    private static final String ALERT_NOT_DIVISIBLE = "La cantidad de numeros debe ser divisible por la cantidad de intervalos.";
    private static final String EXPECTED_SERIES_LABEL = "Esperada.";
    private static final String OBSERVED_SERIES_LABEL = "Observada.";
    private static final String X_AXIS_LABEL = "Intervalos.";
    private static final String Y_AXIS_LABEL = "Frecuencia relativa.";
    private static final String TABLE_VIEW_INTERVAL_COLUMN_TITLE = "Intervalo";
    private static final String TABLE_VIEW_EXPECTED_FREQUENCY_COLUMN_LABEL = "FE";
    private static final String TABLE_VIEW_OBSERVED_FREQUENCY_COLUMN_LABEL = "FO";
    private static final String TABLE_VIEW_RESULT_COLUMN_LABEL = "(FE-FO)^2/2";
    private static final String HO_ACEPTED = "ACEPTADA";
    private static final String HO_REJECTED = "RECHAZADA";
    private static final String COMBO_BOX_JAVA_NATIVE = "Nativo (Java)";
    private static final String COMBO_BOX_CONGRUENTIAL = "Congruencial";
    private static final int COMBO_BOX_FIRST_ELEMTENT = 0;
    private static final int SPINNER_INTEGER_MIN_VALUE = 2;
    private static final int SPINNER_INTEGER_MAX_VALUE = Integer.MAX_VALUE;
    private static final int SPINNER_NO_INCREMENT_STEP = 0;

    private static final double SPINNER_DOUBLE_MIN_VALUE = 0.0001;
    private static final double SPINNER_DOUBLE_MAX_VALUE = 1;
    private static final double SPINNER_DOUBLE_INITIAL_VALUE = 0.10;
    private static final double SPINNER_DOUBLE_STEP_VALUE = 0.05;
    public static final int PLACES = 4;


    private TableView<Interval> tblIntervalTable;

    @FXML
    private TitledPane paneTablePanel;

    @FXML
    private Spinner<Integer> spnAmountOfNumbers;

    @FXML
    private Spinner<Integer> spnAmountOfIntervals;

    @FXML
    private Spinner<Double> spnAlpha;

    @FXML
    private BarChart<String, Number> grpGraficoDeFrecuencias;


    @FXML
    private ComboBox<String> cmbGenerador;

    @FXML
    private Label lblSumaDeResultado;

    @FXML
    private Label lblChiEsperado;

    @FXML
    private Label lblResultado;

    public ChiCuadradoTestController() {
    }

    @FXML
    public void initialize() {
        initializeFrequencyBarChartGraph();
        initializeIntervalTableView();
        initializeGeneratorComboBox();
        initializeSpinners();
    }


    private void initializeFrequencyBarChartGraph() {
        grpGraficoDeFrecuencias.setAnimated(false);
        grpGraficoDeFrecuencias.getXAxis().setAnimated(Boolean.FALSE);
        grpGraficoDeFrecuencias.getYAxis().setAnimated(Boolean.FALSE);
        grpGraficoDeFrecuencias.getXAxis().setLabel(X_AXIS_LABEL);
        grpGraficoDeFrecuencias.getYAxis().setLabel(Y_AXIS_LABEL);
    }

    private void initializeIntervalTableView() {
        tblIntervalTable = new TableView<>();

        TableColumn<Interval, String> colIntervalo = new TableColumn<>(TABLE_VIEW_INTERVAL_COLUMN_TITLE);
        colIntervalo.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().displayName()));

        TableColumn<Interval, Integer> colFE = new TableColumn<>(TABLE_VIEW_EXPECTED_FREQUENCY_COLUMN_LABEL);
        colFE.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getExpectedFrequency()).asObject());

        TableColumn<Interval, Integer> colFO = new TableColumn<>(TABLE_VIEW_OBSERVED_FREQUENCY_COLUMN_LABEL);
        colFO.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getObservedFrequency()).asObject());

        TableColumn<Interval, String> colResultado = new TableColumn<>(TABLE_VIEW_RESULT_COLUMN_LABEL);

        colResultado.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDisplayableResult()));

        tblIntervalTable.getColumns().addAll(colIntervalo, colFE, colFO, colResultado);

        paneTablePanel.setContent(tblIntervalTable);
    }

    private void initializeGeneratorComboBox() {
        cmbGenerador.getItems().setAll(FXCollections.observableArrayList(COMBO_BOX_CONGRUENTIAL, COMBO_BOX_JAVA_NATIVE));
        cmbGenerador.getSelectionModel().select(COMBO_BOX_FIRST_ELEMTENT);
    }

    private void initializeSpinners() {
        spnAmountOfIntervals.setValueFactory(getIntegerValueFactory());
        spnAmountOfIntervals.focusedProperty().addListener(getListenerForChangeValue(spnAmountOfIntervals));
        spnAmountOfNumbers.setValueFactory(getIntegerValueFactory());
        spnAmountOfNumbers.focusedProperty().addListener(getListenerForChangeValue(spnAmountOfNumbers));
        spnAlpha.setValueFactory(getDoubleValueFactory());
        spnAlpha.focusedProperty().addListener(getListenerForChangeValue(spnAlpha));

    }

    private SpinnerValueFactory<Integer> getIntegerValueFactory() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE);
    }

    private SpinnerValueFactory<Double> getDoubleValueFactory() {
        return new SpinnerValueFactory.DoubleSpinnerValueFactory(SPINNER_DOUBLE_MIN_VALUE,
                SPINNER_DOUBLE_MAX_VALUE,
                SPINNER_DOUBLE_INITIAL_VALUE,
                SPINNER_DOUBLE_STEP_VALUE);
    }

    private <T> ChangeListener<? super Boolean> getListenerForChangeValue(Spinner<T> spinner) {
        return (observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(SPINNER_NO_INCREMENT_STEP);
            }
        };
    }

    @FXML
    void btnGenerarClick(ActionEvent event) {
        generateIntervalsAndPublishToTableViewAndGraph();
    }

    private void generateIntervalsAndPublishToTableViewAndGraph() {
        try{
            ObservableList<Interval> list = getIntervals();
            setItemsInTableView(list);
            plotIntervalsInGraph(list);
            setResultsLabels(list);
        } catch (IntervalNotDivisibleException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, ALERT_NOT_DIVISIBLE, ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void setItemsInTableView(ObservableList<Interval> listOfIntervals) {
        tblIntervalTable.getItems().setAll(listOfIntervals);
    }

    private void plotIntervalsInGraph(List<Interval> listOfIntervals) {

        XYChart.Series<String, Number> expected = new XYChart.Series<>();
        expected.setName(EXPECTED_SERIES_LABEL);

        XYChart.Series<String, Number> observed = new XYChart.Series<>();
        observed.setName(OBSERVED_SERIES_LABEL);

        for (Interval interval : listOfIntervals) {
            expected.getData().add(new XYChart.Data<>(interval.getPlottableInterval(), interval.getExpectedFrequency()));
            observed.getData().add(new XYChart.Data<>(interval.getPlottableInterval(), interval.getObservedFrequency()));
        }
        grpGraficoDeFrecuencias.getData().clear();
        grpGraficoDeFrecuencias.getData().add(expected);
        grpGraficoDeFrecuencias.getData().add(observed);
    }

    private void setResultsLabels(List<Interval> listOfIntervals) {

        double result = listOfIntervals.stream().mapToDouble(Interval::getResult).sum();
        lblSumaDeResultado.setText(Double.toString(MathUtils.round(result, PLACES)));

        double chisquared = getChiSquaredTableValueFromParameters();
        lblChiEsperado.setText(Double.toString(MathUtils.round(chisquared, PLACES)));

        lblResultado.setText(result < chisquared ? HO_ACEPTED : HO_REJECTED);
    }

    private double getChiSquaredTableValueFromParameters() {
        int degreesOfFreedom = spnAmountOfIntervals.getValue() - 1;
        double alpha = spnAlpha.getValue();
        return new ChiSquaredDistribution(degreesOfFreedom).inverseCumulativeProbability(alpha);

    }

    private ObservableList<Interval> getIntervals() throws IntervalNotDivisibleException {
        IntervalsCreator intervalsCreator = createIntervalCreatorFromParameters();
        return FXCollections.observableArrayList(intervalsCreator.getIntervals());
    }

    private IntervalsCreator createIntervalCreatorFromParameters() throws IntervalNotDivisibleException {
        int amountOfNumbers = spnAmountOfNumbers.getValue();
        int amountOfIntervals = spnAmountOfIntervals.getValue();

        IntervalsCreator.GeneratorType generatorType = getTypeFromComboBox();

        return buildIntervalsCreator(amountOfNumbers, amountOfIntervals, generatorType);

    }

    private IntervalsCreator buildIntervalsCreator(int amountOfNumbers,
                                                   int amountOfIntervals,
                                                   IntervalsCreator.GeneratorType type) throws IntervalNotDivisibleException {

        return new IntervalsCreator(amountOfNumbers, amountOfIntervals, type);
    }

    private IntervalsCreator.GeneratorType getTypeFromComboBox() {
        if (cmbGenerador.getSelectionModel().getSelectedItem().equals(COMBO_BOX_JAVA_NATIVE)) {
            return IntervalsCreator.GeneratorType.JAVA_NATIVE;
        }
        return IntervalsCreator.GeneratorType.CONGRUENTIAL;
    }


}
