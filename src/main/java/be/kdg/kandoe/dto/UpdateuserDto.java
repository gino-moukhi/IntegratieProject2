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
    private Calendar birthday;


    public UpdateuserDto() {
    }

    public UpdateuserDto(String username, String password, String firstName, String lastName, Gender gender, Calendar birthday) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
    }

    public UpdateuserDto(User user){
        this.username = user.getUsername();
        this.password = "";
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        //TODO birthday
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

    public int getYear(){
        return birthday.get(Calendar.YEAR);
    }

    public int getMonth(){
        //The months in calendar go from {0-11} (so we have to add 1)
        return birthday.get(Calendar.MONTH) + 1;
    }

    public int getDay(){
        return birthday.get(Calendar.DAY_OF_MONTH);
    }
}
