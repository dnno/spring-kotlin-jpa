package de.rpr.mycity.domain.city.api.dto

import de.rpr.mycity.domain.location.api.CoordinateDto

data class UpdateCityDto(
        val name: String?,
        val description: String?,
        val location: CoordinateDto?)