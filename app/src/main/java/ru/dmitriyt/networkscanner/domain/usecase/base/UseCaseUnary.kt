package ru.dmitriyt.networkscanner.domain.usecase.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.dmitriyt.networkscanner.data.model.LoadableState

abstract class UseCaseUnary<in Params, Result> {

    abstract suspend fun execute(params: Params): Result

    fun executeFlow(params: Params): Flow<LoadableState<Result>> = flow {
        try {
            emit(LoadableState.Loading<Result>())
            val result = execute(params)
            emit(LoadableState.Success(result))
        } catch (t: Throwable) {
            emit(LoadableState.Error<Result>(t))
        }
    }
}