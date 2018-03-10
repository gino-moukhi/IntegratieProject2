package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.user.Gender;

import java.util.Calendar;

public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private Gender gender;
    private Calendar birthday;

    public UserDto() {
    }

    public UserDto(String username, String password, String firstName, String lastName, String email, int age, Gender gender, Calendar birthday) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.birthday = birthday;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
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
