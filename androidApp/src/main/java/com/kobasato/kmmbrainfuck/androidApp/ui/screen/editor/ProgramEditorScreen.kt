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
import com.kobasato.kmmbrainfuck.androidApp.ui.purple700
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.NavigationViewModel
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.Screen
import com.kobasato.kmmbrainfuck.shared.brainfuck.Output
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun ProgramEditorScreen(
    navigationViewModel: NavigationViewModel,
    programEditorViewModel: ProgramEditorViewModel
) {
    val state = programEditorViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                backgroundColor = purple700,
                navigationIcon = {
                    IconButton(onClick = {
                        navigationViewModel.navigateTo(Screen.List)
                    }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                },
                actions = {
                    Providers(AmbientContentAlpha provides ContentAlpha.high, children = {
                        Icon(
                            asset = Icons.Outlined.Save,
                            modifier = Modifier
                                .clickable(onClick = {
                                    programEditorViewModel.saveProgram()
                                })
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                                .preferredHeight(24.dp)
                        )
                    })
                },
            )
        },
        bodyContent = {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                ProgramEditor(state.value.title, state.value.input, {
                    programEditorViewModel.onTitleChange(it)
                }, {
                    programEditorViewModel.onInputChange(it)
                })
                Spacer(modifier = Modifier.preferredHeight(16.dp))
                Button(
                    onClick = {
                        programEditorViewModel.runProgram()
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
