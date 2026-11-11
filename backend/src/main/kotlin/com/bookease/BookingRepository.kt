package com.bookease

import org.springframework.data.mongodb.repository.MongoRepository

interface BookingRepository : MongoRepository<Booking, String>