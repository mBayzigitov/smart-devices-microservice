package ru.handh.homeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.handh.homeservice.dto.HomeRoomDto
import ru.handh.homeservice.entity.RoomEntity

@Repository
interface RoomRepository : JpaRepository<RoomEntity, Int> {

    @Query("""
        select new ru.handh.homeservice.dto.HomeRoomDto(
            h.id,
            r.id,
            h.userId
        )
        from RoomEntity r
            join HomeEntity h on r.homeId = h.id 
        where r.id = :roomId
    """)
    fun getRoomRelationsByRoomId(@Param("roomId") roomId: Int): HomeRoomDto?

}