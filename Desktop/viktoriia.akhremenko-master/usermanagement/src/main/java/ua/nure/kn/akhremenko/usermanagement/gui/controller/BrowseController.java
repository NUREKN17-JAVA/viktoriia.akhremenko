package ua.nure.kn.akhremenko.usermanagement.gui.controller;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Event;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.agent.RequestServer;
import ua.nure.kn.akhremenko.usermanagement.agent.SearchAgent;
import ua.nure.kn.akhremenko.usermanagement.agent.SearchException;
import ua.nure.kn.akhremenko.usermanagement.agent.SearchRequestBehaviour;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;
import ua.nure.kn.akhremenko.usermanagement.db.UserDao;
import ua.nure.kn.akhremenko.usermanagement.util.UTF8Control;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class BrowseController {

    public GridPane mainPane;
    public TableView<User> usersTable;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;
    public Button detailsButton;

    public TextField firstNameSearchField;
    public TextField lastNameSearchField;

    private static SearchAgent searchAgent;
    private static ObservableList<User> users;

    private ResourceBundle bundle = ResourceBundle.getBundle("strings", Locale.getDefault(), new UTF8Control());
    private UserDao dao = DaoFactory.getInstance().getUserDao();

    public BrowseController() {
        users = FXCollections.observableArrayList();

        Platform.runLater(() -> {
            loadAllUsers();
            usersTable.setItems(users);
            addButton.requestFocus();
        });
    }

    public static void setSearchAgent(SearchAgent sa) {
        searchAgent = sa;
    }

    public static void setUsers(Collection<User> users) {
        BrowseController.users.setAll(users);
    }

    @FXML
    private void showAddUserPane() throws IOException {
        Parent addUserPane = FXMLLoader.load(getClass().getResource("/view/add_user.fxml"), bundle);
        Platform.runLater(() -> {
            Stage window = (Stage) mainPane.getScene().getWindow();
            window.setScene(new Scene(addUserPane));
        });
    }

    @FXML
    private void showEditUserPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_user.fxml"), bundle);
        Parent editUserPane = loader.load();
        EditUserController controller = loader.getController();

        User selectedUser = getSelectedUser();
        if (selectedUser != null) {
            controller.setUserData(selectedUser);
            Platform.runLater(() -> {
                Stage window = (Stage) mainPane.getScene().getWindow();
                window.setScene(new Scene(editUserPane));
            });
        }
    }

    @FXML
    private void showUserDetailsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_details.fxml"), bundle);
        Parent userDetailsPane = loader.load();
        UserDetailsController controller = loader.getController();

        User selectedUser = getSelectedUser();
        if (selectedUser != null) {
            controller.setUserData(selectedUser);
            Platform.runLater(() -> {
                Stage window = (Stage) mainPane.getScene().getWindow();
                window.setScene(new Scene(userDetailsPane));
            });
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = getSelectedUser();
        if (selectedUser != null) {
            Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("browse.alert.deleteUser"));
            Optional<ButtonType> result = warningAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    dao.delete(selectedUser.getId());
                    loadAllUsers();
                } catch (DatabaseException e) {
                    showErrorAlert(e.getMessage());
                }
            }
        }
    }

    private User getSelectedUser() {
        return usersTable.getSelectionModel().getSelectedItem();
    }

    public void loadAllUsers() {
        Platform.runLater(() -> {
            try {
                users.setAll(dao.findAll());
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        });
    }

    public void searchUsers() {
        String firstName = firstNameSearchField.getText();
        String lastName = lastNameSearchField.getText();
        try {
//            JadeGateway.init(null, new Properties());
//            DFService.s
//            JadeGateway.execute(new SearchRequestBehaviour(aids, firstName, lastName));
            searchAgent.search(firstName, lastName);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert(e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
}
