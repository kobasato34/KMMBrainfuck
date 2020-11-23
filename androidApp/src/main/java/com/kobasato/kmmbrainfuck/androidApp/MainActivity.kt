package com.kobasato.kmmbrainfuck.androidApp

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.graphics.Color
import com.kobasato.kmmbrainfuck.androidApp.ui.AppTheme
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor.ProgramEditorArguments
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor.ProgramEditorScreen
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.list.ProgramListScreen
import com.kobasato.kmmbrainfuck.shared.ProgramService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

val ApplicationAmbient = ambientOf<Application>()
val ProgramServiceAmbient = ambientOf<ProgramService>()

class MainActivity : AppCompatActivity(), DIAware {
    override val di: DI by di()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val programService: ProgramService by instance()

        setContent {
            Providers(
                ApplicationAmbient provides application,
                ProgramServiceAmbient provides programService
            ) {
                MyApp {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "list") {
                        composable("list") { ProgramListScreen(navController) }
                        composable("editor?id={id}&title={title}&input={input}") {
                            val arguments = ProgramEditorArguments(
                                id = it.arguments?.getString("id"),
                                title = it.arguments?.getString("title") ?: "",
                                input = it.arguments?.getString("input") ?: ""
                            )
                            ProgramEditorScreen(navController, arguments)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    AppTheme {
        Surface(color = Color.Yellow) {
            content()
        }
    }
}
