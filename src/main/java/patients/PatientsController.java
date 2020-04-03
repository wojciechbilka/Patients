package patients;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
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
    private TextField balanceField;
    @FXML
    private Label logMessage;

    PatientsService patientsService;
    UserLog userLog;
    PatientsValidator patientsValidator;

    private EventHandler<MouseEvent> handleDoubleClick = event -> {
        if (event.getClickCount() == 2) {
            if (event.getSource() instanceof Label) {
                Label label = (Label) event.getSource();
                if (label.getParent() instanceof GridPane) {
                    GridPane gp = (GridPane) label.getParent();
                    for (Node node : gp.getChildren()) {
                        if (node instanceof Label && GridPane.getRowIndex(node).equals(GridPane.getRowIndex(label))) {
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
                                    balanceField.setText(l.getText());
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
        patientsService = new PatientsService(FILE_PATH, SHEET_NAME);
        userLog = new UserLog(logMessage);
        patientsValidator = new PatientsValidator(patientsService, userLog);
        initializeFile();
        updateDisplay();
    }

    @FXML
    public void addPatient() {
        if (allValid() & patientsValidator.validPatientNotExist(personalNumberField.getText())) {
            Patient patient = new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(balanceField));
            patientsService.register(patient);
            updateDisplay();
            clearTextFields();
            userLog.clearMessage();
        }
    }

    @FXML
    public void removePatient() {
        if (allValid() & patientsValidator.validatePatientExist(personalNumberField.getText())) {
            Patient patient = new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(balanceField));
            patientsService.removePatient(patient);
            updateDisplay();
            clearTextFields();
            userLog.clearMessage();
        }
    }

    @FXML
    private void performTest() {
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
            if (node instanceof Label && GridPane.getRowIndex(node).equals(row) && GridPane.getColumnIndex(node).equals(column)) {
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
        balanceField.clear();
    }

    private boolean allValid() {
        boolean nameFieldCorrect = patientsValidator.validateTextFieldConsistsOfLetters(nameField);
        boolean surnameFieldCorrect = patientsValidator.validateTextFieldConsistsOfLetters(surnameField);
        boolean personalNumberCorrect = patientsValidator.validateTextFieldIsAIntegerNumberOfXDigit(personalNumberField, 8);
        boolean walletFieldCorrect = patientsValidator.validateTextFieldIsAnIntegerOr2DecimalNumber(balanceField);
        return nameFieldCorrect && surnameFieldCorrect && personalNumberCorrect && walletFieldCorrect;
    }

    // add filter for only number access
    private Double getTextFieldDoubleValue(TextField textField) {
        double doubleNumber = Double.parseDouble(textField.getText().isEmpty() ?
                "0" :
                textField.getText().replace(',', '.'));
        return doubleNumber;
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
