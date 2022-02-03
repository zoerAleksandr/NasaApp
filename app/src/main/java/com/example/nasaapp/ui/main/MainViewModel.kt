package com.example.nasaapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.AppState
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.PodDTO
import com.example.nasaapp.retrofit.PodRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: PodRetrofitImpl = PodRetrofitImpl()
) : ViewModel() {

    private val nasaFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    private fun getYesterday(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return nasaFormat.format(calendar.time)
    }

    private fun getBeforeDay(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)
        return nasaFormat.format(calendar.time)
    }

    fun getDataToday(): LiveData<AppState> {
        sendServerRequestToday()
        return liveDataToObserve
    }

    private fun sendServerRequestToday() {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataToObserve.value = AppState.Error(Throwable("Нет ключа"))
        } else {
            retrofitImpl.getRetrofitTodayImpl().getPod(apiKey).enqueue(object : Callback<PodDTO> {
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

    fun getDataYesterday(): LiveData<AppState> {
        sendServerRequestYesterday()
        return liveDataToObserve
    }

    private fun sendServerRequestYesterday() {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataToObserve.value = AppState.Error(Throwable("Нет ключа"))
        } else {
            retrofitImpl.getRetrofitYesterdayImpl().getPod(apiKey, getYesterday())
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

    fun getDataBeforeDay(): LiveData<AppState> {
        sendServerRequestBeforeDay()
        return liveDataToObserve
    }

    private fun sendServerRequestBeforeDay() {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataToObserve.value = AppState.Error(Throwable("Нет ключа"))
        } else {
            retrofitImpl.getRetrofitBeforeDayImpl().getPod(apiKey, getBeforeDay())
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
}