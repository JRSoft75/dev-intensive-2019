package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {
    private val query =  mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chatItems = mutableLiveData(LoadChats())
//    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
//        val result = MediatorLiveData<List<ChatItem>>()
//        val filterF = {
//            val queryStr = query.value!!
//            val chat_list = chatItems.value!!
//
//            result.value = if(queryStr.isEmpty()) chat_list
//            else chat_list.filter { it.title!!.contains(queryStr, true) }
//        }
//        return@map chats.filter { !it.isArchived }
//            .map { it.toChatItem() }
//            .sortedBy { it.id.toInt() }
//    }
//

    private fun LoadChats(): List<ChatItem> = chatRepository.loadChats().value!!.map { it.toChatItem() }

    fun getChatData(): LiveData<List<ChatItem>>{
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chatItems.value!!

            result.value = if(queryStr.isEmpty()) chats
            else chats.filter{ it.title.contains(queryStr, true) }
        }

        result.addSource(chatItems){filterF.invoke()}
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