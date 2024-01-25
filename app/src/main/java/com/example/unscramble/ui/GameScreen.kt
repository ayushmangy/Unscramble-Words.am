package com.example.unscramble.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.R

@Composable
fun GameScreen(modifier: Modifier = Modifier, gameViewModel: GameVM = viewModel()) {

    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Unscramble",
            style = MaterialTheme.typography.titleLarge )
        // Your Compose content goes here

        GameLayout(onUserGuessChanged = {gameViewModel.updateUserGuess(it)},
            userGuess = gameViewModel.userGuess,
            onKeyboardDone = {gameViewModel.checkUserGuess() },
            currScrambledWord = gameUiState.currScrambledWord,
            isGuessWrong = gameUiState.isGuessWrong,
            wordCount = gameUiState.currWordCount,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp))

        Column( modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Button(onClick = { gameViewModel.checkUserGuess() },
                modifier= Modifier.fillMaxWidth()) {

                Text(text = "Submit",
                    fontSize = 16.sp)
            }

            OutlinedButton(onClick = { gameViewModel.skipWord() },
                modifier = Modifier.fillMaxWidth()) {
                Text("Skip",
                    fontSize = 16.sp)
            }


        }
        Gamestatus(score = gameUiState.score)

    }
    if (gameUiState.isGameOver){
        FinalScoreDialog(score = gameUiState.score,
            onPlayAgain = { gameViewModel.resetGame() })
    }



}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)
    AlertDialog(

        title = {
            Text(text = "Congratulations")
        },
        text = { Text(text = stringResource(R.string.you_scored, score)) },

        modifier = modifier,
        onDismissRequest = {

        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPlayAgain()
                }
            ) {
                Text("Play Again")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text("Exit")
            }
        }
    )

}



@Composable
fun Gamestatus(score: Int = 0) {
    Card {
        Text(text = stringResource(id = R.string.score, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

    }
}

@Composable
fun GameLayout( onUserGuessChanged: (String) -> Unit,
                userGuess: String,
                isGuessWrong : Boolean,
                onKeyboardDone: () -> Unit,
               currScrambledWord : String,
                wordCount : Int
               ,modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    )
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .align(alignment = Alignment.End)
                    .padding(8.dp),
                text = stringResource(id = R.string.wordCount,wordCount),
                style = typography.titleMedium,
                color = colorScheme.onPrimary,
                )

            Text(text =currScrambledWord, style = MaterialTheme.typography.displayMedium)

            Text(
                text = stringResource(id = R.string.ins),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )



            OutlinedTextField(
                value = userGuess,
                onValueChange = onUserGuessChanged,
                label = {
                        if (isGuessWrong){
                            Text(text = "Wrong guess!")
                        }
                    else{
                        Text(text = "Enter your word")
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface),
                isError = isGuessWrong,
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {onKeyboardDone()}),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                shape = shapes.large

            )

        }
    }
}



