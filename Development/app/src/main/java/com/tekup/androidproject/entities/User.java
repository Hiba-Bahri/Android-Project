package com.tekup.androidproject.entities;

public class User {

    private String login;
    private String pw;

    public User() {
    }

    public User(String login, String pw) {
        this.login = login;
        this.pw = pw;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
