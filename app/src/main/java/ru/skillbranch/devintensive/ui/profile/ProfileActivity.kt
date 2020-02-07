package ru.skillbranch.devintensive.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Bender


class ProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val status = savedInstanceState?.getString("STATUS") ?:Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?:Bender.Question.NAME.name

    }





    override fun onSaveInstanceState(outState: Bundle) {
        outState?.let { super.onSaveInstanceState(it) }
    }




}
