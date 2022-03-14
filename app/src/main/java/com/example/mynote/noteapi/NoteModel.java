package com.example.mynote.noteapi;

public class NoteModel {
    String id,userEmail, title, body, token;

    public NoteModel(String id, String userEmail, String title, String body, String token) {
        this.id = id;
        this.userEmail = userEmail;
        this.title = title;
        this.body = body;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
