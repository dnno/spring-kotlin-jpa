package de.rpr.mycity.domain.city.entity

import de.rpr.mycity.domain.city.api.dto.CityDto
import de.rpr.mycity.domain.city.api.dto.CreateCityDto
import de.rpr.mycity.domain.city.api.dto.UpdateCityDto
import de.rpr.mycity.domain.location.jpa.Coordinate
import java.time.LocalDateTime
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "city")
internal data class CityEntity(
        @Id val id: String? = null,
        val name: String,
        val description: String? = null,
        @Embedded val location: Coordinate,
        val updatedAt: LocalDateTime = LocalDateTime.now(),
        val createdAt: LocalDateTime = LocalDateTime.now()) {

    // Default constructor for JPA
    @Suppress("unused")
    private constructor() : this(
            name = "",
            location = Coordinate.origin(),
            updatedAt = LocalDateTime.MIN)

    fun toDto(): CityDto = CityDto(
            id = this.id!!,
            name = this.name,
            description = this.description,
            location = this.location.toDto(),
            updatedAt = this.updatedAt,
            createdAt = this.createdAt
    )

    companion object {

        fun fromDto(dto: CityDto) = CityEntity(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                location = Coordinate.fromDto(dto.location),
                updatedAt = dto.updatedAt,
                createdAt = dto.createdAt)

        fun fromDto(dto: CreateCityDto) = CityEntity(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                location = Coordinate(dto.location.longitude, dto.location.latitude))

        fun fromDto(dto: UpdateCityDto, defaultCity: CityEntity) = CityEntity(
                id = defaultCity.id!!,
                name = dto.name ?: defaultCity.name,
                description = dto.description ?: defaultCity.description,
                location = if (dto.location != null) Coordinate(dto.location.longitude, dto.location.latitude) else defaultCity.location,
                updatedAt = LocalDateTime.now(),
                createdAt = defaultCity.createdAt)

    }

}