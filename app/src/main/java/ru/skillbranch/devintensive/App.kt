package ru.skillbranch.devintensive

import android.content.Context

abstract class App  {
    companion object {
        private lateinit var context: Context


        fun setContext(con: Context) {
            context = con
        }

        fun getContext(): Context {
            return context
        }
    }





}