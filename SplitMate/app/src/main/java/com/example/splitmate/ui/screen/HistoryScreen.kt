package com.example.splitmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.splitmate.model.Check
import com.example.splitmate.ui.widget.HistoryItem

@Composable
fun HistoryScreen(
    checks: List<Check>,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
                items(items = checks, key = { it.id }) { check ->
                    HistoryItem(
                        check = check,
                        onClick = { onSelect(check.id) }
                    )
                }
            }
        }
    }
