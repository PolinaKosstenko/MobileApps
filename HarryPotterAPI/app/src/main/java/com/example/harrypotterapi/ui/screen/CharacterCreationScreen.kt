package com.example.harrypotterapi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.CharacterFields
import com.example.harrypotterapi.ui.viewmodel.FieldError
import com.example.harrypotterapi.ui.viewmodel.UserViewModel
import java.time.LocalDate
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun CharacterCreationScreen(
    userCharacter: Character,
    userHolder: UserViewModel,
    onCreate: () -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }
    val nameErrorMessage = userHolder.nameErrorMessage.collectAsState().value
    val fieldErrorMessage = userHolder.fieldErrorMessage.collectAsState().value
    val fieldErrorActive = userHolder.fieldErrorActive.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary),
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Создание персонажа",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary),
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Создайте своего персонажа для вселенной Гарри Поттера \uD83E\uDE84✨",
                    fontSize = 17.sp
                )
            }
        }


        item {
            OutlinedTextField(
                value = userCharacter.name,
                onValueChange = { showErrors = true; userHolder.onNameChange(it) },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                isError = showErrors && !nameErrorMessage.isEmpty(),
                supportingText = {
                    if (!nameErrorMessage.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = nameErrorMessage,
                            color = colorScheme.error
                        )
                    }
                }
            )
        }

        item {
            AppDropdownList(
                label = "Пол",
                selectedValue = userCharacter.gender,
                options = CharacterFields.genders,
                onOptionSelected = { userHolder.onGenderChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.GENDER.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }

        item {
            OutlinedTextField(
                value = userCharacter.dateOfBirth.toString(),
                onValueChange = { },
                label = { Text("Дата рождения") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            if (showDatePicker) {
                DatePickerModal(
                    label = "Дата рождения",
                    onDateSelected = { userHolder.onDateOfBirthChange(it) },
                    onDismiss = { showDatePicker = false }
                )
            }
        }


        item {
            AppDropdownList(
                label = "Родословная",
                selectedValue = userCharacter.ancestry,
                options = CharacterFields.ancestries,
                onOptionSelected = { userHolder.onAncestryChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.ANCESTRY.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }


        item {
            AppDropdownList(
                label = "Цвет глаз",
                selectedValue = userCharacter.eyeColour,
                options = CharacterFields.eyeColours,
                onOptionSelected = { userHolder.onEyeColorChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.EYE_COLOR.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }


        item {
            AppDropdownList(
                label = "Цвет волос",
                selectedValue = userCharacter.hairColour,
                options = CharacterFields.hairColours,
                onOptionSelected = { userHolder.onHairColorChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.HAIR_COLOR.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }


        item {
            AppDropdownList(
                label = "Патронус",
                selectedValue = userCharacter.patronus,
                options = CharacterFields.patronuses,
                onOptionSelected = { userHolder.onPatronusChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.PATRONUS.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }


        item {
            AppDropdownList(
                label = "Древесина палочки",
                selectedValue = userCharacter.wandWood,
                options = CharacterFields.wandWoods,
                onOptionSelected = { userHolder.onWandWoodChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.WAND_WOOD.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }


        item {
            AppDropdownList(
                label = "Сердцевина палочки",
                selectedValue = userCharacter.wandCore,
                options = CharacterFields.wandCores,
                onOptionSelected = { userHolder.onWandCoreChange(it) },
                showErrors = showErrors && fieldErrorActive[FieldError.WAND_CORE.ordinal],
                fieldErrorMessage = fieldErrorMessage
            )
        }

        item {
            Button(
                onClick = { showErrors = true; userHolder.onCreate(onCreate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Создать")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownList(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showErrors: Boolean = false,
    fieldErrorMessage: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Нажмите для выбора") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = showErrors && !fieldErrorMessage.isEmpty(),
            supportingText = {
                if (showErrors) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = fieldErrorMessage,
                        color = colorScheme.error
                    )
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    label: String,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {


    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year
        }
    })

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        Text(label)
        DatePicker(state = datePickerState)
    }
}
