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
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import androidx.ui.tooling.preview.Preview
import com.kobasato.kmmbrainfuck.androidApp.MyApp
import com.kobasato.kmmbrainfuck.androidApp.ProgramServiceAmbient
import com.kobasato.kmmbrainfuck.androidApp.ui.purple700
import com.kobasato.kmmbrainfuck.db.Program
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun ProgramListScreen(navController: NavController) {
    val programService = ProgramServiceAmbient.current
    val viewModel: ProgramListViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                ProgramListViewModel::class.java -> {
                    ProgramListViewModel(programService) as T
                }
                else -> throw RuntimeException("Cannot create an instance of $modelClass")
            }
        }
    })
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TopAppBar") }, backgroundColor = purple700)
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("editor?title=New program&input=")
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
                    navController.navigate("editor?id=${it.id}&title=${it.title}&input=${it.input}")
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

@ExperimentalCoroutinesApi
@Preview
@Composable
fun ProgramListScreenPreview() {
    val navController = rememberNavController()
    MyApp {
        ProgramListScreen(navController)
    }
}
