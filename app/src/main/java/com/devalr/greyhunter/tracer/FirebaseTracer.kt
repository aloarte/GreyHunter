package com.devalr.greyhunter.tracer

import com.devalr.framework.AppTracer
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseTracer : AppTracer {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun setScreen(name: String) {
        crashlytics.setCustomKey("screen", name)
    }

    override fun log(event: String) {
        crashlytics.log(event)
    }

    override fun setKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun recordError(t: Throwable) {
        crashlytics.recordException(t)
    }
}