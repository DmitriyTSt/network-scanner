package ru.dmitriyt.networkscanner.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NetInterface(
    open val name: String,
    open val isUp: Boolean,
    open val isLoopback: Boolean,
) : Parcelable {
    @Parcelize
    class Connected(
        override val name: String,
        override val isUp: Boolean,
        override val isLoopback: Boolean,
        val currentDevice: NetDevice,
        val prefixLength: Short,
        val networkIpAddress: String,
    ) : NetInterface(name, isUp, isLoopback)

    @Parcelize
    class Disconnected(
        override val name: String,
        override val isUp: Boolean,
        override val isLoopback: Boolean,
    ) : NetInterface(name, isUp, isLoopback)
}