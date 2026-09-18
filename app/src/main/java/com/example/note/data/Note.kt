package com.example.note.data

import java.io.Serializable

data class Note(
    val id: Long = 0,
    val text: String,
    val dateTime: Long = System.currentTimeMillis(),
    val isChecked: Boolean = false,
) : Serializable