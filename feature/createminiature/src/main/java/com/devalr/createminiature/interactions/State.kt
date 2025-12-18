package com.devalr.createminiature.interactions


enum class ErrorType {
    EmptyTitle,
    AddDatabase,
    ErrorUpdatingProgress,
    ImageImport
}

data class State(
    val projectId: Long? = null,
    val miniatureName: String? = null,
    val miniatureImage: String? = null,
    val errorType: ErrorType? = null
)