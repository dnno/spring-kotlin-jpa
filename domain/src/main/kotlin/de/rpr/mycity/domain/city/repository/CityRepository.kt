package de.rpr.mycity.domain.city.repository

import de.rpr.mycity.domain.city.entity.CityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
internal interface CityRepository : JpaRepository<CityEntity, String>