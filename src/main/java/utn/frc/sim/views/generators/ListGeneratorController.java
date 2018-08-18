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
    private Button btnAgregar;

    @FXML
    private Spinner<Integer> spnA;

    @FXML
    private Spinner<Integer> spnC;

    @FXML
    private Spinner<Integer> spnM;

    @FXML
    private Spinner<Integer> spnSeed;


    @FXML
    public void initialize(){
        initializeSpinners();
    }

    private void initializeSpinners() {
        spnA.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnA.focusedProperty().addListener(getListenerForChangeValue(spnA));
        spnC.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnC.focusedProperty().addListener(getListenerForChangeValue(spnC));
        spnM.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_M_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnM.focusedProperty().addListener(getListenerForChangeValue(spnM));
        spnSeed.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnSeed.focusedProperty().addListener(getListenerForChangeValue(spnSeed));
    }

    private SpinnerValueFactory<Integer> getIntegerValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    private <T> ChangeListener<? super Boolean> getListenerForChangeValue(Spinner<T> spinner) {
        return (observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(SPINNER_NO_INCREMENT_STEP);
            }
        };
    }

    @FXML
    void btnAgregarClick(ActionEvent event) {
        try {
            generateNumberAndAddToList();
        } catch (Exception e) {
            logger.error("Error during add.", e);
        }
    }

    @FXML
    void btnGenerarClick(ActionEvent event) {
        try {
            generateNumbersAndSetToList();
        } catch (Exception e) {
            logger.error("Error during generation.", e);
        }
    }

    @FXML
    void btnLimpiarClick(ActionEvent event) {
        clearListView();
    }

    @FXML
    void chkDefaultClick(ActionEvent event) {
        updateStatusOfTxtFieldUponChkClick();
    }

    private void generateNumberAndAddToList() {
        addNumberForListView(MathUtils.round(getGenerator().random(), 4));
    }

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

    private void setListToListView(List<Double> listToAdd) {
        ObservableList<Double> items = listItems.getItems();
        items.clear();
        items.addAll(listToAdd);
    }

    private void addNumberForListView(Double number) {
        listItems.getItems().add(number);
    }

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

    private RandomGenerator getGenerator() {
        if (generator == null) {
            createGenerator();
        }
        return generator;
    }

    private void clearListView() {
        listItems.getItems().clear();
    }

    private void setDefaultValuesToSpinner() {
        setValueToSpinner(spnA, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnC, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnM, SPINNER_INTEGER_DEFAULT_VALUE);
        setValueToSpinner(spnSeed, SPINNER_INTEGER_DEFAULT_VALUE);
    }

    private void setGeneratorDefaultsValuesToSpinners() {
        setValueToSpinner(spnA, Congruential.DEFAULT_A);
        setValueToSpinner(spnC, Congruential.MIXED_CG_DEFAULT_C);
        setValueToSpinner(spnM, Congruential.DEFAULT_M);
        setValueToSpinner(spnSeed, Congruential.DEFAULT_SEED);
    }

    private void updateStatusOfTxtFieldUponChkClick() {
        if (chkDefault.isSelected()) {
            setGeneratorDefaultsValuesToSpinners();
        } else {
            setDefaultValuesToSpinner();
        }
        setStatusOfSpinners(chkDefault.isSelected());
    }

    private void setStatusOfSpinners(boolean status) {
        spnA.setDisable(status);
        spnC.setDisable(status);
        spnM.setDisable(status);
        spnSeed.setDisable(status);
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

    private <T> void setValueToSpinner(Spinner<T> spinner, T value){
        spinner.getValueFactory().setValue(value);
    }
}
