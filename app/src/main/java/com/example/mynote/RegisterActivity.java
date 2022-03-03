package com.example.mynote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class RegisterActivity extends Activity {
    EditText email, name, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        email = findViewById(R.id.edit_email_register);
        name= findViewById(R.id.edit_name_register);
        password= findViewById(R.id.edit_password_register);


    }

    public void onClickRegisterButton(View view) {
        //todo malik handle register
    }

    public void onClickLoginButton(View view) {
        //todo malik handle login
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();

    }
}
