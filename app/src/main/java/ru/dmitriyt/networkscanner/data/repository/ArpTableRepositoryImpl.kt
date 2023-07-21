package ru.dmitriyt.networkscanner.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import ru.dmitriyt.networkscanner.domain.repository.ArpTableRepository
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ArpTableRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) : ArpTableRepository {

    companion object {
        private const val MAC_REGEX_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    }

    /**
     * Arp таблица
     * @return map: ключ - ip адрес, значение - mac адрес
     */
    override suspend fun getArpTable(): Map<String, String> = withContext(dispatcherProvider.io) {
        suspendCoroutine { continuation ->
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getArpTableApi29Impl()
            } else {
                getArpTableApiLowImpl()
            }
            continuation.resume(result)
        }
    }

    @RequiresApi(29)
    private fun getArpTableApi29Impl(): Map<String, String> {
        // Android 10 access denied to /proc/net/arp
        return emptyMap()
    }

    private fun getArpTableApiLowImpl(): Map<String, String> {
        val maxRegex = MAC_REGEX_PATTERN.toRegex()
        var bufferedReader: BufferedReader? = null
        val result = mutableMapOf<String, String>()
        try {
            bufferedReader = BufferedReader(FileReader("/proc/net/arp"))
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val splitted = line.split("\\s+".toRegex()).toTypedArray()
                if (splitted.size >= 4) {
                    val ip = splitted[0]
                    val mac = splitted[3]
                    if (mac.matches(maxRegex)) {
                        result[ip] = mac
                    }
                }
                line = bufferedReader.readLine()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }
}