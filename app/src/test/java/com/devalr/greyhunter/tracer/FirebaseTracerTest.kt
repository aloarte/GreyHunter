package com.devalr.greyhunter.tracer

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FirebaseTracerTest {

    private var crashlytics: FirebaseCrashlytics = mockk()

    private lateinit var tracer: FirebaseTracer

    @Before
    fun setup() {
        tracer = FirebaseTracer(crashlytics)
    }

    @Test
    fun `WHEN setScreen is called THEN firebase setCustomKey with screen key is called`() {
        // GIVEN
        every { crashlytics.setCustomKey("screen", "Home") } just Runs

        // WHEN
        tracer.setScreen("Home")

        // THEN
        verify(exactly = 1) { crashlytics.setCustomKey("screen", "Home") }
    }

    @Test
    fun `WHEN log is called THEN firebase log is called`() {
        // GIVEN
        every { crashlytics.log("event") } just Runs

        // WHEN
        tracer.log("event")

        // THEN
        verify(exactly = 1) { crashlytics.log("event") }
    }

    @Test
    fun `WHEN setKey is called THEN firebase setCustomKey is called`() {
        // GIVEN
        every { crashlytics.setCustomKey("customKey", "keyValue") } just Runs

        // WHEN
        tracer.setKey("customKey", "keyValue")

        // THEN
        verify(exactly = 1) { crashlytics.setCustomKey("customKey", "keyValue") }
    }

    @Test
    fun `WHEN recordError is called THEN firebase recordException is called`() {
        // GIVEN
        val throwable = RuntimeException()
        every { crashlytics.recordException(throwable) } just Runs

        // WHEN
        tracer.recordError(throwable)

        // THEN
        verify(exactly = 1) { crashlytics.recordException(throwable) }
    }
}