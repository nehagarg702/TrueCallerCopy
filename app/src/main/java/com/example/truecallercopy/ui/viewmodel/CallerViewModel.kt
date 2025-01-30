package com.example.truecallercopy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truecallercopy.data.model.CallerInfo
import com.example.truecallercopy.data.repository.CallerRepository
import kotlinx.coroutines.launch

class CallerViewModel(private val repository: CallerRepository) : ViewModel() {
    private val _callerInfo = MutableLiveData<CallerInfo>()
    val callerInfo: LiveData<CallerInfo> = _callerInfo

    fun fetchCallerInfo(phoneNumber: String) {
        viewModelScope.launch {
            val info = repository.getCallerData(phoneNumber)
            _callerInfo.postValue(info)
        }
    }
}
