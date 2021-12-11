package com.example.secondapp.providers;

import com.example.secondapp.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostProvider {
    CollectionReference ppCollection;

    public PostProvider() {
        ppCollection = FirebaseFirestore.getInstance().collection("Post");
    }

    public Task<Void> save(Post post){
    return ppCollection.document().set(post);
      //  return ppCollection.document(post.getId()).set(post);

    }
}
