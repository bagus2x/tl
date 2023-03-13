package bagus2x.tl.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import bagus2x.tl.presentation.common.LocalBlurScaffold
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@OptIn(ExperimentalComposeUiApi::class)
private val DialogProperties = DialogProperties(usePlatformDefaultWidth = false)

@Composable
fun AsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    AsyncImage(
        model,
        contentDescription,
        modifier = modifier.clickable {
            visible = true
        },
        transform,
        onState,
        alignment,
        contentScale,
        alpha,
        colorFilter,
        filterQuality
    )
    if (visible) {
        val blurScaffold = LocalBlurScaffold.current
        DisposableEffect(Unit ) {
            blurScaffold(8.dp)
            onDispose {
                blurScaffold(0.dp)
            }
        }
        Dialog(
            onDismissRequest = { visible = false },
            properties = DialogProperties,
        ) {
            Box {
                var scale by rememberSaveable { mutableStateOf(1f) }
                var rotationState by rememberSaveable { mutableStateOf(0f) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, zoom, rotation ->
                                scale *= zoom
                                rotationState += rotation
                            }
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = model,
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = maxOf(.5f, minOf(3f, scale))
                                scaleY = maxOf(.5f, minOf(3f, scale))
                                rotationZ = rotationState
                            },
                        contentScale = ContentScale.Fit
                    )
                }
                TopAppBar(
                    modifier = Modifier.align(Alignment.TopStart),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                    IconButton(
                        onClick = { visible = false }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
