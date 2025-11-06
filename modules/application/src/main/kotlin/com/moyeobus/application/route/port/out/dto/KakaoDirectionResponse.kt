package com.moyeobus.application.route.port.out.dto

data class KakaoDirectionResponse(
    val routes: List<Route>? = null
) {
    data class Route(
        val result: Result?,
        val summary: Summary?,
        val sections: List<Section>?
    )
    data class Result(val code: Int, val msg: String)
    data class Summary(
        val origin: Point,
        val destination: Point,
        val distance: Int,
        val duration: Int
    )
    data class Section(
        val distance: Int,
        val duration: Int,
        val guides: List<Guide>?
    )
    data class Guide(
        val name: String?,
        val x: Double,
        val y: Double,
        val distance: Int,
        val duration: Int
    )
}
