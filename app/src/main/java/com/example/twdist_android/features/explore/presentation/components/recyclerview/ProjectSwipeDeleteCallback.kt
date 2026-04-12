package com.example.twdist_android.features.explore.presentation.components.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.twdist_android.R

class ProjectSwipeDeleteCallback(
    private val adapter: ProjectListRecyclerAdapter,
    var deleteSwipeLabel: String,
    private val onSwipeDeleteThreshold: (projectId: Long) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textAlign = Paint.Align.CENTER
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) return
        val project = adapter.currentList.getOrNull(position) ?: return
        adapter.notifyItemChanged(position)
        onSwipeDeleteThreshold(project.id)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.4f

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = defaultValue * 6f

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0f) {
            val dm = recyclerView.resources.displayMetrics
            val density = dm.density

            // Same corner radius as the project row card (12dp).
            val cardCornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                dm
            )
            val horizontalPad = 8f * density

            redPaint.color = ContextCompat.getColor(recyclerView.context, R.color.swipe_delete_background)

            val itemView = viewHolder.itemView
            val left = itemView.left.toFloat()
            val top = itemView.top.toFloat()
            val right = left + dX
            val bottom = itemView.bottom.toFloat()

            val redRect = RectF(left, top, right, bottom)
            // Cap radius when the strip is narrow so drawRoundRect stays valid and matches the row feel.
            val rowHeight = bottom - top
            val cornerR = minOf(cardCornerRadius, dX / 2f, rowHeight / 2f)
            c.drawRoundRect(redRect, cornerR, cornerR, redPaint)

            val label = deleteSwipeLabel
            val baseTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                16f,
                dm
            )
            val minTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                10f,
                dm
            )

            textPaint.textSize = baseTextSize
            var textWidth = textPaint.measureText(label)
            val maxTextWidth = (dX - 2 * horizontalPad).coerceAtLeast(0f)

            while (textWidth > maxTextWidth && textPaint.textSize > minTextSize) {
                textPaint.textSize -= 0.5f
                textWidth = textPaint.measureText(label)
            }

            val textX = left + dX / 2f
            val fontMetrics = textPaint.fontMetrics
            val textY = top + rowHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f

            if (dX > horizontalPad && maxTextWidth > 0f) {
                c.save()
                if (textWidth > maxTextWidth) {
                    val scaleX = (maxTextWidth / textWidth).coerceIn(0.45f, 1f)
                    c.scale(scaleX, 1f, textX, textY)
                }
                c.drawText(label, textX, textY, textPaint)
                c.restore()
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.translationX = 0f
        viewHolder.itemView.translationY = 0f
    }
}
