package com.cd.rounded.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cd.rounded.R
import com.cd.rounded.ui.theme.StarlingColors

/**
 * RoundUp Error screen, displays a full screen branding and messaging about the error
 * that has happened.
 * @param throwable Throwable of the error.
 * @param modifer Modifier used for laying out the elements of the screen.
 */
@Composable
fun RoundUpError(
    throwable: Throwable?,
    modifier: Modifier = Modifier
) {
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

        Spacer(modifier = Modifier.padding(vertical = 32.dp))

        Text(
            text = "Well this is awkward, there was a problem trying to complete the last action. It seems there was an error because:",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = throwable?.localizedMessage ?: "Unspecified error",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle(1),
            fontWeight = FontWeight(600)

        )

        Text(
            text = "Please try again at a later time",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}