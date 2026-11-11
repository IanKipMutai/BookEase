
package com.bookease

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "bookings")
data class Booking(
    @Id
    val id: String? = null,
    val customerName: String = "",
    val date: String = "",
    val time: String = ""
)
