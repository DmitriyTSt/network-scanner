package ru.dmitriyt.networkscanner.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.dmitriyt.networkscanner.data.Similarable

@Parcelize
data class NetDevice(
    val host: String,
    val addressUInt: UInt,
    val hostName: String?,
    val isCurrentDevice: Boolean = false,
    val mac: String? = null,
) : Similarable<NetDevice>, Parcelable {
    override fun areItemsTheSame(other: NetDevice): Boolean {
        return this.host == other.host
    }

    override fun areContentsTheSame(other: NetDevice): Boolean {
        return this == other
    }
}