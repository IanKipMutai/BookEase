
package com.bookease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bookease.data.BookingDto
import com.bookease.data.RetrofitClient
import kotlinx.coroutines.launch

private val Purple = Color(0xFF59446F)
private val DarkPurple = Color(0xFF292038)
private val Background = Color(0xFFF7F5F8)
private val TextDark = Color(0xFF292631)
private val Muted = Color(0xFF817B87)
private val BorderColor = Color(0xFFE6E1EA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Purple,
                    secondary = DarkPurple,
                    background = Background,
                    surface = Color.White
                )
            ) {
                BookingScreen()
            }
        }
    }
}

@Composable
fun BookingScreen() {
    val scope = rememberCoroutineScope()

    var bookings by remember {
        mutableStateOf<List<BookingDto>>(emptyList())
    }

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    fun refresh() {
        scope.launch {
            isLoading = true
            try {
                bookings = RetrofitClient.api.getBookings()
                error = null
            } catch (e: Exception) {
                error = "Could not load bookings. Check your internet connection."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    Scaffold(
        containerColor = Background
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            DarkPurple,
                            RoundedCornerShape(18.dp)
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        "BOOKEASE",
                        color = Color(0xFFD8C8E9),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Make a booking.",
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(7.dp))

                    Text(
                        "Simple scheduling, without the fuss.",
                        color = Color(0xFFD8D0DD),
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        Text(
                            "Your reservation",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Text(
                            "Fill in your details to reserve your time.",
                            fontSize = 13.sp,
                            color = Muted
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(11.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date") },
                            placeholder = { Text("YYYY-MM-DD") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(11.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            label = { Text("Time") },
                            placeholder = { Text("e.g. 10:30 AM") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(11.dp),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (
                                    name.isBlank() ||
                                    date.isBlank() ||
                                    time.isBlank()
                                ) {
                                    error = "Please complete all fields."
                                } else {
                                    scope.launch {
                                        isSaving = true
                                        error = null

                                        try {
                                            RetrofitClient.api.createBooking(
                                                BookingDto(
                                                    customerName = name.trim(),
                                                    date = date.trim(),
                                                    time = time.trim()
                                                )
                                            )

                                            name = ""
                                            date = ""
                                            time = ""

                                            bookings =
                                                RetrofitClient.api.getBookings()

                                        } catch (e: Exception) {
                                            error = "Booking failed. Please try again."
                                        } finally {
                                            isSaving = false
                                        }
                                    }
                                }
                            },
                            enabled = !isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(11.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple
                            )
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(21.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Confirm booking",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        error?.let {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Your bookings",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Text(
                            "Your saved reservations",
                            fontSize = 13.sp,
                            color = Muted
                        )
                    }

                    TextButton(onClick = { refresh() }) {
                        Text("Refresh", color = Purple)
                    }
                }
            }

            if (isLoading && bookings.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(25.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Purple)
                    }
                }
            } else if (bookings.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No bookings yet",
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "Your reservations will appear here.",
                                color = Muted,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(17.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    booking.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextDark
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    "${booking.date} • ${booking.time}",
                                    color = Muted,
                                    fontSize = 13.sp
                                )
                            }

                            Surface(
                                color = Color(0xFFF0EAF5),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    "Booked",
                                    modifier = Modifier.padding(
                                        horizontal = 11.dp,
                                        vertical = 7.dp
                                    ),
                                    color = Purple,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
