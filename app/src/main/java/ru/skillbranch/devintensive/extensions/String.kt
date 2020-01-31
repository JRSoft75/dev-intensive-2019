package ru.skillbranch.devintensive.extensions

fun String.truncate(count:Int = 16):String?{
    var result = this
    if(result.length > count) {
        result = result.substring(0, count ).trimEnd() + "..."
    }
    return result
}

fun String.stripHtml():String? {
    val strRegEx = "<[^>]*>"
    var str = this

    str = str.replace(strRegEx.toRegex(), "")
    //replace &nbsp; with space
    str = str.replace("&nbsp;", " ")
    //replace &amp; with &
    str = str.replace("&amp;", "&")
    //OR remove all HTML entities
    str = str.replace("&.*?;".toRegex(), "")
    str = str.replace("\\s{2,}".toRegex(), " ").trim()
    return str

}

