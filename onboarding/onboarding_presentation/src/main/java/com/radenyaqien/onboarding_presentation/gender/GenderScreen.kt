package com.radenyaqien.onboarding_presentation.gender


import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.radenyaqien.core.R
import com.radenyaqien.core.domain.model.Gender
import com.radenyaqien.core.util.UiEvent
import com.radenyaqien.core_ui.LocalSpasing
import com.radenyaqien.onboarding_presentation.ActionButton
import com.radenyaqien.onboarding_presentation.components.SelectableBUtton

@Composable
fun GenderScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: GenderViewModel = hiltViewModel()
) {

    val spacing = LocalSpasing.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> Unit
            }
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.whats_your_gender))
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row {
                SelectableBUtton(
                    text = stringResource(id = R.string.male),
                    isSelected = viewModel.selectedGender == Gender.Male,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onclick = { viewModel.onGenderClick(Gender.Male) },
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal)
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                SelectableBUtton(
                    text = stringResource(id = R.string.female),
                    isSelected = viewModel.selectedGender == Gender.Female,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onclick = { viewModel.onGenderClick(Gender.Female) },
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal)
                )
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onclick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

}