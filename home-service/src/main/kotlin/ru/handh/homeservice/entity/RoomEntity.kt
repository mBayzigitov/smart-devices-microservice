package ru.handh.homeservice.entity

import org.jetbrains.annotations.NotNull
import ru.handh.homeservice.dto.Room
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

@Entity
@Table(name = "rooms")
class RoomEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,

    val homeId: Int,

    @NotNull
    val name: String

) {
    fun toDto() =
        Room(
            id = id,
            name = name
        )
}