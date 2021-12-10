package com.example.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondapp.R;
import com.example.secondapp.models.User;
import com.example.secondapp.providers.AuthProviders;
import com.example.secondapp.providers.UsersProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    TextView mTextViewRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    AuthProviders mAuthProvider;
    UsersProvider mUsersProvider;

    AlertDialog mDialog;

    SignInButton mBtnSignInGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    //FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // inserta un vista

        //-----------------------
        mTextViewRegister = findViewById(R.id.TextViewRegister); //accedo a un un texto que tiene ese id en el xml
        mTextInputEmail = findViewById(R.id.log_email);
        mTextInputPassword = findViewById(R.id.log_pass);
        mButtonLogin = findViewById(R.id.log_btn);
        mBtnSignInGoogle = findViewById(R.id.log_btnGoogle);

        mAuthProvider = new AuthProviders();
        mUsersProvider = new UsersProvider();
        //mFirestore =FirebaseFirestore.getInstance();

        mDialog = new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.titledialog)
                .setCancelable(false).build();

        mBtnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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


    // [START signin]
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        mDialog.show();
        mAuthProvider.googleLogin(account).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.dismiss();
                        if (task.isSuccessful()) {

                            String id = mAuthProvider.getUid();
                            checkUserExist(id);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }

                    }
                });
    }


    private void checkUserExist(final String id) {
        mDialog.show();
        //mFirestore.collection("Users").document(id).get()
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mDialog.dismiss();
                if(documentSnapshot.exists()) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success"); FirebaseUser user = mAuth.getCurrentUser(); updateUI(user);
                    Toast.makeText(MainActivity.this, "Login Successful with Google", Toast.LENGTH_SHORT).show();
                    Intent intentHome = new Intent(MainActivity.this, Home_Activity.class);
                    startActivity(intentHome);
                }else{
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Successful with Google", Toast.LENGTH_SHORT).show();
                                Intent intentHome = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intentHome);
                            }else{
                                Toast.makeText(MainActivity.this, "Login Failure with Google, No se pudo guardar informacion en la DB", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            } //fin de OnSuccess
        });
    }
    // [END auth_with_google]


    private void login() {
        String email = mTextInputEmail.getText().toString();
        String pass = mTextInputPassword.getText().toString();
        mDialog.show();

        mAuthProvider.login(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intentHome = new Intent(MainActivity.this, Home_Activity.class);
                    startActivity(intentHome);

                }else{
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Log.d("Campo", "email: "+email);
        Log.d("Campo", "password: "+pass);

    }

}//fin de la clase