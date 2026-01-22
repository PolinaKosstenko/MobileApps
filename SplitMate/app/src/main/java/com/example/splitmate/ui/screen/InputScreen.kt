package com.example.splitmate.ui.screen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splitmate.model.Check
import com.example.splitmate.ui.viewmodel.SplitMateUIState


@Composable
fun InputScreen(
    uiState: SplitMateUIState,
    validTotal: Boolean,
    validPeople: Boolean,
    validTip: Boolean,
    onChangeTotal: (String) -> Unit,
    onChangePeople: (String) -> Unit,
    onChangeTip: (String) -> Unit,
    onSaveCheck: () -> Unit,
    onLoadResult: () -> Unit
) {
    Card(
        modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OutlinedTextField(
                value = uiState.currTotal,
                onValueChange = onChangeTotal,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                label = @Composable { Text("Total") },
                singleLine = true,
                isError = !validTotal
            )

            OutlinedTextField(
                value = uiState.currPeople,
                onValueChange = onChangePeople,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                label = @Composable { Text("People") },
                singleLine = true,
                isError = !validPeople
            )

            OutlinedTextField(
                value = uiState.currTip,
                onValueChange = onChangeTip,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                label = @Composable { Text("Tip") },
                singleLine = true,
                isError = !validTip
            )

            Button(
                enabled = (validTotal && validPeople && validTip),
                onClick = {
                    onSaveCheck()
                    onLoadResult()
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Calculate")
            }
        }
    }
}
