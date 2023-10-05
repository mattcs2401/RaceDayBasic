package com.mcssoft.racedaybasic.domain.dto

import com.mcssoft.racedaybasic.domain.model.Meeting

data class MeetingDto(
    val _links: Links,
//    val displayMeetingName: String,
//    val exoticPools: List<ExoticPool>,
//    val fixedOddsOnly: Boolean,
    val meetingName: String,
    val location: String,
    val raceType: String,
    val meetingDate: String,
    val prizeMoney: String,
    val weatherCondition: String,
    val trackCondition: String,
    val railPosition: String,
    val venueMnemonic: String,
    val races: List<RaceDto>,
    val sellCode: SellCodeDto     // TBA - contains e.g. {"meetingCode":"B","scheduledType":"R"}
)

fun MeetingDto.toMeeting(): Meeting {
    return Meeting(
        location = location,                     // QLD
        meetingDate = meetingDate,               // e.g. 2023-08-23
        meetingTime = "",                        // TBA - 1st Race time ?
        meetingName = meetingName,               // e.g. Sunshine Coast
        prizeMoney = prizeMoney,                 // TBA.
        raceType = raceType,                     // e.g. R
        railPosition = railPosition,             // True
        trackCondition = trackCondition,         // e.g. Good4
        venueMnemonic = venueMnemonic,           // e.g SSC
        weatherCondition = weatherCondition,     // Fine
        racesNo = races.size,                    // the number of associated Races.
        meetingId = "$venueMnemonic:$meetingDate",    // TBA - no actual id value in Dto.
        sellCode = "${sellCode.meetingCode}${sellCode.scheduledType}",
        racesBaseUrl = _links.races
    )
}