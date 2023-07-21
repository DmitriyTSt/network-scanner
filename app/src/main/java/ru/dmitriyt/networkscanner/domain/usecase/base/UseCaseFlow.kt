package ru.dmitriyt.networkscanner.domain.usecase.base

import kotlinx.coroutines.flow.Flow

abstract class UseCaseFlow<in Params, Result> {

    abstract suspend operator fun invoke(params: Params): Flow<Result>
}