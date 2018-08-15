package utn.frc.sim.views.generators;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.congruential.Congruential;
import utn.frc.sim.generators.congruential.CongruentialGenerator;
import utn.frc.sim.util.MathUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ListGeneratorController {

    private static RandomGenerator generator;
    private static Logger logger = LogManager.getLogger(ListGeneratorController.class);

    @FXML
    private CheckBox chkDefault;

    @FXML
    private ListView<Double> listItems;

    @FXML
    private Button btnAgregar;

    @FXML
    private TextField txtA;

    @FXML
    private TextField txtC;

    @FXML
    private TextField txtM;

    @FXML
    private TextField txtSeed;

    @FXML
    private Button btnGenerar;


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
    void btnResetClick(ActionEvent event) {
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
                .random(20)
                .stream()
                .map(num -> MathUtils.round(num, 4))
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

    private void clearTxts() {
        txtA.setText(Strings.EMPTY);
        txtC.setText(Strings.EMPTY);
        txtM.setText(Strings.EMPTY);
        txtSeed.setText(Strings.EMPTY);
    }

    private void defaultTxts() {
        setDefaultValueToTxtA();
        setDefaultValueToTxtC();
        setDefaultValueToTxtM();
        setDefaultValueToTxtSeed();
    }

    private void updateStatusOfTxtFieldUponChkClick() {
        if (chkDefault.isSelected()) {
            defaultTxts();
        } else {
            clearTxts();
        }
        setStatusOfTxtFields(chkDefault.isSelected());
    }

    private void setStatusOfTxtFields(boolean status) {
        txtA.setDisable(status);
        txtC.setDisable(status);
        txtM.setDisable(status);
        txtSeed.setDisable(status);
    }

    private int getA() {
        try {
            return Integer.parseInt(txtA.getText());
        } catch (Exception e) {
            logger.error("Error getting A. Returning default.");
            setDefaultValueToTxtA();
            return Congruential.DEFAULT_A;
        }
    }

    private int getC() {
        try {
            return Integer.parseInt(txtC.getText());
        } catch (Exception e) {
            logger.error("Error getting C. Returning mixed default.");
            setDefaultValueToTxtC();
            return Congruential.MIXED_CG_DEFAULT_C;
        }
    }

    private int getM() {
        try {
            return Integer.parseInt(txtM.getText());
        } catch (Exception e) {
            logger.error("Error getting M. Returning default.");
            setDefaultValueToTxtM();
            return Congruential.DEFAULT_M;
        }

    }

    private int getSeed() {
        try {
            return Integer.parseInt(txtSeed.getText());
        } catch (Exception e) {
            logger.error("Error getting seed. Returning default.");
            setDefaultValueToTxtSeed();
            return Congruential.DEFAULT_SEED;
        }
    }

    private void setDefaultValueToTxtA() {
        txtA.setText(Integer.toString(Congruential.DEFAULT_A));
    }

    private void setDefaultValueToTxtC() {
        txtC.setText(Integer.toString(Congruential.MIXED_CG_DEFAULT_C));
    }

    private void setDefaultValueToTxtM() {
        txtM.setText(Integer.toString(Congruential.DEFAULT_M));
    }

    private void setDefaultValueToTxtSeed() {
        txtSeed.setText(Integer.toString(Congruential.DEFAULT_SEED));
    }
}
