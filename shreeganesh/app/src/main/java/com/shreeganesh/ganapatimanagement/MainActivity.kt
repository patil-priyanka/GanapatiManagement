package com.shreeganesh.ganapatimanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GanapatiManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "ABC",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GanapatiManagementTheme {
        GanapatiDashboard()
    }
}

@Composable
fun GanapatiDashboard() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "🙏 Ganapati Management",
            fontSize = 24.sp
        )

        Text(
            text = "Event Dashboard",
            fontSize = 18.sp
        )

        Text(
            text = "Total Donation: ₹50,000"
        )

        Text(
            text = "Total Expense: ₹20,000"
        )

        Text(
            text = "Balance: ₹30,000"
        )
    }
}