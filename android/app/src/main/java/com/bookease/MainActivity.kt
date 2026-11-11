
package com.bookease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bookease.data.BookingDto
import com.bookease.data.RetrofitClient
import kotlinx.coroutines.launch

// ----------------------------------------------------
// COLOUR PALETTE
// ----------------------------------------------------

private val Navy = Color(0xFF172B3A)
private val Teal = Color(0xFF287D78)
private val Background = Color(0xFFF4F6F5)
private val CardWhite = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF26343B)
private val Muted = Color(0xFF78858A)
private val Border = Color(0xFFE2E8E6)
private val SoftTeal = Color(0xFFE6F2F0)
private val SoftBlue = Color(0xFFEAF0F5)
private val SoftOrange = Color(0xFFFFF1E5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Teal,
                    secondary = Navy,
                    background = Background,
                    surface = CardWhite
                )
            ) {
                BookingScreen()
            }
        }
    }
}

// ----------------------------------------------------
// MAIN SCREEN
// ----------------------------------------------------

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
    var success by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    fun refresh() {
        scope.launch {
            isLoading = true

            try {
                bookings = RetrofitClient.api.getBookings()
                error = null
            } catch (e: Exception) {
                error = "Unable to load appointments. Check your connection."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    val uniquePatients = bookings
        .map { it.customerName.trim().lowercase() }
        .distinct()
        .size

    Scaffold(
        containerColor = Background
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 35.dp
            ),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ----------------------------------------
            // TOP BAR
            // ----------------------------------------

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(43.dp)
                                .background(Navy, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "B",
                                color = Color.White,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                "BookEase",
                                color = Navy,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "CLINIC MANAGEMENT",
                                color = Muted,
                                fontSize = 9.sp,
                                letterSpacing = 1.3.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        color = SoftTeal,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(
                            "Reception",
                            modifier = Modifier.padding(
                                horizontal = 13.dp,
                                vertical = 9.dp
                            ),
                            color = Teal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ----------------------------------------
            // WELCOME SECTION
            // ----------------------------------------

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Navy,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(22.dp)
                ) {

                    Text(
                        "CLINIC RECEPTION",
                        color = Color(0xFF9CCBC4),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(Modifier.height(15.dp))

                    Text(
                        "Appointments,\norganised.",
                        color = Color.White,
                        fontSize = 29.sp,
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(9.dp))

                    Text(
                        "Manage patient visits and keep the clinic's daily schedule in order.",
                        color = Color(0xFFD1DCE0),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )

                    Spacer(Modifier.height(17.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color(0xFF83D2AE),
                                    CircleShape
                                )
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            "Patient appointment desk",
                            color = Color(0xFFD1DCE0),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ----------------------------------------
            // DASHBOARD STATISTICS
            // ----------------------------------------

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    SectionHeading(
                        title = "Overview",
                        subtitle = "Your appointment register at a glance"
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            title = "Appointments",
                            value = bookings.size.toString(),
                            description = "Records saved",
                            background = SoftBlue
                        )

                        StatisticCard(
                            modifier = Modifier.weight(1f),
                            title = "Patients",
                            value = uniquePatients.toString(),
                            description = "Unique names",
                            background = SoftTeal
                        )
                    }
                }
            }

            // ----------------------------------------
            // PATIENT REGISTRATION FORM
            // ----------------------------------------

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(19.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardWhite
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(19.dp),
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {

                        Text(
                            "NEW APPOINTMENT",
                            color = Teal,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp
                        )

                        Text(
                            "Register a patient",
                            color = TextDark,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Enter the patient's details below to add a visit to the clinic register.",
                            color = Muted,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        HorizontalDivider(color = Border)

                        Text(
                            "Patient information",
                            color = TextDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                success = null
                            },
                            label = { Text("Patient's full name") },
                            placeholder = {
                                Text("Enter full name")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )

                        Text(
                            "Appointment schedule",
                            color = TextDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        OutlinedTextField(
                            value = date,
                            onValueChange = {
                                date = it
                                success = null
                            },
                            label = { Text("Appointment date") },
                            placeholder = {
                                Text("YYYY-MM-DD")
                            },
                            supportingText = {
                                Text("Example: 2026-11-15")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = time,
                            onValueChange = {
                                time = it
                                success = null
                            },
                            label = { Text("Appointment time") },
                            placeholder = {
                                Text("e.g. 10:30 AM")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        // Informational note

                        Surface(
                            color = SoftTeal,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {

                                Text(
                                    "i",
                                    color = Teal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    "Please verify the appointment date and time with the patient before saving. The clinic can use this register to organise upcoming visits.",
                                    color = Color(0xFF42645F),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Button(
                            onClick = {

                                if (
                                    name.isBlank() ||
                                    date.isBlank() ||
                                    time.isBlank()
                                ) {
                                    error = "Please complete all three fields."
                                    success = null
                                } else {

                                    scope.launch {
                                        isSaving = true
                                        error = null
                                        success = null

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

                                            success =
                                                "Patient appointment saved successfully."

                                        } catch (e: Exception) {

                                            error =
                                                "Unable to save or refresh the appointment. Please check your connection."

                                        } finally {
                                            isSaving = false
                                        }
                                    }
                                }
                            },

                            enabled = !isSaving,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),

                            shape = RoundedCornerShape(12.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Teal
                            )
                        ) {

                            if (isSaving) {

                                CircularProgressIndicator(
                                    modifier = Modifier.size(21.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )

                                Spacer(Modifier.width(10.dp))

                                Text("Saving appointment...")

                            } else {

                                Text(
                                    "Save appointment",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
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

                        success?.let {
                            Text(
                                it,
                                color = Teal,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // ----------------------------------------
            // APPOINTMENT REGISTER
            // ----------------------------------------

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            "Appointment register",
                            color = TextDark,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Patients registered with the clinic",
                            color = Muted,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(
                        onClick = { refresh() },
                        enabled = !isLoading
                    ) {
                        Text(
                            "Refresh",
                            color = Teal,
                            fontWeight = FontWeight.SemiBold
                        )
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
                        CircularProgressIndicator(color = Teal)
                    }
                }

            } else if (bookings.isEmpty()) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardWhite
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "No appointments recorded",
                                color = TextDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Spacer(Modifier.height(7.dp))

                            Text(
                                "New patient appointments will appear here after they have been saved.",
                                color = Muted,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
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
                            containerColor = CardWhite
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(43.dp)
                                    .background(
                                        SoftTeal,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    booking.customerName
                                        .firstOrNull()
                                        ?.uppercase() ?: "?",
                                    color = Teal,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(13.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    booking.customerName,
                                    color = TextDark,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(5.dp))

                                Text(
                                    "Appointment date",
                                    color = Muted,
                                    fontSize = 11.sp
                                )

                                Text(
                                    booking.date,
                                    color = TextDark,
                                    fontSize = 13.sp
                                )

                                Text(
                                    booking.time,
                                    color = Teal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Surface(
                                color = SoftOrange,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    "Recorded",
                                    modifier = Modifier.padding(
                                        horizontal = 9.dp,
                                        vertical = 6.dp
                                    ),
                                    color = Color(0xFF98612C),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // ----------------------------------------
            // FOOTER
            // ----------------------------------------

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    HorizontalDivider(color = Border)

                    Spacer(Modifier.height(15.dp))

                    Text(
                        "BOOKEASE",
                        color = Navy,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        "A simpler way to organise clinic appointments.",
                        color = Muted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// REUSABLE DASHBOARD COMPONENTS
// ----------------------------------------------------

@Composable
private fun SectionHeading(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            title,
            color = TextDark,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            subtitle,
            color = Muted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun StatisticCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    description: String,
    background: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            Text(
                title,
                color = TextDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(9.dp))

            Text(
                value,
                color = Navy,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                description,
                color = Muted,
                fontSize = 11.sp
            )
        }
    }
}
