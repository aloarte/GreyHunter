package com.devalr.greyhunter

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enums.ProgressColorType.TrafficLight
import com.devalr.domain.enums.ThemeType.Dark
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository: SettingsRepository = mockk()
    private val context: Context = mockk(relaxed = true)
    private val packageManager: PackageManager = mockk(relaxed = true)

    private val packageName = "com.test.app"
    private val appVersion = "2.0.0"

    @Before
    fun setup() {
        val packageInfo = PackageInfo().apply { versionName = appVersion }
        every { packageManager.getPackageInfo(packageName, 0) } returns packageInfo
        every { context.packageManager } returns packageManager
        every { context.packageName } returns packageName
    }

    @Test
    fun `should set version and update states from repository`() = runTest {
        // GIVEN
        val appearanceFlow = MutableStateFlow(Dark)
        val colorFlow = MutableStateFlow(TrafficLight)
        coEvery { repository.getAppearanceConfiguration() } returns appearanceFlow
        coEvery { repository.getProgressColorConfiguration() } returns colorFlow
        coEvery { repository.setAppVersion(appVersion) } just Runs

        // WHEN
        val viewModel = MainViewModel(repository, context)
        advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) { repository.getAppearanceConfiguration() }
        coVerify(exactly = 1) { repository.getProgressColorConfiguration() }
        coVerify(exactly = 1) { repository.setAppVersion(appVersion) }
        assertEquals(Dark, viewModel.darkModeState.value)
        assertEquals(TrafficLight, viewModel.colorState.value)
    }
}