package de.rpr.mycity.web

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import de.rpr.mycity.domain.city.api.CityService
import de.rpr.mycity.domain.city.api.dto.CityDto
import de.rpr.mycity.domain.location.api.CoordinateDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@WebMvcTest(CityController::class)
class CityControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var cityService: CityService

    @TestConfiguration
    class Config {

        @Bean
        fun cityService(): CityService = Mockito.mock(CityService::class.java)
    }

    @Before
    fun setup() {
        reset(cityService)
    }

    @Test
    fun `Retrieving an unknown city should result in status 404`() {
        mockMvc.perform(get("/cities/unknown")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `Creating a city with an invalid request body should result in status 400 `() {
        mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `Creating a city with a valid request body should result in status 201 and a location header`() {
        `when`(cityService.addCity(any()))
                .thenReturn(CityDto("city", "cityname", null, CoordinateDto.origin(), LocalDateTime.now(), LocalDateTime.now()))
        mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"city\", \"name\":\"Cityname\", \"location\": {\"longitude\": 0.0, \"latitude\": 0.0}}"))
                .andExpect(status().isCreated)
                .andExpect(header().string("location", "http://localhost/cities/city"))
        verify(cityService).addCity(any())
    }

    @Test
    fun `Successfully updating a city should result in status 200`() {
        `when`(cityService.updateCity(any(), any()))
                .thenReturn(CityDto("cityId", "name", "description", CoordinateDto(1.0, -1.0), LocalDateTime.now(), LocalDateTime.now()))

        mockMvc.perform(put("/cities/cityId")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Cityname\", \"location\": {\"longitude\": 0.0, \"latitude\": 0.0}}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", equalTo("cityId")))
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.desc", equalTo("description")))
                .andExpect(jsonPath("$.loc.long", equalTo(1.0)))
                .andExpect(jsonPath("$.loc.lat", equalTo(-1.0)))

        verify(cityService).updateCity(eq("cityId"), any())
    }
}

