package com.example.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.secondapp.R;
import com.example.secondapp.models.User;
import com.example.secondapp.providers.AuthProviders;
import com.example.secondapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Register_Activity extends AppCompatActivity {

    CircleImageView rgCircleImageView;
    TextInputEditText rgTextInputUsername;
    TextInputEditText rgTextInputEmail;
    TextInputEditText rgTextInputPass;
    TextInputEditText rgTextInputConfirmPass;
    Button rgButtonRegister;
    AuthProviders rgAuthProvider;
    UsersProvider rgUsersProvider;
    //FirebaseAuth rgAuth;
    //FirebaseFirestore rgFirestore;

    AlertDialog rgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rgTextInputUsername = findViewById(R.id.rg_username);
        rgTextInputEmail = findViewById(R.id.rg_email);
        rgTextInputPass = findViewById(R.id.rg_pass);
        rgTextInputConfirmPass = findViewById(R.id.rg_repass);
        rgButtonRegister = findViewById(R.id.rg_btn);


        //rgAuth = FirebaseAuth.getInstance();
        //rgFirestore = FirebaseFirestore.getInstance();
        rgAuthProvider = new AuthProviders();
        rgUsersProvider = new UsersProvider();

        rgDialog = new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.titledialog)
                .setCancelable(false).build();

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
            if(isEmailValid(email)) {
                if(pass.equals(confirmPass)) {
                    if (pass.length() >= 6) {
                        createUser(username,email,pass);
                    }else{
                        Toast.makeText(this, "La contraseña debe tener mìnimo 6 caràcteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Has insertado todos los campos, pero el correo no es válido", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Mandar un Toast de que no se pudo registrar
            Toast.makeText(this, "Lo siento! Has dejado algunos campos en blanco", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUser(final String username, final String email, String password){
        rgDialog.show();
        rgAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                rgDialog.dismiss();
                if(task.isSuccessful()){
                   String id = rgAuthProvider.getUid();
                   //Map <String,Object> map = new HashMap<>();map.put("username",username);map.put("email",email);map.put("password",password);
                   User user = new User(id,email,password,username);
                   rgUsersProvider.create(user); //rgFirestore.collection("Users").document(id).set(map);

                   Toast.makeText(Register_Activity.this, "El usuario se registró correctamente", Toast.LENGTH_SHORT).show();

               }else{
                   Toast.makeText(Register_Activity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();

               }

            }
        });

    }

    //Verifica si el email es valido
    public boolean isEmailValid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}