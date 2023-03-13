package bagus2x.tl.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

inline fun <Local, Remote> networkBoundResource(
    crossinline local: () -> Flow<Local>,
    crossinline remote: suspend () -> Remote,
    crossinline update: suspend (Remote) -> Unit,
    crossinline shouldUpdate: (Local) -> Boolean = { true }
) = flow {
    val data = local().first()

    val flow = if (shouldUpdate(data)) {
        emit(data)
        update(remote())
        local()
    } else {
        local()
    }

    emitAll(flow)
}
