package com.yernazar.pidapplication.domain

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.yernazar.pidapplication.App
import org.koin.core.component.KoinComponent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    KoinComponent {
    fun getContext() = getApplication<App>()
    fun getString(@StringRes id: Int): String = getContext().getString(id)
}