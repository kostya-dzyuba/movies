package ru.kostyadzyuba.movies

import androidx.recyclerview.widget.DiffUtil

class MoviesDiff(
    private val old: List<Movie>,
    private val new: List<Movie>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].name == new[newItemPosition].name &&
                old[oldItemPosition].year == new[newItemPosition].year

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]
}