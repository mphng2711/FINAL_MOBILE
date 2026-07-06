package com.example.purepawapp.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    public interface Inflater<VB extends ViewBinding> {
        VB inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToParent);
    }

    private final Inflater<VB> bindingInflater;
    private VB binding;

    protected BaseFragment(Inflater<VB> bindingInflater) {
        this.bindingInflater = bindingInflater;
    }

    protected VB getBinding() {
        return binding;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = bindingInflater.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        observeViewModel();
    }

    protected void setupViews() {
    }

    protected void observeViewModel() {
    }

    protected void showLoading() {
        if (getActivity() instanceof LoadingHost) {
            ((LoadingHost) getActivity()).showLoading();
        }
    }

    protected void hideLoading() {
        if (getActivity() instanceof LoadingHost) {
            ((LoadingHost) getActivity()).hideLoading();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        hideLoading();
    }
}
