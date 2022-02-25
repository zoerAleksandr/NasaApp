package com.example.nasaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.*
import com.example.nasaapp.model.MarsPhotoListDTO
import com.example.nasaapp.model.PodDTO
import com.example.nasaapp.model.PodRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: PodRetrofitImpl = PodRetrofitImpl(),
    var resource: SelectedTab = SelectedTab.Earth
) : ViewModel() {

    private val apiKey: String = BuildConfig.NASA_API_KEY

    fun getData(date: String): LiveData<AppState> {
        when (resource) {
            is SelectedTab.Earth -> sendServerRequestPOD(date)
            is SelectedTab.Mars -> sendServerRequestMarsPhoto(date)
        }
        return liveDataToObserve
    }

    private fun sendServerRequestPOD(date: String) {
        liveDataToObserve.value = AppState.Loading(null)
        if (apiKey.isBlank()) {
            liveDataToObserve.value = AppState.Error(Throwable("Нет ключа"))
        } else {
            retrofitImpl.getPodRetrofitImpl().getPod(apiKey, date)
                .enqueue(object : Callback<PodDTO> {
                    override fun onResponse(call: Call<PodDTO>, response: Response<PodDTO>) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataToObserve.value = AppState.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataToObserve.value =
                                    AppState.Error(Throwable("Неизвестная ошибка"))
                            } else {
                                liveDataToObserve.value = AppState.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PodDTO>, t: Throwable) {
                        liveDataToObserve.value = AppState.Error(t)
                    }
                })
        }
    }

    private fun sendServerRequestMarsPhoto(date: String) {
        liveDataToObserve.value = AppState.Loading(null)
        if (apiKey.isBlank()) {
            liveDataToObserve.value = AppState.Error(Throwable("Нет ключа"))
        } else {
            retrofitImpl.getMarsPhotoRetrofitImpl().getMarsPhoto(date, apiKey)
                .enqueue(object : Callback<MarsPhotoListDTO> {
                    override fun onResponse(
                        call: Call<MarsPhotoListDTO>,
                        response: Response<MarsPhotoListDTO>
                    ) {
                        Log.d("Debug", "${response.body()}")
                        if (response.isSuccessful && response.body() != null) {
                            liveDataToObserve.value = AppState.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                AppState.Error(Throwable("Неизвестная ошибка"))
                            } else {
                                liveDataToObserve.value = AppState.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarsPhotoListDTO>, t: Throwable) {
                        liveDataToObserve.value = AppState.Error(t)
                    }
                })
        }
    }
}