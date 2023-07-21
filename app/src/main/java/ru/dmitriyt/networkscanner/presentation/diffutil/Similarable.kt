package ru.dmitriyt.networkscanner.presentation.diffutil

interface Similarable<T> {
    fun areItemsTheSame(other: T): Boolean
    fun areContentsTheSame(other: T): Boolean
}