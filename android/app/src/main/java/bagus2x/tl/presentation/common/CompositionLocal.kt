package bagus2x.tl.presentation.common

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import bagus2x.tl.domain.model.Auth

val LocalShowSnackbar = compositionLocalOf<(String) -> Unit> { {} }

val LocalUnreadNotifications = compositionLocalOf { 0 }

val LocalBlurScaffold = compositionLocalOf<(Dp) -> Unit> { {} }

val LocalAuthUser = compositionLocalOf<Auth?> { null }
