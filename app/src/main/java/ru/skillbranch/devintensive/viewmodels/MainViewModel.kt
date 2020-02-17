package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLideData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {
    private val chatRepository = ChatRepository

    fun getChatData() : LiveData<List<ChatItem>> {
        return mutableLideData(loadChats())
    }

    private fun loadChats(): List<ChatItem> {
       val chats =  chatRepository.loadChats()
        return chats.map {
            it.toChatItem()
        }
    }


}