package com.devalr.framework

interface AppTracer {
    fun setScreen(name: String)
    fun log(event: String)
    fun setKey(key: String, value: String)
    fun recordError(t: Throwable)
}