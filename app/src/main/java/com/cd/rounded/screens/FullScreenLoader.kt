package com.cd.rounded.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cd.rounded.ui.theme.StarlingColors

/**
 * Full Screen Circular Progress loader to display to the customer
 * when fetching data from the server
 * @param modifer Modifier used for laying out the elements of the screen.
 */
@Composable
fun FullScreenLoader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .padding(100.dp),
            color = StarlingColors.StarlingPurple,
            strokeWidth = 10.dp
        )
    }
}