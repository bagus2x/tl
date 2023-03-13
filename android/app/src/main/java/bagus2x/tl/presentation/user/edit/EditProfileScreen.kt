package bagus2x.tl.presentation.user.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.*
import java.io.File

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val saved by viewModel.saved.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    val context = LocalContext.current
    LaunchedEffect(snackbar, saved) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
        }
        if (saved) {
            showSnackbar(context.getString(R.string.text_profile_saved))
            navController.navigateUp()
        }
    }

    val user by viewModel.user.collectAsState()
    val photo by viewModel.photo.collectAsState()
    val name by viewModel.name.collectAsState()
    val bio by viewModel.bio.collectAsState()
    val invitable by viewModel.invitable.collectAsState()
    val university by viewModel.university.collectAsState()
    val batch by viewModel.batch.collectAsState()
    val faculty by viewModel.faculty.collectAsState()
    val department by viewModel.department.collectAsState()
    val studyProgram by viewModel.studyProgram.collectAsState()
    val stream by viewModel.stream.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val age by viewModel.age.collectAsState()
    val location by viewModel.location.collectAsState()
    val skills by viewModel.skills.collectAsState()
    val certifications by viewModel.certifications.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val loading by viewModel.loading.collectAsState()
    EditProfileScreen(
        photoUrl = user?.photo,
        photo = photo,
        setPhoto = viewModel::setPhoto,
        name = name,
        setName = viewModel::setName,
        bio = bio,
        setBio = viewModel::setBio,
        invitable = invitable,
        setInvitable = viewModel::setInvitable,
        university = university,
        setUniversity = viewModel::setUniversity,
        batch = batch,
        faculty = faculty,
        setFaculty = viewModel::setFaculty,
        setBatch = viewModel::setBatch,
        department = department,
        setDepartment = viewModel::setDepartment,
        studyProgram = studyProgram,
        setStudyProgram = viewModel::setStudyProgram,
        stream = stream,
        setStream = viewModel::setStream,
        gender = gender,
        setGender = viewModel::setGender,
        age = age,
        setAge = viewModel::setAge,
        location = location,
        setLocation = viewModel::setLocation,
        skills = skills,
        setSkills = viewModel::setSkills,
        certifications = certifications,
        setCertifications = viewModel::setCertifications,
        achievements = achievements,
        setAchievements = viewModel::setAchievements,
        save = viewModel::save,
        navigateUp = navController::navigateUp,
        loading = loading
    )
}

@Composable
fun EditProfileScreen(
    photoUrl: String?,
    photo: File?,
    setPhoto: (File) -> Unit,
    name: String,
    setName: (String) -> Unit,
    bio: String,
    setBio: (String) -> Unit,
    invitable: Boolean,
    setInvitable: (Boolean) -> Unit,
    university: String,
    setUniversity: (String) -> Unit,
    faculty: String,
    setFaculty: (String) -> Unit,
    batch: Int?,
    setBatch: (Int?) -> Unit,
    department: String,
    setDepartment: (String) -> Unit,
    studyProgram: String,
    setStudyProgram: (String) -> Unit,
    stream: String,
    setStream: (String) -> Unit,
    gender: String,
    setGender: (String) -> Unit,
    age: Int?,
    setAge: (Int?) -> Unit,
    location: String,
    setLocation: (String) -> Unit,
    skills: String,
    setSkills: (String) -> Unit,
    certifications: String,
    setCertifications: (String) -> Unit,
    achievements: String,
    setAchievements: (String) -> Unit,
    save: () -> Unit,
    navigateUp: () -> Unit,
    loading: Boolean
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_edit_profile),
                buttonText = stringResource(R.string.text_save),
                onButtonClicked = save,
                onBackClicked = navigateUp
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilePicker(
                selected = photo,
                onSelected = setPhoto,
                placeholder = photoUrl,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = name,
                onValueChange = setName,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_NAME),
                label = {
                    Text(text = stringResource(R.string.text_name))
                }
            )
            TextField(
                value = bio,
                onValueChange = setBio,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_bio))
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.text_accep_invitation),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1F)
                )
                Switch(
                    checked = invitable,
                    onCheckedChange = setInvitable
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TextField(
                    value = university,
                    modifier = Modifier.weight(0.7F),
                    onValueChange = setUniversity,
                    label = {
                        Text(text = stringResource(R.string.text_university))
                    }
                )
                TextField(
                    value = batch?.toString() ?: "",
                    onValueChange = { setBatch(it.toIntOrNull()) },
                    modifier = Modifier.weight(0.3F),
                    label = {
                        Text(text = stringResource(R.string.text_batch))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            TextField(
                value = faculty,
                onValueChange = setFaculty,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_faculty))
                }
            )
            TextField(
                value = department,
                onValueChange = setDepartment,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_department))
                }
            )
            TextField(
                value = studyProgram,
                onValueChange = setStudyProgram,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_study_program))
                }
            )
            TextField(
                value = stream,
                onValueChange = setStream,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_stream))
                },
                options = stringArrayResource(id = R.array.text_stream_list),
                contentPadding = PaddingValues(0.dp)
            )
            val context = LocalContext.current
            val genders = remember {
                listOf(
                    "M" to context.getString(R.string.text_male),
                    "F" to context.getString(R.string.text_female)
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.text_gender),
                    style = MaterialTheme.typography.body1,
                )
                RadioGroup(
                    selected = gender,
                    options = genders,
                    onChange = setGender
                )
            }
            TextField(
                value = age?.toString() ?: "",
                onValueChange = { setAge(it.toIntOrNull()) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_age))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = location,
                onValueChange = setLocation,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_city))
                }
            )
            TextField(
                value = skills,
                onValueChange = setSkills,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_skills))
                }
            )
            TextField(
                value = certifications,
                onValueChange = setCertifications,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_certifications))
                }
            )
            TextField(
                value = achievements,
                onValueChange = setAchievements,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.text_achievements))
                }
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
