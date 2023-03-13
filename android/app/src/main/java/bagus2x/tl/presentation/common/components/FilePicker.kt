package bagus2x.tl.presentation.common.components

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import bagus2x.tl.presentation.common.LocalShowSnackbar
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FilePicker(
    selected: File?,
    onSelected: (File) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showSnackbar = LocalShowSnackbar.current
    val pick = rememberLauncherForActivityResult(StartActivityForResult()) { res ->
        val uri = res.data?.data
        scope.launch (Dispatchers.IO){
            try {
                if (uri != null) {
                    if (uri.getMimeType(context)?.contains("pdf") == true) {
                        context.contentResolver.openFileDescriptor(uri, "r")
                            ?.use { parcelFileDescriptor ->
                                val pdfRenderer =
                                    PdfRenderer(parcelFileDescriptor).openPage(0)
                                val bitmap = Bitmap.createBitmap(
                                    pdfRenderer.width,
                                    pdfRenderer.height,
                                    Bitmap.Config.ARGB_8888
                                )
                                pdfRenderer.render(
                                    bitmap,
                                    null,
                                    null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                )
                                pdfRenderer.close()
                                val temp = context.createTempFile()
                                val os = FileOutputStream(temp)
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                                os.flush()
                                os.close()
                                onSelected(temp)
                            }
                    } else if (uri.getMimeType(context)?.contains("image") == true) {
                        onSelected(uri.toFile(context))
                    } else {
                        showSnackbar("File type not supported")
                    }
                }
            }catch (e: Exception) {
                Timber.e(e)
                showSnackbar(e.message ?: "Failed to load media")
            }
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.small)
                .border(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colors.onBackground.copy(0.2F),
                    width = 1.dp
                )
                .clickable {
                    pick.launch(getFileChooserIntentForImageAndPdf())
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.PhotoLibrary,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AnimatedContent(targetState = selected) { selected ->
            val model = selected ?: placeholder
            if (model != null) {
                AsyncImage(
                    model = model,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

fun Uri.getMimeType(context: Context): String? {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(this)
        ContentResolver.SCHEME_FILE -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            MimeTypeMap.getFileExtensionFromUrl(toString()).lowercase(Locale.US)
        )
        else -> null
    }
}

fun getFileChooserIntentForImageAndPdf(): Intent {
    val mimeTypes = arrayOf("image/*", "application/pdf")
    return Intent(Intent.ACTION_GET_CONTENT)
        .setType(mimeTypes.joinToString("/"))
        .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
}

private fun Uri.toFile(context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = context.createTempFile()

    val inputStream = contentResolver.openInputStream(this) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

private fun Context.createTempFile(): File {
    val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("${System.currentTimeMillis()}", ".jpg", storageDir)
}
