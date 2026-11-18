package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.Expense
import com.shreeganesh.ganapatimanagement.model.ExpenseCategory
import com.shreeganesh.ganapatimanagement.model.PaymentMode

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val voucherNumber: String,
    val title: String,
    val vendorName: String,
    val category: ExpenseCategory,
    val amount: Double,
    val paymentMode: PaymentMode = PaymentMode.CASH,
    val date: String,
    val time: String,
    val paidBy: String = "खजिनदार",
    val billNumber: String = "",
    val note: String = "",
    val isPending: Boolean = false,
    val pendingAmount: Double = 0.0,
    val imageUri: String? = null,
    val createdAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): Expense {
        return Expense(
            id = id,
            voucherNumber = voucherNumber,
            title = title,
            vendorName = vendorName,
            category = category,
            amount = amount,
            paymentMode = paymentMode,
            date = date,
            time = time,
            paidBy = paidBy,
            billNumber = billNumber,
            note = note,
            isPending = isPending,
            pendingAmount = pendingAmount,
            imageUri = imageUri
        )
    }

    companion object {
        fun fromDomainModel(expense: Expense, timestamp: Long = System.currentTimeMillis()): ExpenseEntity {
            return ExpenseEntity(
                id = expense.id,
                voucherNumber = expense.voucherNumber,
                title = expense.title,
                vendorName = expense.vendorName,
                category = expense.category,
                amount = expense.amount,
                paymentMode = expense.paymentMode,
                date = expense.date,
                time = expense.time,
                paidBy = expense.paidBy,
                billNumber = expense.billNumber,
                note = expense.note,
                isPending = expense.isPending,
                pendingAmount = expense.pendingAmount,
                imageUri = expense.imageUri,
                createdAtTimestamp = timestamp
            )
        }
    }
}
