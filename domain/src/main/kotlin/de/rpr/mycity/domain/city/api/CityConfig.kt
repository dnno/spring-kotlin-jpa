package de.rpr.mycity.domain.city.api

import de.rpr.mycity.domain.city.InternalCityConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = arrayOf(InternalCityConfig::class))
class CityConfig
