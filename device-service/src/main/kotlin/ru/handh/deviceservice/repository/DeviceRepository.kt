package ru.handh.deviceservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.handh.deviceservice.entity.DeviceEntity

interface DeviceRepository : JpaRepository<DeviceEntity, Int> {

    fun findByTuyaDeviceId(tuyaDeviceId: String): DeviceEntity?

    fun findAllByRoomId(roomId: Int): List<DeviceEntity>

    fun findAllByHomeId(homeId: Int): List<DeviceEntity>

    fun deleteAllByHomeId(homeId: Int)

    @Modifying
    @Query("""
        update DeviceEntity device
        set device.roomId = null 
        where device.roomId = :roomId
    """)
    fun resetDeviceRoomId(@Param("roomId") roomId: Int)

}