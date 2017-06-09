package de.rpr.mycity.domain.city

import de.rpr.mycity.domain.city.entity.CityEntity
import de.rpr.mycity.domain.city.repository.CityRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(basePackageClasses = arrayOf(CityRepository::class))
@EntityScan(basePackageClasses = arrayOf(CityEntity::class))
@EnableTransactionManagement
internal class InternalCityConfig