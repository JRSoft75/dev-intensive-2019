package ru.skillbranch.devintensive.ui.archive

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_group.fab
import kotlinx.android.synthetic.main.activity_group.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item?.itemId == android.R.id.home){
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
             true
        }else{
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {}
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val chatId = it.id
            viewModel.restoreFromArchive(chatId)
            val snackbar = Snackbar
                .make(rv_chat_list, "Восстановить чат с ${it.title} из архива?", Snackbar.LENGTH_LONG)
                .setAction("Отмена?") {
                    viewModel.restoreFromArchive(chatId)
                    // показываем сообщение "Все вернулось на свои места!"
                    Snackbar.make(
                        rv_chat_list,
                        "Восстановление отменено!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            snackbar.show()

        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_user_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }



    }



    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
//        viewModel.getSelectedData().observe(this, Observer {
//            updateChips(it)
//            toggleFab(it.size>1)
//        })
    }

    private fun toggleFab(isShow: Boolean) {
        if(isShow) fab.show()
        else fab.hide()

    }

//    private fun addChipToGroup(user:UserItem){
//        val chip = Chip(this).apply {
//            text = user.fullName
//            chipIcon = resources.getDrawable(R.drawable.avatar_default, theme)
//            isCloseIconVisible = true
//            tag = user.id
//            isClickable = true
//            closeIconTint = ColorStateList.valueOf(Color.WHITE)
//            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.color_primary_light))
//            setTextColor(Color.WHITE)
//        }
//
////        chip.setOnCloseIconClickListener{viewModel.handleRemoveChip(it.tag.toString())}
//        chip_group.addView(chip)
//    }

//    private fun updateChips(listUsers: List<UserItem>){
//        chip_group.visibility = if(listUsers.isEmpty()) View.GONE else View.VISIBLE
//        val users = listUsers
//            .associate { user -> user.id to user}
//            .toMutableMap()
//
//        val views = chip_group.children.associate { view -> view.tag to view}
//
//        for((k,v) in views){
//            if(!users.containsKey(k)) chip_group.removeView(v)
//            else users.remove(k)
//        }
//
//        users.forEach{(_,v)->addChipToGroup(v)}
//    }
}
