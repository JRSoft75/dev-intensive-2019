package ru.skillbranch.devintensive.repositories

import ru.skillbranch.devintensive.models.data.Chat

object ChatRepository {
    fun loadChats() :List<Chat> {
        return DataGenerator.generateChats(10)
    }
}