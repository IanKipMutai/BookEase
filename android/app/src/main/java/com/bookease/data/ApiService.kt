
package com.bookease.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("api/bookings")
    suspend fun getBookings(): List<BookingDto>

    @POST("api/bookings")
    suspend fun createBooking(
        @Body booking: BookingDto
    ): BookingDto
}

object RetrofitClient {

    // Replace this with your deployed backend HTTPS URL.
    private const val BASE_URL =
        "https://YOUR-BACKEND-ADDRESS/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
