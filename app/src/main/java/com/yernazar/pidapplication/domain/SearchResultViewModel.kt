package com.yernazar.pidapplication.domain

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yernazar.pidapplication.data.repository.AppRepositoryImpl
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.Route

class SearchResultViewModel(application: Application, private val repositoryImpl: AppRepositoryImpl) : BaseViewModel(application) {




}