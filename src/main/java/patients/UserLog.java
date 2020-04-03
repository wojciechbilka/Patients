package patients;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserLog {
    private Label logMessage;
    private Map<String, String> fieldNamesMap;

    public UserLog(Label logMessage) {
        this.logMessage = logMessage;
        initialize();
    }

    private void initialize() {
        initializeMap();
        ObservableList<String> styleClass = this.logMessage.getStyleClass();
        if (!styleClass.contains("error")) {
            styleClass.add("error");
        }
    }

    public UserLog(Label logMessage, Map<String, String> fieldNamesMap) {
        this.logMessage = logMessage;
        this.fieldNamesMap = fieldNamesMap;
        ObservableList<String> styleClass = this.logMessage.getStyleClass();
        if (!styleClass.contains("error")) {
            styleClass.add("error");
        }
    }

    public void textFieldEmptyPrompt(TextField textField, boolean validatorResult) {
        String previousMessage = logMessage.getText();
        String promptMessage = "Text field " + fieldNamesMap.get(textField.getId()) + " is empty.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertPromptMessage(previousMessage, promptMessage);
        }
    }

    public void textFieldIsNotAWordPrompt(TextField textField, boolean validatorResult) {
        String previousMessage = logMessage.getText();
        String promptMessage = "Text field " + fieldNamesMap.get(textField.getId()) + " consists of characters other than letters.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertPromptMessage(previousMessage, promptMessage);
        }
    }

    private void removePromptMessage(String previousMessage, String promptMessage) {
        if (previousMessage.contains(promptMessage)) {
            if (previousMessage.contains("\n" + promptMessage)) {
                logMessage.setText(previousMessage.replace("\n" + promptMessage, ""));
            } else if (previousMessage.contains(promptMessage + "\n")) {
                logMessage.setText(previousMessage.replace(promptMessage + "\n", ""));
            } else {
                logMessage.setText(previousMessage.replace(promptMessage, ""));
            }
        }
    }

    private void insertPromptMessage(String previousMessage, String promptMessage) {
        logMessage.setVisible(true);
        if (!previousMessage.contains(promptMessage)) {
            if (!previousMessage.isEmpty()) {
                logMessage.setText(previousMessage + "\n");
            }
            logMessage.setText(logMessage.getText() + promptMessage);
        }
    }

    private void hideLogMessage() {
        logMessage.setVisible(false);
    }

    public void textFiledMustContainsNumberOfXDigitsPrompt(TextField textField, int numberOfDigits, boolean validatorResult) {
        logMessage.setVisible(true);
        String previousMessage = logMessage.getText();
        String promptMessage = "Text field " + fieldNamesMap.get(textField.getId()) + " must contain " + numberOfDigits + " digits.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertPromptMessage(previousMessage, promptMessage);
        }

    }

    public void textFiledMustContainsNumberOrDecimalWith2DigitsPrompt(TextField textField, boolean validatorResult) {
        logMessage.setVisible(true);
        String previousMessage = logMessage.getText();
        String promptMessage = "Text field " + fieldNamesMap.get(textField.getId()) + " must contain integer number \n or number with 2 digits.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertPromptMessage(previousMessage, promptMessage);
        }
    }

    public void patientDoesNotExistPrompt(boolean validatorResult) {
        logMessage.setVisible(true);
        String previousMessage = logMessage.getText();
        String promptMessage = "Patient with given data does not exist.";
        String promptMessageToReplace = "Patient with given data already exist.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertOrReplacePromptMessage(previousMessage, promptMessageToReplace, promptMessage);
        }
    }

    public void patientExistPrompt(boolean validatorResult) {
        logMessage.setVisible(true);
        String previousMessage = logMessage.getText();
        String promptMessage = "Patient with given data already exist.";
        String promptMessageToReplace = "Patient with given data does not exist.";
        if (validatorResult) {
            removePromptMessage(previousMessage, promptMessage);
        } else {
            insertOrReplacePromptMessage(previousMessage, promptMessageToReplace, promptMessage);
        }
    }

    private void insertOrReplacePromptMessage(String previousMessage, String promptMessageToReplace, String promptMessage) {
        logMessage.setVisible(true);
        if (!previousMessage.contains(promptMessage)) {
            if(previousMessage.contains(promptMessageToReplace)) {
                logMessage.setText(previousMessage.replace(promptMessageToReplace, promptMessage));
                return;
            } else if (!previousMessage.isEmpty()) {
                logMessage.setText(previousMessage + "\n");
            }
            logMessage.setText(logMessage.getText() + promptMessage);
        }
    }

    private void initializeMap() {
        fieldNamesMap = new HashMap<>();
        fieldNamesMap.put("nameField", "Name");
        fieldNamesMap.put("surnameField", "Surname");
        fieldNamesMap.put("personalNumberField", "Personal Number");
        fieldNamesMap.put("balanceField", "Money pain in");
    }

    public void clearMessage() {
        logMessage.setText("");
        hideLogMessage();
    }
}
