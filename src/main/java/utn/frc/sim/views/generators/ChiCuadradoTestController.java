package utn.frc.sim.views.generators;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.chicuadrado.Interval;
import utn.frc.sim.generators.chicuadrado.IntervalsCreator;
import utn.frc.sim.generators.chicuadrado.exceptions.IntervalNotDivisibleException;
import utn.frc.sim.generators.congruential.CongruentialGenerator;
import utn.frc.sim.generators.javanative.JavaGenerator;
import utn.frc.sim.util.MathUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Clase que maneja la logica de la vista de test de chicuadrado.
 */
public class ChiCuadradoTestController {

    private static final String ALERT_NOT_DIVISIBLE = "La cantidad de numeros debe ser divisible por la cantidad de intervalos.";
    private static final String ALERT_ERROR = "Ocurrio un error";
    private static final String EXPECTED_SERIES_LABEL = "Esperada.";
    private static final String OBSERVED_SERIES_LABEL = "Observada.";
    private static final String X_AXIS_LABEL = "Intervalos.";
    private static final String Y_AXIS_LABEL = "Frecuencia relativa.";
    private static final String TABLE_VIEW_INTERVAL_COLUMN_TITLE = "Intervalo";
    private static final String TABLE_VIEW_EXPECTED_FREQUENCY_COLUMN_LABEL = "FE";
    private static final String TABLE_VIEW_OBSERVED_FREQUENCY_COLUMN_LABEL = "FO";
    private static final String TABLE_VIEW_RESULT_COLUMN_LABEL = "(FE-FO)^2/FE";
    private static final String HO_ACCEPTED = "NO RECHAZADA";
    private static final String HO_REJECTED = "RECHAZADA";
    private static final String COMBO_BOX_JAVA_NATIVE = "Nativo (Java)";
    private static final String COMBO_BOX_CONGRUENTIAL = "Congruencial";
    private static final int COMBO_BOX_FIRST_ELEMTENT = 0;
    private static final int SPINNER_INTEGER_MIN_VALUE = 2;
    private static final int SPINNER_INTEGER_MAX_VALUE = Integer.MAX_VALUE;
    private static final int SPINNER_NO_INCREMENT_STEP = 0;

    private static final double SPINNER_DOUBLE_MIN_VALUE = 0.01;
    private static final double SPINNER_DOUBLE_MAX_VALUE = 1;
    private static final double SPINNER_DOUBLE_INITIAL_VALUE = 0.01;
    private static final double SPINNER_DOUBLE_STEP_VALUE = 0.01;
    private static final int PLACES = 4;


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

    /**
     * Metodo que se ejectua luego de la inicializacion de los
     * componentes FXML.
     */
    @FXML
    public void initialize() {
        initializeFrequencyBarChartGraph();
        initializeIntervalTableView();
        initializeGeneratorComboBox();
        initializeSpinners();
    }

    /**
     * Metodo que inicializa el grafico con las caracteristicas
     * que se necesitan. Se le quitan las animaciones.
     */
    private void initializeFrequencyBarChartGraph() {
        grpGraficoDeFrecuencias.setAnimated(Boolean.FALSE);
        grpGraficoDeFrecuencias.getXAxis().setAnimated(Boolean.FALSE);
        grpGraficoDeFrecuencias.getYAxis().setAnimated(Boolean.FALSE);
        grpGraficoDeFrecuencias.getXAxis().setLabel(X_AXIS_LABEL);
        grpGraficoDeFrecuencias.getYAxis().setLabel(Y_AXIS_LABEL);
    }

    /**
     * Metodo que inicializa la tabla. Se generan las properties de las
     * cuales escucha cada columna para obtener su valor de la clase Interval.
     */
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

    /**
     * Metodo que inicializa los el combobox de tipo de generador.
     * Se selecciona el generador congruencial como defecto.
     */
    private void initializeGeneratorComboBox() {
        cmbGenerador.getItems().setAll(FXCollections.observableArrayList(COMBO_BOX_CONGRUENTIAL, COMBO_BOX_JAVA_NATIVE));
        cmbGenerador.getSelectionModel().select(COMBO_BOX_FIRST_ELEMTENT);
    }

    /**
     * Metodo inicializador de los spinners de cantidad de intervalos, numeros y alpha.
     */
    private void initializeSpinners() {
        spnAmountOfIntervals.setValueFactory(getIntegerValueFactory());
        spnAmountOfIntervals.focusedProperty().addListener(getListenerForChangeFocus(spnAmountOfIntervals));
        setTextFieldListenerToSpinner(spnAmountOfIntervals);
        spnAmountOfNumbers.setValueFactory(getIntegerValueFactory());
        spnAmountOfNumbers.focusedProperty().addListener(getListenerForChangeFocus(spnAmountOfNumbers));
        setTextFieldListenerToSpinner(spnAmountOfNumbers);
        spnAlpha.setValueFactory(getDoubleValueFactory());
    }

