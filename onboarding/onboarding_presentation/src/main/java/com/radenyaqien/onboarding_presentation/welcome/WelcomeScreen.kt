package com.radenyaqien.onboarding_presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.radenyaqien.core.R
import com.radenyaqien.core_ui.LocalSpasing
import com.radenyaqien.onboarding_presentation.ActionButton

@Composable
fun WelcomeScreen(onNextClick: () -> Unit) {
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
            onclick = { onNextClick() },
            isEnabled = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}