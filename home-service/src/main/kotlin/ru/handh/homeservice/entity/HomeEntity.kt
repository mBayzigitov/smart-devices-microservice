package ru.handh.homeservice.entity

import ru.handh.homeservice.dto.Home
import ru.handh.homeservice.dto.HomeSimple
import ru.handh.homeservice.dto.UserIdDto
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "homes")
data class HomeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,

    @Column(name = "user_id", nullable = false)
    val userId: Int,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val address: String?,

    @OneToMany(mappedBy = "homeId",
               targetEntity = RoomEntity::class,
               fetch = FetchType.LAZY,
               orphanRemoval = true,
               cascade = [CascadeType.ALL])
    var rooms: List<RoomEntity>? = null
) {
    fun toDto() =
        Home(
            id = id,
            name = name,
            address = address,
            rooms = rooms.orEmpty().map { it.toDto() }
        )

    fun toSimpleDto() =
        HomeSimple(
            id = id,
            name = name
        )

    fun toUserIdDto() =
        UserIdDto(userId = userId)
}