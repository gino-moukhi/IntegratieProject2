package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;

import java.util.Calendar;

public class UpdateuserDto {
    //Username can not be changed and is only used for checking if the user is trying to change the same user object
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;
    private int year;
    private int month;
    private int day;

    public UpdateuserDto() {
    }

    public UpdateuserDto(String username, String password, String firstName, String lastName, Gender gender, int year, int month, int day) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public UpdateuserDto(User user){
        this.username = user.getUsername();
        this.password = "";
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.year = user.getYear();
        this.month = user.getMonth();
        this.day = user.getDay();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
