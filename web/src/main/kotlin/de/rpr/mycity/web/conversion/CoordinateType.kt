package de.rpr.mycity.web.conversion

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.rpr.mycity.domain.location.api.CoordinateDto


@JsonSerialize(using = CoordinateSerializer::class)
@JsonDeserialize(using = CoordinateDeserializer::class)
data class CoordinateType(
        val longitude: Double,
        val latitude: Double) {
    companion object {

        fun fromDto(dto: CoordinateDto): CoordinateType = CoordinateType(
                longitude = dto.longitude,
                latitude = dto.latitude
        )

    }
}

class CoordinateSerializer : JsonSerializer<CoordinateType>() {

    override fun serialize(
            value: CoordinateType?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?) {
        if (gen != null && value != null) {
            gen.writeStartObject()
            gen.writeNumberField("lat", value.latitude)
            gen.writeNumberField("long", value.longitude)
            gen.writeEndObject()
        }
    }

}

class CoordinateDeserializer : JsonDeserializer<CoordinateType?>() {
    override fun deserialize(
            p: JsonParser?,
            ctxt: DeserializationContext?): CoordinateType? {
        if (p != null && ctxt != null) {
            val node: JsonNode = p.codec.readTree(p)
            return CoordinateType(
                    longitude = node.get("long").doubleValue(),
                    latitude = node.get("lat").doubleValue())
        } else {
            return null
        }
    }

}
