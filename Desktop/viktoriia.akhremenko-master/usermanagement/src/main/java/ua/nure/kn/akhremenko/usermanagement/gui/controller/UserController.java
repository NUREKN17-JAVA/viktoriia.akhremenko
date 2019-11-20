package ua.nure.kn.akhremenko.usermanagement.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ua.nure.kn.akhremenko.usermanagement.util.UTF8Control;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserController {

    public GridPane root;
    public TextField firstNameField;
    public TextField lastNameField;
    public DatePicker dateOfBirthPicker;
    public Button confirmButton;
    public Button cancelButton;

    UserController() {
    }

    public void showBrowsePane() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("strings", Locale.getDefault(), new UTF8Control());
        Parent browsePane = FXMLLoader.load(getClass().getResource("/view/browse.fxml"), bundle);
        Platform.runLater(() -> {
            Stage window = (Stage) root.getScene().getWindow();
            window.setScene(new Scene(browsePane));
        });
    }

    boolean validateFields() {
        boolean isValid = true;

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();

        if (firstName.isEmpty()) {
            highlightError(firstNameField);
            isValid = false;
        } else {
            resetHighlight(firstNameField);
        }
        if (lastName.isEmpty()) {
            highlightError(lastNameField);
            isValid = false;
        } else {
            resetHighlight(lastNameField);
        }
        if (dateOfBirth == null) {
            highlightError(dateOfBirthPicker);
            isValid = false;
        } else {
            resetHighlight(dateOfBirthPicker);
        }

        return isValid;
    }

    private void highlightError(Control field) {
        field.setStyle("-fx-border-color: red;  -fx-border-width: 2px");
    }

    private void resetHighlight(Control field) {
        field.setStyle("-fx-border-color: default;  -fx-border-width: default");
    }

    void initDatePicker() {
        Platform.runLater(() -> dateOfBirthPicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return date.toString();
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string);
                } else {
                    return null;
                }
            }
        }));
    }
}
