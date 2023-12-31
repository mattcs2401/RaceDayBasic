package com.mcssoft.raceday.ui.components.settings

sealed class SettingsEvent {

    data class Checked(val checked: Boolean, val type: EventType): SettingsEvent()  //: SettingsEvent()

    sealed class EventType {
        data object SOURCE_FROM_API: EventType()   // source the application's data from the Api.
        data object AUTO_ADD_TRAINERS: EventType () // auto add Trainers and update the Summary.
        data object USE_NOTIFICATIONS: EventType()
    }
}
