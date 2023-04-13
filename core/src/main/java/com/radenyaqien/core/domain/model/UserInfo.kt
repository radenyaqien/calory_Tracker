package com.radenyaqien.core.domain.model

data class UserInfo(
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Int,
    val type: GoalType,
    val level: ActivityLevel,
    val carbRatio: Float,
    val fatRatio: Float,
    val proteinRatio: Float,
)
