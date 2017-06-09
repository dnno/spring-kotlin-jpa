package de.rpr.mycity.web

import de.rpr.mycity.domain.city.api.CityService
import de.rpr.mycity.domain.city.api.dto.CreateCityDto
import de.rpr.mycity.domain.city.api.dto.UpdateCityDto
import de.rpr.mycity.web.CITIES_PATH
import de.rpr.mycity.web.resource.CityResource
import org.slf4j.Logger
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping(
        value = CITIES_PATH,
        produces = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE))
class CityController(val cityService: CityService, val log: Logger) {

    @GetMapping
    fun retrieveCities(): HttpEntity<Resources<CityResource>> {
        log.debug("Retrieving cities")

        val result = cityService.retrieveCities()
        return ResponseEntity.ok(Resources(result.map { CityResource.fromDto(it) }))
    }


    @GetMapping("{id}")
    fun retrieveCity(@PathVariable("id") cityId: String): HttpEntity<CityResource> {
        log.debug("Retrieving city: {}", cityId)

        val result = cityService.retrieveCity(cityId)
        if (result != null) {
            val resource = CityResource.fromDto(result)
            resource.add(linkTo(methodOn(this::class.java).retrieveCity(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping(consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.APPLICATION_XML_VALUE))
    fun addCity(@RequestBody city: CreateCityDto, uriBuilder: UriComponentsBuilder): HttpEntity<CityResource> {
        log.debug("Request to add a city")

        val result = cityService.addCity(city)
        val resource = CityResource.fromDto(result)
        resource.add(linkTo(methodOn(this::class.java).retrieveCity(result.id)).withSelfRel())
        return ResponseEntity
                .created(uriBuilder.path("$CITIES_PATH/{id}").buildAndExpand(result.id).toUri())
                .body(resource)
    }

    @PutMapping("{id}")
    fun updateCity(@PathVariable("id") cityId: String, @RequestBody city: UpdateCityDto): HttpEntity<CityResource> {
        log.debug("Request to update city: {}", cityId)

        val result = cityService.updateCity(cityId, city)
        if (result != null) {
            val resource = CityResource.fromDto(result)
            resource.add(linkTo(methodOn(this::class.java).retrieveCity(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}

