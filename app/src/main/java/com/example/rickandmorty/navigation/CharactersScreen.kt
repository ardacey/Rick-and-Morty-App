package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rickandmorty.components.CharacterBottomSheet
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.components.TopBar
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreen(navController: NavHostController) {
    val characterViewModel = koinViewModel<CharacterViewModel>()
    val state = characterViewModel.state
    val showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.background(Color.Transparent),
        topBar = { TopBar("Characters") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(
                    state.searchQuery,
                    {characterViewModel.updateSearchQuery(it)},
                    "Search Characters",
                    showOptionsSheet = { showBottomSheet.value = true })
                LazyColumn(
                    Modifier.fillMaxSize(),
                    content = {
                        val filteredCharacters = state.filteredCharacters
                        items(filteredCharacters.size) { index ->
                            CharacterUI(
                                character = filteredCharacters[index]
                            ) { navController.navigate("Character Details/${filteredCharacters[index].id}") }
                        }
                    }
                )
            }
            if (showBottomSheet.value) {
                CharacterBottomSheet(onDismissRequest = { showBottomSheet.value = false })
            }
        }
    )
}

@Composable
private fun CharacterUI(character: Character, onClick: () -> Unit = { }) {
    Card(
        Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable { onClick.invoke() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)) {
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
                    text = character.name,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Origin",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 10.sp
                )
                Text(
                    text = character.origin.name,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 12.sp
                )
                Text(
                    text = "Status",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 10.sp
                )
                Text(
                    text = character.status + " - " + character.species,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontSize = 12.sp
                )
            }
        }
    }
}