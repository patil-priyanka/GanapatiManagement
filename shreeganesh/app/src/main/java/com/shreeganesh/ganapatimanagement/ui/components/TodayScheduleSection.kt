package com.shreeganesh.ganapatimanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.model.AartiEvent
import com.shreeganesh.ganapatimanagement.ui.theme.DonationGreen
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed

@Composable
fun defaultAartiSchedule(): List<AartiEvent> {
    return listOf(
        AartiEvent(
            id = "aarti_1",
            title = "Morning Aarti & Naivedya",
            marathiTitle = "सकाळची महाआरती व नैवेद्य",
            time = "०८:०० AM",
            location = "मुख्य मंडप",
            isCompleted = true,
            prasadDetails = "उकडीचे मोदक महाप्रसाद",
            host = "पाटील कुटुंब"
        ),
        AartiEvent(
            id = "aarti_2",
            title = "Afternoon Mahaprasad",
            marathiTitle = "दुपारचे महाप्रसाद वाटप",
            time = "०१:०० PM",
            location = "प्रसाद मंडप",
            isCompleted = true,
            prasadDetails = "सर्व भाविकांसाठी अन्नदान",
            host = "युवा समिती"
        ),
        AartiEvent(
            id = "aarti_3",
            title = "Evening Maha Aarti",
            marathiTitle = "संध्याकाळची महाआरती व भजन",
            time = "०७:३० PM",
            location = "मुख्य मंडप",
            isNext = true,
            prasadDetails = "लाडू व पंचखाद्य प्रसाद",
            host = "आमदार श्री. मोरे यांच्या हस्ते"
        ),
        AartiEvent(
            id = "aarti_4",
            title = "Cultural Event & Dhol Tasha",
            marathiTitle = "सांस्कृतिक कार्यक्रम व ढोल ताशा",
            time = "०९:०० PM",
            location = "उत्सव रंगमंच",
            isNext = false,
            prasadDetails = "बालकलाकार नृत्य व संगीत",
            host = "कला मंडळ"
        )
    )
}

@Composable
fun TodayScheduleSection(
    events: List<AartiEvent> = defaultAartiSchedule(),
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit = {},
    onEventClick: (AartiEvent) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "आजचे कार्यक्रम व आरती (Schedule)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = "वेळापत्रक व महाप्रसाद माहिती",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }

            Surface(
                modifier = Modifier.clickable { onViewAllClick() },
                color = SaffronPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "सर्व वेळापत्रक ›",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronDeep,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Schedule items
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            events.forEach { event ->
                ScheduleItemCard(
                    event = event,
                    onClick = { onEventClick(event) }
                )
            }
        }
    }
}

@Composable
fun ScheduleItemCard(
    event: AartiEvent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (event.isNext) SaffronPrimary else FestiveBorderLight
    val containerColor = if (event.isNext) Color(0xFFFFF9F2) else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (event.isNext) 4.dp else 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(borderColor))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time Badge
            Surface(
                color = if (event.isNext) SaffronPrimary else if (event.isCompleted) Color(0xFFF0EBE5) else Color(0xFFFFF3E0),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (event.isCompleted) Icons.Default.CheckCircle else Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = if (event.isNext) Color.White else if (event.isCompleted) DonationGreen else SaffronDeep,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = event.time,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.isNext) Color.White else TextPrimaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = event.marathiTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )

                    if (event.isNext) {
                        Surface(
                            color = VermilionRed,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "🔔 पुढील आरती",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    } else if (event.isCompleted) {
                        Text(
                            text = "✓ संपन्न",
                            color = DonationGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                event.prasadDetails?.let { prasad ->
                    Text(
                        text = "🪔 $prasad",
                        fontSize = 11.sp,
                        color = SaffronDeep,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = TextSecondaryLight,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${event.location} • ${event.host ?: "मंडळ"}",
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                }
            }
        }
    }
}
