package com.example.mynote.userapi;

import android.app.Activity;

import java.util.ArrayList;

public interface UserDataPresenter {
    void onSuccessUpdate(Activity activity, String name, String email, String password,String imageUrl, String token);

}
