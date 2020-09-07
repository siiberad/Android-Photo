package com.siiberad.photo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val data = MutableLiveData<List<String>>()
    fun setdata(list : List<String>) {
        data.postValue(list)
    }

    fun getData(): LiveData<List<String>> {
        return data
    }

    var pathUri = MutableLiveData<String>()
}