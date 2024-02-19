package ru.handh.deviceservice.connector;

import com.tuya.connector.api.annotations.Body;
import com.tuya.connector.api.annotations.GET;
import com.tuya.connector.api.annotations.POST;
import com.tuya.connector.api.annotations.Path;
import ru.handh.deviceservice.dto.DeviceInfoDto;
import ru.handh.deviceservice.dto.CapabilityDto;
import ru.handh.deviceservice.dto.tuya.TuyaSendCommandsRequest;

import java.util.List;

public interface DeviceConnector {

    @GET("/v2.0/cloud/thing/{deviceId}")
    DeviceInfoDto getDeviceInfo(@Path("deviceId") String deviceId);

    @POST("/v1.0/iot-03/devices/{deviceId}/commands")
    Object sendCommand(@Path("deviceId") String deviceId, @Body TuyaSendCommandsRequest body);

    @GET("/v1.0/iot-03/devices/{deviceId}/status")
    List<CapabilityDto> getDeviceStatus(@Path("deviceId") String deviceId);

}
