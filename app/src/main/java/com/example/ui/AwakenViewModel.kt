package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AwakenRepository
import com.example.model.CalendarDayInfo
import com.example.model.PlayerProfile
import com.example.model.QuestItem
import com.example.model.StatType
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    SYSTEM_OPENING,
    AWAKENING_INTRO,
    DAILY_QUESTS,
    SYSTEM_CALENDAR,
    STATUS_WINDOW,
    FOCUS_QUEST,
    EVOLUTION_PATH
}

enum class TimerState {
    STOPPED,
    RUNNING,
    PAUSED
}

class AwakenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AwakenRepository(application)

    val playerProfile: StateFlow<PlayerProfile> = repository.playerProfile
    val quests: StateFlow<List<QuestItem>> = repository.quests
    val evolutionTiers = repository.evolutionTiers

    private val _currentScreen = MutableStateFlow(AppScreen.SYSTEM_OPENING)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Quest detail modal state (Ref 2 third panel)
    private val _selectedQuestForDetail = MutableStateFlow<QuestItem?>(null)
    val selectedQuestForDetail: StateFlow<QuestItem?> = _selectedQuestForDetail.asStateFlow()

    private val _showSystemNotification = MutableStateFlow<String?>(null)
    val showSystemNotification: StateFlow<String?> = _showSystemNotification.asStateFlow()

    // Focus Quest timer state (Ref 3)
    private val _timerSecondsRemaining = MutableStateFlow(25 * 60)
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _totalTimerSeconds = MutableStateFlow(25 * 60)
    val totalTimerSeconds: StateFlow<Int> = _totalTimerSeconds.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    // System Calendar State
    private val initialCal = Calendar.getInstance()
    private val _calendarYear = MutableStateFlow(initialCal.get(Calendar.YEAR))
    val calendarYear: StateFlow<Int> = _calendarYear.asStateFlow()

    private val _calendarMonth = MutableStateFlow(initialCal.get(Calendar.MONTH))
    val calendarMonth: StateFlow<Int> = _calendarMonth.asStateFlow()

    private val _selectedDayInfo = MutableStateFlow<CalendarDayInfo?>(null)
    val selectedDayInfo: StateFlow<CalendarDayInfo?> = _selectedDayInfo.asStateFlow()

    private val _calendarDays = MutableStateFlow<List<CalendarDayInfo>>(emptyList())
    val calendarDays: StateFlow<List<CalendarDayInfo>> = _calendarDays.asStateFlow()

    init {
        refreshCalendar()
    }

    fun setScreen(screen: AppScreen) {
        _currentScreen.value = screen
        vibrate(30)
    }

    fun replayOpeningAnimation() {
        _currentScreen.value = AppScreen.SYSTEM_OPENING
        vibrate(40)
    }

    fun openQuestDetail(quest: QuestItem) {
        _selectedQuestForDetail.value = quest
        vibrate(25)
    }

    fun closeQuestDetail() {
        _selectedQuestForDetail.value = null
    }

    fun updateQuestProgress(questId: String, increment: Int) {
        repository.updateQuestProgress(questId, increment)
        vibrate(40)
        refreshCalendar()
        // update detail if currently open
        val updatedQuest = repository.quests.value.find { it.id == questId }
        if (updatedQuest != null && _selectedQuestForDetail.value?.id == questId) {
            _selectedQuestForDetail.value = updatedQuest
        }
    }

    fun toggleQuestCompleted(questId: String) {
        repository.toggleQuestCompleted(questId)
        vibrate(60)
        refreshCalendar()
        val updatedQuest = repository.quests.value.find { it.id == questId }
        if (updatedQuest != null && _selectedQuestForDetail.value?.id == questId) {
            _selectedQuestForDetail.value = updatedQuest
        }
    }

    fun allocateStat(stat: StatType) {
        if (playerProfile.value.availableAp > 0) {
            repository.allocateStat(stat)
            vibrate(50)
            showNotification("Allocated +1 to ${stat.name}!")
        }
    }

    fun addCustomQuest(title: String, target: Int, unit: String) {
        if (title.isNotBlank() && target > 0) {
            repository.addCustomQuest(title, target, unit)
            vibrate(40)
            showNotification("New System Quest Registered!")
        }
    }

    fun deleteQuest(questId: String) {
        repository.deleteQuest(questId)
        if (_selectedQuestForDetail.value?.id == questId) {
            _selectedQuestForDetail.value = null
        }
        vibrate(30)
    }

    fun completeAllDaily() {
        repository.completeAllDailyQuests()
        vibrate(100)
        refreshCalendar()
        showNotification("Daily Quest: Strange Training Completed! +150 EXP")
        _selectedQuestForDetail.value = null
    }

    // Pomodoro Timer Controls
    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return
        _timerState.value = TimerState.RUNNING
        vibrate(40)
        timerJob = viewModelScope.launch {
            while (_timerState.value == TimerState.RUNNING && _timerSecondsRemaining.value > 0) {
                delay(1000L)
                _timerSecondsRemaining.value -= 1
            }
            if (_timerSecondsRemaining.value <= 0) {
                _timerState.value = TimerState.STOPPED
                val minutesCompleted = _totalTimerSeconds.value / 60
                repository.recordFocusSession(minutesCompleted)
                refreshCalendar()
                vibrate(150)
                showNotification("Focus Quest Cleared! +${minutesCompleted * 2} EXP")
                _timerSecondsRemaining.value = _totalTimerSeconds.value
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = TimerState.PAUSED
        timerJob?.cancel()
        vibrate(30)
    }

    fun resetTimer() {
        _timerState.value = TimerState.STOPPED
        timerJob?.cancel()
        _timerSecondsRemaining.value = _totalTimerSeconds.value
        vibrate(30)
    }

    fun setTimerPreset(minutes: Int) {
        pauseTimer()
        _totalTimerSeconds.value = minutes * 60
        _timerSecondsRemaining.value = minutes * 60
        _timerState.value = TimerState.STOPPED
        vibrate(25)
    }

    private fun showNotification(msg: String) {
        _showSystemNotification.value = msg
        viewModelScope.launch {
            delay(3500L)
            if (_showSystemNotification.value == msg) {
                _showSystemNotification.value = null
            }
        }
    }

    fun clearNotification() {
        _showSystemNotification.value = null
    }

    fun selectCalendarDay(day: CalendarDayInfo) {
        _selectedDayInfo.value = day
        vibrate(25)
    }

    fun prevMonth() {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, _calendarYear.value)
            set(Calendar.MONTH, _calendarMonth.value)
            add(Calendar.MONTH, -1)
        }
        _calendarYear.value = cal.get(Calendar.YEAR)
        _calendarMonth.value = cal.get(Calendar.MONTH)
        _selectedDayInfo.value = null
        refreshCalendar()
        vibrate(30)
    }

    fun nextMonth() {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, _calendarYear.value)
            set(Calendar.MONTH, _calendarMonth.value)
            add(Calendar.MONTH, 1)
        }
        _calendarYear.value = cal.get(Calendar.YEAR)
        _calendarMonth.value = cal.get(Calendar.MONTH)
        _selectedDayInfo.value = null
        refreshCalendar()
        vibrate(30)
    }

    fun goToTodayMonth() {
        val cal = Calendar.getInstance()
        _calendarYear.value = cal.get(Calendar.YEAR)
        _calendarMonth.value = cal.get(Calendar.MONTH)
        _selectedDayInfo.value = null
        refreshCalendar()
        vibrate(35)
    }

    fun refreshCalendar() {
        _calendarDays.value = computeCalendarDays(_calendarYear.value, _calendarMonth.value)
        if (_selectedDayInfo.value == null) {
            _selectedDayInfo.value = _calendarDays.value.find { it.isToday }
                ?: _calendarDays.value.firstOrNull { it.isCurrentMonth }
        } else {
            val key = _selectedDayInfo.value?.dateKey
            val updated = _calendarDays.value.find { it.dateKey == key }
            if (updated != null) {
                _selectedDayInfo.value = updated
            }
        }
    }

    private fun computeCalendarDays(year: Int, month: Int): List<CalendarDayInfo> {
        val todayCal = Calendar.getInstance()
        val todayYear = todayCal.get(Calendar.YEAR)
        val todayMonth = todayCal.get(Calendar.MONTH)
        val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        // Convert to Monday = 0 .. Sunday = 6
        val leadingDays = (firstDayOfWeek - Calendar.MONDAY + 7) % 7

        val list = mutableListOf<CalendarDayInfo>()

        // Previous month leading days
        val prevCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val maxDaysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val prevMonth1Indexed = prevCal.get(Calendar.MONTH) + 1
        val prevYear = prevCal.get(Calendar.YEAR)

        for (i in (maxDaysInPrevMonth - leadingDays + 1)..maxDaysInPrevMonth) {
            val dateKey = String.format(Locale.US, "%04d-%02d-%02d", prevYear, prevMonth1Indexed, i)
            list.add(
                CalendarDayInfo(
                    year = prevYear,
                    month = prevMonth1Indexed,
                    day = i,
                    dateKey = dateKey,
                    isToday = false,
                    isCurrentMonth = false,
                    isCompleted = false,
                    focusMinutes = 0
                )
            )
        }

        // Current month days
        val currentMonth1Indexed = month + 1
        val allQuestsCompleted = quests.value.all { it.isCompleted }
        val currentStreak = playerProfile.value.currentStreak

        for (day in 1..maxDaysInMonth) {
            val isToday = (year == todayYear && month == todayMonth && day == todayDay)
            val isPastDay = (year < todayYear) ||
                (year == todayYear && month < todayMonth) ||
                (year == todayYear && month == todayMonth && day < todayDay)

            val daysBeforeToday = todayDay - day
            val isStreakDay = isPastDay && (year == todayYear && month == todayMonth) && (daysBeforeToday in 1..currentStreak)
            val isCompleted = if (isToday) allQuestsCompleted else (isStreakDay || (isPastDay && (day % 3 != 0)))
            val dateKey = String.format(Locale.US, "%04d-%02d-%02d", year, currentMonth1Indexed, day)

            val focusMins = when {
                isToday -> playerProfile.value.todayFocusMinutes
                isCompleted -> 30 + ((day * 7) % 45)
                else -> 0
            }

            val completedTasksList = when {
                isToday -> quests.value.filter { it.isCompleted }.map { "${it.title}: [${it.currentProgress}/${it.targetGoal} ${it.unit}]" }
                isCompleted -> listOf(
                    "Push-ups: [100/100 reps]",
                    "Sit-ups: [100/100 reps]",
                    "Squats: [100/100 reps]",
                    "Running: [10/10 km]"
                )
                else -> emptyList()
            }

            list.add(
                CalendarDayInfo(
                    year = year,
                    month = currentMonth1Indexed,
                    day = day,
                    dateKey = dateKey,
                    isToday = isToday,
                    isCurrentMonth = true,
                    isCompleted = isCompleted,
                    hasPenalty = isPastDay && !isCompleted && (day % 7 == 0),
                    focusMinutes = focusMins,
                    expEarned = if (isCompleted) 150 else 0,
                    completedTasks = completedTasksList
                )
            )
        }

        // Next month trailing days to complete 42 cells
        val remainingDays = 42 - list.size
        val nextCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        val nextMonth1Indexed = nextCal.get(Calendar.MONTH) + 1
        val nextYear = nextCal.get(Calendar.YEAR)

        for (i in 1..remainingDays) {
            val dateKey = String.format(Locale.US, "%04d-%02d-%02d", nextYear, nextMonth1Indexed, i)
            list.add(
                CalendarDayInfo(
                    year = nextYear,
                    month = nextMonth1Indexed,
                    day = i,
                    dateKey = dateKey,
                    isToday = false,
                    isCurrentMonth = false,
                    isCompleted = false,
                    focusMinutes = 0
                )
            )
        }

        return list
    }

    private fun vibrate(millis: Long) {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(millis)
                }
            }
        } catch (_: Exception) {}
    }
}
