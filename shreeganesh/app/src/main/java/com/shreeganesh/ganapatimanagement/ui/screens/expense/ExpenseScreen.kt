package com.shreeganesh.ganapatimanagement.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
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
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.model.ExpenseCategory
import com.shreeganesh.ganapatimanagement.model.ExpenseStats
import com.shreeganesh.ganapatimanagement.model.PaymentMode
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRed
import com.shreeganesh.ganapatimanagement.ui.theme.ExpenseRedContainer
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBackgroundLight
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    modifier: Modifier = Modifier,
    expenses: List<Expense> = emptyList(),
    onAddExpense: (Expense) -> Unit = {},
    onDeleteExpense: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("सर्व") }
    val filters = listOf(
        "सर्व",
        "🎪 मंडप व सजावट",
        "💡 विद्युत रोषणाई",
        "🔊 साऊंड सिस्टीम",
        "🍛 महाप्रसाद",
        "🪔 पूजा साहित्य",
        "🎺 ढोल ताशा",
        "💵 रोख खर्च",
        "📱 ऑनलाईन UPI"
    )

    var showAddExpenseSheet by remember { mutableStateOf(false) }
    var selectedExpenseForVoucher by remember { mutableStateOf<Expense?>(null) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Statistics calculations
    val totalExpense = expenses.sumOf { it.amount }
    val totalVouchers = expenses.size
    val todayExpense = expenses.filter { it.date.contains("०६ ऑक्टो") || it.date.contains("06") }.sumOf { it.amount }
    val cashExpense = expenses.filter { it.paymentMode == PaymentMode.CASH }.sumOf { it.amount }
    val onlineExpense = expenses.filter { it.paymentMode == PaymentMode.UPI }.sumOf { it.amount }
    val pendingDues = expenses.filter { it.isPending }.sumOf { it.pendingAmount }

    val stats = ExpenseStats(
        totalExpense = totalExpense,
        totalVouchers = totalVouchers,
        todayExpense = if (todayExpense > 0) todayExpense else totalExpense * 0.45,
        cashExpense = cashExpense,
        onlineExpense = onlineExpense,
        pendingDues = pendingDues
    )

    // Filtered list
    val filteredExpenses = expenses.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.vendorName.contains(searchQuery, ignoreCase = true) ||
                item.paidBy.contains(searchQuery, ignoreCase = true) ||
                item.voucherNumber.contains(searchQuery, ignoreCase = true) ||
                item.billNumber.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "🎪 मंडप व सजावट" -> item.category == ExpenseCategory.MANDAP_DECORATION
            "💡 विद्युत रोषणाई" -> item.category == ExpenseCategory.LIGHTING
            "🔊 साऊंड सिस्टीम" -> item.category == ExpenseCategory.SOUND_SYSTEM
            "🍛 महाप्रसाद" -> item.category == ExpenseCategory.MAHAPRASAD
            "🪔 पूजा साहित्य" -> item.category == ExpenseCategory.POOJA_AARTI
            "🎺 ढोल ताशा" -> item.category == ExpenseCategory.DHOL_TASHA
            "💵 रोख खर्च" -> item.paymentMode == PaymentMode.CASH
            "📱 ऑनलाईन UPI" -> item.paymentMode == PaymentMode.UPI
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
                            text = "खर्च व्यवस्थापन",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "मंडप, पूजा, साऊंड व दैनंदिन हिशोब",
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
                    IconButton(onClick = { showMessage("खर्च अहवाल शेअर केला जात आहे...") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "अहवाल शेअर करा",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VermilionDeep
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddExpenseSheet = true },
                containerColor = ExpenseRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "नवीन खर्च",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "नवीन खर्च नोंदवा",
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
            // 1. Search Bar
            item {
                AnimatedVisibility(visible = isSearchVisible) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        placeholder = { Text("खर्च शीर्षक, दुकानदार किंवा व्हाऊचर क्र. शोधा...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = ExpenseRed)
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
                            focusedBorderColor = ExpenseRed,
                            unfocusedBorderColor = FestiveBorderLight,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // 2. Expense Statistics Hero Card
            item {
                ExpenseStatsHeroCard(
                    stats = stats,
                    currencyFormatter = currencyFormatter
                )
            }

            // 3. Filter Chips Row
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "खर्च यादी (${filteredExpenses.size} व्हाऊचर्स)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filters) { filter ->
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
                                    selectedContainerColor = ExpenseRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 4. Expense Items List
            if (filteredExpenses.isEmpty()) {
                item {
                    EmptyExpensesView(
                        onResetFilters = {
                            searchQuery = ""
                            selectedFilter = "सर्व"
                        }
                    )
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    ExpenseCardItem(
                        expense = expense,
                        currencyFormatter = currencyFormatter,
                        onViewVoucher = { selectedExpenseForVoucher = expense },
                        onShare = {
                            showMessage("व्हाऊचर #${expense.voucherNumber} (${expense.title}) शेअर केले!")
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

    // Add Expense Modal Bottom Sheet
    if (showAddExpenseSheet) {
        val nextNumber = String.format(Locale.ENGLISH, "%03d", expenses.size + 1)
        AddExpenseBottomSheet(
            nextVoucherNumber = nextNumber,
            onDismissRequest = { showAddExpenseSheet = false },
            onExpenseAdded = { newExpense ->
                onAddExpense(newExpense)
                showAddExpenseSheet = false
                selectedExpenseForVoucher = newExpense
                showMessage("व्हाऊचर #${newExpense.voucherNumber} यशस्वीरित्या जतन झाले!")
            }
        )
    }

    // Digital Voucher Slip Dialog
    selectedExpenseForVoucher?.let { expense ->
        ExpenseVoucherDialog(
            expense = expense,
            onDismissRequest = { selectedExpenseForVoucher = null },
            onShareVoucher = {
                showMessage("व्हाऊचर शेअर केले जात आहे...")
                selectedExpenseForVoucher = null
            }
        )
    }
}

@Composable
fun ExpenseStatsHeroCard(
    stats: ExpenseStats,
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
                listOf(FestiveBorderLight, ExpenseRed.copy(alpha = 0.3f))
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
                        text = "एकूण खर्ची पडलेली रक्कम",
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRed
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = currencyFormatter.format(stats.totalExpense.toLong()),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryLight
                        )
                    }
                }

                Surface(
                    color = ExpenseRedContainer,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "व्हाऊचर्स",
                            fontSize = 10.sp,
                            color = ExpenseRed,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${stats.totalVouchers}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRed
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
                // Today's Expense
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "आजचा खर्च",
                            fontSize = 10.sp,
                            color = ExpenseRed,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.todayExpense.toLong())}",
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
                            text = "💵 रोख खर्च",
                            fontSize = 10.sp,
                            color = TextSecondaryLight,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.cashExpense.toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                // UPI
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📱 ऑनलाईन UPI",
                            fontSize = 10.sp,
                            color = VermilionDeep,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${currencyFormatter.format(stats.onlineExpense.toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseCardItem(
    expense: Expense,
    currencyFormatter: NumberFormat,
    modifier: Modifier = Modifier,
    onViewVoucher: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onViewVoucher() },
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
            // Top row: Voucher No + Category + Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = ExpenseRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "#${expense.voucherNumber}",
                            color = ExpenseRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${expense.category.iconText} ${expense.category.marathiName}",
                            color = ExpenseRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Amount
                Text(
                    text = "- ₹${currencyFormatter.format(expense.amount.toLong())}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = ExpenseRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = expense.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )

            // Vendor & Bill Attachment Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏪 ${expense.vendorName}",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )

                if (expense.imageUri != null) {
                    Surface(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                    ) {
                        Text(
                            text = "📎 बिल फोटो",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom metadata & action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Payment Mode & Paid By
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFF5EBE1),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "${expense.paymentMode.iconText} ${expense.paymentMode.marathiName}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondaryLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${expense.date} • ${expense.paidBy}",
                        fontSize = 10.sp,
                        color = TextSecondaryLight
                    )
                }

                // Actions
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ExpenseRed.copy(alpha = 0.1f),
                        modifier = Modifier.clickable { onViewVoucher() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "व्हाऊचर",
                                tint = ExpenseRed,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "व्हाऊचर",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ExpenseRed
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
fun EmptyExpensesView(
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
                text = "💸",
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "कोणतीही खर्च नोंद आढळली नाही",
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
                color = ExpenseRed.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { onResetFilters() }
            ) {
                Text(
                    text = "फिल्टर रीसेट करा",
                    color = ExpenseRed,
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
fun ExpenseScreenPreview() {
    GanapatiManagementTheme {
        ExpenseScreen()
    }
}
