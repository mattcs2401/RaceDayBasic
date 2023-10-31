package com.mcssoft.racedaybasic.data.repository.database

import com.mcssoft.racedaybasic.domain.model.Meeting
import com.mcssoft.racedaybasic.domain.model.Race
import com.mcssoft.racedaybasic.domain.model.Runner
import com.mcssoft.racedaybasic.domain.model.Scratching
import com.mcssoft.racedaybasic.domain.model.Summary
import javax.inject.Inject

class DbRepoImpl @Inject constructor(
    private val dao: IDbRepo
) : IDbRepo {

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }

    //<editor-fold default state="collapsed" desc="Region: Meeting related.">
    override suspend fun insertMeeting(meeting: Meeting): Long {
        return dao.insertMeeting(meeting)
    }

    override suspend fun getMeeting(mId: Long): Meeting {
        return dao.getMeeting(mId)
    }

    override suspend fun getMeetings(): List<Meeting> {
        return dao.getMeetings()
    }

    override suspend fun getMeetingSubset(): List<IDbRepo.MeetingSubset> {
        return dao.getMeetingSubset()
    }

    override suspend fun deleteMeetings(): Int {
        return dao.deleteMeetings()
    }
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Race related.">
    override suspend fun insertRaces(races: List<Race>): List<Long> {
        return dao.insertRaces(races)
    }

    override suspend fun getRaces(mtgId: Long): List<Race> {
        return dao.getRaces(mtgId)
    }

    override suspend fun getRace(rId: Long): Race {
        return dao.getRace(rId)
    }

    override suspend fun getRaceIdByVenueCodeAndRaceNo(venueMnemonic: String, raceNumber: Int): Long {
        return dao.getRaceIdByVenueCodeAndRaceNo(venueMnemonic, raceNumber)
    }
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Runner related.">
    override suspend fun insertRunners(runners: List<Runner>): List<Long> {
        return dao.insertRunners(runners)
    }

    override suspend fun getRunner(runnerId: Long): Runner {
        return dao.getRunner(runnerId)
    }

    override suspend fun getRunners(raceId: Long): List<Runner> {
        return dao.getRunners(raceId)
    }

    override suspend fun updateRunnerAsScratched(runner: Runner) {
        dao.updateRunnerAsScratched(runner)
    }

    override suspend fun updateRunnerAsChecked(runner: Runner) {
        return dao.updateRunnerAsChecked(runner)
    }
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Summary related.">
    override suspend fun getSummaries(): List<Summary> {
        return dao.getSummaries()
    }

    override suspend fun insertSummary(summary: Summary): Long {
        return dao.insertSummary(summary)
    }

    override suspend fun insertSummaries(summaries: List<Summary>): List<Long> {
        return dao.insertSummaries(summaries)
    }

//    override suspend fun deleteSummary(id: Long) {
//        dao.deleteSummary(id)
//    }
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Region: Scratching related.">
    override suspend fun insertScratchings(scratchings: List<Scratching>): List<Long> {
        return dao.insertScratchings(scratchings)
    }

    override suspend fun deleteScratchings(): Int {
        return dao.deleteScratchings()
    }

    override suspend fun getScratchingsForRace(venueMnemonic: String, raceNumber: Int): List<Scratching> {
        return dao.getScratchingsForRace(venueMnemonic, raceNumber)
    }
    //</editor-fold>
}