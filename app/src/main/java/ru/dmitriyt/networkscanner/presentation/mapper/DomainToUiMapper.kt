package ru.dmitriyt.networkscanner.presentation.mapper

interface DomainToUiMapper<DomainModel, UiModel> {

    fun fromDomainToUi(domain: DomainModel): UiModel

    fun fromDomainToUi(domains: List<DomainModel>): List<UiModel> = domains.map { fromDomainToUi(it) }
}