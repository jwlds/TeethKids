package com.example.teethkids.model

import java.time.LocalDate

data class Notification(
    val body: String,
    val data: LocalDate,
)