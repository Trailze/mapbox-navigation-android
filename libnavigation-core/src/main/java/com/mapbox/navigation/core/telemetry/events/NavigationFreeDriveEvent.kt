package com.mapbox.navigation.core.telemetry.events

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.gson.Gson
import com.mapbox.android.telemetry.Event
import com.mapbox.android.telemetry.TelemetryUtils
import com.mapbox.navigation.base.metrics.MetricEvent
import com.mapbox.navigation.base.metrics.NavigationMetrics

@SuppressLint("ParcelCreator")
internal class NavigationFreeDriveEvent(
    phoneState: PhoneState
) : Event(), MetricEvent {

    val created: String = TelemetryUtils.obtainCurrentDate() // Schema pattern
    val volumeLevel: Int = phoneState.volumeLevel
    val batteryLevel: Int = phoneState.batteryLevel
    val screenBrightness: Int = phoneState.screenBrightness
    val batteryPluggedIn: Boolean = phoneState.isBatteryPluggedIn
    val connectivity: String? = phoneState.connectivity
    val audioType: String = phoneState.audioType
    val applicationState: String = phoneState.applicationState // Schema minLength 1
    val event: String = NavigationMetrics.FREE_DRIVE
    var eventVersion: Int = 0
    var locationEngine: String? = null
    var percentTimeInPortrait: Int = 0
    var percentTimeInForeground: Int = 0
    var simulation: Boolean = false

    // Schema pattern TelemetryUtils.obtainCurrentDate() - Timestamp when user started navigation
    var navigatorSessionIdentifier: String? = null // group id of modes under one Telemetry session
    var startTimestamp: String? = null // mode start time
    var sessionIdentifier: String? = null // mode id

    var location: TelemetryLocation? = null
    var eventType: String? = null

    override val metricName: String
        get() = NavigationMetrics.FREE_DRIVE

    override fun toJson(gson: Gson): String = gson.toJson(this)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
    }
}

internal enum class FreeDriveEventType(val type: String) {
    START("start"),
    STOP("stop")
}
