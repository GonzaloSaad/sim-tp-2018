package utn.frc.sim.views.generators;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.congruential.Congruential;
import utn.frc.sim.generators.congruential.CongruentialGenerator;
import utn.frc.sim.util.MathUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que maneja la logica detras de la vista de generacion de numeros aleatorios.
 */
public class ListGeneratorController {

    private static RandomGenerator generator;
    private static Logger logger = LogManager.getLogger(ListGeneratorController.class);
    private static final int SPINNER_INTEGER_MIN_VALUE = 0;
    private static final int SPINNER_INTEGER_M_MIN_VALUE = 1;
    private static final int SPINNER_INTEGER_DEFAULT_VALUE = 1;
    private static final int SPINNER_INTEGER_MAX_VALUE = Integer.MAX_VALUE;
    private static final int SPINNER_NO_INCREMENT_STEP = 0;
    private static final int DEFAULT_AMOUNT_OF_NUMBERS = 20;
    private static final int DEFAULT_DECIMAL_PLACES = 4;

    @FXML
    private CheckBox chkDefault;

    @FXML
    private ListView<Double> listItems;

    @FXML
    private Spinner<Integer> spnA;

    @FXML
    private Spinner<Integer> spnC;

    @FXML
    private Spinner<Integer> spnM;

    @FXML
    private Spinner<Integer> spnSeed;


    /**
     * Metodo disparado luego de la inicializacion del contenido
     * del FXML a la escena. Se ejecuta una vez ha terminado la
     * carga y todos los componentes han sido instanciados.
     */
    @FXML
    public void initialize(){
        initializeSpinners();
    }

    /**
     * Inicializacion de los elementos Spinner. Se les establece
     * la fabrica que les da rango numero y ademas un listener por un bug
     * en la actualizacion manual que viene de JavaFX.
     */
    private void initializeSpinners() {
        spnA.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnA.focusedProperty().addListener(getListenerForChangeValue(spnA));
        setTextFieldListenerToSpinner(spnA);
        spnC.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnC.focusedProperty().addListener(getListenerForChangeValue(spnC));
        setTextFieldListenerToSpinner(spnC);
        spnM.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_M_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnM.focusedProperty().addListener(getListenerForChangeValue(spnM));
        setTextFieldListenerToSpinner(spnM);
        spnSeed.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnSeed.focusedProperty().addListener(getListenerForChangeValue(spnSeed));
        setTextFieldListenerToSpinner(spnSeed);
    }

