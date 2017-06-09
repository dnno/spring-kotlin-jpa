package de.rpr.mycity.domain.city.api

import de.rpr.mycity.domain.city.api.dto.CityDto
import de.rpr.mycity.domain.city.api.dto.CreateCityDto
import de.rpr.mycity.domain.city.api.dto.UpdateCityDto

interface CityService {

    fun retrieveCity(cityId: String): CityDto?

    fun retrieveCities(): List<CityDto>

    fun addCity(city: CreateCityDto): CityDto

    fun updateCity(id: String, city: UpdateCityDto): CityDto?
}