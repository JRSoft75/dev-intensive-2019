package ru.skillbranch.devintensive.data.managers

object CacheManager {
    val chats = mutableLiveData(DataGenerator.stabChats)

    fun loadChats(){
        return chats
    }
}