package com.shreeganesh.ganapatimanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shreeganesh.ganapatimanagement.model.Member
import com.shreeganesh.ganapatimanagement.model.MemberDepartment
import com.shreeganesh.ganapatimanagement.model.MemberRole

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val role: MemberRole,
    val department: MemberDepartment,
    val phoneNumber: String,
    val allocatedDuty: String = "",
    val bloodGroup: String = "",
    val address: String = "",
    val joiningYear: String = "२०२०",
    val isActive: Boolean = true,
    val createdAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): Member {
        return Member(
            id = id,
            name = name,
            role = role,
            department = department,
            phoneNumber = phoneNumber,
            allocatedDuty = allocatedDuty,
            bloodGroup = bloodGroup,
            address = address,
            joiningYear = joiningYear,
            isActive = isActive
        )
    }

    companion object {
        fun fromDomainModel(member: Member, timestamp: Long = System.currentTimeMillis()): MemberEntity {
            return MemberEntity(
                id = member.id,
                name = member.name,
                role = member.role,
                department = member.department,
                phoneNumber = member.phoneNumber,
                allocatedDuty = member.allocatedDuty,
                bloodGroup = member.bloodGroup,
                address = member.address,
                joiningYear = member.joiningYear,
                isActive = member.isActive,
                createdAtTimestamp = timestamp
            )
        }
    }
}
