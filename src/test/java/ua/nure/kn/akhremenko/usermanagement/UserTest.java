package ua.nure.kn.akhremenko.usermanagement;

import ua.nure.kn.akhremenko.usermanagement.User;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UserTest {
    private final static String FIRST_NAME = "Viktoriia";
    private final static String LAST_NAME = "Akhremenko";
    private final static int AGE = 19;
    private User user;

    @Before
    public void setUp() {
        this.user = new User();
    }

    /**
     * Tests concatenation of user's first and last name.
     *
     * Expected result: LAST_NAME, FIRST_NAME
     */
    @Test
    public void getFullName() {
        this.user.setFirstName(FIRST_NAME);
        this.user.setLastName(LAST_NAME);

        String FULL_NAME = LAST_NAME + ", " + FIRST_NAME;
        String userFullName = this.user.getFullName();

        assertEquals(FULL_NAME, userFullName);
    }

    /**
     * Tests getAge when user's birthday is today.
     *
     * Expected result: AGE
     */
    @Test
    public void getAge_WhenBirthdayIsToday_ReturnsAge() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(AGE);
        this.user.setDateOfBirth(dateOfBirth);

        assertEquals(AGE, this.user.getAge());
    }

    /**
     * Tests getAge when user's birthday was yesterday.
     *
     * Expected result: AGE
     */
    @Test
    public void getAge_WhenBirthdayWasYesterday_ReturnsAge() {
//        // Sets calendar to one day before current day AGE years ago
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -AGE);
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//
//        // Sets user's date of birth
//        Date dateOfBirth = calendar.getTime();
        LocalDate dateOfBirth = LocalDate.now().minusYears(AGE).minusDays(1);
        this.user.setDateOfBirth(dateOfBirth);

        assertEquals(AGE, this.user.getAge());
    }

    /**
     * Tests getAge when user's birthday was yesterday.
     *
     * Expected result: AGE
     */
    @Test
    public void getAge_WhenBirthdayWillBeTomorrow_ReturnsAgeMinusOne() {
//        // Sets calendar to one day before current day AGE years ago
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -AGE);
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        // Sets user's date of birth
//        Date dateOfBirth = calendar.getTime();
        LocalDate dateOfBirth = LocalDate.now().minusYears(AGE).plusDays(1);
        this.user.setDateOfBirth(dateOfBirth);

        assertEquals(AGE - 1, this.user.getAge());
    }

    /**
     * Tests getAge when user's birthday is 31 of December 1995.
     *
     * THIS TEST IS ACTUAL ONLY TILL THE END OF 2019
     *
     * Expected result: AGE
     */
    @Test
    public void getAge_WhenBirthdayIsDecember31_ReturnsAge() {
//        // Sets calendar to 31 of December, 1995
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(1995, Calendar.DECEMBER, 31);
//
//        // Sets user's date of birth to current day and month AGE years ago
//        Date dateOfBirth = calendar.getTime();
        LocalDate dateOfBirth = LocalDate.of(1995, 12, 31);
        this.user.setDateOfBirth(dateOfBirth);

        assertEquals(AGE, this.user.getAge());
    }

    /**
     * Tests getAge when user's birthday is 1 of January 1996.
     *
     * THIS TEST IS ACTUAL ONLY TILL THE END OF 2019
     *
     * Expected result: AGE
     */
    @Test
    public void getAge_WhenBirthdayIsJanuary1_ReturnsAge() {
//        // Sets calendar to 31 of December, 1995
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(1996, Calendar.JANUARY, 1);
//
//        // Sets user's date of birth to current day and month AGE years ago
//        Date dateOfBirth = calendar.getTime();
        LocalDate dateOfBirth = LocalDate.of(1996, 1, 1);
        this.user.setDateOfBirth(dateOfBirth);

        assertEquals(AGE, this.user.getAge());
    }
}