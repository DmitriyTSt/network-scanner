package ru.dmitriyt.networkscanner.domain.usecase.base

abstract class UseCaseUnary<in Params, Result> {

    abstract suspend operator fun invoke(params: Params): Result
}