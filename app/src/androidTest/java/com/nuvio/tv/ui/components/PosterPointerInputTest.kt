package com.nuvio.tv.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTvMaterial3Api::class)
class PosterPointerInputTest {
    @get:Rule val compose = createComposeRule()
    private var clicks = 0
    private var longClicks = 0

    private fun showCard() {
        compose.setContent {
            Card(
                onClick = { clicks++ },
                modifier = Modifier.size(160.dp).testTag("poster")
                    .posterPointerInput("test", { clicks++ }, { longClicks++ })
            ) { Box(Modifier.size(160.dp)) }
        }
    }

    @Test fun touchOpensExactlyOnce() {
        showCard()
        compose.onNodeWithTag("poster").performTouchInput { click() }
        compose.runOnIdle {
            assertEquals(1, clicks)
            assertEquals(0, longClicks)
        }
    }

    @Test fun longPressDoesNotOpenOnRelease() {
        showCard()
        compose.onNodeWithTag("poster").performTouchInput { longClick() }
        compose.runOnIdle {
            assertEquals(0, clicks)
            assertEquals(1, longClicks)
        }
    }

    @Test fun cancelledGestureDoesNotOpen() {
        showCard()
        compose.onNodeWithTag("poster").performTouchInput {
            down(center)
            cancel()
        }
        compose.runOnIdle { assertEquals(0, clicks) }
    }

    @Test fun accessibilityClickStillOpensExactlyOnce() {
        showCard()
        compose.onNodeWithTag("poster").performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }
}
