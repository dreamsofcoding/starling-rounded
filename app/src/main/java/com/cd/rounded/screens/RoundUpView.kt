package com.cd.rounded.business

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cd.rounded.screens.AccessTokenEntry
import com.cd.rounded.screens.FullScreenLoader
import com.cd.rounded.screens.RoundUpError
import com.cd.rounded.screens.RoundUpLoaded
import com.cd.rounded.screens.RoundUpTransferComplete
import kotlin.math.round

/**
 * RoundUp View that handles the initialisng of the Viewmodel and states so navigation
 * to appropriate areas can be triggered
 * @param modifer Modifier used for laying out the elements of the screen.
 */
@Composable
fun RoundUpView(modifier: Modifier = Modifier) {
    val roundUpViewModel: RoundUpViewModel = hiltViewModel()
    val uiState: RoundUpViewModel.State by roundUpViewModel.state.collectAsState()

    when (uiState) {
        RoundUpViewModel.State.AccessToken -> AccessTokenEntry(
            accessTokenProvided = { roundUpViewModel.doOnLoad() }
        )
        is RoundUpViewModel.State.Error -> RoundUpError((uiState as RoundUpViewModel.State.Error).throwable)
        RoundUpViewModel.State.Loading -> FullScreenLoader()
        RoundUpViewModel.State.Loaded -> RoundUpLoaded(
            roundUpViewModel.selectedWeek.value,
            { selectedWeek ->
                roundUpViewModel.getTransactionsForWeek(selectedWeek)
                roundUpViewModel.selectedWeek.value = selectedWeek
            },
            roundUpViewModel.roundUpAmount.value,
            roundUpViewModel.savingsGoalAmount.value,
            {
                roundUpViewModel.makeTransferToSavingsGoal()
            }
        )

        RoundUpViewModel.State.TransferSuccess -> RoundUpTransferComplete(
            returnToRoundUpScreen = { roundUpViewModel.doOnLoad() }
        )


    }


}