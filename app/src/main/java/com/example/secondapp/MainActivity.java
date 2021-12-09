package com.example.secondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {


    TextView mTextViewRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // inserta un vista

        //-----------------------
        mTextViewRegister = findViewById(R.id.TextViewRegister); //accedo a un un texto que tiene ese id en el xml
        mTextInputEmail = findViewById(R.id.log_email);
        mTextInputPassword = findViewById(R.id.log_pass);
        mButtonLogin = findViewById(R.id.log_btn);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }


        });


        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //La intencion es que MainActivity llame a Register Activity
                Intent intentRegister = new Intent(MainActivity.this, Register_Activity.class);
                startActivity(intentRegister);

            }
        });

    } //fin del onCreate

    private void login() {
        String email = mTextInputEmail.getText().toString();
        String pass = mTextInputPassword.getText().toString();

        Log.d("Campo", "email: "+email);
        Log.d("Campo", "password: "+pass);

    }

}//fin de la clase