package com.example.i4c

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_scans")
data class PhoneScanEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val phone: String,
    val fraudScore: Int,
    val riskLevel: String,
    val carrier: String,
    val country: String,
    val timestamp: Long = System.currentTimeMillis()
)
