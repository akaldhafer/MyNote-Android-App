package com.example.mynote.userapi;

public interface UserVeiwfetchMessage {
    void onUpdateSuccess(UserModel message);
    void onUpdateFailure(String message);
}
