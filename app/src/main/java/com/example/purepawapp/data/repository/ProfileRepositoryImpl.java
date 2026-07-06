package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.User;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileRepositoryImpl implements ProfileRepository {

    private final FirebaseFirestore firestore;

    public ProfileRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void getUser(String uid, RepoCallback<User> callback) {
        firestore.collection(FirestoreCollections.USERS).document(uid).get()
                .addOnSuccessListener(doc -> {
                    User user = doc.toObject(User.class);
                    if (user == null) {
                        callback.onError(new IllegalStateException("User not found"));
                    } else {
                        callback.onSuccess(user);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void updateUser(User user, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.USERS).document(user.getUid()).set(user)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getAllUsers(RepoCallback<List<User>> callback) {
        firestore.collection(FirestoreCollections.USERS).get()
                .addOnSuccessListener(snapshot -> {
                    List<User> users = snapshot.toObjects(User.class);
                    Collections.sort(users, Comparator.comparingLong(User::getCreatedAt).reversed());
                    callback.onSuccess(users);
                })
                .addOnFailureListener(callback::onError);
    }
}
