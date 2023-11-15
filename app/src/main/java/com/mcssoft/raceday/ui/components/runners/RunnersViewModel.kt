package com.mcssoft.raceday.ui.components.runners

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcssoft.raceday.data.repository.preferences.Preference
import com.mcssoft.raceday.domain.model.Race
import com.mcssoft.raceday.domain.model.Runner
import com.mcssoft.raceday.domain.usecase.UseCases
import com.mcssoft.raceday.ui.components.snackbar.SnackbarController
import com.mcssoft.raceday.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunnersViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RunnersState.initialise())
    val state = _state.asStateFlow()

    private var raceId = 0L

    init {
        /*
          The Runners screen expects a "raceId" (supplied in the navigation from the RacesScreen
          to the RunnersScreen). However, when navigating back from the Runners screen, there is no
           need to supply one, so the original is saved into the preferences and reused.
         */
        savedStateHandle.get<Long>(Constants.KEY_RACE_ID)?.let { rceId ->
            if (rceId > 0) {
                raceId = rceId
                // Save the Race id to the preferences (for back nav from Runners screen).
                saveRaceId(Preference.RaceIdPref, raceId)
            } else {
                // Get the Race id from the preferences.
                getRaceId(Preference.RaceIdPref)
                // Race id is returned in the state.
                raceId = state.value.raceId
            }
            // Get Race and Runner values for the screen.
            getRace(raceId)
            getRunners(raceId)
        }
    }

    /**
     * Events raised from the UI.
     * @param event: The event type.
     */
    fun onEvent(event: RunnersEvent) {
        when(event) {
            is RunnersEvent.Check -> {
                // TODO - create/update Summary record.
                // Note: Room will update the Runner database record with the isChecked status of
                // this Runner object.
                setRunnerChecked(event.race, event.runner)
            }
        }
    }

    /**
     * Set the metadata 'checked' on the Runner object.
     * @param runner: The Runner.
     */
    private fun setRunnerChecked(race: Race, runner: Runner) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.setRunnerChecked(race, runner).collect { result ->
                when {
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = RunnersState.Status.Failure,
                                loading = false
                            )
                        }
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Success,
                                loading = false,
                                checked = result.data.toBoolean()
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the Runners associated with a Race from the database.
     * @param raceId: The Race id (_id value).
     */
    private fun getRunners(raceId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getRunners(raceId).collect { result ->
                when {
                    result.loading -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Loading,
                                loading = true
                            )
                        }
                    }
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = RunnersState.Status.Failure,
                                loading = false
                            )
                        }
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Success,
                                loading = false,
                                runners = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }

        }
    }

    /**
     * Get the Race object from the database.
     * @param raceId: The Race id (_id value).
     */
    private fun getRace(raceId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getRace(raceId).collect { result ->
                when {
                    result.loading -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Loading,
                                loading = true
                            )
                        }
                    }
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = RunnersState.Status.Failure,
                                loading = false
                            )
                        }
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Success,
                                loading = false,
                                race = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    //<editor-fold default state="collapsed" desc="Region: Preferences methods">
    /**
     * Save the Race id to the preferences.
     * @param pref: The Preference type.
     * @param raceId: The Race id value to save.
     */
    private fun saveRaceId(pref: Preference.RaceIdPref, raceId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.savePreferences(pref, raceId).collect { result ->
                when {
                    result.loading -> {}
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = RunnersState.Status.Failure,
                                loading = false
                            )
                        }
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Success,
                                loading = false,
                                raceId = raceId
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the Race id from the preferences.
     * @param pref: The Preference type.
     */
    private fun getRaceId(pref: Preference.RaceIdPref) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getPreferences(pref).collect { result ->
                when {
                    result.loading -> {}
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = RunnersState.Status.Failure,
                                loading = false
                            )
                        }
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = RunnersState.Status.Success,
                                loading = false,
                                raceId = result.data as Long
                            )
                        }
                    }
                }
            }
        }
    }
    //</editor-fold>

}