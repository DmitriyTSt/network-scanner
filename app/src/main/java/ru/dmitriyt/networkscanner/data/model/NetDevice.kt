package ru.dmitriyt.networkscanner.data.model

data class NetDevice(
    val host: String,
    val addressUInt: UInt,
    val hostName: String?,
    val isCurrentDevice: Boolean = false,
    val mac: String? = null,
)