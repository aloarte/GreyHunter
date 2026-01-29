package com.devalr.createminiature.interactions

import com.devalr.domain.model.MiniatureBo


enum class ErrorType {
    BadId,
    EmptyName,
    AddDatabase,
    EditDatabase,
    ErrorUpdatingProgress,
    ImportImage
}

data class State(
    val error: Boolean = false,
    val projectId: Long? = null,
    val projectName: String? = null,
    val miniatureToUpdate: MiniatureBo? = null,
    val miniatureName: String? = null,
    val miniatureImage: String? = null,
    val editMode: Boolean = false
)