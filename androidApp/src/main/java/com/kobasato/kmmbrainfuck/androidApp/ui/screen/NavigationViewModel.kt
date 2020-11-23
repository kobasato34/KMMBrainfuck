package com.kobasato.kmmbrainfuck.androidApp.ui.screen

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kobasato.kmmbrainfuck.androidApp.ui.util.getMutableStateOf
import kotlinx.android.parcel.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    object List : Screen()

    @Parcelize
    object Editor : Screen()
}

private const val screenKey = "key_screen"

private fun Screen.toBundle(): Bundle {
    return bundleOf(screenKey to this)
}

private fun Bundle.toScreen(): Screen {
    return requireNotNull(getParcelable(screenKey)) { "Missing key in $this" }
}

class NavigationViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var currentScreen: Screen by savedStateHandle.getMutableStateOf(
        key = "screen",
        default = Screen.List,
        save = { it.toBundle() },
        restore = { it.toScreen() }
    )
        private set

    @MainThread
    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}
