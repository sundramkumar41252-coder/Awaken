package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AwakenRepository
import com.example.model.StatType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Awaken", appName)
    }

    @Test
    fun `repository initializes default quests and handles progress`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = AwakenRepository(context)

        val quests = repo.quests.value
        assertTrue("Quests list should not be empty", quests.isNotEmpty())

        val pushups = quests.first { it.title == "Push-ups" }
        assertEquals(50, pushups.currentProgress)

        repo.updateQuestProgress(pushups.id, 25)
        val updated = repo.quests.value.first { it.id == pushups.id }
        assertEquals(75, updated.currentProgress)
    }

    @Test
    fun `allocating stat increases attribute and decreases AP`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = AwakenRepository(context)

        val initialAp = repo.playerProfile.value.availableAp
        val initialStr = repo.playerProfile.value.strength

        if (initialAp > 0) {
            repo.allocateStat(StatType.STR)
            val updatedProfile = repo.playerProfile.value
            assertEquals(initialAp - 1, updatedProfile.availableAp)
            assertEquals(initialStr + 1, updatedProfile.strength)
        }
    }

    @Test
    fun `calendar days list computes 42 cells and contains today`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.AwakenViewModel(app)

        val days = vm.calendarDays.value
        assertEquals("Calendar grid should contain 42 days", 42, days.size)

        val today = days.find { it.isToday }
        assertTrue("Calendar grid should identify today's date", today != null)
        assertTrue("Today should belong to current month", today!!.isCurrentMonth)
    }

    @Test
    fun `app starts on SYSTEM_OPENING screen and can transition to DAILY_QUESTS`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.AwakenViewModel(app)

        assertEquals(com.example.ui.AppScreen.SYSTEM_OPENING, vm.currentScreen.value)
        vm.setScreen(com.example.ui.AppScreen.DAILY_QUESTS)
        assertEquals(com.example.ui.AppScreen.DAILY_QUESTS, vm.currentScreen.value)
    }
}
