package ru.dmitriyt.networkscanner.presentation.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.presentation.ui.views.LinearSpaceItemDecoration
import ru.dmitriyt.networkscanner.presentation.ui.views.VerticalDividerItemDecoration

fun RecyclerView.addLinearSpaceItemDecoration(
    @DimenRes spacing: Int = R.dimen.padding_4,
    showFirstVerticalDivider: Boolean = false,
    showLastVerticalDivider: Boolean = false,
    showFirstHorizontalDivider: Boolean = false,
    showLastHorizontalDivider: Boolean = false,
    conditionProvider: LinearSpaceItemDecoration.ConditionProvider? = null,
) {
    this.addItemDecoration(
        LinearSpaceItemDecoration(
            context.resources.getDimensionPixelSize(spacing),
            showFirstVerticalDivider,
            showLastVerticalDivider,
            showFirstHorizontalDivider,
            showLastHorizontalDivider,
            conditionProvider,
        )
    )
}

fun RecyclerView.addVerticalDividerItemDecoration(
    @DrawableRes drawableRes: Int = R.drawable.view_recycler_view_divider,
    firstDividerPosition: Int = 0,
    shouldDrawFirstDivider: Boolean = false,
    endOffset: Int = 1
) {
    fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(this, drawableRes)
    }

    context.getDrawableCompat(drawableRes)?.let {
        addItemDecoration(
            VerticalDividerItemDecoration(
                it,
                firstDividerPosition,
                shouldDrawFirstDivider,
                endOffset
            )
        )
    }
}