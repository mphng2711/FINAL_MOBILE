package com.example.purepawapp.data.repository;

public interface AuthRepository {
    String getCurrentUserId();

    void signUp(String name, String email, String password, String phone, String role, RepoCallback<String> callback);

    void login(String email, String password, RepoCallback<String> callback);

    void sendPasswordReset(String email, RepoCallback<Void> callback);

    void logout();
}
