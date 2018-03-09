package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.user.Gender;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String gender;

    //Test
    private String role;

    public RequestUserDto() {
    }

    public RequestUserDto(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public RequestUserDto(String username, String firstName, String lastName, String email, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public RequestUserDto(String username, String firstName, String lastName, String email, Calendar dateTime, Gender gender) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.birthday = format.format(dateTime.getTime());
        this.gender = gender.name();
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
