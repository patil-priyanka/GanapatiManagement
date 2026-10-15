package com.shreeganesh.ganapatimanagement.ui.screens.karyakram

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.FestivalDayInfo
import com.shreeganesh.ganapatimanagement.model.KaryakramCategory
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import com.shreeganesh.ganapatimanagement.model.getFestivalDaysList
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBackgroundLight
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldWarm
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaryakramScreen(
    modifier: Modifier = Modifier,
    karyakramList: List<KaryakramItem> = emptyList(),
    onAddKaryakram: (KaryakramItem) -> Unit = {},
    onUpdateKaryakramStatus: (String, EventStatus) -> Unit = { _, _ -> },
    onDeleteKaryakram: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onPlayAarti: (String) -> Unit = {}
) {
    val festivalDays = remember { getFestivalDaysList() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    // 0 means "All Days", 1 to 10 means specific day
    var selectedDayNumber by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<KaryakramCategory?>(null) }
    var selectedStatusFilter by remember { mutableStateOf<EventStatus?>(null) }

    var showAddKaryakramSheet by remember { mutableStateOf(false) }
    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedItemForDetails by remember { mutableStateOf<KaryakramItem?>(null) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    fun dialPhone(phoneNumber: String) {
        if (phoneNumber.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun shareFullSchedule(context: Context, items: List<KaryakramItem>) {
        val shareText = buildString {
            append("🚩 *॥ श्री गणेशाय नमः ॥* 🚩\n\n")
            append("*श्री गणेश उत्सव २०२६ - संपूर्ण कार्यक्रम वेळापत्रक*\n\n")
            festivalDays.forEach { day ->
                val dayEvents = items.filter { it.dayNumber == day.dayNumber }
                if (dayEvents.isNotEmpty()) {
                    append("━━━━━━━━━━━━━━━━━━━\n")
                    append("📅 *दिवस ${day.dayNumber} • ${day.dateText} (${day.tithi})*\n")
                    dayEvents.forEach { ev ->
                        append("⏰ ${ev.time} : ${ev.title}\n")
                        append("   📍 ${ev.location}")
                        if (ev.inchargePerson.isNotBlank()) append(" (समन्वयक: ${ev.inchargePerson})")
                        append("\n")
                    }
                    append("\n")
                }
            }
            append("━━━━━━━━━━━━━━━━━━━\n")
            append("॥ गणपती बाप्पा मोरया • मंगलमूर्ती मोरया ॥\n")
            append("सर्व भाविक भक्तांनी उपस्थित राहावे ही नम्र विनंती.")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "संपूर्ण उत्सव पत्रिका शेअर करा")
        try {
            context.startActivity(shareIntent)
        } catch (_: Exception) {}
    }

    // Filter items
    val filteredItems = karyakramList.filter { item ->
        val matchesDay = (selectedDayNumber == 0) || (item.dayNumber == selectedDayNumber)
        val matchesCategory = (selectedCategory == null) || (item.category == selectedCategory)
        val matchesStatus = (selectedStatusFilter == null) || (item.status == selectedStatusFilter)
        val matchesSearch = if (searchQuery.isBlank()) {
            true
        } else {
            val q = searchQuery.trim().lowercase()
            item.title.lowercase().contains(q) ||
                    item.description.lowercase().contains(q) ||
                    item.location.lowercase().contains(q) ||
                    item.chiefGuest.lowercase().contains(q) ||
                    item.inchargePerson.lowercase().contains(q) ||
                    item.category.marathiTitle.lowercase().contains(q)
        }
        matchesDay && matchesCategory && matchesStatus && matchesSearch
    }

    // Stats
    val totalCount = karyakramList.size
    val liveCount = karyakramList.count { it.status == EventStatus.LIVE_NOW }
    val upcomingCount = karyakramList.count { it.status == EventStatus.UPCOMING }
    val completedCount = karyakramList.count { it.status == EventStatus.COMPLETED }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestiveBackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "कार्यक्रम पत्रिका",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "१० दिवसीय उत्सव वेळापत्रक व नियोजन",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "मागे जा",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "शोधा",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { shareFullSchedule(context, karyakramList) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "वेळापत्रक शेअर करा",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddKaryakramSheet = true },
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "कार्यक्रम जोडा",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "नवीन कार्यक्रम",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar (collapsible)
            AnimatedVisibility(visible = isSearchVisible) {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("कार्यक्रम, स्थळ, पाहुणे किंवा समन्वयक शोधा...", fontSize = 13.sp) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = SaffronPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "हटवा",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                unfocusedBorderColor = FestiveBorderLight,
                                focusedContainerColor = Color(0xFFFFFBF7),
                                unfocusedContainerColor = Color(0xFFFFFBF7)
                            )
                        )
                    }
                }
            }

            // Stats Quick Summary Cards Bar
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalCount",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronDeep
                        )
                        Text(
                            text = "एकूण कार्यक्रम",
                            fontSize = 10.sp,
                            color = TextSecondaryLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(Color(0xFFEEEEEE))
                    )

                    // Live Now (Clickable filter)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedStatusFilter = if (selectedStatusFilter == EventStatus.LIVE_NOW) null else EventStatus.LIVE_NOW
                            },
                        color = if (selectedStatusFilter == EventStatus.LIVE_NOW) Color(0xFFFFEBEE) else Color.Transparent
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(Color(0xFFD32F2F), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$liveCount",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                            }
                            Text(
                                text = "थेट सुरू आहे",
                                fontSize = 10.sp,
                                fontWeight = if (selectedStatusFilter == EventStatus.LIVE_NOW) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFFD32F2F)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(Color(0xFFEEEEEE))
                    )

                    // Upcoming (Clickable filter)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedStatusFilter = if (selectedStatusFilter == EventStatus.UPCOMING) null else EventStatus.UPCOMING
                            },
                        color = if (selectedStatusFilter == EventStatus.UPCOMING) Color(0xFFE3F2FD) else Color.Transparent
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$upcomingCount",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "आगामी",
                                fontSize = 10.sp,
                                fontWeight = if (selectedStatusFilter == EventStatus.UPCOMING) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFF1976D2)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(Color(0xFFEEEEEE))
                    )

                    // Completed (Clickable filter)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedStatusFilter = if (selectedStatusFilter == EventStatus.COMPLETED) null else EventStatus.COMPLETED
                            },
                        color = if (selectedStatusFilter == EventStatus.COMPLETED) Color(0xFFE8F5E9) else Color.Transparent
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$completedCount",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF388E3C)
                            )
                            Text(
                                text = "संपन्न",
                                fontSize = 10.sp,
                                fontWeight = if (selectedStatusFilter == EventStatus.COMPLETED) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFF388E3C)
                            )
                        }
                    }
                }
            }

            // 10-Day Festival Timeline Carousel Selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF9F0))
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "उत्सव कालमर्यादा (१० दिवस):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronDeep
                    )
                    if (selectedDayNumber != 0 || selectedCategory != null || selectedStatusFilter != null) {
                        Text(
                            text = "फिल्टर हटवा",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VermilionDeep,
                            modifier = Modifier.clickable {
                                selectedDayNumber = 0
                                selectedCategory = null
                                selectedStatusFilter = null
                                searchQuery = ""
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // "All Days" Card
                    item {
                        val isAllSelected = selectedDayNumber == 0
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedDayNumber = 0 },
                            color = if (isAllSelected) SaffronDeep else Color.White,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isAllSelected) SaffronPrimary else FestiveBorderLight
                            ),
                            shadowElevation = if (isAllSelected) 3.dp else 0.5.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "सर्व दिवस",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAllSelected) Color.White else TextPrimaryLight
                                )
                                Text(
                                    text = "१० दिवस",
                                    fontSize = 10.sp,
                                    color = if (isAllSelected) Color(0xFFFFE082) else TextSecondaryLight
                                )
                            }
                        }
                    }

                    // Days 1 to 10 Cards
                    items(festivalDays) { day ->
                        val isSelected = selectedDayNumber == day.dayNumber
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedDayNumber = day.dayNumber },
                            color = if (isSelected) SaffronDeep else Color.White,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SaffronPrimary else if (day.isMainDay) MarigoldGold else FestiveBorderLight
                            ),
                            shadowElevation = if (isSelected) 3.dp else 0.5.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (day.isMainDay) {
                                        Text(text = "⭐ ", fontSize = 9.sp)
                                    }
                                    Text(
                                        text = "दिवस ${day.dayNumber}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextPrimaryLight
                                    )
                                }
                                Text(
                                    text = day.dateText,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFFFE082) else TextSecondaryLight
                                )
                            }
                        }
                    }
                }
            }

            // Category Chips Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All Categories chip
                item {
                    val isAllCat = selectedCategory == null
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { selectedCategory = null },
                        color = if (isAllCat) SaffronPrimary else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isAllCat) SaffronDeep else FestiveBorderLight
                        )
                    ) {
                        Text(
                            text = "सर्व वर्गवारी",
                            fontSize = 11.sp,
                            fontWeight = if (isAllCat) FontWeight.Bold else FontWeight.Medium,
                            color = if (isAllCat) Color.White else TextPrimaryLight,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                items(KaryakramCategory.values().filter { it != KaryakramCategory.ALL }) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable {
                                selectedCategory = if (isSelected) null else cat
                            },
                        color = if (isSelected) SaffronPrimary else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) SaffronDeep else FestiveBorderLight
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cat.iconEmoji, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = cat.marathiTitle,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimaryLight
                            )
                        }
                    }
                }
            }

            // Results count label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedDayNumber == 0) "एकूण ${filteredItems.size} कार्यक्रम" else "दिवस $selectedDayNumber चे ${filteredItems.size} कार्यक्रम",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondaryLight
                )

                if (selectedStatusFilter != null) {
                    Surface(
                        color = SaffronLight.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${selectedStatusFilter?.marathiTitle} कार्यक्रम फिल्टर",
                            fontSize = 10.sp,
                            color = SaffronDeep,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Program Cards List
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🚩", fontSize = 42.sp)
                        Text(
                            text = "या निकषांनुसार कोणतेही कार्यक्रम उपलब्ध नाहीत",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Text(
                            text = "कृपया दिवस किंवा वर्गवारी फिल्टर बदलून पहा किंवा नवीन कार्यक्रम जोडा.",
                            fontSize = 12.sp,
                            color = TextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedDayNumber = 0
                                    selectedCategory = null
                                    selectedStatusFilter = null
                                    searchQuery = ""
                                },
                            color = SaffronLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "सर्व फिल्टर पूर्ववत करा",
                                color = SaffronDeep,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 86.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        KaryakramCard(
                            item = item,
                            onClick = { selectedItemForDetails = item },
                            onCallClick = { dialPhone(item.inchargePhone) },
                            onPlayAartiClick = {
                                if (item.aartiId != null) {
                                    onPlayAarti(item.aartiId)
                                }
                            },
                            onStatusChange = { newStatus ->
                                onUpdateKaryakramStatus(item.id, newStatus)
                                showMessage("${item.title} ची स्थिती अपडेट केली")
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Add Program
    if (showAddKaryakramSheet) {
        AddKaryakramBottomSheet(
            sheetState = addSheetState,
            onDismissRequest = { showAddKaryakramSheet = false },
            onKaryakramAdded = { newItem ->
                onAddKaryakram(newItem)
                showAddKaryakramSheet = false
                showMessage("✨ '${newItem.title}' उत्सव पत्रिकेत जोडले गेले!")
            }
        )
    }

    // Details / Invitation Modal Dialog
    selectedItemForDetails?.let { detailItem ->
        KaryakramDetailsDialog(
            item = detailItem,
            onDismissRequest = { selectedItemForDetails = null },
            onStatusToggle = { targetItem, newStatus ->
                onUpdateKaryakramStatus(targetItem.id, newStatus)
                selectedItemForDetails = targetItem.copy(status = newStatus)
                showMessage("कार्यक्रमाची स्थिती बदलली: ${newStatus.marathiTitle}")
            },
            onPlayAarti = { aartiId ->
                selectedItemForDetails = null
                onPlayAarti(aartiId)
            }
        )
    }
}

@Composable
fun KaryakramCard(
    item: KaryakramItem,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onPlayAartiClick: () -> Unit,
    onStatusChange: (EventStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.status == EventStatus.LIVE_NOW) Color(0xFFFFF9F5) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isPinned) 4.dp else 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = if (item.status == EventStatus.LIVE_NOW) {
                Brush.horizontalGradient(listOf(Color(0xFFD32F2F), SaffronPrimary))
            } else if (item.isPinned) {
                Brush.horizontalGradient(listOf(MarigoldGold, SaffronPrimary))
            } else {
                Brush.linearGradient(listOf(FestiveBorderLight, FestiveBorderLight))
            },
            width = if (item.status == EventStatus.LIVE_NOW || item.isPinned) 1.5.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Meta Row: Day Badge + Category + Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Day Badge
                    Surface(
                        color = SaffronLight.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "दिवस ${item.dayNumber}",
                            color = SaffronDeep,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Category Pill
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${item.category.iconEmoji} ${item.category.marathiTitle}",
                            color = TextPrimaryLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (item.isPinned) {
                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "⭐ मुख्य",
                                color = Color(0xFFF57F17),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Status Pill
                val (statusBg, statusFg, statusText) = when (item.status) {
                    EventStatus.LIVE_NOW -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "🔴 सुरू आहे")
                    EventStatus.UPCOMING -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "🔔 आगामी")
                    EventStatus.COMPLETED -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "✓ संपन्न")
                }

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, statusFg.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = statusText,
                        color = statusFg,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Program Title
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Timing & Venue Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = SaffronDeep,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (item.endTime.isNotBlank()) "${item.time} - ${item.endTime}" else item.time,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryLight
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = TextSecondaryLight,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = item.location,
                    fontSize = 12.sp,
                    color = TextSecondaryLight,
                    maxLines = 1
                )
            }

            // Chief Guest (if present)
            if (item.chiefGuest.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF57C00),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "पाहुणे: ${item.chiefGuest}",
                        fontSize = 11.sp,
                        color = Color(0xFF6A1B9A),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom action row: Incharge coordinator + Call + Aarti button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Incharge text
                if (item.inchargePerson.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "प्रमुख: ${item.inchargePerson}",
                            fontSize = 11.sp,
                            color = TextSecondaryLight,
                            maxLines = 1
                        )
                        if (item.inchargePhone.isNotBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = onCallClick,
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(Color(0xFFE8F5E9), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "कॉल करा",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // If Aarti item, show quick listen button!
                if (item.aartiId != null) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onPlayAartiClick() },
                        color = SaffronLight,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SaffronDeep.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🪔 ", fontSize = 11.sp)
                            Text(
                                text = "आरती ऐका",
                                color = SaffronDeep,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
