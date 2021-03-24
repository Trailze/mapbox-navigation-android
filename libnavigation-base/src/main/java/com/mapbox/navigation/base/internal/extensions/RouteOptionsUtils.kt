@file:JvmName("MapboxRouteOptionsUtils")

package com.mapbox.navigation.base.internal.extensions

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Point

/**
 * Applies default [RouteOptions] parameters to the RouteOptions builder
 *
 * @receiver RouteOptions.Builder
 * @return RouteOptions.Builder
 */
fun RouteOptions.Builder.applyDefaultParams(): RouteOptions.Builder = also {
    baseUrl(Constants.BASE_API_URL)
    user(Constants.MAPBOX_USER)
    profile(DirectionsCriteria.PROFILE_DRIVING)
    geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
    requestUuid("")
}

/**
 * Takes a list of [Point]s and correctly adds them as waypoints in the correct order.
 *
 * @receiver RouteOptions.Builder
 * @param origin Point
 * @param waypoints List<Point?>?
 * @param destination Point
 * @return RouteOptions.Builder
 */
@JvmOverloads
fun RouteOptions.Builder.coordinates(
    origin: Point,
    waypoints: List<Point?>? = null,
    destination: Point
): RouteOptions.Builder {
    val coordinates = mutableListOf<Point>().apply {
        add(origin)
        waypoints?.filterNotNull()?.forEach { add(it) }
        add(destination)
    }

    coordinates(coordinates)

    return this
}
