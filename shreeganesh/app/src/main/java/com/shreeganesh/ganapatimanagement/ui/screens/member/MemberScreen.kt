package com.shreeganesh.ganapatimanagement.ui.screens.member

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.Member
import com.shreeganesh.ganapatimanagement.model.MemberDepartment
import com.shreeganesh.ganapatimanagement.model.MemberRole
import com.shreeganesh.ganapatimanagement.model.MemberStats
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
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    members: List<Member> = emptyList(),
    onAddMember: (Member) -> Unit = {},
    onDeleteMember: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("सर्व") }
    val filters = listOf(
        "सर्व",
        "🚩 मुख्य पदाधिकारी",
        "🤝 स्वयंसेवक",
        "🪔 पूजा व धार्मिक",
        "🍛 महाप्रसाद व्यवस्था",
        "🎪 मंडप व ध्वनी",
        "🛡️ सुरक्षा व रांग"
    )

    var showAddMemberSheet by remember { mutableStateOf(false) }
    var selectedMemberForDetails by remember { mutableStateOf<Member?>(null) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Statistics
    val totalMembers = members.size
    val officeBearers = members.count { it.role.isCoreBear }
    val volunteers = members.count { it.role == MemberRole.VOLUNTEER }
    val stats = MemberStats(
        totalMembers = totalMembers,
        officeBearers = officeBearers,
        volunteers = volunteers,
        onDutyToday = (totalMembers * 0.75).toInt()
    )

    // Filtered list
    val filteredMembers = members.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.phoneNumber.contains(searchQuery, ignoreCase = true) ||
                item.allocatedDuty.contains(searchQuery, ignoreCase = true) ||
                item.role.marathiTitle.contains(searchQuery, ignoreCase = true) ||
                item.department.marathiTitle.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "🚩 मुख्य पदाधिकारी" -> item.role.isCoreBear
            "🤝 स्वयंसेवक" -> item.role == MemberRole.VOLUNTEER
            "🪔 पूजा व धार्मिक" -> item.department == MemberDepartment.POOJA_RITUAL
            "🍛 महाप्रसाद व्यवस्था" -> item.department == MemberDepartment.MAHAPRASAD
            "🎪 मंडप व ध्वनी" -> item.department == MemberDepartment.MANDAP_SOUND
            "🛡️ सुरक्षा व रांग" -> item.department == MemberDepartment.SECURITY_CROWD
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestiveBackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "मंडळ कार्यकर्ते",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "पदाधिकारी, समिती व स्वयंसेवक सूची",
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
                    IconButton(onClick = { showMessage("मंडळ संपर्क डिरेक्टरी शेअर केली जात आहे...") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "डिरेक्टरी शेअर करा",
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
                onClick = { showAddMemberSheet = true },
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "नवीन कार्यकर्ता",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "नवीन कार्यकर्ता",
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
                        placeholder = { Text("कार्यकर्त्याचे नाव, पद किंवा मोबाईल नंबर शोधा...") },
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

            // 2. Member Statistics Hero Card
            item {
                MemberStatsHeroCard(stats = stats)
            }

            // 3. Filter Chips
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "कार्यकर्ते यादी (${filteredMembers.size} सदस्य)",
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
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 4. Member Items List
            if (filteredMembers.isEmpty()) {
                item {
                    EmptyMembersView(
                        onResetFilters = {
                            searchQuery = ""
                            selectedFilter = "सर्व"
                        }
                    )
                }
            } else {
                items(filteredMembers, key = { it.id }) { member ->
                    MemberCardItem(
                        member = member,
                        onCardClick = { selectedMemberForDetails = member },
                        onCallClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${member.phoneNumber}")
                            }
                            context.startActivity(intent)
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

    // Add Member Bottom Sheet
    if (showAddMemberSheet) {
        AddMemberBottomSheet(
            onDismissRequest = { showAddMemberSheet = false },
            onMemberAdded = { newMember ->
                onAddMember(newMember)
                showAddMemberSheet = false
                selectedMemberForDetails = newMember
                showMessage("${newMember.name} यांची नोंदणी यशस्वी झाली!")
            }
        )
    }

    // Member Details Dialog
    selectedMemberForDetails?.let { member ->
        MemberDetailsDialog(
            member = member,
            onDismissRequest = { selectedMemberForDetails = null },
            onShareMember = {
                showMessage("${member.name} यांचे संपर्क तपशील शेअर केले!")
                selectedMemberForDetails = null
            }
        )
    }
}

@Composable
fun MemberStatsHeroCard(
    stats: MemberStats,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "एकूण मंडळ परिवार",
                        fontSize = 12.sp,
                        color = TextSecondaryLight
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${stats.totalMembers}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = SaffronDeep
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "कार्यकर्ते व सदस्य",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "आज कर्तव्यावर",
                            fontSize = 10.sp,
                            color = SaffronDeep,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${stats.onDutyToday} जण",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronDeep
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Split tiles: Office Bearers vs Volunteers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🚩 मुख्य पदाधिकारी",
                            fontSize = 10.sp,
                            color = VermilionDeep,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${stats.officeBearers} जण",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🤝 सक्रीय स्वयंसेवक",
                            fontSize = 10.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${stats.volunteers} जण",
                            fontSize = 15.sp,
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
fun MemberCardItem(
    member: Member,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onCardClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FestiveBorderLight))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Avatar with Initials
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        brush = Brush.linearGradient(
                            if (member.role.isCoreBear) {
                                listOf(VermilionRed, SaffronPrimary)
                            } else {
                                listOf(SaffronPrimary, MarigoldGold)
                            }
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.initials,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Member Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (member.bloodGroup.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = member.bloodGroup,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VermilionDeep,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Role + Department Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (member.role.isCoreBear) Color(0xFFFFEBEE) else Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${member.role.iconEmoji} ${member.role.marathiTitle}",
                            color = if (member.role.isCoreBear) VermilionDeep else SaffronDeep,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = member.department.marathiTitle,
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                }

                // Duty if present
                if (member.allocatedDuty.isNotBlank()) {
                    Text(
                        text = "🎯 ${member.allocatedDuty}",
                        fontSize = 11.sp,
                        color = TextSecondaryLight,
                        modifier = Modifier.padding(top = 3.dp),
                        maxLines = 1
                    )
                }
            }

            // Quick Call Button
            IconButton(
                onClick = onCallClick,
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFE8F5E9), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "कॉल करा",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyMembersView(
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
                text = "👥",
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "कोणताही कार्यकर्ता आढळला नाही",
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
fun MemberScreenPreview() {
    GanapatiManagementTheme {
        MemberScreen()
    }
}
