package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.AvatarImageView
import ru.skillbranch.devintensive.ui.custom.TextDrawable
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class ProfileActivity : AppCompatActivity() {

    companion object{
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var drawable: TextDrawable
    var isEditMode = false
    lateinit var viewFields : Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        App.setContext(this)
        initViews(savedInstanceState)
        initViewModel()
//        val status = savedInstanceState?.getString("STATUS") ?:Bender.Status.NORMAL.name
//        val question = savedInstanceState?.getString("QUESTION") ?:Bender.Question.NAME.name

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.let { super.onSaveInstanceState(it) }
        outState.putBoolean(IS_EDIT_MODE,isEditMode)
    }


    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE,false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if(isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener{
            viewModel.switchTheme()
        }


        et_repository.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { if(!isEditMode){
                return
            }
                val repo = s.toString().trim()
                if (!Utils.validateGithubRepo(repo)) {
                    wr_repository.isErrorEnabled = true
                    wr_repository.error = "Невалидный адрес репозитория"
                }else{
                    wr_repository.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(editable: Editable) { // действия после того, как что то введено
                // editable - то, что введено. В строку - editable.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) { // действия перед тем, как что то введено
            }
        })

        }



    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName","lastName","about","repository").contains(it.key) }
        for ((_,v) in info){
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if(isEdit) 255 else 0
        }

        ic_eye.visibility = if(isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit){
            val filter: ColorFilter? = if(isEdit){
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            }else{
                null
            }

            val icon = if(isEdit){
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            }else{
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun initViewModel(){
//        viewModel = ViewModelProvider(this, ).get(ProfileViewModel::class.java)
        viewModel = ViewModelProviders.of(this ).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })


    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity","update Theme")
        delegate.setLocalNightMode(mode)
    }


    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for((k, v) in viewFields){
                v.text = it[k].toString()
            }
        }
//        profile.nickName = Utils.transliteration(profile.firstName + " " + profile.lastName,"_")
        val initials = Utils.toInitials(firstName = profile.firstName, lastName = profile.lastName)
        if(initials !=null && iv_avatar is AvatarImageView){
            iv_avatar.setInitials(initials)
       //     drawable = TextDrawable(initials,Color.WHITE, resources.getColor(R.color.color_accent, theme))
       //     iv_avatar.setImageDrawable (drawable)

            //iv_avatar.setBorderColor("#7f05002a")  //2131034154
            //Log.d("M_ProfileActivity","iv_avatar.getBorderColor()=" + iv_avatar.getBorderColor())
        }
    }

    private fun saveProfileInfo(){
        if(wr_repository.isErrorEnabled){
            et_repository.text!!.clear()
            wr_repository.isErrorEnabled = false
            wr_repository.error = null
        }
        Profile(
            firstName = et_first_name.text.toString().trim(),
            lastName = et_last_name.text.toString().trim(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString(),
//            nickName = tv_nick_name.text.toString()
            nickName = Utils.transliteration(et_first_name.text.toString().trim() + " " + et_last_name.text.toString().trim(),"_")
        ).apply {
            viewModel.saveProfileData(this)
        }
    }




}
