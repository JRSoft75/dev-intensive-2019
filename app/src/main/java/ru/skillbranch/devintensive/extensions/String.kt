package ru.skillbranch.devintensive.extensions

fun String.truncate(count:Int = 16):String?{
    var result = this
    if(result.length > count) {
        result = result.substring(0, count ).trimEnd() + "..."
    }
    return result
}