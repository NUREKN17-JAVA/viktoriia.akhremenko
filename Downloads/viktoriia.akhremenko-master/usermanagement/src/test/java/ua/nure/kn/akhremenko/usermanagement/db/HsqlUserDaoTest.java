package ua.nure.kn.akhremenko.usermanagement.db;

import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import ua.nure.kn.akhremenko.usermanagement.User;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class HsqlUserDaoTest extends DatabaseTestCase {

    private static final int TEST_DATA_LENGTH = 2;

    private static final Long EXISTING_ID = 2L;
    private static final String EXISTING_FIRST_NAME = "John";
    private static final String EXISTING_LAST_NAME = "Doe";
    private static final LocalDate EXISTING_DATE_OF_BIRTH = LocalDate.of(1972, 6, 8);

    private static final Long NEW_ID = 2L;
    private static final String NEW_FIRST_NAME = "Quentin";
    private static final String NEW_LAST_NAME = "Tarantino";
    private static final LocalDate NEW_DATE_OF_BIRTH = LocalDate.of(1963, 3, 27);

    private HsqlUserDao dao;
    private ConnectionFactory connectionFactory;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.dao = new HsqlUserDao(connectionFactory);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        connectionFactory = new ConnectionFactoryImpl(
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:file:db/usermanagement",
                "sa",
                "");
        return new DatabaseConnection(connectionFactory.createConnection());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        IDataSet dataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSet.xml"));
        return dataSet;
    }

    @Test
    public void testCreate() throws DatabaseException {
        User user = new User();
        user.setFirstName("Viktoriia");
        user.setLastName("Akhremenko");
        user.setDateOfBirth(LocalDate.now());
        assertNull(user.getId());

        User createdUser = this.dao.create(user);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
    }

    @Test
    public void testFind() {
        try {
            User foundUser = this.dao.find(EXISTING_ID);

            assertEquals(foundUser.getId(), EXISTING_ID);
            assertEquals(foundUser.getFirstName(), EXISTING_FIRST_NAME);
            assertEquals(foundUser.getLastName(), EXISTING_LAST_NAME);
            assertEquals(foundUser.getDateOfBirth(), EXISTING_DATE_OF_BIRTH);

        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testFindAll() {
        try {
            Collection result = this.dao.findAll();

            assertNotNull("Query result is null.", result);
            assertEquals("Collection size in not TEST_DATA_LENGTH", result.size(), TEST_DATA_LENGTH);

            IDataSet databaseDataSet = this.getConnection().createDataSet();
            ITable actualTable = databaseDataSet.getTable("users");

            IDataSet expectedDataSet = this.getDataSet();
            ITable expectedTable = expectedDataSet.getTable("users");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testUpdate() {
        try {
            User user = new User(NEW_ID, NEW_FIRST_NAME, NEW_LAST_NAME, NEW_DATE_OF_BIRTH);
            this.dao.update(user);

            IDataSet databaseDataSet = this.getConnection().createDataSet();
            ITable actualTable = databaseDataSet.getTable("users");

            IDataSet expectedDataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSetAfterUpdate.xml"));
            ITable expectedTable = expectedDataSet.getTable("users");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testDelete() {
        try {
            this.dao.delete(EXISTING_ID);

            IDataSet databaseDataSet = this.getConnection().createDataSet();
            ITable actualTable = databaseDataSet.getTable("users");

            IDataSet expectedDataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSetAfterDelete.xml"));
            ITable expectedTable = expectedDataSet.getTable("users");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}