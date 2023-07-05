package com.romkapo.todoapp.utils

enum class Importance {
    LOW,
    MEDIUM,
    HIGH,
}

fun parseImportanceToLocal(text: String?): Importance {
    return when (text) {
        "low" -> Importance.LOW
        "basic" -> Importance.MEDIUM
        "important" -> Importance.HIGH
        else -> throw UnsupportedOperationException("Can`t parse network importance to local value: $text")
    }
}

fun Importance.parseToNetwork(): String {
    return when (this) {
        Importance.LOW -> "low"
        Importance.MEDIUM -> "basic"
        Importance.HIGH -> "important"
    }
}
