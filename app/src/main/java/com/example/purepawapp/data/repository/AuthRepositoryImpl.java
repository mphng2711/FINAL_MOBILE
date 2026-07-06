package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.User;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public AuthRepositoryImpl(FirebaseAuth auth, FirebaseFirestore firestore) {
        this.auth = auth;
        this.firestore = firestore;
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    @Override
    public void signUp(String name, String email, String password, String phone, String role, RepoCallback<String> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    if (firebaseUser == null) {
                        callback.onError(new IllegalStateException("Sign up failed: no user id returned"));
                        return;
                    }
                    String uid = firebaseUser.getUid();
                    User user = new User();
                    user.setUid(uid);
                    user.setFullName(name);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setRole(role);
                    firestore.collection(FirestoreCollections.USERS).document(uid).set(user)
                            .addOnSuccessListener(v -> callback.onSuccess(uid))
                            .addOnFailureListener(callback::onError);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void login(String email, String password, RepoCallback<String> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    if (firebaseUser == null) {
                        callback.onError(new IllegalStateException("Login failed: no user id returned"));
                    } else {
                        callback.onSuccess(firebaseUser.getUid());
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void logout() {
        auth.signOut();
    }
}
