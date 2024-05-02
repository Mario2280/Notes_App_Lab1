package com.example.notes_app_lab1

import android.app.Application
import android.content.Context

class app: Application() {
    companion object {
        var context: Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }

}