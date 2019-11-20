package ua.nure.kn.akhremenko.usermanagement.web;

import org.junit.Before;
import org.junit.Test;
import ua.nure.kn.akhremenko.usermanagement.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BrowseServletTest extends MockServletTestCase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        createServlet(BrowseServlet.class);
    }

    @Test
    public void shouldReturnUserList() {
        // given:
        User user = new User(1000L, "Quentin", "Tarantino", LocalDate.parse("1963-03-27"));
        List<User> userList = Collections.singletonList(user);
        getMockUserDao().expectAndReturn("findAll", userList);

        // when:
        doGet();

        // then:
        Collection<User> userCollection = (Collection<User>) getWebMockObjectFactory().getMockSession().getAttribute("users");
        assertNotEquals("Could not find users in session", Collections.emptyList(), userCollection);
        assertSame(userList, userCollection);
    }

    @Test
    public void shouldOpenEditUserPage() {
        // given:
        User user = new User(1000L, "Quentin", "Tarantino", LocalDate.parse("1963-03-27"));
        getMockUserDao().expectAndReturn("find", 1000L, user);

        addRequestParameter("editButton", "Edit");
        addRequestParameter("id", "1000");

        // when:
        doPost();

        // then:
        User sessionUser = (User) getWebMockObjectFactory().getMockSession().getAttribute("user");
        assertNotNull(sessionUser);
        assertSame(user, sessionUser);
    }

    @Test
    public void shouldDeleteUser() {
        // given:
        User user = new User(1000L, "Quentin", "Tarantino", LocalDate.parse("1963-03-27"));
        List<User> userList = Collections.singletonList(user);
        getMockUserDao().expectAndReturn("findAll", userList);
        getMockUserDao().expect("delete", 1000L);
        getMockUserDao().expectAndReturn("findAll", Collections.EMPTY_LIST);

        // when:
        doGet();
        addRequestParameter("deleteButton", "Delete");
        addRequestParameter("id", "1000");
        doPost();

        // then:
        Collection<User> userCollection = (Collection<User>) getWebMockObjectFactory().getMockSession().getAttribute("users");
        assertEquals(0, userCollection.size());
    }
}
