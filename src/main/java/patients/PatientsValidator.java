package patients;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientsValidator {
    private UserLog userLog;

    public boolean validateTextFieldNotEmpty(TextField tf) {
        boolean result = !tf.getText().isEmpty();
        userLog.textFieldEmptyPrompt(tf, result);
        setTextFieldBorderAlert(tf, result);
        return result;
    }

    public boolean validateTextFieldConsistsOfLetters(TextField tf) {
        boolean result = tf.getText().matches("^[a-zA-z]+");
        if (validateTextFieldNotEmpty(tf)) {
            result = tf.getText().matches("^[a-zA-z]+");
            userLog.textFieldIsNotAWordPrompt(tf, result);
        }
        setTextFieldBorderAlert(tf, result);
        return result;
    }

    public boolean validateTextFieldIsAIntegerNumberOfXDigit(TextField tf, int digitNumber) {
        boolean result = tf.getText().matches("[0-9]{" + digitNumber + "}");
        userLog.textFiledMustContainsNumberOfXDigitsPrompt(tf, digitNumber, result);
        setTextFieldBorderAlert(tf, result);
        return result;
    }

    public boolean validateTextFieldIsAnIntegerOr2DecimalNumber(TextField tf) {
        boolean result = tf.getText().matches("([0-9]+)([.,][0-9]{2})?");
        userLog.textFiledMustContainsNumberOrDecimalWith2DigitsPrompt(tf, result);
        setTextFieldBorderAlert(tf, result);
        return result;
    }

    public boolean validatePatientExist(List<Patient> patients, Patient patient) {
        boolean result = patients.contains(patient);
        userLog.patientDoesNotExistPrompt(result);
        return result;
    }

    public boolean validatePatientNotExist(List<Patient> patients, Patient patient) {
        boolean result = !patients.contains(patient);
        userLog.patientExistPrompt(result);
        return result;
    }

    public boolean validatePatientsPersonalNumberIsUnique(List<Patient> patients, String personalNumber) {
        boolean result = true;
        for(Patient p : patients) {
            if(p.getPersonalNumber().equals(personalNumber)) {
                result = false;
            }
        }
        userLog.personalNumberIsNotUniquePrompt(result);
        return result;
    }

    public boolean validatePatientIsSolvent(Patient patient, double price) {
        boolean result = patient.isSolvent(price);
        userLog.patientIsSolventPrompt(result);
        return result;
    }

    public void clearLog() {
        userLog.clearMessage();
    }

    public boolean validateStringIsAnIntegerOr2DecimalNumber(String text) {
        return text.matches("([0-9]+)([.,][0-9]{2})?");
    }

    private void setTextFieldBorderAlert(TextField tf, boolean isValid) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (isValid) {
            if (styleClass.contains("error")) {
                styleClass.removeAll(Collections.singleton("error"));
            }
        } else {
            styleClass.add("error");
        }
    }
}



