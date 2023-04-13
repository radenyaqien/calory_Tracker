package com.radenyaqien.onboarding_presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.radenyaqien.core.R
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.core.util.UiEvent
import com.radenyaqien.core_ui.LocalSpasing
import com.radenyaqien.onboarding_presentation.ActionButton

@Composable
fun WelcomeScreen(onNavigate: (UiEvent.Navigate) -> Unit) {
    val localSpasing = LocalSpasing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(localSpasing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1,

            )
        Spacer(
            modifier = Modifier.height(localSpasing.spaceMedium)
        )
        ActionButton(
            text = stringResource(id = R.string.next),
            onclick = { onNavigate(UiEvent.Navigate(Route.GENDER)) },
            isEnabled = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}