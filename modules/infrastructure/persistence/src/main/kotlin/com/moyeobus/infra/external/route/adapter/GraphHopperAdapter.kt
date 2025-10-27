package com.moyeobus.infra.external.route.adapter

import com.graphhopper.GHRequest
import com.graphhopper.GraphHopper
import com.graphhopper.config.Profile
import com.graphhopper.util.CustomModel
import com.graphhopper.util.shapes.GHPoint
import com.moyeobus.application.address.dto.RouteDataWrapper
import com.moyeobus.application.route.port.out.RouteEngineOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class GraphHopperAdapter(
    private val addressRepo: AddressJpaRepository
) : RouteEngineOutPort {
    private val hopper = GraphHopper().apply {
        osmFile = "modules/infrastructure/persistence/src/main/resources/osm/south-korea-251017.osm.pbf"
        graphHopperLocation = "build/graph-cache"

        val customModel = CustomModel()
        setProfiles(
            Profile("car")
                .setVehicle("car")
                .setWeighting("custom")
                .setCustomModel(customModel)
        )

        importOrLoad()
    }

    override fun calculatePath(stops: List<Address>): RouteDataWrapper {
        val req = GHRequest().apply {
            stops.forEach { addPoint(GHPoint(it.lat, it.lon)) }
            profile = "car"
            locale = Locale.KOREA
        }
        val rsp = hopper.route(req)
        if (rsp.hasErrors()) throw IllegalStateException("경로 탐색 실패: ${rsp.errors}")

        val path = rsp.best
        return RouteDataWrapper(stops, path.distance / 1000.0, path.time / 60000.0)
    }
}