package com.example.purepawapp.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacePx;
    private final boolean horizontal;

    public SpacingItemDecoration(int spacePx) {
        this(spacePx, false);
    }

    public SpacingItemDecoration(int spacePx, boolean horizontal) {
        this.spacePx = spacePx;
        this.horizontal = horizontal;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) return;
        if (horizontal) {
            outRect.left = spacePx;
        } else {
            outRect.top = spacePx;
        }
    }
}
