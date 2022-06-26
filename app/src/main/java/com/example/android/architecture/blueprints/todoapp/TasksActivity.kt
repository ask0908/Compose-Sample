package com.example.android.architecture.blueprints.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the todoapp
 */
@AndroidEntryPoint
class TasksActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppCompatTheme {
                TodoNavGraph()
            }
        }
    }
}
