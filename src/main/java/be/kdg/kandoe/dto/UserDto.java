package be.kdg.kandoe.dto;

import be.kdg.kandoe.domain.Gender;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UserDto {

    @NotNull(message = "form.invalid.size")
    @NotEmpty(message = "form.invalid.empty")
    private String firstName;

    @NotNull(message = "form.invalid.size")
    @NotEmpty(message = "form.invalid.empty")
    private String lastName;

    @NotNull(message = "form.invalid.size")
    @NotEmpty(message = "form.invalid.empty")
    private String password;

    @NotNull(message = "form.invalid.size")
    @Email(message = "{form.invalid.email}")
    @NotEmpty(message = "form.invalid.empty")
    private String email;

    @NotNull(message = "form.invalid.size")
    @NotEmpty(message = "form.invalid.empty")
    private int age;

    @NotNull(message = "form.invalid.size")
    @NotEmpty(message = "form.invalid.empty")
    private String username;


    private Gender gender;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
