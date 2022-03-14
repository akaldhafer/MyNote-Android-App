package com.example.mynote.userapi;

public class UserModel {
    private String name, email, password,imageUrl, token;

    public UserModel(String name, String email, String password, String imageUrl, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.token = token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
