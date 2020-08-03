package com.andrius.notesappdemo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey var id: Long?,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "sequence") var sequence: Int,
    @ColumnInfo(name = "date_created") var dateCreated: String,
    @ColumnInfo(name = "last_modified") var lastModified: String
)