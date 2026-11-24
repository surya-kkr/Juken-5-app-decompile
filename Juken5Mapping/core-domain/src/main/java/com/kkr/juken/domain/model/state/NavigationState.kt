package com.kkr.juken.domain.model.state

data class NavigationState(
    val currentRoute: String = "splash",
    val backstack: List<String> = emptyList(),
    val pendingDeepLink: String? = null
)
