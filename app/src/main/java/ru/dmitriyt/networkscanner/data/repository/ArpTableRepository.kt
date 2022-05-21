package ru.dmitriyt.networkscanner.data.repository

interface ArpTableRepository {
    suspend fun getArpTable(): Map<String, String>
}