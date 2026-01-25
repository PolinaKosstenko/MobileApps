package com.example.splitmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.splitmate.model.Check
import java.time.LocalDate
import kotlin.Int

data class SplitMateUIState(
    val currTotal: String = "",
    val currPeople: String = "",
    val currTip: String = "10"
)

class SplitMateViewModel: ViewModel() {
    val checks: MutableList<Check> = mutableListOf()
    var uiState: SplitMateUIState by mutableStateOf(SplitMateUIState())

    val validTotal: Boolean
        get() {
            return uiState.currTotal.toIntOrNull() != null && uiState.currTotal.toInt() > 0
        }

    val validPeople: Boolean
        get() {
            return uiState.currPeople.toIntOrNull() != null && uiState.currPeople.toInt() > 0
        }

    val validTip: Boolean
        get() {
            return uiState.currTip.toIntOrNull() != null && uiState.currTip.toInt() in 0..100
        }

    fun tipAmount(total: Int, tip: Int): Int {
        return total / 100 * tip
    }


    fun totalWithTip(total: Int,  tip: Int): Int {
        return total + tipAmount(total, tip)
    }

    fun perPerson(total: Int,  tip: Int, people: Int): Int {
        return totalWithTip(total, tip) / people
    }

    fun onChangeTotal(total: String) {
        uiState = uiState.copy(
            currTotal = total
        )
    }

    fun onChangePeople(people: String) {
        uiState = uiState.copy(
            currPeople = people
        )
    }

    fun onChangeTip(tip: String) {
        uiState = uiState.copy(
            currTip = tip
        )
    }

    fun onSaveCheck(changeLast: Boolean) {
        val newCheck = Check(
            id = checks.size,
            total = uiState.currTotal.toInt(),
            people = uiState.currPeople.toInt(),
            tip = uiState.currTip.toInt(),
            date = LocalDate.now()
        )

        if (changeLast) {
            checks[checks.size - 1] = newCheck
        }
        else {
            checks.add(newCheck)
        }
    }

    fun resetState() {
        uiState = uiState.copy(
            currTotal = "",
            currPeople = "",
            currTip = "10"
        )
    }

}