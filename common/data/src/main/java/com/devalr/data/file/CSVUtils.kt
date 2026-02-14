package com.devalr.data.file

import kotlin.text.iterator

fun String?.escapeCsv(): String {
    if (this == null) return ""

    val escaped = replace("\"", "\"\"")
    return if (escaped.contains(",") || escaped.contains("\n")) {
        "\"$escaped\""
    } else escaped
}

fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val sb = StringBuilder()
    var inQuotes = false

    for (char in line) {
        when {
            char == '"' -> inQuotes = !inQuotes
            char == ',' && !inQuotes -> {
                result += sb.toString()
                sb.clear()
            }
            else -> sb.append(char)
        }
    }
    result += sb.toString()
    return result
}