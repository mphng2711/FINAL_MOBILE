package com.example.purepawapp.ui.common;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T extends ViewModel> implements ViewModelProvider.Factory {

    public interface Creator<T extends ViewModel> {
        T create();
    }

    private final Creator<T> creator;

    public ViewModelFactory(Creator<T> creator) {
        this.creator = creator;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <U extends ViewModel> U create(@NonNull Class<U> modelClass) {
        return (U) creator.create();
    }
}
