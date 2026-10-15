package com.shreeganesh.ganapatimanagement.ui.screens.donation

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.Donation
import com.shreeganesh.ganapatimanagement.model.DonationCategory
import com.shreeganesh.ganapatimanagement.model.DonationStats
import com.shreeganesh.ganapatimanagement.model.PaymentMode
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreenContainer
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBackgroundLight
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    modifier: Modifier = Modifier,
    donations: List<Donation> = emptyList(),
    onAddDonation: (Donation) -> Unit = {},
    onDeleteDonation: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("सर्व") }
    val filters = listOf("सर्व", "💵 रोख", "📱 ऑनलाईन UPI", "🍛 महाप्रसाद", "🪔 आरती", "⭐ विशेष (₹५,०००+)")

    var showAddDonationSheet by remember { mutableStateOf(false) }
    var selectedDonationForReceipt by remember { mutableStateOf<Donation?>(null) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Calculate live statistics
    val totalAmount = donations.sumOf { it.amount }
    val totalDonors = donations.size
    val todayAmount = donations.filter { it.date.contains("०६ ऑक्टो") || it.date.contains("06") }.sumOf { it.amount }
    val cashAmount = donations.filter { it.paymentMode == PaymentMode.CASH }.sumOf { it.amount }
    val upiAmount = donations.filter { it.paymentMode == PaymentMode.UPI }.sumOf { it.amount }
    val stats = DonationStats(
        totalAmount = totalAmount,
        totalDonors = totalDonors,
        todayAmount = if (todayAmount > 0) todayAmount else totalAmount * 0.25,
        cashAmount = cashAmount,
        upiAmount = upiAmount
    )

    // Filtered list
    val filteredDonations = donations.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.donorName.contains(searchQuery, ignoreCase = true) ||
                item.phoneNumber.contains(searchQuery, ignoreCase = true) ||
                item.receiptNumber.contains(searchQuery, ignoreCase = true) ||
                item.address.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "💵 रोख" -> item.paymentMode == PaymentMode.CASH
            "📱 ऑनलाईन UPI" -> item.paymentMode == PaymentMode.UPI
            "🍛 महाप्रसाद" -> item.category == DonationCategory.MAHAPRASAD
            "🪔 आरती" -> item.category == DonationCategory.AARTI
            "⭐ विशेष (₹५,०००+)" -> item.amount >= 5000.0
            else -> true
        }

        matchesSearch && matchesFilter
    }

    val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestiveBackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "देणगी व्यवस्थापन",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "एकूण पावती व संकलन नोंदी",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
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
                    IconButton(onClick = { showMessage("देणगी अहवाल तयार केला जात आहे...") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "अहवाल शेअर करा",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronDeep
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDonationSheet = true },
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "देणगी नोंद",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "नवीन पावती",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Search Bar (Expandable or always accessible)
            item {
                AnimatedVisibility(visible = isSearchVisible) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        placeholder = { Text("देणगीदाराचे नाव, मोबाईल क्र. किंवा पावती क्र. शोधा...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SaffronPrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "हटवा")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaffronPrimary,
                            unfocusedBorderColor = FestiveBorderLight,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // 2. Donation Statistics Hero Card
            item {
                DonationStatsHeroCard(
                    stats = stats,
                    currencyFormatter = currencyFormatter
                )
            }

            // 3. Filter Chips Row
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "पावती यादी (${filteredDonations.size} नोंदी)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        filters.take(3).forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        filters.drop(3).forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 4. Donation Items List
            if (filteredDonations.isEmpty()) {
                item {
                    EmptyDonationsView(
                        onResetFilters = {
                            searchQuery = ""
                            selectedFilter = "सर्व"
                        }
                    )
                }
            } else {
                items(filteredDonations, key = { it.id }) { donation ->
                    DonationCardItem(
                        donation = donation,
                        currencyFormatter = currencyFormatter,
                        onViewReceipt = { selectedDonationForReceipt = donation },
                        onShare = {
                            showMessage("पावती क्र. #${donation.receiptNumber} (${donation.donorName}) शेअर केली!")
                        }
                    )
                }
            }

            // Bottom space for FAB
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    // Add Donation Modal Bottom Sheet
    if (showAddDonationSheet) {
        val nextNumber = (100 + donations.size + 1).toString()
        AddDonationBottomSheet(
            nextReceiptNumber = nextNumber,
            onDismissRequest = { showAddDonationSheet = false },
            onDonationAdded = { newDonation ->
                onAddDonation(newDonation)
                showAddDonationSheet = false
                selectedDonationForReceipt = newDonation
                showMessage("पावती क्र. #${newDonation.receiptNumber} यशस्वीरित्या नोंदवली गेली!")
            }
        )
    }

    // Digital Receipt Slip Dialog
    selectedDonationForReceipt?.let { donation ->
        DigitalReceiptDialog(
            donation = donation,
            onDismissRequest = { selectedDonationForReceipt = null },
            onShareReceipt = {
                showMessage("पावती WhatsApp वर शेअर केली जात आहे...")
                selectedDonationForReceipt = null
            }
        )
    }
}

@Composable
fun DonationStatsHeroCard(
    stats: DonationStats,
    currencyFormatter: NumberFormat,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(FestiveBorderLight, SaffronLight.copy(alpha = 0.5f))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "एकूण गोळा झालेली देणगी",
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DonationGreen
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = currencyFormatter.format(stats.totalAmount.toLong()),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryLight
                        )
                    }
                }

                Surface(
                    color = DonationGreenContainer,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "देणगीदार",
                            fontSize = 10.sp,
                            color = DonationGreen,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${stats.totalDonors}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DonationGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Split metrics: Today vs Cash vs UPI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Today's Collection
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "आजचे संकलन",
                            fontSize = 10.sp,
                            color = SaffronDeep,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.todayAmount.toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                // Cash
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFF3ECE4),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💵 रोख देणगी",
                            fontSize = 10.sp,
                            color = TextSecondaryLight,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.cashAmount.toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                // UPI
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📱 ऑनलाईन UPI",
                            fontSize = 10.sp,
                            color = DonationGreen,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.upiAmount.toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Digital UPI Percentage indicator
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "डिजिटल UPI संकलन प्रमाण",
                        fontSize = 10.sp,
                        color = TextSecondaryLight
                    )
                    Text(
                        text = "${(stats.upiPercentage * 100).toInt()}% ऑनलाईन",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DonationGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { stats.upiPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = DonationGreen,
                    trackColor = Color(0xFFEFE8DE)
                )
            }
        }
    }
}

