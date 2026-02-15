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
    var i = 0
    var inQuotes = false

    while (i < line.length) {
        val char = line[i]

        when {
            char == '"' -> {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    sb.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            }

            char == ',' && !inQuotes -> {
                result += sb.toString()
                sb.clear()
            }

            else -> sb.append(char)
        }

        i++
    }

    result += sb.toString()
    return result
}