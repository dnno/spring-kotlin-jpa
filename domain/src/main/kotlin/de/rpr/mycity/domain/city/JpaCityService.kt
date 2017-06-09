package de.rpr.mycity.domain.city

import de.rpr.mycity.domain.city.api.CityService
import de.rpr.mycity.domain.city.api.dto.CityDto
import de.rpr.mycity.domain.city.api.dto.CreateCityDto
import de.rpr.mycity.domain.city.api.dto.UpdateCityDto
import de.rpr.mycity.domain.city.entity.CityEntity
import de.rpr.mycity.domain.city.repository.CityRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
internal class JpaCityService(val cityRepo: CityRepository, val log: Logger) : CityService {

    override fun retrieveCity(cityId: String): CityDto? {
        log.debug("Retrieving city: {}", cityId)

        return cityRepo.findOne(cityId)?.toDto()
    }

    override fun retrieveCities(): List<CityDto> {
        log.debug("Retrieving cities")

        return cityRepo.findAll().map { it.toDto() }
    }

    override fun updateCity(id: String, city: UpdateCityDto): CityDto? {
        log.debug("Updating city: {} with data: {}", id, city)

        val currentCity = cityRepo.findOne(id)
        return if (currentCity != null) cityRepo.save(CityEntity.fromDto(city, currentCity)).toDto()
        else null
    }

    override fun addCity(city: CreateCityDto): CityDto {
        log.debug("Adding City: {}", city)

        return cityRepo.save(CityEntity.fromDto(city)).toDto()
    }
}