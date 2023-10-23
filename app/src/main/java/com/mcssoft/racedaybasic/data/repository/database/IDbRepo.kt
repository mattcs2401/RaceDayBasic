package com.mcssoft.racedaybasic.data.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mcssoft.racedaybasic.domain.dto.MeetingDto
import com.mcssoft.racedaybasic.domain.dto.RaceDto
import com.mcssoft.racedaybasic.domain.dto.toMeeting
import com.mcssoft.racedaybasic.domain.dto.toRace
import com.mcssoft.racedaybasic.domain.dto.RunnerDto
import com.mcssoft.racedaybasic.domain.dto.toRunner
import com.mcssoft.racedaybasic.domain.model.Meeting
import com.mcssoft.racedaybasic.domain.model.Race
import com.mcssoft.racedaybasic.domain.model.Runner
import com.mcssoft.racedaybasic.domain.model.Summary
import com.mcssoft.racedaybasic.utility.DateUtils

@Dao
interface IDbRepo {

    @Transaction
    // Note: Here the meetingId is the database row id (_id).
    suspend fun insertMeetingWithRaces(meeting: MeetingDto, races: List<RaceDto>) {
        val meetingId = insertMeeting(meeting.toMeeting())

        val racesWithMeetingId  = races.map { raceDto ->
            raceDto.raceStartTime = DateUtils().getTime(raceDto.raceStartTime)

            val scratchings = raceDto.scratchings.map { scratchingDto ->

            }

            raceDto.toRace(meetingId, meeting.venueMnemonic!!)
        }

        insertRaces(racesWithMeetingId)
    }

    @Transaction
    suspend fun insertRunnersWithRaceId(raceId: Long, runners: List<RunnerDto>) {
        val runnersWithRaceId = runners.map { runnerDto ->
            runnerDto.toRunner(raceId)
        }
        insertRunners(runnersWithRaceId)
    }

    @Query("select _id from race where venue = :venueCode and raceNo = :raceNum")
    suspend fun getRaceIdByVenueCodeAndRaceNo(venueCode: String, raceNum: String): Long

    //<editor-fold default state="collapsed" desc="Region: MeetingDto related.">
    data class MeetingSubset(
        val meetingDate: String,
        val venueMnemonic: String,
        val numRaces: Int
    )
    @Query("select meetingDate, venueMnemonic, numRaces from Meeting")
    suspend fun getMeetingSubset(): List<MeetingSubset>

    /**
     * Insert a MeetingDto.
     * @param meeting: The MeetingDto to insert.
     * @return The _id of the inserted MeetingDto record.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: Meeting): Long

    /**
     * Select all from Meetings.
     * @return A list of MeetingDto.
     */
    @Query("select * from Meeting")
    suspend fun getMeetings(): List<Meeting>

    /**
     * Get a MeetingDto based on it's row id.
     * @param mId: The MeetingDto id.
     * @return A MeetingDto.
     */
    @Query("select * from Meeting where _id = :mId")
    suspend fun getMeeting(mId: Long): Meeting

    /**
     * Delete all from Meetings.
     */
    @Query("delete from Meeting")
    suspend fun deleteMeetings(): Int       // CASCADE should take care of Races/Runners etc.

//    /**
//     * Get a count of the MeetingDto records.
//     */
//    @Query("select count(*) from Meeting")
//    suspend fun meetingCount(): Int
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: RaceDto related.">
    /**
     * Insert a list of Races.
     * @param races: The list of Races.
     * @return A list of the _ids of the inserted RaceDto records. Usage is TBA ATT.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRaces(races: List<Race>): List<Long>

    /**
     * Get a listing of the Races based on their (foreign key) MeetingDto id.
     * @param mtgId: The Meeting id (database row number).
     * @return A list of Races.
     */
    @Query("select * from race where mtgId= :mtgId")
    suspend fun getRaces(mtgId: Long): List<Race>

    @Query("select * from Race where _id= :rId")
    suspend fun getRace(rId: Long): Race

//    /**
//     * Get the id (database row id) of a RaceDto based on the (foreign key) MeetingDto id and RaceDto number.
//     * @param mtgId: The MeetingDto id (database row number).
//     * @param raceNo: The RaceDto number.
//     * @return A the RaceDto id.
//     */
//    @Query("select _id from RaceDto where mtgId= :mtgId and raceNumber= :raceNo")
//    suspend fun getRaceId(mtgId: Long, raceNo: Int): Long
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Runner related.">
    /**
     * Insert a list of Runners.
     * @param runners: The list of Runners.
     * @return A list of the _ids of the inserted Runner records. Usage is TBA ATT.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunners(runners: List<Runner>): List<Long>

    @Query("select * from Runner where _id = :runnerId")
    suspend fun getRunner(runnerId: Long): Runner

    /**
     * Get a list of Runners based on the id of the associated RaceDto.
     * @param raceId: The RaceDto id (database row id).
     * @return A list of Runners.
     */
    @Query("select * from Runner where raceId= :raceId")
    suspend fun getRunners(raceId: Long): List<Runner>

    @Query("update Runner set isChecked= :checked where _id= :runnerId")
    suspend fun setRunnerChecked(runnerId: Long, checked: Boolean)
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Summary related.">
    @Query("select * from Summary")
    suspend fun getSummaries(): List<Summary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: Summary): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummaries(summaries: List<Summary>): List<Long>
    //</editor-fold>
}