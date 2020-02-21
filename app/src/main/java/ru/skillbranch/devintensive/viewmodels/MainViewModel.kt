package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {
    private val query =  mutableLiveData("")
    private val chatRepository = ChatRepository
    //private val chatItems = mutableLiveData(LoadChats())

    private val archiveChatsCount = mutableLiveData(LoadChats()).value!!.filter { it.chatType == ChatType.ARCHIVE }.size
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        //val queryStr = query.value!!

        return@map chats.filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() }
    }

    private fun LoadChats(): List<ChatItem> = chatRepository.loadChats().value!!.map { it.toChatItem() }

    fun getArchiveChatsCount() : Int =  archiveChatsCount

//    fun getChatData() : LiveData<List<ChatItem>> {
//        return chats
//    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats_items = chats.value!!

            result.value = if(queryStr.isEmpty()) chats_items
            else chats_items.filter {it.chatType != ChatType.ARCHIVE && it.title!!.contains(queryStr, true) }
            Log.d("M_MainViewModel","getChatData=${queryStr}")

        }

        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}
        return result
    }




    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }


}