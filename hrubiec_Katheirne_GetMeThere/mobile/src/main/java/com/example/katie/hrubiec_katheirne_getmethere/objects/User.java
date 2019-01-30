package com.example.katie.hrubiec_katheirne_getmethere.objects;

public class User {

    String email;
    String password;
    String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
