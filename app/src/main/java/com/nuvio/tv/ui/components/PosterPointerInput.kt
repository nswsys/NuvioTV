package com.nuvio.tv.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Adds touch/pointer gestures to a TV Card without adding another focus target,
 * key handler or accessibility click action. The TV Card owns those paths.
 * Standard Compose cancellation lets a parent scroll consume a drag; long press
 * consumes the remainder of its gesture instead of also opening on release.
 */
@Composable
internal fun Modifier.posterPointerInput(
    posterId: String,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?
): Modifier {
    val currentClick by rememberUpdatedState(onClick)
    val currentLongClick by rememberUpdatedState(onLongClick)
    return pointerInput(posterId, onLongClick != null) {
        detectTapGestures(
            onPress = {
                Log.d("NuvioQuestInput", "poster=$posterId pointer=DOWN")
                val released = tryAwaitRelease()
                Log.d("NuvioQuestInput", "poster=$posterId pointer=${if (released) "RELEASE" else "CANCEL"}")
            },
            onTap = {
                Log.d("NuvioQuestInput", "poster=$posterId callback=CLICK")
                currentClick()
            },
            onLongPress = if (onLongClick != null) {
                {
                    Log.d("NuvioQuestInput", "poster=$posterId callback=LONG_CLICK")
                    currentLongClick?.invoke()
                }
            } else null
        )
    }
}
