package ua.nure.kn.akhremenko.usermanagement.gui;

import com.mockobjects.dynamic.Mock;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.MockDaoFactory;
import ua.nure.kn.akhremenko.usermanagement.util.UTF8Control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;

public class BrowseTest extends ApplicationTest {

    private static final int COLUMNS_COUNT = 3;
    private static final String INITIAL_FIRST_NAME = "Quentin";
    private static final String INITIAL_LAST_NAME = "Tarantino";
    private static final String INITIAL_DATE_OF_BIRTH = "1963-03-27";
    private static final String UPDATE_FIRST_NAME = "Johnny";
    private static final String UPDATE_LAST_NAME = "Depp";
    private static final String UPDATE_DATE_OF_BIRTH = "1963-06-08";

    private Mock mockUserDao;
    private ResourceBundle bundle;

    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("dao.factory", MockDaoFactory.class.getName());
        DaoFactory.init(properties);

        mockUserDao = ((MockDaoFactory) DaoFactory.getInstance()).getMockUserDao();
        mockUserDao.expectAndReturn("findAll", new ArrayList());


        bundle = ResourceBundle.getBundle("strings", Locale.getDefault(), new UTF8Control());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/browse.fxml"), bundle);

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @After
    public void tearDown() {
        try {
            mockUserDao.verify();
            FxToolkit.hideStage();
            release(new KeyCode[]{});
            release(new MouseButton[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldContainBrowseControls() {
        verifyThat("#usersTable", isNotNull());
        verifyThat("#addButton", isNotNull());
        verifyThat("#editButton", isNotNull());
        verifyThat("#deleteButton", isNotNull());
        verifyThat("#detailsButton", isNotNull());
    }

    @Test
    public void shouldContainTableColumns() {
        TableView table = lookup("#usersTable").query();
        ObservableList<TableColumn> columns = table.getColumns();

        assertEquals(COLUMNS_COUNT, columns.size());
        assertEquals(bundle.getString("user.id"), columns.get(0).getText());
        assertEquals(bundle.getString("user.firstName"), columns.get(1).getText());
        assertEquals(bundle.getString("user.lastName"), columns.get(2).getText());
    }

    @Test
    public void shouldAddUser() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User(INITIAL_FIRST_NAME, INITIAL_LAST_NAME, LocalDate.parse(INITIAL_DATE_OF_BIRTH));
        User expectedUser = new User(1L, INITIAL_FIRST_NAME, INITIAL_LAST_NAME, LocalDate.parse(INITIAL_DATE_OF_BIRTH));
        users.add(expectedUser);

        mockUserDao.expectAndReturn("create", user, expectedUser);
        mockUserDao.expectAndReturn("findAll", users);

        // given:
        verifyThat("#usersTable", hasNumRows(0));

        // when:
        clickOn("#addButton");
        clickOn("#firstNameField").write(INITIAL_FIRST_NAME);
        clickOn("#lastNameField").write(INITIAL_LAST_NAME);
        clickOn("#dateOfBirthPicker").write(INITIAL_DATE_OF_BIRTH);
        clickOn("#confirmButton");

        // than:
        verifyThat("#usersTable", hasNumRows(1));
    }

//    @Test
//    public void shouldEditUser() throws DatabaseException {
//        ArrayList<User> users = new ArrayList<>();
//        User newUser = new User(1L, INITIAL_FIRST_NAME, INITIAL_LAST_NAME, LocalDate.parse(INITIAL_DATE_OF_BIRTH));
//        users.add(newUser);
//
//        mockUserDao.expectAndReturn("findAll", users);
//        mockUserDao.expect("create", new AnyConstraintMatcher());
//        mockUserDao.expect("update", new AnyConstraintMatcher());
//        User updatedUser = new User(1L, UPDATE_FIRST_NAME, UPDATE_LAST_NAME, LocalDate.parse(UPDATE_DATE_OF_BIRTH));
//        users.set(0, updatedUser);
//        mockUserDao.expectAndReturn("findAll", users);
//        mockUserDao.expectAndReturn("findAll", users);
//
//        // given:
//        User user = new User(1L, INITIAL_FIRST_NAME, INITIAL_LAST_NAME, LocalDate.parse(INITIAL_DATE_OF_BIRTH));
//        userDao.create(user);
//        BrowseController controller = loader.getController();
//        controller.loadUsersTableData();
//        verifyThat("#usersTable", hasNumRows(1));
//
//
//        // when:
//        TableView table = lookup("#usersTable").query();
//        table.getSelectionModel().select(0);
//        clickOn("#editButton");
//        clickOn("#firstNameField").eraseText(INITIAL_FIRST_NAME.length()).write(UPDATE_FIRST_NAME);
//        clickOn("#lastNameField").eraseText(INITIAL_LAST_NAME.length()).write(UPDATE_LAST_NAME);
//        clickOn("#dateOfBirthPicker").eraseText(INITIAL_DATE_OF_BIRTH.length()).write(UPDATE_DATE_OF_BIRTH);
//        clickOn("#confirmButton");
//
//        // then:
//        user.setFirstName(UPDATE_FIRST_NAME);
//        user.setLastName(UPDATE_LAST_NAME);
//        user.setDateOfBirth(LocalDate.parse(UPDATE_DATE_OF_BIRTH));
//        verifyThat("#usersTable", containsRowAtIndex(0, user));
//    }
}