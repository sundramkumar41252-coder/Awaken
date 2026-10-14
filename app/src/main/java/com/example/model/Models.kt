package com.example.model

enum class StatType {
    STR, VIT, AGI, INT, PER
}

data class PlayerProfile(
    val name: String = "Jin-Woo",
    val title: String = "The One Who Overcomes",
    val rank: String = "E-Rank Hunter",
    val level: Int = 18,
    val currentXp: Int = 850,
    val maxXp: Int = 1500,
    val currentHp: Int = 1420,
    val maxHp: Int = 1420,
    val currentMp: Int = 850,
    val maxMp: Int = 850,
    val fatigue: Int = 0,
    val availableAp: Int = 5,
    val strength: Int = 38,
    val vitality: Int = 29,
    val agility: Int = 35,
    val intelligence: Int = 24,
    val perception: Int = 22,
    val currentStreak: Int = 7,
    val bestStreak: Int = 14,
    val todayFocusMinutes: Int = 45,
    val dailyFocusGoalMinutes: Int = 120,
    val completedSessions: Int = 2
)

data class QuestItem(
    val id: String,
    val title: String,
    val category: String = "Daily Quest",
    val currentProgress: Int,
    val targetGoal: Int,
    val unit: String = "reps",
    val isCompleted: Boolean = false,
    val expReward: Int = 30,
    val statReward: String = "STR +1"
)

data class EvolutionTier(
    val tier: Int,
    val title: String,
    val subtitle: String,
    val requiredLevel: Int,
    val buffs: List<String>,
    val isUnlocked: Boolean
)

data class CalendarDayInfo(
    val year: Int,
    val month: Int, // 1-12
    val day: Int,
    val dateKey: String, // "YYYY-MM-DD"
    val isToday: Boolean,
    val isCurrentMonth: Boolean,
    val isCompleted: Boolean,
    val hasPenalty: Boolean = false,
    val focusMinutes: Int = 0,
    val expEarned: Int = 0,
    val completedTasks: List<String> = emptyList()
)
