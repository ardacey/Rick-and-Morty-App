package com.example.rickandmorty.presentation.ui.settings_screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.R

@Composable
fun Settings(
    navController: NavController,
    isDarkMode : Boolean,
    onDarkModeToggle : (Boolean) -> Unit,
    appVersion: String
) {
    var showDialog by remember { mutableStateOf(false) }
    val project = "https://github.com/ardacey/Rick-and-Morty-App"
    val github = "https://github.com/ardacey"
    val linkedin = "https://www.linkedin.com/in/arda-ceylan/"
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 48.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(48.dp))
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Mode", style = MaterialTheme.typography.displayLarge)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onDarkModeToggle(it) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(end = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Project Link",
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(project))
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.external_link),
                        contentDescription = "Project",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Github Page",
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(github))
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.external_link),
                        contentDescription = "Github",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Linkedin",
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedin))
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.external_link),
                        contentDescription = "Linkedin",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About the App",
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About"
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp, bottom = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = "Version: $appVersion",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(text = "About")
            },
            text = {
                Column {
                    Text(
                        text = "Thank you for using my app! This app was developed by Arda Ceylan. I try to improve my app regularly, so feel free to contact me if you have any problems or suggestions!",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "My E-mail Address:",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "ac.ardaceylan@gmail.com",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            try {
                                context.sendMail("ac.ardaceylan@gmail.com", "About the App")
                            } catch (e: ActivityNotFoundException) {
                                e.printStackTrace()
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    }
}

fun Context.sendMail(to: String, subject: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "vnd.android.cursor.item/email"
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(intent)
}