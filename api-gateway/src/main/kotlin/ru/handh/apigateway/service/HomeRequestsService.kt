package ru.handh.apigateway.service

import org.springframework.stereotype.Service
import ru.handh.apigateway.client.HomeRequestsClient
import ru.handh.apigateway.dto.Home
import ru.handh.apigateway.dto.HomeSimple
import ru.handh.apigateway.dto.UserHomeDto
import ru.handh.apigateway.dto.request.HomeRequest

@Service
class HomeRequestsService(
    private val homeRequestsClient: HomeRequestsClient,
    private val gatewayService: GatewayService
) {

    fun createHome(request: HomeRequest): Home {
        val userId = RequestContext.get()

        return homeRequestsClient.createHome(
            UserHomeDto(
                userId = userId,
                name = request.name,
                address = request.address
            )
        )
    }

    fun editHome(request: HomeRequest,
                 homeId: Int): Home {
        gatewayService.checkHomeOwner(homeId)

        return homeRequestsClient.editHome(
            request = UserHomeDto(
                userId = RequestContext.get(),
                name = request.name,
                address = request.address
            ),
            homeId = homeId
        )
    }

    fun getHomeInfo(homeId: Int): Home {
        gatewayService.checkHomeOwner(homeId)

        return homeRequestsClient.getHomeInfo(homeId)
    }

    fun getAllHomes(): List<HomeSimple> {
        val userId = RequestContext.get()

        return homeRequestsClient.getAllHomes(userId)
    }

    fun deleteHome(homeId: Int) {
        gatewayService.checkHomeOwner(homeId)

        homeRequestsClient.deleteHome(homeId)
    }

}