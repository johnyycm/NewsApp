package com.example.newsapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.NewViewModel
import com.example.newsapp.R
import com.example.newsapp.ViewState
import com.example.newsapp.network.News
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(newViewModel: NewViewModel) {
    Column {
        SearchView(loadResult = { newViewModel.loadSearchResult(it) })
        NewsList(newViewModel.newsStateFlow)
    }
}

@Composable
fun NewsList(state: StateFlow<ViewState<List<News>>>) {
    val viewState = state.collectAsState().value
    Box(modifier = Modifier.fillMaxSize()) {
        when (viewState) {
            is ViewState.Loaded -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val newsList = viewState.data
                    items(count = newsList.size, itemContent = {
                        newItemView(news = newsList[it])
                    })
                }
            }
            is ViewState.Error -> {
                Text(
                    text = viewState.errorMsg,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is ViewState.None -> {
                Text(
                    text = "Type to search for result",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is ViewState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else -> {}
        }
    }
}

@Composable
fun newItemView(news: News) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(CornerSize(16.dp)),
        elevation = 2.dp
    ) {
        Row {
            AsyncImage(
                model = news.urlToImage,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = news.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = news.description, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun SearchView(loadResult: (searchQuery: String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    TextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                loadResult(textState.value.text)
                focusManager.clearFocus()
            }
        ),
        value = textState.value,
        onValueChange = { value ->
            textState.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (textState.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        textState.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}