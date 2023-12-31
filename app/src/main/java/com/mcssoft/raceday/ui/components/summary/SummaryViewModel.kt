package com.mcssoft.raceday.ui.components.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcssoft.raceday.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SummaryState.initialise())
    val state = _state.asStateFlow()

    init {
        getSummaries()
    }

    private fun getSummaries() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getSummaries().collect { result ->
                when {
                    result.loading -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = SummaryState.Status.Loading,
                                loading = true
                            )
                        }
                        _state.emit(state.value)
                    }
                    result.failed -> {
                        _state.update { state ->
                            state.copy(
                                exception = result.exception,
                                status = SummaryState.Status.Failure,
                                loading = false
                            )
                        }
                        _state.emit(state.value)
                    }
                    result.successful -> {
                        _state.update { state ->
                            state.copy(
                                exception = null,
                                status = SummaryState.Status.Success,
                                loading = false,
                                summaries = result.data ?: emptyList()
                            )
                        }
                        _state.emit(state.value)
                    }
                }
            }
        }
    }

}