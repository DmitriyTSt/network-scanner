package ru.dmitriyt.networkscanner.data.repository

import android.content.Context
import ru.dmitriyt.networkscanner.domain.repository.ResourceRepository
import javax.inject.Inject

class ResourceRepositoryImpl @Inject constructor(
    private val context: Context,
) : ResourceRepository {

    override fun getString(stringRes: Int): String = context.getString(stringRes)
}