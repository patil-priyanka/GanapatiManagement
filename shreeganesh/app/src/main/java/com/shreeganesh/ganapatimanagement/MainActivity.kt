package com.shreeganesh.ganapatimanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shreeganesh.ganapatimanagement.ui.GanapatiApp
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GanapatiManagementTheme {
                GanapatiApp()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    GanapatiManagementTheme {
        GanapatiApp()
    }
}