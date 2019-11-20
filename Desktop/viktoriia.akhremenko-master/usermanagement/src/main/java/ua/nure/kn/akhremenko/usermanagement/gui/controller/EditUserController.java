package ua.nure.kn.akhremenko.usermanagement.gui.controller;

import javafx.scene.control.Alert;
import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;
import ua.nure.kn.akhremenko.usermanagement.db.UserDao;

import java.io.IOException;

public class EditUserController extends UserController {

    private User user;
    private UserDao dao = DaoFactory.getInstance().getUserDao();

    public EditUserController() {
        initDatePicker();
    }

    void setUserData(User user) {
        this.user = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        dateOfBirthPicker.setValue(user.getDateOfBirth());
        try {
            dao.update(user);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void editUser() throws IOException {
        if (validateFields() && user != null) {
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());
            user.setDateOfBirth(dateOfBirthPicker.getValue());

            try {
                dao.update(user);
            } catch (DatabaseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }

            showBrowsePane();
        }
    }
}
