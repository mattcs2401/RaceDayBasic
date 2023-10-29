package com.mcssoft.racedaybasic.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Scratching",
    indices = [
        Index(
            value = ["venueMnemonic","raceNumber"]
        )],
)
data class Scratching(
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0L,

    val bettingStatus: String,
    val runnerName: String,
    val runnerNumber: Int,

    var venueMnemonic: String,         // venue code (not in ScratchingDto).
    var raceNumber: Int                // Race number (not in ScratchingDto).
)