package ua.nure.kn.akhremenko.usermanagement.web;

import org.junit.Test;
import ua.nure.kn.akhremenko.usermanagement.User;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class EditUserServletTest extends MockServletTestCase {

    private static final Long ID = 1000L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.parse("1984-05-10");

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createServlet(EditUserServlet.class);
    }

    @Test
    public void shouldUpdateUser() {
        // given:
        User user = new User(ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH);
        getMockUserDao().expect("update", user);

        addRequestParameter("id", ID.toString());
        addRequestParameter("firstName", FIRST_NAME);
        addRequestParameter("lastName", LAST_NAME);
        addRequestParameter("dateOfBirth", DATE_OF_BIRTH.toString());
        addRequestParameter("okButton", "OK");

        // when:
        doPost();

        // then:
        // no error
    }

    @Test
    public void shouldReturnErrorOnEmptyFirstName() {
        // given:
        addRequestParameter("id", ID.toString());
        addRequestParameter("lastName", LAST_NAME);
        addRequestParameter("dateOfBirth", DATE_OF_BIRTH.toString());
        addRequestParameter("okButton", "OK");

        // when:
        doPost();

        // then:
        String error = (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull(error);
    }

    @Test
    public void shouldReturnErrorOnEmptyLastName() {
        // given:
        addRequestParameter("id", ID.toString());
        addRequestParameter("firstName", FIRST_NAME);
        addRequestParameter("dateOfBirth", DATE_OF_BIRTH.toString());
        addRequestParameter("okButton", "OK");

        // when:
        doPost();

        // then:
        String error = (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull(error);
    }

    @Test
    public void shouldReturnErrorOnEmptyDateOfBirth() {
        // given:
        addRequestParameter("id", ID.toString());
        addRequestParameter("firstName", FIRST_NAME);
        addRequestParameter("lastName", LAST_NAME);
        addRequestParameter("okButton", "OK");

        // when:
        doPost();

        // then:
        String error = (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull(error);
    }

    @Test
    public void shouldReturnErrorOnInvalidDateOfBirth() {
        // given:
        addRequestParameter("id", ID.toString());
        addRequestParameter("firstName", FIRST_NAME);
        addRequestParameter("lastName", LAST_NAME);
        addRequestParameter("dateOfBirth", "invalid");
        addRequestParameter("okButton", "OK");

        // when:
        doPost();

        // then:
        String error = (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull(error);
    }
}
