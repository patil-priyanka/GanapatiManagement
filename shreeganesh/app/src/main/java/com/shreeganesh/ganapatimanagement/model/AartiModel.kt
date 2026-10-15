package com.shreeganesh.ganapatimanagement.model

data class AartiItem(
    val id: String,
    val title: String,
    val marathiTitle: String,
    val deity: String,
    val durationSeconds: Int,
    val author: String,
    val lyrics: String,
    val description: String = ""
)
