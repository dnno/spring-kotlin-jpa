package de.rpr.mycity.domain

import java.math.BigDecimal
import javax.persistence.AttributeConverter

class DoubleAttributeConverter : AttributeConverter<Double, BigDecimal?> {

    override fun convertToDatabaseColumn(attribute: Double?) =
            if (attribute != null) { BigDecimal(attribute) } else { null }

    override fun convertToEntityAttribute(dbData: BigDecimal?) =
            dbData?.toDouble()
}