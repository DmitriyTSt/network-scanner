package ru.dmitriyt.networkscanner.data

interface Similarable<T> {
    fun areItemsTheSame(other: T): Boolean
    fun areContentsTheSame(other: T): Boolean
}