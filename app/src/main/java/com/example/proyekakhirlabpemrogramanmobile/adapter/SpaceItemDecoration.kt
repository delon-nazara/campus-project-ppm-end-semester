package com.example.proyekakhirlabpemrogramanmobile.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = space
        outRect.bottom = space

        outRect.left = space
        outRect.right = space
    }
}
