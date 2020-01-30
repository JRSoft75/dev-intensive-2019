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
}