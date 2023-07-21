package ru.dmitriyt.networkscanner.domain.model

data class NetHost(
    val host: String,
    val addressUInt: UInt,
    val hostName: String?,
)