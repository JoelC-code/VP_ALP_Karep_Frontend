package com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.AllNotificationsResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.NotificationRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.NotificationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel(
    private val notificationRepository: NotificationRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {
    private val _getAllNotificationsStatus = MutableStateFlow<NotificationStatusUIState>(
        NotificationStatusUIState.Start
    )
    val getAllNotificationsStatus = _getAllNotificationsStatus

    private val _deleteNotificationStatus = MutableStateFlow<StringDataStatusUIState>(
        StringDataStatusUIState.Start
    )
    val deleteNotificationStatus = _deleteNotificationStatus

    fun getAllNotifications() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getAllNotificationsStatus.value =
                    NotificationStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getAllNotificationsStatus.value = NotificationStatusUIState.Loading

            try {
                val call = notificationRepository.getAllNotifications(token)

                call.enqueue(object: Callback<AllNotificationsResponse>{
                    override fun onResponse(
                        call: Call<AllNotificationsResponse?>,
                        res: Response<AllNotificationsResponse?>
                    ) {
                        if (res.isSuccessful) {
                            _getAllNotificationsStatus.value = NotificationStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _getAllNotificationsStatus.value = NotificationStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<AllNotificationsResponse?>,
                        t: Throwable
                    ) {
                        _getAllNotificationsStatus.value = NotificationStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getAllNotificationsStatus.value = NotificationStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _deleteNotificationStatus.value =
                    StringDataStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _deleteNotificationStatus.value = StringDataStatusUIState.Loading

            try {
                val call = notificationRepository.deleteNotification(token, notificationId)

                call.enqueue(object: Callback<GeneralResponseCompanyModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            _deleteNotificationStatus.value = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _deleteNotificationStatus.value = StringDataStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        _deleteNotificationStatus.value = StringDataStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _deleteNotificationStatus.value = StringDataStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearDeleteNotificationErrorMessage() {
        _deleteNotificationStatus.value = StringDataStatusUIState.Start
    }

    fun clearGetAllNotificationsErrorMessage() {
        _getAllNotificationsStatus.value = NotificationStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val notificationRepository = application.container.notificationRepository
                val loginRepository = application.container.loginRegistRepository
                NotificationViewModel(notificationRepository, loginRepository)
            }
        }
    }
}