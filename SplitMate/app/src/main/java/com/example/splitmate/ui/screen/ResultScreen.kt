package com.example.splitmate.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.splitmate.model.Check

@Composable
fun ResultScreen(
    check: Check,
    tipAmount: (Int, Int) -> Int,
    totalWithTip: (Int, Int) -> Int,
    perPerson: (Int, Int, Int) -> Int,
    showBackToEdit: Boolean,
    onBackToEdit: () -> Unit,
    onNewCalculation: () -> Unit,
    onLoadHistory: () -> Unit
) {

    Column(
        Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = check.date.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text ="Tip Amount: ${tipAmount(check.total, check.tip)} (${check.tip}%)",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Text(
            text ="Total with tip: ${totalWithTip(check.total, check.tip)}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Text(
            text ="Per person: ${perPerson(check.total, check.tip, check.people)}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        if (showBackToEdit) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBackToEdit
            ) {
                Text(
                    text = "Back to edit",
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNewCalculation
        ) {
            Text(
                text = "New calculation",
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoadHistory
        ) {
            Text(
                text = "History",
                textAlign = TextAlign.Center
            )
        }

        val context = LocalContext.current
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, """
                        |${check.date}
                        |Tip Amount: ${tipAmount(check.total, check.tip)} (${check.tip}%)
                        |Total with tip: ${totalWithTip(check.total, check.tip)}
                        |Per person: ${perPerson(check.total, check.tip, check.people)}
                    """.trimMargin())
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)

                startActivity(context, shareIntent, null)
        }) {
            Text(
                text = "Share",
                textAlign = TextAlign.Center
            )
        }
    }
}
