package com.mcssoft.raceday.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mcssoft.raceday.ui.components.navigation.NavGraph
import com.mcssoft.raceday.ui.theme.RaceDayBasicTheme
import com.mcssoft.raceday.utility.notification.NotificationService
import com.mcssoft.raceday.utility.notification.NotificationState.START_SERVICE
import com.mcssoft.raceday.utility.notification.NotificationState.STOP_SERVICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RaceDayBasicTheme {
                NavGraph()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG","Start service.")
        Intent(applicationContext, NotificationService::class.java).also { intent ->
            intent.action = START_SERVICE.toString()
            startService(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG","Stop service.")
        Intent(applicationContext, NotificationService::class.java).also { intent ->
            intent.action = STOP_SERVICE.toString()
            startService(intent)
        }
    }
}
