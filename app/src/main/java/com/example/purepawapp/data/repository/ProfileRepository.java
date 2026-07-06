package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.User;

import java.util.List;

public interface ProfileRepository {
    void getUser(String uid, RepoCallback<User> callback);

    void updateUser(User user, RepoCallback<Void> callback);

    void getAllUsers(RepoCallback<List<User>> callback);
}
