package com.kobasato.kmmbrainfuck.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kobasato.kmmbrainfuck.androidApp.ui.AppTheme
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.NavigationViewModel
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.Screen
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor.ProgramEditorScreen
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor.ProgramEditorViewModel
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.list.ProgramListScreen
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.list.ProgramListViewModel
import com.kobasato.kmmbrainfuck.shared.ProgramService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class MainActivity : AppCompatActivity(), DIAware {
    override val di: DI by di()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val programService: ProgramService by instance()

        val navigationViewModel by viewModels<NavigationViewModel>()
        val programEditorViewModel by viewModels<ProgramEditorViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return when (modelClass) {
                        ProgramEditorViewModel::class.java -> {
                            ProgramEditorViewModel(programService) as T
                        }
                        else -> throw RuntimeException("Cannot create an instance of $modelClass")
                    }
                }
            }
        })
        val programListViewModel by viewModels<ProgramListViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return when (modelClass) {
                        ProgramListViewModel::class.java -> {
                            ProgramListViewModel(programService) as T
                        }
                        else -> throw RuntimeException("Cannot create an instance of $modelClass")
                    }
                }
            }
        })

        setContent {
            MyApp {
                Crossfade(current = navigationViewModel.currentScreen) {
                    when (it) {
                        is Screen.List -> ProgramListScreen(
                            navigationViewModel,
                            programListViewModel,
                            programEditorViewModel,
                        )
                        is Screen.Editor -> ProgramEditorScreen(
                            navigationViewModel,
                            programEditorViewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    AppTheme {
        Surface {
            content()
        }
    }
}
