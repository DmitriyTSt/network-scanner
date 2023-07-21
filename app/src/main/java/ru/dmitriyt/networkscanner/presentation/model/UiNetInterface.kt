package ru.dmitriyt.networkscanner.presentation.model

import androidx.annotation.DrawableRes
import ru.dmitriyt.networkscanner.domain.model.NetInterface

data class UiNetInterface(
    val name: String,
    val address: String,
    @DrawableRes val statusIconRes: Int,
    val isLoopback: Boolean,
    val isConnected: Boolean,

    val domainModel: NetInterface,
)