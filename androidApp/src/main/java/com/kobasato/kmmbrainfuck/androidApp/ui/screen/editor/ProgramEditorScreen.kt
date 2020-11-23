package com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.ui.tooling.preview.Preview
import com.kobasato.kmmbrainfuck.androidApp.MyApp
import com.kobasato.kmmbrainfuck.androidApp.ProgramServiceAmbient
import com.kobasato.kmmbrainfuck.androidApp.ui.purple700
import com.kobasato.kmmbrainfuck.db.Program
import com.kobasato.kmmbrainfuck.shared.brainfuck.Output
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.Serializable

data class ProgramEditorArguments(
    val id: String?,
    val title: String,
    val input: String,
) : Serializable

@ExperimentalCoroutinesApi
@Composable
fun ProgramEditorScreen(navController: NavController, arguments: ProgramEditorArguments) {
    val programService = ProgramServiceAmbient.current
    val viewModel: ProgramEditorViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                ProgramEditorViewModel::class.java -> {
                    if (arguments.id == null) {
                        ProgramEditorViewModel(
                            programService,
                            arguments.title,
                            arguments.input
                        ) as T
                    } else {
                        val program = Program(arguments.id, arguments.title, arguments.input)
                        ProgramEditorViewModel(programService, program) as T
                    }
                }
                else -> throw RuntimeException("Cannot create an instance of $modelClass")
            }
        }
    })
    val state = viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                backgroundColor = purple700,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                },
                actions = {
                    Providers(AmbientContentAlpha provides ContentAlpha.high, children = {
                        // Search icon
                        Icon(
                            asset = Icons.Outlined.Save,
                            modifier = Modifier
                                .clickable(onClick = {
                                    viewModel.saveProgram()
                                })
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                                .preferredHeight(24.dp)
                        )
                    })
                })
        },
        bodyContent = {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                ProgramEditor(state.value.title, state.value.input, {
                    viewModel.onTitleChange(it)
                }, {
                    viewModel.onInputChange(it)
                })
                Spacer(modifier = Modifier.preferredHeight(16.dp))
                Button(
                    onClick = {
                        viewModel.runProgram()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Run")
                }
                Spacer(modifier = Modifier.preferredHeight(16.dp))
                ProgramOutput(state.value.output)
            }
        },
    )
}

@Composable
fun ProgramEditor(
    title: String,
    input: String,
    onTitleChange: (String) -> Unit,
    onInputChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.preferredHeight(16.dp))
        OutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            label = { Text("Input") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun ProgramOutput(output: Output) {
    when (output) {
        is Output.Success -> {
            Text(output.outputString)
        }
        is Output.Error -> {
            Text(output.cause.message ?: "", color = Color.Red)
        }
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
fun ProgramEditorScreenPreview() {
    val navController = rememberNavController()
    MyApp {
        ProgramEditorScreen(navController, ProgramEditorArguments("id", "title", "input"))
    }
}
