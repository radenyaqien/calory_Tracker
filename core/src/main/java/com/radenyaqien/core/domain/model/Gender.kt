package com.radenyaqien.core.domain.model

sealed class Gender(val name: String) {
    object Male : Gender("male")
    object Female : Gender("female")

    companion object {
        fun fromString(name: String): Gender {
            return when (name) {
                "female" -> Female
                "male" -> Male
                else -> Female
            }
        }
    }
}
