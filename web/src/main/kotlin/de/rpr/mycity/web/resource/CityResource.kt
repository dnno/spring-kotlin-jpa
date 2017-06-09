package de.rpr.mycity.web.resource

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import de.rpr.mycity.domain.city.api.dto.CityDto
import de.rpr.mycity.web.conversion.CoordinateType
import org.springframework.hateoas.ResourceSupport

data class CityResource
@JsonCreator
constructor(
        @JsonProperty("id") val _id: String,
        @JsonProperty("name") val name: String,
        @JsonProperty("desc") val description: String?,
        @JsonProperty("loc") val location: CoordinateType) : ResourceSupport() {

    companion object {

        fun fromDto(dto: CityDto): CityResource =
                CityResource(
                        _id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        location = CoordinateType.fromDto(dto.location)
                )
    }
}