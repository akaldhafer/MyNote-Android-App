package com.example.mynote.userapi;

public interface UserViewMessage {
    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
