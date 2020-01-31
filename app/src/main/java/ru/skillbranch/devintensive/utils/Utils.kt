package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?):Pair<String?, String?>{
        //ToDo FIX ME!!!
        val parts : List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

//        return Pair(firstName, lastName)
        return firstName to lastName
    }

    fun toInitials(firstName:String? = null, lastName:String? = ""):String? {
        var initials =""
        initials = if(firstName != null && firstName.length != 0) firstName[0].toString().toUpperCase() else ""
        initials += if(lastName != null && lastName.length != 0) lastName[0].toString().toUpperCase() else ""
        return if(initials.length != 0) initials else null
    }

    fun transliterate(payload:String?, divider:String? = " "): String? {
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
}