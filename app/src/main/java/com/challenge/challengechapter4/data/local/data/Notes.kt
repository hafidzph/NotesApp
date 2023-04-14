package com.challenge.challengechapter4.data.local.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ])

data class Notes(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "userId") var userId: Int
)