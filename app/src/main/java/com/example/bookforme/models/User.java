package com.example.bookforme.models;

public class User {
    String username,email,password,userid;

    public User(String username, String email, String password,String userid) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userid=userid;
    }

    public User(){}

    //signup constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
