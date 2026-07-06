package com.example.purepawapp.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val spacePx: Int,
    private val horizontal: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) return
        if (horizontal) {
            outRect.left = spacePx
        } else {
            outRect.top = spacePx
        }
    }
}
