package ru.dmitriyt.networkscanner.domain.repository

interface ArpTableRepository {
    /**
     * Arp таблица
     * @return map: ключ - ip адрес, значение - mac адрес
     */
    suspend fun getArpTable(): Map<String, String>
}