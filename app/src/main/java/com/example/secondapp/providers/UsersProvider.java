package com.example.secondapp.providers;

import com.example.secondapp.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersProvider {

    private CollectionReference upCollection;



    public UsersProvider(){
        upCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return upCollection.document(id).get();
    }

    public Task<Void> create(User user){
        return upCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){
        return upCollection.document(user.getId()).set(user);
    }
}
