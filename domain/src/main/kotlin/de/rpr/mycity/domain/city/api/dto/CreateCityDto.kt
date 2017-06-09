package de.rpr.mycity.domain.city.api.dto

import de.rpr.mycity.domain.location.api.CoordinateDto
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid

data class CreateCityDto(
        @NotEmpty var id: String,
        @NotEmpty var name: String,
        var description: String? = null,
        @Valid var location: CoordinateDto)