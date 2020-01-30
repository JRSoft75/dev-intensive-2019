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
}