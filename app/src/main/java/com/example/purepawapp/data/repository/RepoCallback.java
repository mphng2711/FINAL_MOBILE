package com.example.purepawapp.data.repository;

public interface RepoCallback<T> {
    void onSuccess(T result);
    void onError(Exception error);
}
