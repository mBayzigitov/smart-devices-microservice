package ru.handh.deviceservice.service

import com.tuya.connector.api.exceptions.ConnectorResultException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.handh.deviceservice.connector.DeviceConnector
import ru.handh.deviceservice.converter.TuyaConverter
import ru.handh.deviceservice.converter.dictionary.fromTuyaCode
import ru.handh.deviceservice.dto.CapabilityDto
import ru.handh.deviceservice.dto.Device
import ru.handh.deviceservice.dto.DeviceInfoDto
import ru.handh.deviceservice.dto.DeviceSimple
import ru.handh.deviceservice.dto.command.Command
import ru.handh.deviceservice.dto.request.DeviceControlRequest
import ru.handh.deviceservice.dto.request.DeviceInfoRequest
import ru.handh.deviceservice.dto.request.DeviceInfoSimpleRequest
import ru.handh.deviceservice.dto.tuya.TuyaSendCommandsRequest
import ru.handh.deviceservice.entity.DeviceEntity
import ru.handh.deviceservice.enum.DeviceCategory
import ru.handh.deviceservice.error.ApiError
import ru.handh.deviceservice.repository.DeviceRepository

private val COMMANDS_SET =
    setOf("switch_led", "temp_value_v2", "colour_data_v2", "bright_value_v2")

@Service
class DeviceService(
    private val deviceConnector: DeviceConnector,
    private val deviceRepository: DeviceRepository,
    tuyaConverters: List<TuyaConverter<*>>
) {

    private val tuyaConverters = tuyaConverters
        .filterIsInstance<TuyaConverter<Command>>()
        .associateBy { it.code }

    fun createDevice(request: DeviceInfoRequest): Device {
        deviceRepository.findByTuyaDeviceId(request.tuyaDeviceId)?.let {
            throw ApiError.DEVICE_ALREADY_EXISTS.toException()
        }

        val deviceInfo = getDeviceInfo(request.tuyaDeviceId)
        val deviceStatus = getDeviceStatus(request.tuyaDeviceId)

        val deviceName = request.name ?: deviceInfo.product_name
        val deviceEntity = request.toEntity(
            name = deviceName,
            homeId = request.homeId,
            roomId = request.roomId,
        )

        return saveDevice(deviceEntity).toDto(deviceStatus)
    }

    fun editDevice(deviceId: String,
                   request: DeviceInfoSimpleRequest): Device {
        val foundDevice = deviceRepository.findByTuyaDeviceId(deviceId)
            ?: throw ApiError.DEVICE_NOT_FOUND.toException()

        val editedDevice = foundDevice.copy(
            name = request.name,
            homeId = request.homeId,
            roomId = request.roomId
        )

        val deviceStatus = getDeviceStatus(deviceId)

        return deviceRepository.save(editedDevice).toDto(deviceStatus)
    }

    fun getDevice(deviceId: String): Device {
        val foundDevice = deviceRepository.findByTuyaDeviceId(deviceId)
            ?: throw ApiError.DEVICE_NOT_FOUND.toException()

        val deviceStatus = getDeviceStatus(deviceId)

        return foundDevice.toDto(deviceStatus)
    }

    @Transactional
    fun deleteDevice(deviceId: String) =
        deviceRepository.findByTuyaDeviceId(deviceId)?.let {
            deviceRepository.delete(it)
        } ?: throw ApiError.DEVICE_NOT_FOUND.toException()

    fun getAllDevices(homeId: Int,
                      roomId: Int?): List<DeviceSimple> {
        val resultList = roomId?.let {
            deviceRepository.findAllByRoomId(roomId)
        } ?: let {
            deviceRepository.findAllByHomeId(homeId)
        }

        return resultList.map { it.toSimpleDto() }
    }

    fun getDeviceStatus(deviceId: String): List<CapabilityDto> {
        val deviceState = deviceConnector.getDeviceStatus(deviceId)

        return deviceState.filter {
            it.code in COMMANDS_SET
        }.map {
            CapabilityDto(
                code = it.code.fromTuyaCode(),
                value = it.value
            )
        }
    }

    fun sendCommands(deviceId: String,
                     request: DeviceControlRequest) {
        deviceRepository.findByTuyaDeviceId(deviceId)
            ?: throw ApiError.DEVICE_NOT_FOUND.toException()

        return request.capabilities.map {
            tuyaConverters[it.code]?.convert(it) ?: throw ApiError.UNSUPPORTED_COMMAND_ERROR.toException()
        }.let {
            deviceConnector.sendCommand(deviceId, TuyaSendCommandsRequest(it))
        }
    }

    fun getDeviceInfo(tuyaDeviceId: String): DeviceInfoDto {
        try {
            return deviceConnector.getDeviceInfo(tuyaDeviceId)
        } catch (exc: ConnectorResultException) {
            throw ApiError.DEVICE_NOT_FOUND.toException()
        }
    }

    fun getDeviceRelation(tuyaDeviceId: String) =
        deviceRepository.findByTuyaDeviceId(tuyaDeviceId)?.toRelationDto()
            ?: throw ApiError.DEVICE_NOT_FOUND.toException()

    @Transactional
    fun deleteDevicesUsingHomeId(homeId: Int) =
        deviceRepository.deleteAllByHomeId(homeId)

    @Transactional
    fun resetDeviceRoomId(roomId: Int) =
        deviceRepository.resetDeviceRoomId(roomId)

    fun DeviceInfoRequest.toEntity(id: Int = -1,
                                   homeId: Int,
                                   roomId: Int?,
                                   name: String,
                                   category: DeviceCategory = DeviceCategory.LIGHT) =
        DeviceEntity(
            id = id,
            tuyaDeviceId = tuyaDeviceId,
            name = name,
            category = category,
            homeId = homeId,
            roomId = roomId
        )

    @Transactional
    fun saveDevice(device: DeviceEntity) =
        deviceRepository.save(device)

}