package ru.dmitriyt.networkscanner.presentation.ui.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val _items = mutableListOf<T>()

    val items: List<T>
        get() = _items

    override fun getItemCount() = _items.size

    open fun getItem(position: Int) = _items[position]

    @SuppressLint("NotifyDataSetChanged")
    open fun submitList(newItems: List<T>) {
        _items.clear()
        _items.addAll(newItems)
        notifyDataSetChanged()
    }
}