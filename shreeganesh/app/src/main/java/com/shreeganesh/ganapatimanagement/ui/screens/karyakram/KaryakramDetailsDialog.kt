package com.shreeganesh.ganapatimanagement.ui.screens.karyakram

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldWarm
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KaryakramDetailsDialog(
    item: KaryakramItem,
    onDismissRequest: () -> Unit,
    onStatusToggle: (KaryakramItem, EventStatus) -> Unit = { _, _ -> },
    onPlayAarti: (String) -> Unit = {}
) {
    val context = LocalContext.current

    fun dialPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun shareInvitation(context: Context, karyakram: KaryakramItem) {
        val shareText = buildString {
            append("🚩 *॥ श्री गणेशाय नमः ॥* 🚩\n\n")
            append("*श्री गणेश उत्सव २०२६ - विशेष कार्यक्रम निमंत्रण*\n\n")
            append("📌 *कार्यक्रम:* ${karyakram.title}\n")
            append("🗓️ *दिवस व तारीख:* दिवस ${karyakram.dayNumber} • ${karyakram.dateFormatted}\n")
            append("⏰ *वेळ:* ${karyakram.time}${if (karyakram.endTime.isNotBlank()) " ते ${karyakram.endTime}" else ""}\n")
            append("📍 *ठिकाण:* ${karyakram.location}\n")
            if (karyakram.chiefGuest.isNotBlank()) {
                append("⭐ *प्रमुख उपस्थिती:* ${karyakram.chiefGuest}\n")
            }
            if (karyakram.description.isNotBlank()) {
                append("📝 *तपशील:* ${karyakram.description}\n")
            }
            if (karyakram.inchargePerson.isNotBlank()) {
                append("👤 *समन्वयक:* ${karyakram.inchargePerson}")
                if (karyakram.inchargePhone.isNotBlank()) {
                    append(" (संपर्क: ${karyakram.inchargePhone})")
                }
                append("\n")
            }
            append("\n🙏 *आपले नम्र:* समस्त उत्सव समिती व कार्यकर्ते\n")
            append("॥ गणपती बाप्पा मोरया • मंगलमूर्ती मोरया ॥")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "कार्यक्रम निमंत्रण शेअर करा")
        try {
            context.startActivity(shareIntent)
        } catch (_: Exception) {}
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(
                    listOf(MarigoldGold, SaffronPrimary, VermilionDeep)
                ),
                width = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Festive Header with auspicious motif
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(SaffronDeep, SaffronPrimary, MarigoldWarm)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "${item.category.iconEmoji} ${item.category.marathiTitle}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            IconButton(
                                onClick = onDismissRequest,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "बंद करा",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "॥ श्री गणेशाय नमः ॥",
                            color = Color(0xFFFFE082),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "कार्यक्रम पत्रिका पत्र",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Body content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Status Badge and Day Banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = SaffronLight.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🚩 दिवस ${item.dayNumber} चा कार्यक्रम",
                                color = SaffronDeep,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Status Pill
                        val (statusBg, statusFg, statusText) = when (item.status) {
                            EventStatus.LIVE_NOW -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "🔴 थेट सुरू आहे")
                            EventStatus.UPCOMING -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "🔔 आगामी कार्यक्रम")
                            EventStatus.COMPLETED -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "✓ संपन्न झाला")
                        }

                        Surface(
                            color = statusBg,
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, statusFg.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = statusText,
                                color = statusFg,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Event Title
                    Text(
                        text = item.title,
                        color = TextPrimaryLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )

                    // Schedule Info Card (Date, Time, Venue)
                    Surface(
                        color = Color(0xFFFFF8EE),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Date
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SaffronLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = SaffronDeep,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "तारीख",
                                        fontSize = 11.sp,
                                        color = TextSecondaryLight
                                    )
                                    Text(
                                        text = item.dateFormatted,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryLight
                                    )
                                }
                            }

                            HorizontalDivider(color = FestiveBorderLight.copy(alpha = 0.5f))

                            // Time
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SaffronLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = SaffronDeep,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "वेळ",
                                        fontSize = 11.sp,
                                        color = TextSecondaryLight
                                    )
                                    Text(
                                        text = if (item.endTime.isNotBlank()) "${item.time} ते ${item.endTime}" else item.time,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryLight
                                    )
                                }
                            }

                            HorizontalDivider(color = FestiveBorderLight.copy(alpha = 0.5f))

                            // Location
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SaffronLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = SaffronDeep,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "स्थळ / रंगमंच",
                                        fontSize = 11.sp,
                                        color = TextSecondaryLight
                                    )
                                    Text(
                                        text = item.location,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryLight
                                    )
                                }
                            }
                        }
                    }

                    // Chief Guest Section
                    if (item.chiefGuest.isNotBlank()) {
                        Surface(
                            color = Color(0xFFF9F5FF),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1BEE7))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFFEDE7F6), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFF7B1FA2),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "प्रमुख उपस्थिती / विशेष पाहुणे",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6A1B9A),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = item.chiefGuest,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                }
                            }
                        }
                    }

                    // In-charge Coordinator
                    if (item.inchargePerson.isNotBlank()) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SaffronLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = SaffronDeep,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "कार्यक्रम प्रमुख / समन्वयक",
                                        fontSize = 11.sp,
                                        color = TextSecondaryLight
                                    )
                                    Text(
                                        text = item.inchargePerson,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                }

                                if (item.inchargePhone.isNotBlank()) {
                                    IconButton(
                                        onClick = { dialPhone(item.inchargePhone) },
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
                    }

                    // Description
                    if (item.description.isNotBlank()) {
                        Column {
                            Text(
                                text = "कार्यक्रमाचा सविस्तर तपशील:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondaryLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.description,
                                fontSize = 13.sp,
                                color = TextPrimaryLight,
                                lineHeight = 19.sp
                            )
                        }
                    }

                    // Highlights tags
                    if (item.highlights.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            item.highlights.forEach { tag ->
                                Surface(
                                    color = SaffronLight.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(50),
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SaffronDeep.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = "• $tag",
                                        color = SaffronDeep,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // If linked to Aarti, show Listen Aarti action!
                    if (item.aartiId != null) {
                        Button(
                            onClick = {
                                onDismissRequest()
                                onPlayAarti(item.aartiId)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SaffronDeep
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(text = "🪔 ", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ही आरती ऑडिओसह लावा",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Quick Status Update Actions
                    Text(
                        text = "कार्यक्रमाची स्थिती बदला:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondaryLight
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onStatusToggle(item, EventStatus.UPCOMING)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (item.status == EventStatus.UPCOMING) Color(0xFF1565C0) else Color.LightGray
                            )
                        ) {
                            Text(
                                text = "🔔 आगामी",
                                fontSize = 11.sp,
                                fontWeight = if (item.status == EventStatus.UPCOMING) FontWeight.Bold else FontWeight.Normal,
                                color = if (item.status == EventStatus.UPCOMING) Color(0xFF1565C0) else Color.DarkGray
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                onStatusToggle(item, EventStatus.LIVE_NOW)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (item.status == EventStatus.LIVE_NOW) Color(0xFFC62828) else Color.LightGray
                            )
                        ) {
                            Text(
                                text = "🔴 सुरू आहे",
                                fontSize = 11.sp,
                                fontWeight = if (item.status == EventStatus.LIVE_NOW) FontWeight.Bold else FontWeight.Normal,
                                color = if (item.status == EventStatus.LIVE_NOW) Color(0xFFC62828) else Color.DarkGray
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                onStatusToggle(item, EventStatus.COMPLETED)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (item.status == EventStatus.COMPLETED) Color(0xFF2E7D32) else Color.LightGray
                            )
                        ) {
                            Text(
                                text = "✓ संपन्न",
                                fontSize = 11.sp,
                                fontWeight = if (item.status == EventStatus.COMPLETED) FontWeight.Bold else FontWeight.Normal,
                                color = if (item.status == EventStatus.COMPLETED) Color(0xFF2E7D32) else Color.DarkGray
                            )
                        }
                    }

                    // Bottom Action: Share Invitation
                    Button(
                        onClick = { shareInvitation(context, item) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronPrimary
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WhatsApp वर निमंत्रण पाठवा",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
