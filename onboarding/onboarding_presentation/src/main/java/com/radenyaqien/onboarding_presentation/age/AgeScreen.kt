package com.radenyaqien.onboarding_presentation.age

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.radenyaqien.core.R
import com.radenyaqien.core.util.UiEvent
import com.radenyaqien.core_ui.LocalSpasing
import com.radenyaqien.onboarding_presentation.ActionButton
import com.radenyaqien.onboarding_presentation.components.UnitTextField

@Composable
fun AgeScreen(
    scaffoldState: ScaffoldState,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgeViewModel = hiltViewModel()
) {

    val spacing = LocalSpasing.current
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    onNextClick()
                }

                is UiEvent.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message.asString(context))
                }

                else -> Unit
            }
        }

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.whats_your_age))
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = viewModel.age,
                onValueChange = viewModel::onAgeEnter,
                unit = stringResource(
                    id = R.string.years
                )
            )
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onclick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

