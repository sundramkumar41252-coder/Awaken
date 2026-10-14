package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.EvolutionTier
import com.example.model.PlayerProfile
import com.example.model.QuestItem
import com.example.model.StatType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class AwakenRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("awaken_prefs", Context.MODE_PRIVATE)

    private val _playerProfile = MutableStateFlow(loadPlayerProfile())
    val playerProfile: StateFlow<PlayerProfile> = _playerProfile.asStateFlow()

    private val _quests = MutableStateFlow(loadQuests())
    val quests: StateFlow<List<QuestItem>> = _quests.asStateFlow()

    private val _evolutionTiers = MutableStateFlow(getEvolutionTiers(_playerProfile.value.level))
    val evolutionTiers: StateFlow<List<EvolutionTier>> = _evolutionTiers.asStateFlow()

    private fun loadPlayerProfile(): PlayerProfile {
        return PlayerProfile(
            name = prefs.getString("name", "Jin-Woo") ?: "Jin-Woo",
            title = prefs.getString("title", "The One Who Overcomes") ?: "The One Who Overcomes",
            rank = prefs.getString("rank", "E-Rank Hunter") ?: "E-Rank Hunter",
            level = prefs.getInt("level", 18),
            currentXp = prefs.getInt("currentXp", 850),
            maxXp = prefs.getInt("maxXp", 1500),
            currentHp = prefs.getInt("currentHp", 1420),
            maxHp = prefs.getInt("maxHp", 1420),
            currentMp = prefs.getInt("currentMp", 850),
            maxMp = prefs.getInt("maxMp", 850),
            fatigue = prefs.getInt("fatigue", 0),
            availableAp = prefs.getInt("availableAp", 5),
            strength = prefs.getInt("strength", 38),
            vitality = prefs.getInt("vitality", 29),
            agility = prefs.getInt("agility", 35),
            intelligence = prefs.getInt("intelligence", 24),
            perception = prefs.getInt("perception", 22),
            currentStreak = prefs.getInt("currentStreak", 7),
            bestStreak = prefs.getInt("bestStreak", 14),
            todayFocusMinutes = prefs.getInt("todayFocusMinutes", 45),
            dailyFocusGoalMinutes = prefs.getInt("dailyFocusGoalMinutes", 120),
            completedSessions = prefs.getInt("completedSessions", 2)
        )
    }

    private fun savePlayerProfile(profile: PlayerProfile) {
        prefs.edit().apply {
            putString("name", profile.name)
            putString("title", profile.title)
            putString("rank", profile.rank)
            putInt("level", profile.level)
            putInt("currentXp", profile.currentXp)
            putInt("maxXp", profile.maxXp)
            putInt("currentHp", profile.currentHp)
            putInt("maxHp", profile.maxHp)
            putInt("currentMp", profile.currentMp)
            putInt("maxMp", profile.maxMp)
            putInt("fatigue", profile.fatigue)
            putInt("availableAp", profile.availableAp)
            putInt("strength", profile.strength)
            putInt("vitality", profile.vitality)
            putInt("agility", profile.agility)
            putInt("intelligence", profile.intelligence)
            putInt("perception", profile.perception)
            putInt("currentStreak", profile.currentStreak)
            putInt("bestStreak", profile.bestStreak)
            putInt("todayFocusMinutes", profile.todayFocusMinutes)
            putInt("dailyFocusGoalMinutes", profile.dailyFocusGoalMinutes)
            putInt("completedSessions", profile.completedSessions)
            apply()
        }
    }

    private fun loadQuests(): List<QuestItem> {
        val savedJson = prefs.getString("quests_json", null)
        if (!savedJson.isNullOrEmpty()) {
            try {
                val array = JSONArray(savedJson)
                val list = mutableListOf<QuestItem>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        QuestItem(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            category = obj.optString("category", "Daily Quest"),
                            currentProgress = obj.getInt("currentProgress"),
                            targetGoal = obj.getInt("targetGoal"),
                            unit = obj.optString("unit", "reps"),
                            isCompleted = obj.getBoolean("isCompleted"),
                            expReward = obj.optInt("expReward", 30),
                            statReward = obj.optString("statReward", "STR +1")
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (e: Exception) {
                // fallback to defaults
            }
        }

        // Default quests matching Reference 2
        return listOf(
            QuestItem(
                id = "quest_pushups",
                title = "Push-ups",
                category = "Strange Training",
                currentProgress = 50,
                targetGoal = 100,
                unit = "reps",
                isCompleted = false,
                expReward = 40,
                statReward = "STR +1"
            ),
            QuestItem(
                id = "quest_situps",
                title = "Sit-ups",
                category = "Strange Training",
                currentProgress = 50,
                targetGoal = 100,
                unit = "reps",
                isCompleted = false,
                expReward = 40,
                statReward = "VIT +1"
            ),
            QuestItem(
                id = "quest_squats",
                title = "Squats",
                category = "Strange Training",
                currentProgress = 50,
                targetGoal = 100,
                unit = "reps",
                isCompleted = false,
                expReward = 40,
                statReward = "AGI +1"
            ),
            QuestItem(
                id = "quest_running",
                title = "Running",
                category = "Strange Training",
                currentProgress = 5,
                targetGoal = 10,
                unit = "km",
                isCompleted = false,
                expReward = 60,
                statReward = "VIT +2"
            ),
            QuestItem(
                id = "quest_diamond",
                title = "Diamond Push-ups",
                category = "Personal Added",
                currentProgress = 100,
                targetGoal = 100,
                unit = "reps",
                isCompleted = true,
                expReward = 50,
                statReward = "STR +2"
            )
        )
    }

    private fun saveQuests(list: List<QuestItem>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("category", item.category)
            obj.put("currentProgress", item.currentProgress)
            obj.put("targetGoal", item.targetGoal)
            obj.put("unit", item.unit)
            obj.put("isCompleted", item.isCompleted)
            obj.put("expReward", item.expReward)
            obj.put("statReward", item.statReward)
            array.put(obj)
        }
        prefs.edit().putString("quests_json", array.toString()).apply()
    }

    fun updateQuestProgress(questId: String, increment: Int) {
        val currentList = _quests.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == questId }
        if (index != -1) {
            val item = currentList[index]
            val newProgress = (item.currentProgress + increment).coerceIn(0, item.targetGoal)
            val completedNow = newProgress >= item.targetGoal
            val wasCompleted = item.isCompleted

            currentList[index] = item.copy(
                currentProgress = newProgress,
                isCompleted = completedNow
            )
            _quests.value = currentList
            saveQuests(currentList)

            if (!wasCompleted && completedNow) {
                addExp(item.expReward)
            }
        }
    }

    fun toggleQuestCompleted(questId: String) {
        val currentList = _quests.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == questId }
        if (index != -1) {
            val item = currentList[index]
            val newStatus = !item.isCompleted
            val newProgress = if (newStatus) item.targetGoal else 0
            currentList[index] = item.copy(
                isCompleted = newStatus,
                currentProgress = newProgress
            )
            _quests.value = currentList
            saveQuests(currentList)

            if (newStatus) {
                addExp(item.expReward)
            }
        }
    }

    fun addCustomQuest(title: String, target: Int, unit: String, category: String = "Personal Habit") {
        val newItem = QuestItem(
            id = UUID.randomUUID().toString(),
            title = title,
            category = category,
            currentProgress = 0,
            targetGoal = target.coerceAtLeast(1),
            unit = unit,
            isCompleted = false,
            expReward = 35,
            statReward = "PER +1"
        )
        val updated = _quests.value + newItem
        _quests.value = updated
        saveQuests(updated)
    }

    fun deleteQuest(questId: String) {
        val updated = _quests.value.filterNot { it.id == questId }
        _quests.value = updated
        saveQuests(updated)
    }

    fun addExp(amount: Int) {
        val cur = _playerProfile.value
        var newXp = cur.currentXp + amount
        var newLevel = cur.level
        var newMaxXp = cur.maxXp
        var extraAp = 0

        while (newXp >= newMaxXp) {
            newXp -= newMaxXp
            newLevel++
            newMaxXp = (newMaxXp * 1.25).toInt()
            extraAp += 3
        }

        val updatedProfile = cur.copy(
            level = newLevel,
            currentXp = newXp,
            maxXp = newMaxXp,
            availableAp = cur.availableAp + extraAp,
            currentHp = cur.maxHp, // Full recovery on level up!
            currentMp = cur.maxMp,
            rank = calculateRank(newLevel)
        )
        _playerProfile.value = updatedProfile
        savePlayerProfile(updatedProfile)
        _evolutionTiers.value = getEvolutionTiers(newLevel)
    }

    fun allocateStat(stat: StatType) {
        val cur = _playerProfile.value
        if (cur.availableAp <= 0) return

        val updated = when (stat) {
            StatType.STR -> cur.copy(
                availableAp = cur.availableAp - 1,
                strength = cur.strength + 1
            )
            StatType.VIT -> cur.copy(
                availableAp = cur.availableAp - 1,
                vitality = cur.vitality + 1,
                maxHp = cur.maxHp + 25,
                currentHp = (cur.currentHp + 25).coerceAtMost(cur.maxHp + 25)
            )
            StatType.AGI -> cur.copy(
                availableAp = cur.availableAp - 1,
                agility = cur.agility + 1
            )
            StatType.INT -> cur.copy(
                availableAp = cur.availableAp - 1,
                intelligence = cur.intelligence + 1,
                maxMp = cur.maxMp + 20,
                currentMp = (cur.currentMp + 20).coerceAtMost(cur.maxMp + 20)
            )
            StatType.PER -> cur.copy(
                availableAp = cur.availableAp - 1,
                perception = cur.perception + 1
            )
        }
        _playerProfile.value = updated
        savePlayerProfile(updated)
    }

    fun recordFocusSession(minutes: Int) {
        val cur = _playerProfile.value
        val updated = cur.copy(
            todayFocusMinutes = cur.todayFocusMinutes + minutes,
            completedSessions = cur.completedSessions + 1
        )
        _playerProfile.value = updated
        savePlayerProfile(updated)
        addExp(minutes * 2)
    }

    fun completeAllDailyQuests() {
        val currentList = _quests.value.map { it.copy(currentProgress = it.targetGoal, isCompleted = true) }
        _quests.value = currentList
        saveQuests(currentList)
        addExp(150)
    }

    private fun calculateRank(level: Int): String {
        return when {
            level < 10 -> "E-Rank Hunter"
            level < 20 -> "D-Rank Hunter"
            level < 35 -> "C-Rank Hunter"
            level < 50 -> "B-Rank Hunter"
            level < 70 -> "A-Rank Hunter"
            level < 90 -> "S-Rank Hunter"
            else -> "Shadow Monarch"
        }
    }

    private fun getEvolutionTiers(currentLevel: Int): List<EvolutionTier> {
        return listOf(
            EvolutionTier(
                tier = 1,
                title = "Awakened Shade",
                subtitle = "Initial System connection & baseline awakening",
                requiredLevel = 1,
                buffs = listOf("Focus +5%", "Discipline +5%"),
                isUnlocked = currentLevel >= 1
            ),
            EvolutionTier(
                tier = 2,
                title = "Umbral Ascendant",
                subtitle = "Channel shadow authority into physical daily feats",
                requiredLevel = 15,
                buffs = listOf("Streak Power +10%", "Shadow Power +5%"),
                isUnlocked = currentLevel >= 15
            ),
            EvolutionTier(
                tier = 3,
                title = "Doom Stalker",
                subtitle = "Absolute mastery over physical limits & mental fatigue",
                requiredLevel = 30,
                buffs = listOf("Recovery +15%", "Attribute Mastery +8%"),
                isUnlocked = currentLevel >= 30
            ),
            EvolutionTier(
                tier = 4,
                title = "Monarch of Shadows",
                subtitle = "Sovereign of the system, transcendent productivity",
                requiredLevel = 50,
                buffs = listOf("Unshakable Will", "Infinite Dominion +25%"),
                isUnlocked = currentLevel >= 50
            )
        )
    }
}
