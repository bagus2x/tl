package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bagus2x.tl.R

@Composable
fun Academic(
    modifier: Modifier = Modifier,
    university: String?,
    batch: Int?,
    department: String?,
    studyProgram: String?,
    stream: String?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.text_academic),
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        val field: @Composable (String, String) -> Unit = { field, value ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = field,
                    modifier = Modifier.weight(0.3F),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = value,
                    modifier = Modifier.weight(0.7F),
                    style = MaterialTheme.typography.body2
                )
            }
        }
        if (!university.isNullOrBlank()) {
            field(stringResource(R.string.text_university), ": $university")
        }
        if (batch != null) {
            field(stringResource(R.string.text_batch), ": $batch")
        }
        if (!department.isNullOrBlank()) {
            field(stringResource(R.string.text_department), ": $department")
        }
        if (!studyProgram.isNullOrBlank()) {
            field(stringResource(R.string.text_study_program), ": $studyProgram")
        }
        if (!stream.isNullOrBlank()) {
            field(stringResource(R.string.text_stream), ": $stream")
        }
    }
}
