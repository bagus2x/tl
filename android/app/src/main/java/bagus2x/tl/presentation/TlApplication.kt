package bagus2x.tl.presentation

import android.app.Application
import bagus2x.tl.BuildConfig
import bagus2x.tl.R
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

@HiltAndroidApp
class TlApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            val debugTree = object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
                }
            }
            Timber.plant(debugTree)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .placeholder(R.drawable.bg_gray)
            .error(R.drawable.bg_gray)
            .fetcherDispatcher(Dispatchers.IO)
            .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
            .decoderDispatcher(Dispatchers.Default)
            .transformationDispatcher(Dispatchers.IO)
            .interceptorDispatcher(Dispatchers.Default)
            .transformationDispatcher(Dispatchers.Default)
            .crossfade(1000)
            .crossfade(true)
            .build()
    }
}
