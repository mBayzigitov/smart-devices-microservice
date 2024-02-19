package ru.handh.deviceservice.entity

import ru.handh.deviceservice.dto.CapabilityDto
import ru.handh.deviceservice.dto.Device
import ru.handh.deviceservice.dto.DeviceRelationDto
import ru.handh.deviceservice.dto.DeviceSimple
import ru.handh.deviceservice.enum.DeviceCategory
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "devices")
data class DeviceEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "home_id", nullable = false)
    val homeId: Int,

    @Column(name = "room_id")
    val roomId: Int?,

    @Column(nullable = false, name = "tuya_device_id")
    val tuyaDeviceId: String,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    val category: DeviceCategory

) {

    fun toDto(capabilities: List<CapabilityDto>) =
        Device(
            id = id,
            name = name,
            category = category,
            capabilities = capabilities
        )

    fun toSimpleDto() =
        DeviceSimple(
            id = id,
            name = name,
            category = category
        )

    fun toRelationDto() =
        DeviceRelationDto(
            tuyaDeviceId = tuyaDeviceId,
            homeId = homeId,
            roomId = roomId
        )

}
