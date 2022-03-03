package com.example.nasaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.app.App.Companion.getPodDao
import com.example.nasaapp.model.PodDTO
import com.example.nasaapp.model.repository.LocalRepository
import com.example.nasaapp.model.repository.Repository

class FavoriteViewModel(
    private val repository: LocalRepository = Repository(getPodDao()),
    private val liveDataToObserve: MutableLiveData<FavoriteListState> = MutableLiveData(),
) : ViewModel() {

    fun getData(): LiveData<FavoriteListState>{
        sendDataLocalStorage()
        return liveDataToObserve
    }

    fun removeData(podDTO: PodDTO){
        repository.removePod(podDTO)
    }

    fun insertData(podDTO: PodDTO){
        repository.addPod(podDTO)
    }

    private fun sendDataLocalStorage(){
        liveDataToObserve.value = FavoriteListState.Success(
            repository.getAllPod()
        )
    }
}