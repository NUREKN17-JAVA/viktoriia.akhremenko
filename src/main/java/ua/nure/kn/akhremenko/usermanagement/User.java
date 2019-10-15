package ua.nure.kn.akhremenko.usermanagement;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

public class User implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public User() {
    }

    public User(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public User(Long id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    /**
     * Returns a concatenation of user's first and last names
     * in format "lastName, firstName".
     * For example: "Akhremenko, Viktoriia"
     *
     * @return the concatenation of user's first and last names.
     */
    public String getFullName() {
        return this.lastName + ", " + this.firstName;
    }


    /**
     * Returns user's age based on his date of birth and current system date.
     *
     * @return the user's age.
     */
    public int getAge() {
        LocalDate now = LocalDate.now();

        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int currentDay = now.getDayOfMonth();

        int yearOfBirth = this.dateOfBirth.getYear();
        int monthOfBirth = this.dateOfBirth.getMonthValue();
        int dayOfBirth = this.dateOfBirth.getDayOfMonth();

        int age = currentYear - yearOfBirth;
        if (currentMonth >= monthOfBirth && currentDay >= dayOfBirth) {
            return age;
        }
        return age - 1;
    }

    public boolean equals(Object object){
        if(object == null){
            return false;
        }
        if(this == object){
            return true;
        }
        if(this.getId() == null && ((User)object).getId() == null){
            return true;
        }

        return this.getId().equals(((User)object).getId());
    }

    public int hashCode(){
        if(this.getId() == null){
            return 0;
        }
        return this.getId().hashCode();
    }
}
