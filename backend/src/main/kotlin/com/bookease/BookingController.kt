package com.bookease

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val repository: BookingRepository) {

    @GetMapping
    fun getBookings(): List<Booking> = repository.findAll()

    @PostMapping
    fun createBooking(@RequestBody booking: Booking): ResponseEntity<Booking> =
        ResponseEntity.ok(repository.save(booking))

    @DeleteMapping("/{id}")
    fun deleteBooking(@PathVariable id: String): ResponseEntity<Void> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
