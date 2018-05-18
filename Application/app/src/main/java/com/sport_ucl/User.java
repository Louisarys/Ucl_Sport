package com.sport_ucl;

/**
 * Created by louis on 17/03/18.
 */

public class User {
    private String firstName;
    private String lastName;
    private String genre;
    private String country;
    private String email;
    private String birthday;
    private long id;
    private Boolean isAdmin;



    public User(String firstName, String lastName, String genre, String country, String email, String birthday, long id, Boolean isAdmin){
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.genre = genre;
        this.email = email;
        this.birthday = birthday;
        this.id = id;
        this.isAdmin = isAdmin;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
