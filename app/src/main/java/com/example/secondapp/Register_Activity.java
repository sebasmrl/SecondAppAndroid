package com.example.secondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register_Activity extends AppCompatActivity {

    CircleImageView rgCircleImageView;
    TextInputEditText rgTextInputUsername;
    TextInputEditText rgTextInputEmail;
    TextInputEditText rgTextInputPass;
    TextInputEditText rgTextInputConfirmPass;
    Button rgButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rgTextInputUsername = findViewById(R.id.rg_username);
        rgTextInputEmail = findViewById(R.id.rg_email);
        rgTextInputPass = findViewById(R.id.rg_pass);
        rgTextInputConfirmPass = findViewById(R.id.rg_repass);
        rgButtonRegister = findViewById(R.id.rg_btn);

        rgButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        rgCircleImageView = findViewById(R.id.cicle_imagen_back); //ecuentra al boton retroceder
        rgCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cuando estamos en una actividad que no es la principal
                //y la destruimo esto nos retorna a la actividad principal
                finish();
            }
        });
    }

    private void register() {
        String username = rgTextInputUsername.getText().toString();
        String email = rgTextInputEmail.getText().toString();
        String pass = rgTextInputPass.getText().toString();
        String confirmPass = rgTextInputConfirmPass.getText().toString();

        if(!username.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !confirmPass.isEmpty()) {

            //Mandar un Toast
            Toast.makeText(this, "Has insertado todos los campos", Toast.LENGTH_SHORT).show();


            //Impresion de consola
            Log.d("Campo", "username: " + username);
            Log.d("Campo", "email: " + email);
            Log.d("Campo", "password: " + pass);
            Log.d("Campo", "confirmPassword: " + confirmPass);
        }else{
            //Mandar un Toast de que no se pudo registrar
            Toast.makeText(this, "Lo siento! Has dejado algunos campos en blanco", Toast.LENGTH_SHORT).show();
        }

    }

    //Verifica si el email es valido
    public boolean isEmailValid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}