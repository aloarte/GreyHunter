package com.devalr.createproject.interactions

import com.devalr.createproject.model.MiniatureVo


enum class ErrorType {
    EmptyTitle,
    AddDatabase,
    ImageImport
}

data class State(
    val projectMinis: List<MiniatureVo>? = null,
    val projectName: String? = null,
    val projectDescription: String? = null,
    val errorType: ErrorType? = null,
    val projectImage: String? = null
)