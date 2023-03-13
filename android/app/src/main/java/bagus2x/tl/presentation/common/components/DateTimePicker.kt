package bagus2x.tl.presentation.common.components

import androidx.compose.runtime.*
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDateTime

@Composable
fun DateTimePicker(
    value: LocalDateTime,
    onChanged: (LocalDateTime) -> Unit,
    state: MaterialDialogState,
) {
    val timePickerState = rememberMaterialDialogState()
    var selectedDate by remember(value) { mutableStateOf(value.toLocalDate()) }
    var selectedTime by remember(value) { mutableStateOf(value.toLocalTime()) }
    MaterialDialog(
        dialogState = state,
        buttons = {
            positiveButton(
                text = "Ok",
                onClick = timePickerState::show
            )
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker { selectedDate = it }
    }
    MaterialDialog(
        dialogState = timePickerState,
        buttons = {
            positiveButton(
                text = "Ok",
                onClick = { onChanged(LocalDateTime.of(selectedDate, selectedTime)) }
            )
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker { selectedTime = it }
    }
}
