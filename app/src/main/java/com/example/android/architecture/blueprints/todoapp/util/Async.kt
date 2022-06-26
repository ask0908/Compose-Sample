package com.example.android.architecture.blueprints.todoapp.util

import com.example.android.architecture.blueprints.todoapp.data.Result

/**
 * A generic class that holds a loading signal or a [Result].
 */
sealed class Async<out T> {
    object Loading : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
}