    /**
     * Metodo que contruye fabrica de valores enteros para los spinners.
     */
    private SpinnerValueFactory<Integer> getIntegerValueFactory() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_INTEGER_MIN_VALUE,
                SPINNER_INTEGER_MAX_VALUE);
    }

    /**
     * Metodo que contruye fabrica de valores para decimales.
     */
    private SpinnerValueFactory<Double> getDoubleValueFactory() {
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(SPINNER_DOUBLE_MIN_VALUE,
                SPINNER_DOUBLE_MAX_VALUE,
                SPINNER_DOUBLE_INITIAL_VALUE,
                SPINNER_DOUBLE_STEP_VALUE);

        factory.setConverter(getStringDoubleConverter());
        return factory;
    }

    /**
     * Metodo que crea un convertidor de double a string con
     * un formato #.##
     * Sirve para customizar la cantidad de decimales del spinner
     */
    private StringConverter<Double> getStringDoubleConverter() {
        return new StringConverter<Double>() {
            private final DecimalFormat df = new DecimalFormat("#0.00");

            @Override
            public String toString(Double value) {
                if (value == null) {
                    return "";
                }

                return df.format(value);
            }

            @Override
            public Double fromString(String value) {
                try {
                    if (value == null) {
                        return SPINNER_DOUBLE_INITIAL_VALUE;
                    }

                    value = value.trim();

                    if (value.length() < 1) {
                        return SPINNER_DOUBLE_INITIAL_VALUE;
                    }

                    return df.parse(value).doubleValue();
                } catch (ParseException ex) {
                    return SPINNER_DOUBLE_INITIAL_VALUE;
                }
            }
        };
    }

    /**
     * Metodo que inserta un listener de texto de Texfield
     * a un spinner.
     */
    private void setTextFieldListenerToSpinner(Spinner spinner) {
        TextField textField = spinner.getEditor();
        textField.textProperty().addListener(getListenerForText(textField));
    }

    /**
     * Metodo que genera un Listener para el cambio de
     * texto de un TextField.
     */
    private ChangeListener<String> getListenerForText(TextField textField) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        };
    }

    /**
     * Metodo que genera un listener para perdida de focus, que se usa
     * para compensar el bug de JavaFX en setear el valor al spinner cuando
     * es editado.
     */
    private <T> ChangeListener<? super Boolean> getListenerForChangeFocus(Spinner<T> spinner) {
        return (observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(SPINNER_NO_INCREMENT_STEP);
            }
        };
    }

    /**
     * Evento de click sobre el boton generar. Si ocurre una exception
     * se muestra un alert con lo ocurrido.
     */
    @FXML
    void btnGenerarClick(ActionEvent event) {
        try {
            generateIntervalsAndPublishToTableViewAndGraph();
        } catch (IntervalNotDivisibleException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, ALERT_NOT_DIVISIBLE, ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, ALERT_ERROR, ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Metodo que genera los intervalos y los inserta en la tabla,
     * el grafico y muestra los resultados.
     */
    private void generateIntervalsAndPublishToTableViewAndGraph() throws IntervalNotDivisibleException {
        ObservableList<Interval> list = getIntervals();
        setItemsInTableView(list);
        plotIntervalsInGraph(list);
        setResultsLabels(list);
    }

    /**
     * Metodo que toma una lista de intervalos y los setea en la tabla.
     */
    private void setItemsInTableView(ObservableList<Interval> listOfIntervals) {
        tblIntervalTable.getItems().setAll(listOfIntervals);
    }

    /**
     * Metodo que toma una lista de intervalos y las plotea en el grafico.
     */
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

    /**
     * Metodo que toma una lista de intervalos y elabora el resultado
     * de la distribucion.
     */
    private void setResultsLabels(List<Interval> listOfIntervals) {

        double result = listOfIntervals.stream().mapToDouble(Interval::getResult).sum();
        lblSumaDeResultado.setText(Double.toString(MathUtils.round(result, PLACES)));

        double chisquared = getChiSquaredTableValueFromParameters();
        lblChiEsperado.setText(Double.toString(MathUtils.round(chisquared, PLACES)));

        lblResultado.setText(result < chisquared ? HO_ACCEPTED : HO_REJECTED);
    }

    /**
     * Metodo que obtiene el correspondiente valor de la tabla de
     * chi cuadrado en funcion de los parametros ingresados.
     */
    private double getChiSquaredTableValueFromParameters() {
        int degreesOfFreedom = spnAmountOfIntervals.getValue() - 1;
        double alpha = spnAlpha.getValue();
        return new ChiSquaredDistribution(degreesOfFreedom).inverseCumulativeProbability(1 - alpha);

    }

    /**
     * Metodo que genera los intervalos y los retorna como una lista
     * observable para los elementos de JavaFx.
     */
    private ObservableList<Interval> getIntervals() throws IntervalNotDivisibleException {
        IntervalsCreator intervalsCreator = createIntervalCreatorFromParameters();
        return FXCollections.observableArrayList(intervalsCreator.getIntervals());
    }

    /**
     * Metodo que en funcion de los parametros ingresados, instancia un IntervalsCreator,
     * el cual crea una serie de intervalos con una distribucion uniforme.
     */
    private IntervalsCreator createIntervalCreatorFromParameters() throws IntervalNotDivisibleException {
        int amountOfNumbers = spnAmountOfNumbers.getValue();
        int amountOfIntervals = spnAmountOfIntervals.getValue();

        RandomGenerator generator = getGenerator();

        return IntervalsCreator.createFor(amountOfNumbers, amountOfIntervals, generator);

    }

    /**
     * Metodo que transforma lo seleccionado en el combobox de tipo
     * de generador en el Generador correspondiente.
     */
    private RandomGenerator getGenerator() {
        if (cmbGenerador.getSelectionModel().getSelectedItem().equals(COMBO_BOX_JAVA_NATIVE)) {
            return JavaGenerator.defaultJava();
        }
        return CongruentialGenerator.defaultMixed();
    }
}
