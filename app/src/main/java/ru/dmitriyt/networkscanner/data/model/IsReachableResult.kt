package ru.dmitriyt.networkscanner.data.model

sealed class IsReachableResult {
    object NotExist : IsReachableResult()
    class Exist(
        val device: NetDevice,
    ) : IsReachableResult()
}