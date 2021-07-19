package com.mapbox.navigation.core

internal fun interface NavigationSessionStateObserver {
    fun onNavigationSessionStateChanged(navigationSessionState: NavigationSession.State)
}
