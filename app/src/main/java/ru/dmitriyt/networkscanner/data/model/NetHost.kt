package ru.dmitriyt.networkscanner.data.model

data class NetHost(
    val host: String,
    val addressUInt: UInt,
    val hostName: String?,
)