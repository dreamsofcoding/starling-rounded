package com.cd.rounded.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cd.rounded.Constants
import com.cd.rounded.R
import com.cd.rounded.ui.theme.StarlingColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Access Token Entry Composable Screen, displays message and text edit field to user, upon
 * passing a basic validation, the user will be able to submit the access token and proceed
 * through the app.
 * @param accessTokenProvided function when called will update the state of the app.
 * @param modifer Modifier used for laying out the elements of the screen.
 */
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AccessTokenEntry(
    accessTokenProvided: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Scroll State for automatically scrolling to submit button
    val scrollState = rememberScrollState()
    // Focus Requester for the submit button
    val focusSubmit = remember { FocusRequester() }
    // Bring into view requester to perform scroll
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    // Coroutine Scope
    val scope = rememberCoroutineScope()
    // Keyboard Controller for handling automatic hiding
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
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

        Text(
            text = "Please enter your access token to continue",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        val textState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                scope.launch {
                    //waits half a second once the field has changed to
                    // perform the scroll to the submit button
                    delay(500)
                    bringIntoViewRequester.bringIntoView()
                    focusSubmit.requestFocus()
                    keyboardController?.hide()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = "Customer Access Token Required",
            color = StarlingColors.StarlingWhite,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedButton(
            onClick = {
                //Basic validation check, not blank, not empty
                if (textState.value.text.isNotBlank() && textState.value.text.isNotEmpty()) {
                    Constants.ACCESS_TOKEN = textState.value.text
                    accessTokenProvided.invoke()
                }
            },
            border = BorderStroke(2.dp, StarlingColors.StarlingPurple),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusRequester(focusSubmit),
        ) {
            Text(
                "Submit",
                color = StarlingColors.StarlingWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}