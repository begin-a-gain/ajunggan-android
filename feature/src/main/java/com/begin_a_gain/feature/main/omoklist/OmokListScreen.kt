package com.begin_a_gain.feature.main.omoklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.begin_a_gain.library.core.util.DateTimeFormat
import com.begin_a_gain.library.core.util.DateTimeUtil.isToday
import com.begin_a_gain.library.core.util.DateTimeUtil.toString
import com.begin_a_gain.library.design.component.button.OIconButton
import com.begin_a_gain.library.design.component.dialog.ODatePickerDialog
import com.begin_a_gain.library.design.component.dialog.TodayOrBeforeSelectableDates
import com.begin_a_gain.library.design.component.image.OImage
import com.begin_a_gain.library.design.component.image.OImageRes
import com.begin_a_gain.library.design.component.text.OText
import com.begin_a_gain.library.design.theme.ColorToken
import com.begin_a_gain.library.design.theme.ColorToken.Companion.color
import com.begin_a_gain.library.design.theme.OTextStyle
import com.begin_a_gain.library.design.util.noRippleClickable
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun OmokListScreen(
    viewModel: OmokListViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = TodayOrBeforeSelectableDates()
    )

    LaunchedEffect(state.currentDate) {
        datePickerState.selectedDateMillis = state.currentDate.millis
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorToken.UI_BG.color())
    ) {
        OmokListTopBar(
            navigateToAlarm = {}
        )

        OmokListDateBar(
            date = state.currentDate,
            addDate = { day -> viewModel.addDate(day) }
        ) {
            showDatePicker = true
        }

        Box(modifier = Modifier
            .fillMaxWidth(1f)
            .weight(1f)) {
            
        }

        if (state.omokList.isEmpty()) {
            Box {
                OImage(image = OImageRes.SpeechBubble)
                OText(
                    modifier = Modifier.padding(bottom = 14.dp),
                    text = "대국을 생성해보세요!",
                    style = OTextStyle.Title1,
                    color = ColorToken.TEXT_ON_01
                )
            }
        }
    }

    if (showDatePicker) {
        ODatePickerDialog(
            datePickerState = datePickerState,
            onDismissRequest = {
                showDatePicker = false
            }
        ) { selectedDate ->
            viewModel.setDate(selectedDate)
        }
    }
}

@Composable
private fun OmokListTopBar(
    navigateToAlarm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(ColorToken.UI_BG.color())
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .background(ColorToken.UI_DISABLE_01.color())
                .size(160.dp, 40.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        OIconButton(
            icon = OImageRes.Bell,
            size = 34.dp,
            iconSize = 24.dp
        ) {
            navigateToAlarm()
        }
    }
}

@Composable
private fun OmokListDateBar(
    date: DateTime,
    addDate: (Int) -> Unit,
    showDatePicker: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OImage(
            modifier = Modifier.noRippleClickable {
                addDate(-1)
            },
            image = OImageRes.ArrowLeft,
            size = 20.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        OText(
            modifier = Modifier.noRippleClickable {
                showDatePicker()
            },
            text = date.toString(DateTimeFormat.FullDate),
            style = OTextStyle.Headline
        )
        Spacer(modifier = Modifier.width(8.dp))
        OImage(
            modifier = Modifier.noRippleClickable {
                if (!date.isToday()) {
                    addDate(1)
                }
            },
            image = OImageRes.ArrowRight,
            size = 20.dp,
            color = if (date.isToday()) {
                ColorToken.ICON_DISABLE.color()
            } else {
                ColorToken.ICON_01.color()
            }
        )
    }
}