@Composable
fun DonationCardItem(
    donation: Donation,
    currencyFormatter: NumberFormat,
    modifier: Modifier = Modifier,
    onViewReceipt: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onViewReceipt() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FestiveBorderLight))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top row: Receipt number + Category + Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = SaffronDeep.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "#${donation.receiptNumber}",
                            color = SaffronDeep,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = donation.category.marathiName,
                            color = SaffronDeep,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Amount
                Text(
                    text = "₹${currencyFormatter.format(donation.amount.toLong())}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DonationGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Donor Name
            Text(
                text = donation.donorName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )

            // Address / Note if available
            if (donation.address.isNotBlank() || donation.note.isNotBlank()) {
                val detail = if (donation.address.isNotBlank()) donation.address else donation.note
                Text(
                    text = "📍 $detail",
                    fontSize = 11.sp,
                    color = TextSecondaryLight,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom metadata & action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Payment Mode & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (donation.paymentMode == PaymentMode.UPI) Color(0xFFE8F5E9) else Color(0xFFF5EBE1),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "${donation.paymentMode.iconText} ${donation.paymentMode.marathiName}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (donation.paymentMode == PaymentMode.UPI) DonationGreen else TextSecondaryLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${donation.date} • ${donation.time}",
                        fontSize = 10.sp,
                        color = TextSecondaryLight
                    )
                }

                // Quick Card Actions
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SaffronPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.clickable { onViewReceipt() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "पावती",
                                tint = SaffronDeep,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "पावती",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDeep
                            )
                        }
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "शेअर",
                            tint = TextSecondaryLight,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDonationsView(
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "कोणतीही देणगी पावती आढळली नाही",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            Text(
                text = "कृपया शोध शब्द किंवा फिल्टर बदलून पहा.",
                fontSize = 12.sp,
                color = TextSecondaryLight
            )
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                color = SaffronPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { onResetFilters() }
            ) {
                Text(
                    text = "फिल्टर रीसेट करा",
                    color = SaffronDeep,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DonationScreenPreview() {
    GanapatiManagementTheme {
        DonationScreen()
    }
}
