package com.cd.rounded.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cd.rounded.R
import com.cd.rounded.ui.theme.StarlingColors
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

/**
 * RoundUp Loaded screen, main screen in the app for reviewing the roundup amount from the week
 * presents a dropdown for gthe user to search for different week periods, and presents a button
 * that will make the transfer of the displayed round up amount to the customer' savings goal.
 * @param selectedWeek Int denoting week that is currently being used for the roundup.
 * @param getTransactions Function that requires an Int in order to accept the
 * customer' change on the dropdown and perform a new request for the transactions
 * relating to the selected period.
 * @param roundUpAmount Double, The RoundUp Amount for a selected week period.
 * @param savingsGoalAmount Double,  The customer' Savings Goal balance amount.
 * @param makeTransferToSavingsGoal Function, this will trigger the logic to transfer
 * the round up amount to the customer' savings goal.
 * @param modifer Modifier used for laying out the elements of the screen.
 */

@Composable
fun RoundUpLoaded(
    selectedWeek: Int,
    getTransactions: (selectedWeek: Int) -> Unit,
    roundUpAmount: Double,
    savingsGoalAmount: Double,
    makeTransferToSavingsGoal: () -> Unit,
    modifier: Modifier = Modifier) {

    //To display the amounts to 2 decimal places and with the correct currency (set as default, will
    // display $ on Android emulators that are not set for UK Locale)
    val currencyFormat = NumberFormat.getCurrencyInstance(java.util.Locale.getDefault())

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Icon(
            painter = painterResource(
                id = R.drawable.developer_logo,

                ),
            "Logo",
            tint = StarlingColors.StarlingOffWhite,
            modifier = modifier.scale(0.5f),
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Welcome to RoundUp",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        Text(text = "Please choose a week in the dropdown below and we'll look for eligible transactions to RoundUp for you",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        weekDropdownSelector(
            selectedWeek,
            getTransactions,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Text(text = "${currencyFormat.format(roundUpAmount)}",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Divider(
            Modifier
                .width(100.dp)
                .align(Alignment.CenterHorizontally),
            color = StarlingColors.StarlingOffWhite,
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(text = "Round Up amount",
            color = StarlingColors.StarlingOffWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Click the button below to round up the selected week' transactions and transfer it to your saving goal",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp))

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    makeTransferToSavingsGoal.invoke()
                }
        ) {
            Surface(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
                    .padding(8.dp),
                color = StarlingColors.StarlingBackground,
                border = BorderStroke(4.dp, StarlingColors.StarlingPurple),
                contentColor = StarlingColors.StarlingOffWhite,
                shape = CircleShape
            ) {

                Column(
                    Modifier
                        .matchParentSize()
                        .padding(8.dp)
                ) {

                    Spacer(modifier = Modifier.padding(vertical = 32.dp))




                    Text(text = "${currencyFormat.format(savingsGoalAmount)}",
                        color = StarlingColors.StarlingWhite,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Divider(
                        Modifier
                            .width(50.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp),
                        color = StarlingColors.StarlingOffWhite,
                    )

                    Text("Saving goal balance",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                }
            }
        }

    }
}

@Composable
fun weekDropdownSelector(
    selectedWeek: Int,
    getTransactions: (selectedWeek: Int) -> Unit,
    modifier: Modifier){
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("This week", "Last week", "2 weeks ago", "3 weeks ago")
    var selectedIndex by remember { mutableStateOf(selectedWeek) }
    Box(modifier
        .wrapContentSize()) {

        Text(items[selectedIndex],modifier = Modifier
            .width(200.dp)
            .clickable(onClick = { expanded = true })
            .background(
                StarlingColors.StarlingBackground
            )
            .padding(start = 16.dp),
            color = StarlingColors.StarlingWhite
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Center)
                .background(
                    StarlingColors.StarlingBackground
                )
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    getTransactions.invoke(index)
                },
                    text = {
                        Text(text = s,
                            color = StarlingColors.StarlingWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                )
            }
        }

        val rotation: Float by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            animationSpec = tween(durationMillis = 300),
        )

        Icon(
            painter = painterResource(id = com.google.android.material.R.drawable.mtrl_ic_arrow_drop_down),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    expanded = !expanded
                }
                .padding(end = 16.dp)
                .rotate(rotation),
            tint = StarlingColors.StarlingWhite,
            contentDescription = null
        )
    }
}