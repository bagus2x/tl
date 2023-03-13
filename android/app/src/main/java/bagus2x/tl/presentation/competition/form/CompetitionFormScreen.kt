package bagus2x.tl.presentation.competition.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.*
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TextField
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CompetitionFormScreen(
    navController: NavController,
    viewModel: CompetitionFormViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsStateWithLifecycle()
    val published by viewModel.published.collectAsStateWithLifecycle()
    val snackbarState = LocalShowSnackbar.current
    LaunchedEffect(snackbar, published) {
        if (snackbar.isNotBlank()) {
            snackbarState(snackbar)
            viewModel.snackbarConsumed()
        }
        if (published) {
            navController.navigateUp()
        }
    }

    val poster by viewModel.poster.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val organizer by viewModel.organizer.collectAsStateWithLifecycle()
    val organizerName by viewModel.organizerName.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val country by viewModel.country.collectAsStateWithLifecycle()
    val deadline by viewModel.deadline.collectAsStateWithLifecycle()
    val minimumFee by viewModel.minimumFee.collectAsStateWithLifecycle()
    val maximumFee by viewModel.maximumFee.collectAsStateWithLifecycle()
    val urlLink by viewModel.urlLink.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    CompetitionFormScreen(
        navigateUp = navController::navigateUp,
        publish = viewModel::publish,
        poster = poster,
        setPoster = viewModel::setPoster,
        category = category,
        setCategory = viewModel::setCategory,
        organizer = organizer,
        setOrganizer = viewModel::setOrganizer,
        organizerName = organizerName,
        setOrganizerName = viewModel::setOrganizerName,
        theme = theme,
        setTheme = viewModel::setTheme,
        title = title,
        setTitle = viewModel::setTitle,
        description = description,
        setDescription = viewModel::setDescription,
        city = city,
        setCity = viewModel::setCity,
        country = country,
        setCountry = viewModel::setCountry,
        deadline = deadline,
        setDeadline = viewModel::setDeadline,
        minimumFee = minimumFee,
        setMinimumFee = viewModel::setMinimumFee,
        maximumFee = maximumFee,
        setMaximumFee = viewModel::setMaximumFee,
        urlLink = urlLink,
        setUrlLink = viewModel::setUrlLink,
        loading = loading
    )
}

@Composable
fun CompetitionFormScreen(
    navigateUp: () -> Unit,
    publish: () -> Unit,
    poster: File?,
    setPoster: (File) -> Unit,
    category: String,
    setCategory: (String) -> Unit,
    organizer: String,
    setOrganizer: (String) -> Unit,
    organizerName: String,
    setOrganizerName: (String) -> Unit,
    theme: String,
    setTheme: (String) -> Unit,
    title: String,
    setTitle: (String) -> Unit,
    description: String,
    setDescription: (String) -> Unit,
    city: String,
    setCity: (String) -> Unit,
    country: String,
    setCountry: (String) -> Unit,
    deadline: LocalDateTime,
    setDeadline: (LocalDateTime) -> Unit,
    minimumFee: Long?,
    setMinimumFee: (Long?) -> Unit,
    maximumFee: Long?,
    setMaximumFee: (Long?) -> Unit,
    urlLink: String,
    setUrlLink: (String) -> Unit,
    loading: Boolean
) {
    Scaffold(
        modifier = Modifier
            .imePadding()
            .testTag(Tag.COMPETITION_FORM_SCREEN),
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_form),
                onBackClicked = navigateUp,
                buttonText = stringResource(R.string.text_publish),
                onButtonClicked = publish
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.text_competition_poster),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilePicker(
                selected = poster,
                onSelected = setPoster,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.text_competition_level),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body2
            )
            val context = LocalContext.current
            val categories = remember {
                listOf(
                    "national" to context.getString(R.string.text_national),
                    "international" to context.getString(R.string.text_international)
                )
            }
            RadioGroup(
                selected = category,
                options = categories,
                onChange = setCategory,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = stringResource(R.string.text_organizer),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body2
            )
            val organizers = remember {
                listOf(
                    "government" to context.getString(R.string.text_government),
                    "company" to context.getString(R.string.text_company),
                    "university" to context.getString(R.string.text_university)
                )
            }
            RadioGroup(
                selected = organizer,
                options = organizers,
                onChange = setOrganizer,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(0.dp))
            TextField(
                value = organizerName,
                onValueChange = setOrganizerName,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .testTag(Tag.TF_ORGANIZER_NAME),
                label = {
                    Text(text = stringResource(id = R.string.text_organizer_name))
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = theme,
                onValueChange = setTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Tag.TF_THEME),
                label = {
                    Text(text = stringResource(R.string.text_theme))
                },
                maxLines = 1,
                options = stringArrayResource(id = R.array.text_competition_theme_list),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = title,
                onValueChange = setTitle,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .testTag(Tag.TF_TITLE),
                label = {
                    Text(text = stringResource(R.string.text_title))
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = description,
                onValueChange = setDescription,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .testTag(Tag.TF_DESCRIPTION),
                label = {
                    Text(text = stringResource(R.string.text_description))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.text_location),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body2
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = city,
                    onValueChange = setCity,
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.TF_CITY),
                    label = {
                        Text(text = stringResource(R.string.text_city))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                TextField(
                    value = country,
                    onValueChange = setCountry,
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.TF_COUNTRY),
                    label = {
                        Text(text = stringResource(R.string.text_country))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.text_dealine_submit),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body2
            )
            val formatted = remember(deadline) {
                val df = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale.getDefault())
                deadline.format(df)
            }
            val dateTimePickerState = rememberMaterialDialogState()
            DateTimePicker(
                value = deadline,
                onChanged = setDeadline,
                state = dateTimePickerState
            )
            TextField(
                value = formatted,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .noRippleClickable {
                        dateTimePickerState.show()
                    }
                    .fillMaxWidth()
                    .testTag(Tag.TF_DEADLINE),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.text_registration_fee),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = minimumFee?.toString() ?: "",
                    onValueChange = { setMinimumFee(it.toLongOrNull()) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.TF_MIN),
                    label = {
                        Text(text = stringResource(R.string.text_fee_min))
                    },
                )
                TextField(
                    value = maximumFee?.toString() ?: "",
                    onValueChange = { setMaximumFee(it.toLongOrNull()) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .weight(1F)
                        .testTag(Tag.TF_MAX),
                    label = {
                        Text(text = stringResource(R.string.text_fee_max))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = urlLink,
                onValueChange = setUrlLink,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .testTag(Tag.TF_URL),
                label = {
                    Text(text = stringResource(R.string.text_link))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
