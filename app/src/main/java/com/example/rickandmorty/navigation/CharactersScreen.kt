package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.viewmodel.CharacterViewModel

@Composable
fun CharactersScreen(navController: NavHostController) {
    val characterViewModel = viewModel<CharacterViewModel>()
    val state = characterViewModel.state
    Scaffold(
        modifier = Modifier.background(Color.Transparent),
        topBar = {
            TopBar()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(characterViewModel)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(
                            Color.Transparent
                        ),
                    content = {
                        val filteredCharacters = state.characters.filter { character ->
                            character.name?.contains(state.searchQuery, ignoreCase = true) ?: false
                        }
                        items(filteredCharacters.size) { index ->
                            CharacterUI(
                                character = filteredCharacters[index],
                                navController = navController
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun CharacterUI(character: Character, navController: NavHostController) {
    Card(
        Modifier
            .wrapContentSize()
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp),
            ) {
                Text(
                    text = character.name!!,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Origin",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 10.sp
                )
                Text(
                    text = character.origin!!.name!!,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 12.sp
                )
                Text(
                    text = "Status",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 10.sp
                )
                Text(
                    text = character.status!! + " - " + character.species!!,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 12.sp
                )
            }
        }
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Characters") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(.4f)
        )
    )
}

@Composable
fun SearchBar(characterViewModel: CharacterViewModel) {
    TextField(
        value = characterViewModel.state.searchQuery,
        onValueChange = { characterViewModel.updateSearchQuery(it) },
        placeholder = { Text("Search Characters") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White)
    )
}