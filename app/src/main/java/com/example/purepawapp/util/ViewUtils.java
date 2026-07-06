package com.example.purepawapp.util;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public final class ViewUtils {

    private ViewUtils() {
    }

    public static void visible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void gone(View view) {
        view.setVisibility(View.GONE);
    }

    public static void invisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void setVisibleIf(View view, boolean condition) {
        view.setVisibility(condition ? View.VISIBLE : View.GONE);
    }

    public static void toast(Fragment fragment, String message) {
        Toast.makeText(fragment.requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
