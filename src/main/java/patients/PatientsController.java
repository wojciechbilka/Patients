package patients;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.List;

public class PatientsController {

    private static final String FILE_PATH = "src/main/resources/PatientsList.xlsx";
    private static final String SHEET_NAME = "patients";

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField personalNumberField;
    @FXML
    private TextField balanceField;
    @FXML
    private TextField testCostField;
    @FXML
    private Label logMessage;
    @FXML
    private TableView<Patient> tableView;
    @FXML
    private ChoiceBox<TestResult> testResultChoiceBox;

    private PatientsService patientsService;
    private UserLog userLog;
    private PatientsValidator patientsValidator;
    private ObservableList<Patient> patients;

    @FXML
    private void initialize() {
        patientsService = new PatientsService(FILE_PATH, SHEET_NAME);
        userLog = new UserLog(logMessage);
        patientsValidator = new PatientsValidator(userLog);
        setRowDoubleClickHandler();
        initializeFile();
        initializeTableView(patientsService.getPatients());
        patients = tableView.getItems();
    }

    @FXML
    public void addPatient() {
        if (allValid()) {
            Patient patient = generateNewPatientFromFields();
            if (patientsValidator.validatePatientNotExist(patients, patient)) {
                patientsService.register(patient);
                patients.add(patient);
                clearTextFields();
                userLog.clearMessage();
            }
        }
    }

    @FXML
    public void removePatient() {
        if (allValid()) {
            Patient patient = getPatientAccordingToFields();
            if (patientsValidator.validatePatientExist(patients, patient)) {
                patientsService.removePatient(patient);
                patients.remove(patient);
                clearTextFields();
                userLog.clearMessage();
            }
        }
    }

    @FXML
    public void updatePatientData() {
        if (allValid()) {
            Patient patient = tableView.getSelectionModel().getSelectedItem();
            if (patientsValidator.validatePatientExist(patients, patient)) {
                Patient updatedPatient = generateNewPatientFromFields();
                boolean personalNumberEquals = patient.getPersonalNumber().equals(updatedPatient.getPersonalNumber());
                if (personalNumberEquals || patientsValidator.validatePatientsPersonalNumberIsUnique(patients, updatedPatient.getPersonalNumber())) {
                    int selectionIndex = tableView.getSelectionModel().getSelectedIndex();
                    patients.set(patients.indexOf(patient), updatedPatient);
                    patientsService.updatePatientData(patient, updatedPatient);
                    tableView.getSelectionModel().select(selectionIndex);
                }
            }
        }
    }

    @FXML
    private void performTest() {
        if (allValid()) {
            Patient patient = getPatientAccordingToFields();
            if (patientsValidator.validateTextFieldIsAnIntegerOr2DecimalNumber(testCostField) & patientsValidator.validatePatientExist(patients, patient)) {
                Patient patientClone = patient.clone();
                double cost = getTextFieldDoubleValue(testCostField);
                if (patientsValidator.validatePatientIsSolvent(patient, cost)) {
                    patient.performTest(cost);
                    patientsService.updatePatientData(patientClone, patient);
                    clearTextFields();
                    userLog.clearMessage();
                }
            }
        }
    }

    @FXML
    private void clearTextFields() {
        nameField.clear();
        surnameField.clear();
        personalNumberField.clear();
        balanceField.clear();
        testResultChoiceBox.getSelectionModel().clearSelection();
    }

    private Patient generateNewPatientFromFields() {
        TestResult testResult = testResultChoiceBox.getSelectionModel().getSelectedItem();
        if (testResult == null) {
            testResult = TestResult.NOT_PERFORMED;
        }
        return new Patient(nameField.getText(), surnameField.getText(), personalNumberField.getText(), getTextFieldDoubleValue(balanceField), testResult);
    }

    private Patient getPatientAccordingToFields() {
        for (Patient p : patients) {
            if (nameField.getText().equals(p.getName()) &&
                    surnameField.getText().equals(p.getSurname()) &&
                    personalNumberField.getText().equals(p.getPersonalNumber()) &&
                    testResultChoiceBox.getSelectionModel().getSelectedItem().equals(p.getTestResult()) &&
                    balanceField.getText().equals(String.format("%.2f", p.getBalance()).replace(',', '.'))
            ) {
                return p;
            }
        }
        return null;
    }

    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            PatientsWriter.creatingFile(FILE_PATH, SHEET_NAME);
        }
    }

    private void initializeTableView(List<Patient> patients) {
        for (Patient p : patients) {
            tableView.getItems().add(p);
        }
    }

    private boolean allValid() {
        boolean nameFieldCorrect = patientsValidator.validateTextFieldConsistsOfLetters(nameField);
        boolean surnameFieldCorrect = patientsValidator.validateTextFieldConsistsOfLetters(surnameField);
        boolean personalNumberCorrect = patientsValidator.validateTextFieldIsAIntegerNumberOfXDigit(personalNumberField, 8);
        boolean balanceFieldCorrect = patientsValidator.validateTextFieldIsAnIntegerOr2DecimalNumber(balanceField);
        return nameFieldCorrect && surnameFieldCorrect && personalNumberCorrect && balanceFieldCorrect;
    }

    private Double getTextFieldDoubleValue(TextField textField) {
        return Double.parseDouble(
                textField.getText().isEmpty() ?
                        "0" :
                        textField.getText().replace(',', '.')
        );
    }

    private void setRowDoubleClickHandler() {
        tableView.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Patient patient = row.getItem();
                    nameField.setText(patient.getName());
                    surnameField.setText(patient.getSurname());
                    personalNumberField.setText(patient.getPersonalNumber());
                    testResultChoiceBox.getSelectionModel().select(patient.getTestResult());
                    balanceField.setText(String.format("%.2f", patient.getBalance()).replace(',', '.'));
                }
            });
            return row;
        });
    }
}
