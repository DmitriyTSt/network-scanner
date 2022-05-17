package ru.dmitriyt.networkscanner.data.model

sealed class NetInterface(
    val name: String,
) {
    class Connected(
        name: String,
        val ipAddress: String,
        val prefixLength: Short,
    ) : NetInterface(name)

    class Disconnected(name: String) : NetInterface(name)
}