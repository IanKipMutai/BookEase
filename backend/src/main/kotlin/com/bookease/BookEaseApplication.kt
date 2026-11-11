package com.bookease

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookEaseApplication

fun main(args: Array<String>) {
    runApplication<BookEaseApplication>(*args)
}