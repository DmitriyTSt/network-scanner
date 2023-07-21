package ru.dmitriyt.networkscanner.presentation.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import ru.dmitriyt.networkscanner.presentation.diffutil.Similarable

@Parcelize
data class UiNetDevice(
    val host: String,
    val addressUInt: UInt,
    val hostName: String,
    @DrawableRes val iconRes: Int,
    val mac: String? = null,
) : Similarable<UiNetDevice>, Parcelable {
    override fun areItemsTheSame(other: UiNetDevice): Boolean {
        return this.host == other.host
    }

    override fun areContentsTheSame(other: UiNetDevice): Boolean {
        return this == other
    }
}