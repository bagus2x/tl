package bagus2x.tl.presentation.competition.detail

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bagus2x.tl.R
import bagus2x.tl.domain.model.Competition
import bagus2x.tl.presentation.common.LocalShowSnackbar
import bagus2x.tl.presentation.common.Tag
import bagus2x.tl.presentation.common.components.AsyncImage
import bagus2x.tl.presentation.common.components.FavoriteButton
import bagus2x.tl.presentation.common.components.Scaffold
import bagus2x.tl.presentation.common.components.TopBar
import timber.log.Timber
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CompetitionDetailScreen(
    navController: NavController,
    viewModel: CompetitionDetailViewModel = hiltViewModel()
) {
    val snackbar by viewModel.snackbar.collectAsState()
    val showSnackbar = LocalShowSnackbar.current
    LaunchedEffect(snackbar) {
        if (snackbar.isNotBlank()) {
            showSnackbar(snackbar)
        }
    }

    val competition by viewModel.competition.collectAsState()
    val context = LocalContext.current
    CompetitionDetail(
        competition = competition,
        favorite = viewModel::favorite,
        navigateUp = navController::popBackStack,
        navigateToSearch = {
            navController.navigate("search/competitions")
        },
        navigateToShare = {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, competition?.description)
                putExtra(Intent.EXTRA_TITLE, competition?.title)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = "text/plain"
            }
            val share = Intent.createChooser(intent, null)
            context.startActivity(share)
        }
    )
}

@Composable
fun CompetitionDetail(
    competition: Competition?,
    favorite: () -> Unit,
    navigateUp: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToShare: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                titleText = stringResource(R.string.text_competition_detail),
                onBackClicked = navigateUp,
                onSearchClicked = navigateToSearch,
                onShareClicked = navigateToShare
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (competition != null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = competition.poster,
                        contentDescription = competition.title,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        contentScale = ContentScale.Crop
                    )
                    FavoriteButton(
                        favorite = competition.favorite,
                        onClick = favorite,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset((-16).dp, 16.dp)
                            .testTag(if (competition.favorite) Tag.BTN_FAVORITE else Tag.BTN_UNFAVORITE)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = competition.createdAt.formatted,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = competition.title,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val iconText = @Composable { icon: ImageVector, text: String ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = text,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
                iconText(Icons.Outlined.Domain, competition.organizerName)
                Spacer(modifier = Modifier.height(4.dp))
                iconText(Icons.Outlined.LocationOn, "${competition.city}, ${competition.country}")
                Spacer(modifier = Modifier.height(4.dp))
                iconText(Icons.Outlined.Event, competition.deadline.formatted)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRupiah(
                        minimumFee = competition.minimumFee,
                        maximumFee = competition.maximumFee
                    ),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = stringResource(R.string.text_competition_description),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = competition.description,
                    style = MaterialTheme.typography.body2
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                val uriHandler = LocalUriHandler.current
                Text(
                    text = stringResource(R.string.text_link),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = competition.urlLink,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.clickable {
                        try {
                            uriHandler.openUri(competition.urlLink)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                )
            }
        }
        if (competition == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

private val locale = Locale("id", "ID")
private val numberFormat = NumberFormat.getCurrencyInstance(locale)

@Composable
private fun formatRupiah(minimumFee: Long, maximumFee: Long): String {
    return rememberSaveable {
        listOf(minimumFee, maximumFee)
            .sorted()
            .filter { it != 0L }
            .joinToString(" - ") { numberFormat.format(it) }
    }
}

val LocalDateTime.formatted: String
    @Composable
    get() {
        return rememberSaveable {
            DateTimeFormatter
                .ofPattern("EEEE, dd MMMM yyyy")
                .let(::format)
        }
    }
