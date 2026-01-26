package com.devalr.createminiature.interactions

import com.devalr.domain.model.MiniatureBo


enum class ErrorType {
    BadId,
    EmptyTitle,
    AddDatabase,
    EditDatabase,
    ErrorUpdatingProgress
}

data class State(
    val projectId: Long? = null,
    val miniatureToUpdate: MiniatureBo? = null,
    val miniatureName: String? = null,
    val miniatureImage: String? = null,
    val editMode: Boolean = false,
    val errorType: ErrorType? = null
)