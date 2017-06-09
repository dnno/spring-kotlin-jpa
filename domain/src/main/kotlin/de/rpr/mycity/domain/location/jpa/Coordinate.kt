package de.rpr.mycity.domain.location.jpa

import de.rpr.mycity.domain.DoubleAttributeConverter
import de.rpr.mycity.domain.location.api.CoordinateDto
import javax.persistence.Convert
import javax.persistence.Embeddable

@Embeddable
internal data class Coordinate(
        @Convert(converter = DoubleAttributeConverter::class) val longitude: Double,
        @Convert(converter = DoubleAttributeConverter::class) val latitude: Double) {

    private constructor() : this(0.0, 0.0)

    fun toDto(): CoordinateDto = CoordinateDto(this.longitude, this.latitude)

    companion object {

        fun origin() = Coordinate()

        fun fromDto(dto: CoordinateDto): Coordinate = Coordinate(dto.longitude, dto.latitude)
    }
}