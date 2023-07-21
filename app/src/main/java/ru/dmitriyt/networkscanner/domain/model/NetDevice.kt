package ru.dmitriyt.networkscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetDevice(
    val host: String,
    val addressUInt: UInt,
    val hostName: String?,
    val isCurrentDevice: Boolean = false,
    val mac: String? = null,
) : Parcelable
