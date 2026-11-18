package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary

/**
 * A full-screen loading overlay drawn on top of the current screen content.
 * Shown only on the very first data load; subsequent updates happen in the
 * background while existing data remains visible.
 */
@Composable
fun LoadingOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = SaffronPrimary,
            strokeWidth = 4.dp
        )
    }
}
