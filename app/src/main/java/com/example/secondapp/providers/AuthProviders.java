package com.example.secondapp.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthProviders {
    private FirebaseAuth prAuth;
    private FirebaseFirestore prFirestore;

    public AuthProviders(){
        prAuth = FirebaseAuth.getInstance();
    }



    public Task<AuthResult> login(String email, String pass){
        return prAuth.signInWithEmailAndPassword(email,pass);
    }

    public Task<AuthResult> googleLogin(GoogleSignInAccount googleSignInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return prAuth.signInWithCredential(credential);
    }

    public String getUid(){
        if(prAuth.getCurrentUser() != null){
            return prAuth.getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    public String getEmail(){
        if(prAuth.getCurrentUser() != null){
            return prAuth.getCurrentUser().getEmail();
        }else{
            return null;
        }
    }
    public String getDisplayName(){
        if(prAuth.getCurrentUser() != null){
            return prAuth.getCurrentUser().getDisplayName();
        }else{
            return null;
        }
    }

    public Task<AuthResult> register(String email, String password){
        return prAuth.createUserWithEmailAndPassword(email,password);
    }
}
