package com.devalr.framework

fun String.limitSize(limit: Int): String {
    return if (this.length > limit) {
        "${this.take(limit)}..."
    } else {
        this
    }
}