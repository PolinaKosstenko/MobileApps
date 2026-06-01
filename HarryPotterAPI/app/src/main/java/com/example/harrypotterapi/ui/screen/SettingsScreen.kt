package com.example.harrypotterapi.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quiz.ui.theme.ThemeStyle
import com.example.harrypotterapi.R

@Composable
fun SettingsScreen(
    resetGrades: () -> Unit,
    resetUserCharacter: () -> Unit,
    chooseTheme: (theme: ThemeStyle) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = resetGrades
        ) {
            Text(text = "Сбросить прогресс")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = resetUserCharacter,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            )
        ) {
            Text(text = "Отчислиться")
        }

        Card(colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Выберите тему:"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { chooseTheme(ThemeStyle.GRYFFINDOR) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gryffindorlogo),
                            contentDescription = "Background image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Button(
                        onClick = { chooseTheme(ThemeStyle.SLYTHERIN) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.slytherinlogo),
                            contentDescription = "Background image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { chooseTheme(ThemeStyle.RAVENCLAW) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ravenclawlogo),
                            contentDescription = "Background image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Button(
                        onClick = { chooseTheme(ThemeStyle.HUFFLEPUFF) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hufflepufflogo),
                            contentDescription = "Background image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

            }
        }
    }
}
