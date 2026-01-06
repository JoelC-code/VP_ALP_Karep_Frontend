package com.example.vp_alp_karep_frontend.views.CompanyViews

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.NotificationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.NotificationViewModel

@Composable
fun NotificationView(
    modifier: Modifier = Modifier,
    notificationViewModel: NotificationViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {}
) {
    val getAllNotificationsStatus = notificationViewModel.getAllNotificationsStatus
    val deleteNotificationStatus = notificationViewModel.deleteNotificationStatus

    LaunchedEffect(token) {
        notificationViewModel.getAllNotifications(token)
    }

    LaunchedEffect(deleteNotificationStatus) {
        when (deleteNotificationStatus) {
            is StringDataStatusUIState.Success -> {
                notificationViewModel.getAllNotifications(token)
                notificationViewModel.clearDeleteNotificationErrorMessage()
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    deleteNotificationStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                notificationViewModel.clearDeleteNotificationErrorMessage()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Notifikasi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when(getAllNotificationsStatus) {
            is NotificationStatusUIState.Success -> if (getAllNotificationsStatus.notifications.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getAllNotificationsStatus.notifications) { notification ->
                        NotificationCard(
                            notificationModel = notification,
                            onDelete = {
                                notificationViewModel.deleteNotification(
                                    token = token,
                                    notificationId = notification.id
                                )
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada notifikasi",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            is NotificationStatusUIState.Failed -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada notifikasi",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            else -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircleLoadingTemplate(
                    color = Color.White,
                    trackColor = Color.Transparent
                )
            }
        }
    }
}