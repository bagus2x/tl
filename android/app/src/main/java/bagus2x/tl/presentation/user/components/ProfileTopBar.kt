package bagus2x.tl.presentation.user.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import bagus2x.tl.R

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    onClickEdit: () -> Unit,
    onClickSignOut: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Profile",
                color = MaterialTheme.colors.primary
            )
        },
        actions = {
            var isDialogOpened by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { isDialogOpened = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            if (isDialogOpened) {
                Dialog(onDismissRequest = { isDialogOpened = false }) {
                    Surface(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colors.primary
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TextButton(
                                onClick = onClickEdit,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = stringResource(R.string.text_edit_profile))
                            }
                            TextButton(
                                onClick = onClickSignOut,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = stringResource(R.string.text_signout))
                            }
                        }
                    }
                }
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
}
