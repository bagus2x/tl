package bagus2x.tl.presentation.competition.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bagus2x.tl.domain.usecase.PublishCompetitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CompetitionFormViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val publishCompetitionUseCase: PublishCompetitionUseCase
) : ViewModel() {
    val poster = savedState.getStateFlow<File?>("poster", null)
    val title = savedState.getStateFlow("title", "")
    val description = savedState.getStateFlow("description", "")
    val theme = savedState.getStateFlow("theme", "")
    val city = savedState.getStateFlow("city", "")
    val country = savedState.getStateFlow("country", "")
    val deadline = savedState.getStateFlow("deadline", LocalDateTime.now())
    val minimumFee = savedState.getStateFlow<Long?>("minFee", null)
    val maximumFee = savedState.getStateFlow<Long?>("maxFee", null)
    val category = savedState.getStateFlow("category", "")
    val organizer = savedState.getStateFlow("organizer", "")
    val organizerName = savedState.getStateFlow("organizerName", "")
    val urlLink = savedState.getStateFlow("urlLink", "")
    val loading = savedState.getStateFlow("loading", false)
    val snackbar = savedState.getStateFlow("snackbar", "")
    val published = savedState.getStateFlow("published", false)

    fun snackbarConsumed() {
        savedState["snackbar"] = ""
    }

    fun setPoster(poster: File) {
        savedState["poster"] = poster
    }

    fun setTitle(title: String) {
        savedState["title"] = title
    }

    fun setDescription(description: String) {
        savedState["description"] = description
    }

    fun setTheme(theme: String) {
        savedState["theme"] = theme
    }

    fun setCity(city: String) {
        savedState["city"] = city
    }

    fun setCountry(country: String) {
        savedState["country"] = country
    }

    fun setDeadline(deadline: LocalDateTime) {
        savedState["deadline"] = deadline
    }

    fun setMinimumFee(fee: Long?) {
        savedState["minFee"] = fee
    }

    fun setMaximumFee(fee: Long?) {
        savedState["maxFee"] = fee
    }

    fun setCategory(category: String) {
        savedState["category"] = category
    }

    fun setOrganizer(organizer: String) {
        savedState["organizer"] = organizer
    }

    fun setOrganizerName(organizerName: String) {
        savedState["organizerName"] = organizerName
    }

    fun setUrlLink(urlLink: String) {
        savedState["urlLink"] = urlLink
    }

    fun publish() {
        viewModelScope.launch {
            savedState["loading"] = true
            try {
                publishCompetitionUseCase(
                    poster = poster.value ?: File("dev/null"),
                    title = title.value,
                    description = description.value,
                    theme = theme.value,
                    city = city.value,
                    country = country.value,
                    deadline = deadline.value,
                    minimumFee = minimumFee.value ?: 0,
                    maximumFee = maximumFee.value ?: 0,
                    category = category.value,
                    organizer = organizer.value,
                    organizerName = organizerName.value,
                    urlLink = urlLink.value
                )
                savedState["published"] = true
            } catch (e: Exception) {
                savedState["snackbar"] = e.message ?: ""
                Timber.e(e)
            }
            savedState["loading"] = false
        }
    }
}
