package com.cd.rounded.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cd.rounded.R
import com.cd.rounded.ui.theme.StarlingColors

/**
 * RoundUp Transfer Complete Screen, informs the user that they have completed a transfer, and
 * they are basically awesome.
 * @param modifer Modifier used for laying out the elements of the screen.
 * @param returnToRoundUpScreen Function to allow the customer to return to the main
 * RoundUp screen if triggered.
 */
@Composable
fun RoundUpTransferComplete(
    modifier: Modifier = Modifier,
    returnToRoundUpScreen: () -> Unit,
){
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

        Text(text = "Congratulations, your round up has been successfully transferred to your savings goal",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
            )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "You are on your way to smashing your savings goal!",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedButton(onClick = {
            returnToRoundUpScreen.invoke()
        },
            border = BorderStroke(2.dp, StarlingColors.StarlingPurple),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
            Text("I want to make another RoundUp",
                color = StarlingColors.StarlingWhite,
                textAlign = TextAlign.Center
                )
        }

    }
}