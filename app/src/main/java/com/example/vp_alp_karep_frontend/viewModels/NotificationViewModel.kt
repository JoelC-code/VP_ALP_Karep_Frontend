package com.example.vp_alp_karep_frontend.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.repositories.NotificationRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.NotificationStatusUIState
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.AllNotificationsResponse
import com.example.vp_alp_karep_frontend.models.ErrorModel
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel(
    private val notificationRepository: NotificationRepositoryInterface
): ViewModel() {
    var getAllNotificationsStatus: NotificationStatusUIState by mutableStateOf(
        NotificationStatusUIState.Start)
        private set

    var deleteNotificationStatus: StringDataStatusUIState by mutableStateOf(
        StringDataStatusUIState.Start)
        private set

    fun getAllNotifications(
        token: String,
    ) {
        viewModelScope.launch {
            getAllNotificationsStatus = NotificationStatusUIState.Loading

            try {
                val call = notificationRepository.getAllNotifications(token)

                call.enqueue(object: Callback<AllNotificationsResponse>{
                    override fun onResponse(
                        call: Call<AllNotificationsResponse?>,
                        res: Response<AllNotificationsResponse?>
                    ) {
                        if (res.isSuccessful) {
                            getAllNotificationsStatus = NotificationStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            getAllNotificationsStatus = NotificationStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<AllNotificationsResponse?>,
                        t: Throwable
                    ) {
                        getAllNotificationsStatus = NotificationStatusUIState.Failed(
                            t.localizedMessage
                        )
                    }
                })
            } catch (error: IOException) {
                getAllNotificationsStatus = NotificationStatusUIState.Failed(
                    error.localizedMessage
                )
            }
        }
    }

    fun deleteNotification(
        token: String,
        notificationId: Int
    ) {
        viewModelScope.launch {
            deleteNotificationStatus = StringDataStatusUIState.Loading

            try {
                val call = notificationRepository.deleteNotification(token, notificationId)

                call.enqueue(object: Callback<GeneralResponseModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if (res.isSuccessful) {
                            deleteNotificationStatus = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            deleteNotificationStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseModel>,
                        t: Throwable
                    ) {
                        deleteNotificationStatus = StringDataStatusUIState.Failed(
                            t.localizedMessage
                        )
                    }
                })
            } catch (error: IOException) {
                deleteNotificationStatus = StringDataStatusUIState.Failed(
                    error.localizedMessage
                )
            }
        }
    }

    fun clearDeleteNotificationErrorMessage() {
        deleteNotificationStatus = StringDataStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val notificationRepository = application.container.notificationRepository
                NotificationViewModel(
                    notificationRepository = notificationRepository
                )
            }
        }
    }
}