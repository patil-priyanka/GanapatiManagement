package com.shreeganesh.ganapatimanagement.data.local.converters

import androidx.room.TypeConverter
import com.shreeganesh.ganapatimanagement.model.DonationCategory
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.ExpenseCategory
import com.shreeganesh.ganapatimanagement.model.KaryakramCategory
import com.shreeganesh.ganapatimanagement.model.MemberDepartment
import com.shreeganesh.ganapatimanagement.model.MemberRole
import com.shreeganesh.ganapatimanagement.model.PaymentMode

class Converters {
    @TypeConverter
    fun fromPaymentMode(value: PaymentMode?): String = value?.name ?: PaymentMode.CASH.name

    @TypeConverter
    fun toPaymentMode(value: String?): PaymentMode =
        try {
            if (value != null) PaymentMode.valueOf(value) else PaymentMode.CASH
        } catch (_: Exception) {
            PaymentMode.CASH
        }

    @TypeConverter
    fun fromDonationCategory(value: DonationCategory?): String =
        value?.name ?: DonationCategory.GENERAL.name

    @TypeConverter
    fun toDonationCategory(value: String?): DonationCategory =
        try {
            if (value != null) DonationCategory.valueOf(value) else DonationCategory.GENERAL
        } catch (_: Exception) {
            DonationCategory.GENERAL
        }

    @TypeConverter
    fun fromExpenseCategory(value: ExpenseCategory?): String =
        value?.name ?: ExpenseCategory.OTHER.name

    @TypeConverter
    fun toExpenseCategory(value: String?): ExpenseCategory =
        try {
            if (value != null) ExpenseCategory.valueOf(value) else ExpenseCategory.OTHER
        } catch (_: Exception) {
            ExpenseCategory.OTHER
        }

    @TypeConverter
    fun fromMemberRole(value: MemberRole?): String =
        value?.name ?: MemberRole.VOLUNTEER.name

    @TypeConverter
    fun toMemberRole(value: String?): MemberRole =
        try {
            if (value != null) MemberRole.valueOf(value) else MemberRole.VOLUNTEER
        } catch (_: Exception) {
            MemberRole.VOLUNTEER
        }

    @TypeConverter
    fun fromMemberDepartment(value: MemberDepartment?): String =
        value?.name ?: MemberDepartment.CORE.name

    @TypeConverter
    fun toMemberDepartment(value: String?): MemberDepartment =
        try {
            if (value != null) MemberDepartment.valueOf(value) else MemberDepartment.CORE
        } catch (_: Exception) {
            MemberDepartment.CORE
        }

    @TypeConverter
    fun fromKaryakramCategory(value: KaryakramCategory?): String =
        value?.name ?: KaryakramCategory.OTHER.name

    @TypeConverter
    fun toKaryakramCategory(value: String?): KaryakramCategory =
        try {
            if (value != null) KaryakramCategory.valueOf(value) else KaryakramCategory.OTHER
        } catch (_: Exception) {
            KaryakramCategory.OTHER
        }

    @TypeConverter
    fun fromEventStatus(value: EventStatus?): String =
        value?.name ?: EventStatus.UPCOMING.name

    @TypeConverter
    fun toEventStatus(value: String?): EventStatus =
        try {
            if (value != null) EventStatus.valueOf(value) else EventStatus.UPCOMING
        } catch (_: Exception) {
            EventStatus.UPCOMING
        }

    @TypeConverter
    fun fromStringList(value: List<String>?): String =
        value?.joinToString(separator = "::||::") ?: ""

    @TypeConverter
    fun toStringList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList()
        else value.split("::||::").filter { it.isNotBlank() }
}
