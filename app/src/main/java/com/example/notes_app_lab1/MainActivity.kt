package com.example.notes_app_lab1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app_lab1.data.entity.Note
import com.example.notes_app_lab1.ui.theme.Purple80
import com.example.notes_app_lab1.ui.theme.RoomDatabaseModuleTheme
import com.example.notes_app_lab1.view_model.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_200)
        setContent {
            RoomDatabaseModuleTheme {
                ListOfNotes(viewModel)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ListOfNotes(viewModel: NoteViewModel) {
    var searchVisible by remember { mutableStateOf(false) }
    var createVisible by remember { mutableStateOf(false) }
    var listNotesVisible by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    viewModel.getAllNotesSortedByDate()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Note App", fontSize = 22.sp) },
            modifier = Modifier.background(Purple80),
            actions = {
                if(!(searchVisible or createVisible)){
                    IconButton(
                        modifier = Modifier
                            .background(Purple80, CircleShape),
                        onClick = {
                            listNotesVisible = true
                            viewModel.getAllNotesSortedByDate()
                        }


                    ) { Icon(Icons.Default.List, contentDescription = "All Notes") }
                }
            }
        )
    },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {
                //ADD
                IconButton(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Purple80, CircleShape),
                    onClick = {
                        searchVisible = false
                        listNotesVisible = !listNotesVisible
                        createVisible = !createVisible
                    }


                ) { Icon(Icons.Filled.Add, contentDescription = "") }
                //SEARCH
                IconButton(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Purple80, CircleShape),
                    onClick = {
                        searchVisible = !searchVisible
                        listNotesVisible = !listNotesVisible
                        createVisible = false
                    }


                ) { Icon(Icons.Filled.Search, contentDescription = "") }
            }
        },


    ) {
        val state = viewModel.state

        AnimatedVisibility(
            visible = listNotesVisible,
            enter = scaleIn(animationSpec = tween(durationMillis = 650)),
            exit = scaleOut(animationSpec = tween(durationMillis = 650))
        ){
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = it.calculateTopPadding())
            ) {
                items(state.value) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clickable { },
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp)){
                            Text("Labels: ${it.labels}\nContent: ${it.content}\nCreatedAt: ${SimpleDateFormat("yyyy.MM.dd").format(Date(it.date))}")
                            IconButton(
                                onClick = {
                                    viewModel.deleteNote(it)

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }


                }
            }
        }




        AnimatedVisibility(
            visible = createVisible,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val focusRequester = remember { FocusRequester() }
                var labels by remember { mutableStateOf("") }
                var noteContent by remember { mutableStateOf("") }
                var date by remember { mutableLongStateOf(0) }

                Column(
                    modifier = Modifier.fillMaxHeight(0.3f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    TextField(value = labels,
                        onValueChange = {
                            labels = it
                        },
                        label = { Text("Enter text labels separated by commas") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusRequester.requestFocus() }
                        ),
                        modifier = Modifier.onKeyEvent {
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                                focusRequester.requestFocus()
                                true
                            }
                            false
                        }
                    )

                    TextField(
                        value = noteContent,
                        label = { Text("Enter note's text") },
                        onValueChange = {
                            noteContent = it
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                    )
                    IconButton(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Purple80, CircleShape),
                        onClick = {
                            focusManager.clearFocus()
                            listNotesVisible = !listNotesVisible
                            createVisible = !createVisible
                            date = java.util.Date().time
                            val newNote = Note(
                                labels = labels,
                                content = noteContent,
                                date = date
                            )
                            viewModel.insertNote(newNote)
                        }


                    ) { Icon(Icons.Filled.Done, contentDescription = "") }

                }
            }

        }
        AnimatedVisibility(
            visible = searchVisible,
            enter = fadeIn(
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            var searchText by remember { mutableStateOf("") }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    TextField(
                        value = searchText,
                        label = { Text("Search Note by Label") },
                        modifier = Modifier.width(220.dp),
                        onValueChange = {
                            searchText = it
                        })
                    IconButton(onClick = {
                        listNotesVisible = !listNotesVisible
                        searchVisible = !searchVisible
                        viewModel.searchNoteByLabel(searchText)
                    }) { Icon(Icons.Filled.Search, contentDescription = "") }
                }

            }

        }
    }
}
