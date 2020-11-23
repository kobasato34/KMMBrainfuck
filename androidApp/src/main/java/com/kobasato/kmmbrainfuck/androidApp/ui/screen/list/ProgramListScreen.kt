package com.kobasato.kmmbrainfuck.androidApp.ui.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kobasato.kmmbrainfuck.androidApp.ui.purple700
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.NavigationViewModel
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.Screen
import com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor.ProgramEditorViewModel
import com.kobasato.kmmbrainfuck.db.Program
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun ProgramListScreen(
    navigationViewModel: NavigationViewModel,
    programListViewModel: ProgramListViewModel,
    programEditorViewModel: ProgramEditorViewModel
) {
    val state = programListViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("KMMBrainf*ck") }, backgroundColor = purple700)
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    programEditorViewModel.clear()
                    navigationViewModel.navigateTo(Screen.Editor)
                },
                backgroundColor = purple700,
            ) {
                Text("+")
            }
        },
        bodyContent = {
            ProgramListView(
                programList = state.value.programList,
                onClickItem = {
                    programEditorViewModel.load(it)
                    navigationViewModel.navigateTo(Screen.Editor)
                }
            )
        },
    )
}

@Composable
fun ProgramListView(programList: List<Program>, onClickItem: (Program) -> Unit) {
    LazyColumn {
        items(programList) { program ->
            ListItem(
                text = { Text(program.title) },
                modifier = Modifier.clickable(onClick = {
                    onClickItem(program)
                })
            )
            Divider(color = Color.LightGray)
        }
    }
}
