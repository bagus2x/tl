package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bagus2x.tl.R

@Composable
fun PersonalData(
    modifier: Modifier = Modifier,
    university: String?,
    batch: Int?,
    department: String?,
    studyProgram: String?,
    stream: String?,
    gender: String?,
    age: Int?,
    city: String?,
    skills: List<String>,
    certifications: List<String>,
    achievements: List<String>
) {
    Column(modifier = modifier) {
        Academic(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            university = university,
            batch = batch,
            department = department,
            studyProgram = studyProgram,
            stream = stream
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Bio(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            gender = gender,
            age = age,
            city = city
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_skills),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            skills.forEach { skills ->
                Text(
                    text = skills,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_achievements),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            achievements.forEach { achievement ->
                Text(
                    text = achievement,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_certifications),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            certifications.forEach { certification ->
                Text(
                    text = certification,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}
