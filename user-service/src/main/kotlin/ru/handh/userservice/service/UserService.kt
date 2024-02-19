package ru.handh.userservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.handh.userservice.dto.JwtResponse
import ru.handh.userservice.dto.message.DeletionMessage
import ru.handh.userservice.dto.message.UserDeletionMessage
import ru.handh.userservice.dto.request.AuthDto
import ru.handh.userservice.dto.request.DeleteAccountDto
import ru.handh.userservice.dto.request.RegisterDto
import ru.handh.userservice.entity.OutboxMessageEntity
import ru.handh.userservice.entity.UserEntity
import ru.handh.userservice.error.ApiError
import ru.handh.userservice.repository.OutboxMessageRepository
import ru.handh.userservice.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val outboxMessageRepository: OutboxMessageRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenService: RefreshTokenService,
    @Value(value = "\${topic.user-service.deletion}")
    private val userDeletionTopicName: String,
    private val mapper: ObjectMapper
) {

    fun register(regRequest: RegisterDto): JwtResponse {
        userRepository.findByUsername(regRequest.username)?.let {
            throw ApiError.REGISTRATION_ERROR.toException()
        }

        arePasswordsSame(regRequest)

        val userEntity = saveUser(regRequest.toEntity())

        return refreshTokenService.generateJwtTokens(
            userId = userEntity.id,
            username = regRequest.username
        )
    }

    fun authenticate(authRequest: AuthDto): JwtResponse {
        val user =
            userRepository.findByUsername(authRequest.username) ?: throw ApiError.USER_NOT_FOUND_ERROR.toException()

        val doPasswordsMatch = passwordEncoder.matches(authRequest.password, user.password)
        return if (doPasswordsMatch) {
            refreshTokenService.generateJwtTokens(
                userId = user.id,
                username = authRequest.username
            )
        } else {
            throw ApiError.WRONG_PASSWORD_ERROR.toException()
        }
    }

    fun deleteAccount(request: DeleteAccountDto) {
        val username = jwtService
            .parseTokenInfo(request.accessToken)
            .username

        val user =
            userRepository.findByUsername(username) ?: throw ApiError.USER_NOT_FOUND_ERROR.toException()

        val doPasswordsMatch = passwordEncoder.matches(request.password, user.password)

        if (!doPasswordsMatch) {
            throw ApiError.PASSWORD_CONFIRMATION_ERROR.toException()
        }

        refreshTokenService.deactivateToken(request.accessToken)

        deleteUser(user)
    }

    @Transactional
    fun saveUser(user: UserEntity) =
        userRepository.save(user)

    @Transactional
    fun deleteUser(user: UserEntity) {
        val deletionMessage = UserDeletionMessage(user.id)

        outboxMessageRepository.save(deletionMessage.toEntity())

        userRepository.delete(user)
    }

    private fun arePasswordsSame(request: RegisterDto) {
        if (request.password != request.confirmPassword) {
            throw ApiError.PASSWORD_CONFIRMATION_ERROR.toException()
        }
    }

    private fun RegisterDto.toEntity(id: Int = -1) =
        UserEntity(
            id = id,
            name = name,
            username = username,
            password = passwordEncoder.encode(password)
        )

    private fun DeletionMessage.toEntity(id: Int = -1) =
        OutboxMessageEntity(
            id = id,
            topic = userDeletionTopicName,
            data = mapper.writeValueAsString(this)
        )

}