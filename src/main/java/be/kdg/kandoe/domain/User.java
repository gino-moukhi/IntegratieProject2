package be.kdg.kandoe.domain;

import be.kdg.kandoe.dto.UserDto;

public class User {
    private String name;

    private int age;

    private String email;

    private Gender gender;

    private String password;

    private String username;

    public User() {

    }

    public User(UserDto userDto){
        this.name = userDto.getFirstName() + " " + userDto.getLastName();
        this.age = userDto.getAge();
        this.email = userDto.getEmail();
        this.gender = userDto.getGender();
        this.password = userDto.getPassword();
        this.username = userDto.getUsername();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
