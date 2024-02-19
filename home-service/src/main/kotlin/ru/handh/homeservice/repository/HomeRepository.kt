package ru.handh.homeservice.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.handh.homeservice.entity.HomeEntity

@Repository
interface HomeRepository : JpaRepository<HomeEntity, Int> {

    @EntityGraph(attributePaths = ["rooms"], type = EntityGraph.EntityGraphType.FETCH)
    fun findHomeEntityById(id: Int): HomeEntity?

    fun findAllByUserId(userId: Int): List<HomeEntity>

    fun deleteAllByUserId(userId: Int)

}