    /**
     * Genera una fabrica de valores enteros para darle un limite al spinner.
     */
    private SpinnerValueFactory<Integer> getIntegerValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    /**
     * Creacion del listener de perdida de focus para el bug de JavaFx.
     */
    private <T> ChangeListener<? super Boolean> getListenerForChangeValue(Spinner<T> spinner) {
        return (observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(SPINNER_NO_INCREMENT_STEP);
            }
        };
    }

    /**
     * Metodo que inserta un listener de texto de Texfield
     * a un spinner.
     */
    private void setTextFieldListenerToSpinner(Spinner spinner){
        TextField textField = spinner.getEditor();
        textField.textProperty().addListener(getListenerForText(textField));
    }

    /**
     * Metodo que genera un Listener para el cambio de
     * texto de un TextField.
     */
    private ChangeListener<String> getListenerForText(TextField textField){
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        };
    }

    /**
     * Evento de click en el boton de Agregar.
     */
    @FXML
    void btnAgregarClick(ActionEvent event) {
        try {
            generateNumberAndAddToList();
        } catch (Exception e) {
            logger.error("Error during add.", e);
        }
    }

    /**
     * Evento de click en el boton Generar.
     */
    @FXML
    void btnGenerarClick(ActionEvent event) {
        try {
            generateNumbersAndSetToList();
        } catch (Exception e) {
            logger.error("Error during generation.", e);
        }
    }

    /**
     * Evento de click en el boton de limpiar
     * la lista de numeros generados.
     */
    @FXML
    void btnLimpiarClick(ActionEvent event) {
        clearListView();
    }

    /**
     * Evento que maneja el click para el checkbox
     * de default, que indica si se utiliza el generador
     * por default definido.
     */
    @FXML
    void chkDefaultClick(ActionEvent event) {
        updateStatusOfSpinnersFieldUponChkClick();
    }

    /**
     * Metodo que maneja la logica de generar UN SOLO numero
     * pseudoaleatorio con los parametros especificados
     * y agregarlo a la lista.
     */
    private void generateNumberAndAddToList() {
        addNumberForListView(MathUtils.round(getGenerator().random(), 4));
    }

    /**
     * Metodo que maneja la logica de generun una cantidad
     * default de numeros pseudoaleatorios y agregarlos a
     * la lista.
     */
    private void generateNumbersAndSetToList() {
        createGenerator();
        RandomGenerator generator = getGenerator();

        List<Double> numbers = generator
                .random(DEFAULT_AMOUNT_OF_NUMBERS)
                .stream()
                .map(num -> MathUtils.round(num, DEFAULT_DECIMAL_PLACES))
                .collect(Collectors.toList());

        setListToListView(numbers);
    }

    /**
     * Metodo que toma una lista de numeros y los
     * setea en la lista.
     */
    private void setListToListView(List<Double> listToAdd) {
        ObservableList<Double> items = listItems.getItems();
        items.clear();
        items.addAll(listToAdd);
    }

    /**
     * Metodo que agrega un numero a la lista.
     */
    private void addNumberForListView(Double number) {
        listItems.getItems().add(number);
    }

    /**
     * Metodo que en base a los parametros especificados
     * por el usuario crea un generador.
     * Si el checkbox default esta selecto, lo crea con
     * la configuracion por defecto del generador congruencial.
     */
    private void createGenerator() {
        if (chkDefault.isSelected()) {
            generator = CongruentialGenerator.defaultMixed();
        } else {
            generator = CongruentialGenerator
                    .createOf(getA(),
                            getM(),
                            getC(),
                            getSeed());
        }
    }

    /**
     * Metodo que setea el generador para uso de clase.
     */
    private RandomGenerator getGenerator() {
        if (generator == null) {
            createGenerator();
        }
        return generator;
    }

    /**
     * Metodo que limpia los items de la lista.
     */
    private void clearListView() {
        listItems.getItems().clear();
    }

    /**
     * Metodo que setea los valores por defecto del a los
     * spinners de parametros.
     */
    private void setDefaultValuesToSpinner() {
        setValueToSpinner(spnA, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnC, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnM, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnSeed, SPINNER_INTEGER_DEFAULT_VALUE);
    }

    /**
     * Metodo que setea los valores por defecto DEL GENERADOR
     * a los spinners de parametros.
     * Se usa cuando se tilda el checkbox de default.
     */
    private void setGeneratorDefaultsValuesToSpinners() {
        setValueToSpinner(spnA, Congruential.DEFAULT_A);
        setValueToSpinner(spnC, Congruential.MIXED_CG_DEFAULT_C);
        setValueToSpinner(spnM, Congruential.DEFAULT_M);
        setValueToSpinner(spnSeed, Congruential.DEFAULT_SEED);
    }

    /**
     * Metodo que habilita o deshabilita la edicions de los
     * spinners en base al checkbox de default.
     */
    private void updateStatusOfSpinnersFieldUponChkClick() {
        if (chkDefault.isSelected()) {
            setGeneratorDefaultsValuesToSpinners();
        } else {
            setDefaultValuesToSpinner();
        }
        setStatusOfSpinners(chkDefault.isSelected());
    }

    /**
     * Metodo que setea el estado de todos los spinners.
     */
    private void setStatusOfSpinners(boolean status) {
        spnA.setDisable(status);
        spnC.setDisable(status);
        spnM.setDisable(status);
        spnSeed.setDisable(status);
    }

    /**
     * Metodo que setea un valor a un spinner dado.
     */
    private <T> void setValueToSpinner(Spinner<T> spinner, T value){
        spinner.getValueFactory().setValue(value);
    }
    private int getA() {
        return spnA.getValue();
    }

    private int getC() {
        return spnC.getValue();
    }

    private int getM() {
       return spnM.getValue();

    }

    private int getSeed() {
       return spnSeed.getValue();
    }


}
