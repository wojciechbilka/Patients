package patients;

import com.sun.javafx.css.StyleClassSet;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PatientsController {

    private static final String FILE_PATH = "src/main/resources/PatientsList.xlsx";
    private static final String SHEET_NAME = "patients";

    @FXML
    GridPane grid;
    @FXML
    private Label gridLabel0;
    @FXML
    private Label gridLabel1;
    @FXML
    private Label gridLabel2;
    @FXML
    private Label gridLabel3;
    @FXML
    private Label gridLabel4;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField personalNumberField;
    @FXML
    private TextField walletField;
    @FXML
    private Label logMessage;

    PatientsService patientsService = new PatientsService(FILE_PATH, SHEET_NAME);
    double testPrice = 200;

    private EventHandler<MouseEvent> handleDoubleClick = event -> {
        if (event.getClickCount() == 2) {
            if (event.getSource() instanceof Label) {
                Label label = (Label) event.getSource();
                if (label.getParent() instanceof GridPane) {
                    GridPane gp = (GridPane) label.getParent();
                    for (Node node : gp.getChildren()) {
                        if (node instanceof Label && GridPane.getRowIndex(node) == GridPane.getRowIndex(label)) {
                            Label l = (Label) node;
                            switch (GridPane.getColumnIndex(l)) {
                                case 1:
                                    nameField.setText(l.getText());
                                    break;
                                case 2:
                                    surnameField.setText(l.getText());
                                    break;
                                case 3:
                                    personalNumberField.setText(l.getText());
                                    break;
                                case 4:
                                    walletField.setText(l.getText());
                                    break;
                            }

                        }
                    }
                }
            }
        }
    };

    @FXML
    private void initialize() {
        initializeFile();
        updateDisplay();
    }

    @FXML
    public void addPatient() {
        Patient patient = new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(walletField));
        if (allValid() & validPatientNotExist(patient)) {
            patientsService.register(patient);
            updateDisplay();
            clearTextFields();
            clearLogMessage();
        }
    }

    @FXML
    public void removePatient() {
        Patient patient = new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(walletField));
        if (allValid() & validPatientExist(patient)) {
            patientsService.removePatient(patient);
            updateDisplay();
            clearTextFields();
            clearLogMessage();
        } //Brak porównania wszystkich pól
    }

    @FXML
    private void performTest() {
        TestResult testResult = TestResult.NOT_PERFORMED;
        Patient patient = new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(walletField));
        if (patientsService.patientExist(patient.getPesel())) {
            if (patient.getWallet() > testPrice) {
                patient.payForTest(testPrice);
                testResult = TestResult.NEGATIVE; // zmienic potem zeby byl random
            } else {
                testResult = TestResult.NOT_PERFORMED;
            }
            markPatientRow(patient, testResult);
        }
    }

    private void markPatientRow(Patient patient, TestResult testResult) {
        ObservableList<Node> children = grid.getChildren();
        int rowIndex = -1;
        for (Node node : children) {
            if (node instanceof Label && node.getAccessibleText().equals(patient.getPesel())) {
                rowIndex = GridPane.getRowIndex(node);
            }
        }
        if (rowIndex != -1) {
            for (Node node : children) {
                if ((node instanceof Label) && GridPane.getRowIndex(node) == rowIndex) {
                    Label temp = (Label) node;
                    temp.styleProperty().setValue("error");
                    break;
                }
            }
        }
    }

    private void updateDisplay() {
        List<Patient> patients = patientsService.getPatients();
        System.out.println(patients);
        int colNum = grid.getColumnConstraints().size();
        int rowNum = grid.getRowConstraints().size();
        grid.getChildren().remove(5, grid.getChildren().size());
        for (int r = 1; r < rowNum; r++) {
            for (int c = 0; c <= colNum; c++) {
                removeNodeByRowColumnIndex(r, c, grid);
                System.out.println("row: " + r + "column: " + c);
            }
        }
        int i = 1;
        for (Patient patient : patients) {
            Label tempName = new Label(patient.getName());
            Label tempSurname = new Label(patient.getSurname());
            Label tempPersonalNumber = new Label(patient.getPesel());
            Label tempWallet = new Label(String.format("%.2f", patient.getWallet()).replace(',', '.'));

            tempName.setOnMouseClicked(handleDoubleClick);
            tempSurname.setOnMouseClicked(handleDoubleClick);
            tempPersonalNumber.setOnMouseClicked(handleDoubleClick);
            tempWallet.setOnMouseClicked(handleDoubleClick);

            grid.addRow(i, new Label(String.valueOf(i)));
            grid.addRow(i, tempName);
            grid.addRow(i, tempSurname);
            grid.addRow(i, tempPersonalNumber);
            grid.addRow(i, tempWallet);
            i++;
        }
    }

    public void removeNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (node instanceof Label && gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                Label label = (Label) node;
                gridPane.getChildren().remove(label);
                break;
            }
        }
    }

    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            PatientsWriter.creatingFile(FILE_PATH, SHEET_NAME);
        }
    }

    private void clearTextFields() {
        nameField.clear();
        surnameField.clear();
        personalNumberField.clear();
        walletField.clear();
    }

    private static boolean validate(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText().trim().length() == 0) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
            return false;
        }
        styleClass.removeAll(Collections.singleton("error"));
        return true;
    }

    private static boolean validateNumber(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText().trim().length() == 0 || !tf.getText().matches("([0-9]*)([.,][0-9]{2})?")) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
            return false;
        }
        styleClass.removeAll(Collections.singleton("error"));
        return true;
    }

    private static boolean validatePersonalNumber(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (!tf.getText().matches("[0-9]{8}")) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
            return false;
        }
        styleClass.removeAll(Collections.singleton("error"));
        return true;
    }

    private boolean allValid() {
        boolean nameFieldCorrect = validate(nameField);
        boolean surnameFieldCorrect = validate(surnameField);
        boolean personalNumberCorrect = validatePersonalNumber(personalNumberField);
        boolean walletFieldCorrect = validateNumber(walletField);
        boolean valid = nameFieldCorrect && surnameFieldCorrect && personalNumberCorrect && walletFieldCorrect;
        if (!valid) {
            logMessage.setVisible(true);
            if(!logMessage.getText().contains("Field incorrectly filled or empty.")) {
                logMessage.setText(logMessage.getText().isEmpty() ?
                        logMessage.getText() + "Field incorrectly filled or empty." :
                        logMessage.getText() + "\nField incorrectly filled or empty.");
            }
            ObservableList<String> styleClass = logMessage.getStyleClass();
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        return nameFieldCorrect && surnameFieldCorrect && personalNumberCorrect && walletFieldCorrect;
    }

    private boolean validPatientExist(Patient patient) {
        boolean patientExist = patientsService.patientExist(personalNumberField.getText());
        if (!patientExist) {
            logMessage.setVisible(true);
            if(!logMessage.getText().contains("Patient does not exist on the list.")) {
                logMessage.setText(logMessage.getText().isEmpty() ?
                        logMessage.getText() + "Patient does not exist on the list." :
                        logMessage.getText() + "\nPatient does not exist on the list.");
            }
            ObservableList<String> styleClass = logMessage.getStyleClass();
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        return patientExist;
    }

    private boolean validPatientNotExist(Patient patient) {
        boolean patientExist = patientsService.patientExist(personalNumberField.getText());
        if (patientExist) {
            logMessage.setVisible(true);
            if (!logMessage.getText().contains("Patient already exist on the list.")) {
                logMessage.setText(logMessage.getText().isEmpty() ?
                        logMessage.getText() + "Patient already exist on the list." :
                        logMessage.getText() + "\nPatient already exist on the list.");
            }
            ObservableList<String> styleClass = logMessage.getStyleClass();
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        return !patientExist;
    }

    private void clearLogMessage() {
        logMessage.setText("");
        ObservableList<String> styleClass = logMessage.getStyleClass();
        if (styleClass.contains("error")) {
            styleClass.remove("error");
        }
        logMessage.setVisible(false);
    }

    private Double getTextFieldDoubleValue(TextField textField) {
        Double doubleNumber = Double.valueOf(textField.getText());
        return textField.getText().isEmpty() ? Double.valueOf(0) : Double.valueOf(doubleNumber);
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
