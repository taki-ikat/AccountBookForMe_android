package com.example.accountbookforme.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String

)
