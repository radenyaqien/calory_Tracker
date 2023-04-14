package com.radenyaqien.core.domain.use_case

class FilterOutDigits {

    operator fun invoke(digits: String): String {
        return digits.filter {
            it.isDigit()
        }
    }
}