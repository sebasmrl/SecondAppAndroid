package com.example.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {


    TextInputEditText complTextInputUsername;
    Button complButtonConfirm;
    //FirebaseAuth rgAuth;
    //FirebaseFirestore rgFirestore;
    AuthProviders complAuthProvider;
    UsersProvider complUsersProvider;

    AlertDialog complDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        complTextInputUsername = findViewById(R.id.compl_username);

        complButtonConfirm = findViewById(R.id.compl_btn);


        //rgAuth = FirebaseAuth.getInstance();
        //rgFirestore = FirebaseFirestore.getInstance();
        complUsersProvider = new UsersProvider();
        complAuthProvider = new AuthProviders();


        complDialog = new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.titledialog)
                .setCancelable(false).build();

        complButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String username = complTextInputUsername.getText().toString();

        if(!username.isEmpty()) {
            updateUser(username);

        }else{
            //Mandar un Toast
            Toast.makeText(this, "Lo siento! Username no puede ser blanco", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUser(final String username){
        complDialog.show();
        String id = complAuthProvider.getUid();
        String email = complAuthProvider.getEmail();
        String name = complAuthProvider.getDisplayName();

        Log.d("logueo", "email: "+email);
        Log.d("logueo", "name: "+name);

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        //Map<String,Object> map = new HashMap<>(); map.put("username",username); map.put("email",email);
        //rgFirestore.collection("Users").document(id).set(map)
        complUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                complDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intentHome = new Intent(CompleteProfileActivity.this, Home_Activity.class);
                    startActivity(intentHome);
                    Toast.makeText(CompleteProfileActivity.this, "Datos adicionados exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar el usuario", Toast.LENGTH_SHORT).show();
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