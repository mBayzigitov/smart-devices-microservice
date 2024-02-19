package ru.handh.userservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.handh.userservice.entity.UserEntity

interface UserRepository: JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String) : UserEntity?

}