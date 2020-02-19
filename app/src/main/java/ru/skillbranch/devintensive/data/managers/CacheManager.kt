package ru.skillbranch.devintensive.data.managers

import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User

object CacheManager {
    private val chats = mutableLiveData(DataGenerator.stabChats)
    private val users = mutableLiveData(DataGenerator.stabUsers)

    fun loadChats(){
        return chats
    }

    fun findUserByIds(ids: List<String>) : List<User> {
        return users.value!!.filter{ids.contains(it.id)}
    }

    fun nextChatId(): String{
        return "${chats.value!!.size}"
    }

    fun insertChat(chat: Chat){
        val copy = chats.value!!.toMutableList()
        copy.add(chat)
        chats.value = copy
    }
}