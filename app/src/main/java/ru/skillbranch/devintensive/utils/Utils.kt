package ru.skillbranch.devintensive.utils

import java.util.regex.Matcher
import java.util.regex.Pattern


object Utils {
    fun parseFullName(fullName:String?):Pair<String?, String?>{
        //val fullName_ = fullName?.trim()
        val parts : List<String>? = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)
        if(firstName !=null && firstName.length == 0) {
            firstName = null
        }
        if(lastName !=null && lastName.length == 0) {
            lastName = null
        }
//        return Pair(firstName, lastName)
        return  firstName to lastName
    }

    fun toInitials(firstName:String? = null, lastName:String? = ""):String? {
        var initials =""
        initials = if(firstName != null && firstName.trim().length != 0) firstName.trim()[0].toString().toUpperCase() else ""
        initials += if(lastName != null && lastName.trim().length != 0) lastName.trim()[0].toString().toUpperCase() else ""
        return if(initials.length != 0) initials else null
    }

    fun transliteration(payload:String, divider:String? = " "): String {
        val abcCyr = charArrayOf(
            'а',
            'б',
            'в',
            'г',
            'д',
            'е',
            'ё',
            'ж',
            'з',
            'и',
            'й',
            'к',
            'л',
            'м',
            'н',
            'о',
            'п',
            'р',
            'с',
            'т',
            'у',
            'ф',
            'х',
            'ц',
            'ч',
            'ш',
            'щ',
            'ъ',
            'ы',
            'ь',
            'э',
            'ю',
            'я',
            'А',
            'Б',
            'В',
            'Г',
            'Д',
            'Е',
            'Ё',
            'Ж',
            'З',
            'И',
            'Й',
            'К',
            'Л',
            'М',
            'Н',
            'О',
            'П',
            'Р',
            'С',
            'Т',
            'У',
            'Ф',
            'Х',
            'Ц',
            'Ч',
            'Ш',
            'Щ',
            'Ъ',
            'Ы',
            'Ь',
            'Э',
            'Ю',
            'Я'
        )
        val abcLat = arrayOf(
            "a",
            "b",
            "v",
            "g",
            "d",
            "e",
            "e",
            "zh",
            "z",
            "i",
            "i",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "r",
            "s",
            "t",
            "u",
            "f",
            "h",
            "c",
            "ch",
            "sh",
            "sh'",
            "",
            "i",
            "",
            "e",
            "yu",
            "ya",
            "A",
            "B",
            "V",
            "G",
            "D",
            "E",
            "E",
            "Zh",
            "Z",
            "I",
            "I",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "R",
            "S",
            "T",
            "U",
            "F",
            "H",
            "C",
            "Ch",
            "Sh",
            "Sh'",
            "",
            "I",
            "",
            "E",
            "Yu",
            "Ya"
        )
        val builder = StringBuilder()
        if (payload != null) {
            for (element in payload) {
                if(element.toString() == " "){
                    builder.append(divider)
                }else
                if(!abcCyr.contains(element)){
                    builder.append(element)
                }else {
                    for (x in abcCyr.indices) {
                        if (element == abcCyr[x]) {
                            builder.append(abcLat[x])
                        }
                    }
                }
            }
        }
        return builder.toString()
    }

    fun validateGithubRepo(repo : String):Boolean{
        val blackList: List<String> = listOf(
            "enterprise",
            "features",
            "topics",
            "trending",
            "events",
            "marketplace",
            "pricing",
            "nonprofit",
            "customer-stories",
            "security",
            "login",
            "join"
        )
        var isValid = true
        val regex  = "^(https:\\/\\/|https:\\/\\/www\\.|www\\.)?github\\.com\\/(\\w+[^\\/])$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(repo)

        if (matcher.find()) {
            println(matcher.group(2))
            if(blackList.contains(matcher.group(2))){
                isValid = false
            }
        }else{
            isValid = false
        }


        return isValid
    }


}