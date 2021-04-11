package com.vinners.cube_vishwakarma.core

sealed class TaskState<out T> {

    /**
     * Loading The Data
     */
    object Loading : TaskState<Nothing>()

    /**
     * Error While Loading Data
     */
    data class Error(val error: String) : TaskState<Nothing>()

    /**
     * Success ,Data Loaded or TaskDone
     */
    data class Success<T>(val item: T) : TaskState<T>()
}