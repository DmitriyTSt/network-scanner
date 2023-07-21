package ru.dmitriyt.networkscanner.domain.repository

interface ResourceRepository {
    fun getString(stringRes: Int): String
}