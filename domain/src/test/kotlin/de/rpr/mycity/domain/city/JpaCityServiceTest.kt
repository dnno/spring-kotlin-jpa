package de.rpr.mycity.domain.city

import de.rpr.mycity.domain.city.api.CityConfig
import de.rpr.mycity.domain.city.api.CityService
import de.rpr.mycity.domain.city.api.dto.CreateCityDto
import de.rpr.mycity.domain.city.api.dto.UpdateCityDto
import de.rpr.mycity.domain.city.entity.CityEntity
import de.rpr.mycity.domain.city.repository.CityRepository
import de.rpr.mycity.domain.location.api.CoordinateDto
import de.rpr.mycity.domain.location.jpa.Coordinate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.slf4j.Logger
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(
        JpaCityServiceTest.Config::class,
        CityConfig::class))
@DataJpaTest
internal class JpaCityServiceTest {

    class Config {

        @Bean
        @Scope("prototype")
        fun logger(injectionPoint: InjectionPoint): Logger = mock(Logger::class.java)
    }

    @Autowired
    lateinit var service: CityService
    @Autowired
    lateinit var repository: CityRepository

    @get:Rule
    var softly = JUnitSoftAssertions()

    @Test
    fun `'retrieveCities' should retrieve empty list if repository doesn't contain entities`() {
        assertThat(service.retrieveCities()).isEmpty()
    }

    @Test
    fun `'retrieveCity' should return null if city for cityId doesnt exist`() {
        assertThat(service.retrieveCity("invalid")).isNull()
    }

    @Test
    fun `'retrieveCity' should map existing entity from repository`() {
        repository.save(CityEntity("city", "cityname", "description", Coordinate(1.0, -1.0)))

        val result = service.retrieveCity("city")
        softly.assertThat(result?.id).isNotNull
        softly.assertThat(result?.name).isEqualTo("cityname")
        softly.assertThat(result?.description).isEqualTo("description")
        softly.assertThat(result?.location).isEqualTo(CoordinateDto(1.0, -1.0))
    }

    @Test
    fun `'retrieveCities' should map entity from repository`() {
        repository.save(CityEntity("city", "cityname", "description", Coordinate(1.0, -1.0)))
        val result = service.retrieveCities()

        softly.assertThat(result).hasSize(1)
        result.forEach {
            softly.assertThat(it.id).isNotNull
            softly.assertThat(it.name).isEqualTo("cityname")
            softly.assertThat(it.description).isEqualTo("description")
            softly.assertThat(it.location).isEqualTo(CoordinateDto(1.0, -1.0))
        }
    }

    @Test
    fun `'addCity' should return created entity`() {
        val (id, name, description, location) = service.addCity(CreateCityDto("id", "name", "description", CoordinateDto(1.0, 1.0)))
        softly.assertThat(id).isEqualTo("id")
        softly.assertThat(name).isEqualTo("name")
        softly.assertThat(description).isEqualTo("description")
        softly.assertThat(location).isEqualTo(CoordinateDto(1.0, 1.0))
    }

    @Test
    fun `'updateCity' should update existing values`() {
        val existingCity = repository.save(CityEntity("city", "cityname", "description", Coordinate(1.0, -1.0))).toDto()

        Thread.sleep(1)

        val result = service.updateCity(existingCity.id, UpdateCityDto("new name", "new description", CoordinateDto(-1.0, -1.0)))

        softly.assertThat(result).isNotNull
        softly.assertThat(result?.id).isEqualTo(existingCity.id)
        softly.assertThat(result?.name).isEqualTo("new name")
        softly.assertThat(result?.description).isEqualTo("new description")
        softly.assertThat(result?.location).isEqualTo(CoordinateDto(-1.0, -1.0))
        softly.assertThat(result?.updatedAt).isAfter(existingCity.updatedAt)
        softly.assertThat(result?.createdAt).isEqualTo(existingCity.createdAt)
    }

    @Test
    fun `'updateCity' shouldn't update null values`() {
        val existingCity = repository.save(CityEntity(
                id = "city",
                name = "cityname",
                description = "description",
                location = Coordinate(1.0, -1.0),
                updatedAt = LocalDateTime.now().minusYears(1))).toDto()

        Thread.sleep(1)

        val result = service.updateCity(existingCity.id, UpdateCityDto(null, null, null))

        softly.assertThat(result).isNotNull
        softly.assertThat(result?.id).isEqualTo(existingCity.id)
        softly.assertThat(result?.name).isEqualTo("cityname")
        softly.assertThat(result?.description).isEqualTo("description")
        softly.assertThat(result?.location).isEqualTo(CoordinateDto(1.0, -1.0))
        softly.assertThat(result?.updatedAt).isAfter(existingCity.updatedAt)
        softly.assertThat(result?.createdAt).isEqualTo(existingCity.createdAt)
    }
}
