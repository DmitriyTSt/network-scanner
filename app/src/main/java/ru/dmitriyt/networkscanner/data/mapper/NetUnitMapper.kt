package ru.dmitriyt.networkscanner.data.mapper

import javax.inject.Inject
import kotlin.math.pow

class NetUnitMapper @Inject constructor() {
    fun macAddressFromByteArray(bytes: ByteArray): String {
        return buildString {
            bytes.forEach {
                if (isNotEmpty()) {
                    append(":")
                }
                append("%02x".format(it))
            }
        }
    }

    fun uIntToIpv4(address: UInt): String {
        return address.toString(2)
            .padStart(32, '0')
            .chunked(8)
            .joinToString(".") { it.toInt(2).toString() }
    }

    fun ipv4ToUInt(address: String): UInt {
        return address.split(".").mapIndexed { index, part -> part.toUInt() * 256.0.pow(3 - index).toUInt() }.sum()
    }

    fun prefixLengthToInt(prefix: Short): UInt {
        val mask = buildString {
            repeat(prefix.toInt()) {
                append('1')
            }
            repeat(32 - prefix) {
                append('0')
            }
        }
        return mask.toUInt(2)
    }
}