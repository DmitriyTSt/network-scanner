package ru.dmitriyt.networkscanner.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NetInterface(
    open val name: String,
) : Parcelable {
    @Parcelize
    class Connected(
        override val name: String,
        val ipAddress: String,
        val prefixLength: Short,
        val networkIpAddress: String,
    ) : NetInterface(name)

    @Parcelize
    class Disconnected(override val name: String) : NetInterface(name)
}