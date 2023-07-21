package ru.dmitriyt.networkscanner.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.dmitriyt.networkscanner.domain.repository.ResourceRepository
import javax.inject.Inject

class ResourceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ResourceRepository {

    override fun getString(stringRes: Int): String = context.getString(stringRes)
}