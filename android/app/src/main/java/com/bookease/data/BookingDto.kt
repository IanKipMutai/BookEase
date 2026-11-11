package com.bookease.data

data class BookingDto(
    val id: String? = null,
    val customerName: String,
    val date: String,
    val time: String
)